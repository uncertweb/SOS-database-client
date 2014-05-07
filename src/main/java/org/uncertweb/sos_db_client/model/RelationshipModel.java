package org.uncertweb.sos_db_client.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Repository;

/**
 * Implement model.
 * 
 * @author Benedikt Klus
 */
public class RelationshipModel implements IModel {

	private static final Logger LOG = LogManager.getLogger(RelationshipModel.class);

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

		/**
		 * Exception handling if at least one parameter is empty.
		 */
		for(String param : params) {
			if(param == null) {
				return;
			}
		}

		String jdbcUrl = ApplicationProperties.getJdbcUrl();

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource(tableName);

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		featureBuilder.add(params.get(0));
		featureBuilder.add(params.get(1));

		if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
			/**
			 * SimpleFeatureSource allows creating own feature ID's.
			 */
			featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
		}

		SimpleFeature feature = featureBuilder.buildFeature(params.get(0) + "." + params.get(1));

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
	 * @param params
	 *            parameters to be imported
	 * @param isLeftPrefix
	 *            true if left side assignment is prefix, false if not
	 * @throws IOException
	 */
	public void importIntoDatabase(String tableName, List<String> params, boolean isLeftPrefix) throws IOException {

		/**
		 * Exception handling if at least one parameter is empty.
		 */
		for(String param : params) {
			if(param == null) {
				return;
			}
		}

		String jdbcUrl = ApplicationProperties.getJdbcUrl();

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource(tableName);

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();

		List<String> featureOfInterestIds = Repository.getDataSetIds(jdbcUrl, "feature_of_interest");

		/**
		 * Helper variables for calculating the progress of the current task.
		 */
		int count = featureOfInterestIds.size();
		int index = 0;

		for(String featureOfInterestId : featureOfInterestIds) {

			/**
			 * Update progress bar.
			 */
			index++;
			int currentValue = (int) (100.0 / count * index);
			String currentText = "Reading data set " + index + "/" + count;
			EventBus.publish(new ProgressEvent(false, currentValue, currentText));

			String substring = "";

			/**
			 * May return -1 if Feature of Interest ID does not contain any underscore.
			 */
			int returnVal = featureOfInterestId.lastIndexOf("_");

			if(returnVal != -1) {

				substring = featureOfInterestId.substring(0, returnVal + 1);

				if(isLeftPrefix) {

					if(substring.equals(params.get(0))) {

						String offeringId = params.get(1);

						/**
						 * Check if feature already exists in the database.
						 */
						FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();
						Filter filter = filterFactory.id(filterFactory.featureId(featureOfInterestId + "." + offeringId));

						SimpleFeatureCollection filterResult = featureSource.getFeatures(filter);

						if(!filterResult.isEmpty()) {

							LOG.warn("Relationship '" + featureOfInterestId + "' and '" + offeringId + "' already exists in the database.");

						}

						else {

							featureBuilder.add(featureOfInterestId);
							featureBuilder.add(offeringId);

							if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
								/**
								 * SimpleFeatureSource allows creating own feature ID's.
								 */
								featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
							}

							SimpleFeature feature = featureBuilder.buildFeature(featureOfInterestId + "." + offeringId);
							features.add(feature);

						}

					}

				}

				else {

					if(substring.equals(params.get(1))) {

						String procedureId = params.get(0);

						/**
						 * Check if feature already exists in the database.
						 */
						FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2();
						Filter filter = filterFactory.id(filterFactory.featureId(procedureId + "." + featureOfInterestId));

						SimpleFeatureCollection filterResult = featureSource.getFeatures(filter);

						if(!filterResult.isEmpty()) {

							LOG.warn("Relationship '" + procedureId + "' and '" + featureOfInterestId + "' already exists in the database.");

						}

						else {

							featureBuilder.add(procedureId);
							featureBuilder.add(featureOfInterestId);

							if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
								/**
								 * SimpleFeatureSource allows creating own feature ID's.
								 */
								featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
							}

							SimpleFeature feature = featureBuilder.buildFeature(procedureId + "." + featureOfInterestId);
							features.add(feature);

						}

					}

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
