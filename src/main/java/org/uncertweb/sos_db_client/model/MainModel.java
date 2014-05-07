package org.uncertweb.sos_db_client.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement model.
 * 
 * @author Benedikt Klus
 */
public class MainModel implements IModel {

	private static final Logger LOG = LogManager.getLogger(MainModel.class);

	/**
	 * Import single data set into database.
	 * 
	 * @param tableName
	 *            table name
	 * @param params
	 *            parameters to be imported
	 * @throws IOException
	 */
	public void importIntoDatabase(String tableName, List<String> params) throws IOException {

		String jdbcUrl = ApplicationProperties.getJdbcUrl();

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource(tableName);

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		for(String param : params) {
			featureBuilder.add(param);
		}

		if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
			/**
			 * SimpleFeatureSource allows creating own feature ID's.
			 */
			featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
		}

		SimpleFeature feature = featureBuilder.buildFeature(params.get(0));

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();

		features.add(feature);

		SimpleFeatureCollection featureCollection = new ListFeatureCollection(TYPE, features);

		Transaction transaction = new DefaultTransaction();

		/**
		 * Check if the contents of a file are writable.
		 */
		if(featureSource instanceof SimpleFeatureStore) {

			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
			featureStore.setTransaction(transaction);

			try {
				featureStore.addFeatures(featureCollection);
				/**
				 * Actually write out the features in one go.
				 */
				transaction.commit();
			} catch(IOException e) {
				LOG.error(e.getLocalizedMessage(), e);
				transaction.rollback();
				throw new IOException(e.getLocalizedMessage());
			} finally {

				if(transaction != null) {
					transaction.close();
				}

			}

		}

	}

	/**
	 * Import multiple data sets into database.
	 * 
	 * @param tableName
	 *            table name
	 * @param fileUris
	 *            file URI's
	 * @param params
	 *            parameters to be imported
	 * @param isSerial
	 *            true if database primary key is of type "SERIAL", false if not
	 * @throws IOException
	 */
	public void importIntoDatabase(String tableName, List<String> fileUris, List<String> params, boolean isSerial) throws IOException {

		String jdbcUrl = ApplicationProperties.getJdbcUrl();

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource(tableName);

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();

		/**
		 * Get maximum serial by quickly counting all features available. May return -1 if the information is not readily available. Formats
		 * such as shapefile keep this information available in the header for handy reference. WFS does not provide any way to ask for this
		 * information, and thus always returns -1.
		 */
		int serial = featureSource.getCount(Query.ALL);

		if(serial == -1) {
			serial = featureSource.getFeatures().size();
		}

		for(String fileUri : fileUris) {

			String _typeName = fileUri.substring(fileUri.lastIndexOf(Resource.FILE_SEPARATOR) + 1, fileUri.lastIndexOf('.'));

			SimpleFeatureSource _featureSource = Repository.getDataStore(fileUri).getFeatureSource(_typeName);

			/**
			 * Helper variables for calculating the progress of the current task.
			 */
			int count = _featureSource.getCount(Query.ALL);
			int index = 0;

			SimpleFeatureIterator _featureIterator = _featureSource.getFeatures().features();

			/**
			 * Write attribute values to the appropriate table column.
			 */
			try {

				while(_featureIterator.hasNext()) {

					/**
					 * Update progress bar.
					 */
					index++;
					int currentValue = (int) (100.0 / count * index);
					String currentText = "Reading data set " + index + "/" + count;
					EventBus.publish(new ProgressEvent(false, currentValue, currentText));

					SimpleFeature _feature = _featureIterator.next();

					/**
					 * Increment serial.
					 */
					serial++;

					for(String param : params) {

						if(param != null) {

							/**
							 * Check if parameter is attribute name or value.
							 */
							if(_feature.getAttribute(param) == null) {
								featureBuilder.add(param);
							} else {
								featureBuilder.add(_feature.getAttribute(param));
							}

						} else {
							featureBuilder.add(null);
						}

					}

					if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
						/**
						 * SimpleFeatureSource allows creating own feature ID's.
						 */
						featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
					}

					SimpleFeature feature;

					if(isSerial) {
						feature = featureBuilder.buildFeature(String.valueOf(serial));
					} else {
						feature = featureBuilder.buildFeature(_feature.getAttribute(params.get(0)).toString());
					}

					features.add(feature);

				}

			} finally {

				if(_featureIterator != null) {
					_featureIterator.close();
				}

			}

		}

		/**
		 * Update progress bar.
		 */
		EventBus.publish(new ProgressEvent(true, 0, "Importing into database"));

		SimpleFeatureCollection featureCollection = new ListFeatureCollection(TYPE, features);

		Transaction transaction = new DefaultTransaction();

		/**
		 * Check if the contents of a file are writable.
		 */
		if(featureSource instanceof SimpleFeatureStore) {

			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
			featureStore.setTransaction(transaction);

			try {
				featureStore.addFeatures(featureCollection);
				/**
				 * Actually write out the features in one go.
				 */
				transaction.commit();
			} catch(IOException e) {
				LOG.error(e.getLocalizedMessage(), e);
				transaction.rollback();
				throw new IOException(e.getLocalizedMessage());
			} finally {

				if(transaction != null) {
					transaction.close();
				}

			}

		}

	}

}
