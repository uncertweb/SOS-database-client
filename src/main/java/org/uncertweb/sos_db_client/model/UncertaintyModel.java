package org.uncertweb.sos_db_client.model;

import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class UncertaintyModel implements IModel {

	private static final Logger LOG = LogManager.getLogger(UncertaintyModel.class);

	private String jdbcUrl;

	private List<String> fileUris;

	private Map<String, String> params;

	/**
	 * Import multiple data sets into uncertainty tables.
	 * 
	 * @param fileUris
	 *            file URI's
	 * @param params
	 *            parameters to be imported
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void importIntoDatabase(List<String> fileUris, Map<String, String> params) throws IOException, ClassNotFoundException,
			SQLException {

		this.jdbcUrl = ApplicationProperties.getJdbcUrl();
		this.fileUris = fileUris;
		this.params = params;

		String range = params.get("observation_id");
		String[] segs = range.split("-");
		int lastDataSetIndex = Integer.parseInt(segs[1]) - Integer.parseInt(segs[0]) + 1;

		/**
		 * Iterate through given range.
		 */
		for(int i = 1; i < lastDataSetIndex + 1; i++) {

			/**
			 * Update progress bar.
			 */
			int currentValue = (int) (100.0 / lastDataSetIndex * i);
			String currentText = "Importing data set " + i + "/" + lastDataSetIndex;
			EventBus.publish(new ProgressEvent(false, currentValue, currentText));

			int valueUnitId = importIntoUValueUnit();
			int uncertaintyValuesId = importIntoUUncertainty(valueUnitId);
			importIntoURealisation(uncertaintyValuesId, i);

			int observationId = Integer.parseInt(segs[0]) + i - 1;
			int uncertaintyId = uncertaintyValuesId;
			importIntoObsUnc(observationId, uncertaintyId);

		}

	}

	/**
	 * Import into u_value_unit.
	 * 
	 * @return value_unit_id
	 * @throws IOException
	 */
	private int importIntoUValueUnit() throws IOException {

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource("u_value_unit");

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();

		SimpleFeatureIterator featureIterator = featureSource.getFeatures().features();

		int valueUnitId = -1;

		try {

			while(featureIterator.hasNext()) {

				SimpleFeature feature = featureIterator.next();
				String _attribute = feature.getAttribute("value_unit").toString();

				if(_attribute.equals(params.get("value_unit"))) {
					valueUnitId = (Integer) feature.getAttribute("value_unit_id");
				}

			}

		} finally {

			if(featureIterator != null) {
				featureIterator.close();
			}

		}

		/**
		 * Add value unit if non existing.
		 */
		if(valueUnitId == -1) {

			valueUnitId = featureSource.getCount(Query.ALL);

			if(valueUnitId == -1) {
				valueUnitId = featureSource.getFeatures().size();
			}

			valueUnitId++;

			featureBuilder.add(valueUnitId);
			featureBuilder.add(params.get("value_unit"));

			if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
				/**
				 * SimpleFeatureSource allows creating own feature ID's.
				 */
				featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
			}

			SimpleFeature feature = featureBuilder.buildFeature(String.valueOf(valueUnitId));
			features.add(feature);

		}

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

		return valueUnitId;

	}

	/**
	 * Import into u_uncertainty.
	 * 
	 * @param valueUnitId
	 *            value_unit_id
	 * @return uncertainty_values_id
	 * @throws IOException
	 */
	private int importIntoUUncertainty(int valueUnitId) throws IOException {

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource("u_uncertainty");

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();

		int uncertaintyId = featureSource.getCount(Query.ALL);

		if(uncertaintyId == -1) {
			uncertaintyId = featureSource.getFeatures().size();
		}

		uncertaintyId++;

		int uncertaintyValuesId = uncertaintyId;

		featureBuilder.add(uncertaintyId);
		featureBuilder.add(uncertaintyValuesId);
		featureBuilder.add("real");
		featureBuilder.add(valueUnitId);

		if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
			/**
			 * SimpleFeatureSource allows creating own feature ID's.
			 */
			featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
		}

		SimpleFeature feature = featureBuilder.buildFeature(String.valueOf(uncertaintyId));
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

		return uncertaintyValuesId;

	}

	/**
	 * Import into u_realisation.
	 * 
	 * @param realisationId
	 *            realisation_id
	 * @param dataSetIndex
	 *            data set index
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void importIntoURealisation(int realisationId, int dataSetIndex) throws IOException, ClassNotFoundException, SQLException {

		Object[] continuousValues = new Object[fileUris.size()];
		Object[] categoricalValues = new Object[fileUris.size()];

		int index = 0;

		for(String fileUri : fileUris) {

			String typeName = fileUri.substring(fileUri.lastIndexOf(Resource.FILE_SEPARATOR) + 1, fileUri.lastIndexOf('.'));

			SimpleFeatureSource featureSource = Repository.getDataStore(fileUri).getFeatureSource(typeName);

			SimpleFeatureIterator featureIterator = featureSource.getFeatures().features();

			SimpleFeature feature = null;

			/**
			 * Get feature at passed index.
			 */
			try {
				for(int i = 1; i < dataSetIndex + 1; i++) {
					if(featureIterator.hasNext()) {
						feature = featureIterator.next();
					}
				}
			} finally {

				if(featureIterator != null) {
					featureIterator.close();
				}

			}

			if(params.get("continuous_values") != null) {
				continuousValues[index] = feature.getAttribute(params.get("continuous_values"));
			}

			if(params.get("categorical_values") != null) {
				categoricalValues[index] = feature.getAttribute(params.get("categorical_values"));
			}

			index++;

		}

		/**
		 * JDBCFeatureSource has no mapping for columns of array type, thus need for using the PostgreSQL JDBC driver directly.
		 */
		Class.forName("org.postgresql.Driver");

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {

			connection = DriverManager.getConnection(jdbcUrl);

			Object weight = params.get("weight");

			Array _continuousValues = null;

			/**
			 * Do not initialize helper variable to be imported if all continuous values are null.
			 */
			for(Object entry : continuousValues) {
				if(entry != null) {
					/**
					 * Type name must be written in lower-case letters.
					 * 
					 * @see <a
					 *      href="http://grepcode.com/file/repo1.maven.org/maven2/postgresql/postgresql/9.0-801.jdbc4/org/postgresql/jdbc2/TypeInfoCache.java#TypeInfoCache.0types"></a>
					 */
					_continuousValues = connection.createArrayOf("numeric", continuousValues);
					break;
				}
			}

			Array _categoricalValues = null;

			/**
			 * Do not initialize helper variable to be imported if all categorical values are null.
			 */
			for(Object entry : categoricalValues) {
				if(entry != null) {
					_categoricalValues = connection.createArrayOf("varchar", categoricalValues);
					break;
				}
			}

			String statement = "INSERT INTO u_realisation(realisation_id, weight, continuous_values, categorical_values) VALUES(?, ?, ?, ?)";

			preparedStatement = connection.prepareStatement(statement);
			preparedStatement.setInt(1, realisationId);
			preparedStatement.setObject(2, weight);
			preparedStatement.setArray(3, _continuousValues);
			preparedStatement.setArray(4, _categoricalValues);
			preparedStatement.executeUpdate();

		} finally {

			if(preparedStatement != null) {
				preparedStatement.close();
			}

			if(connection != null) {
				connection.close();
			}

		}

	}

	/**
	 * Import into obs_unc.
	 * 
	 * @param observationId
	 *            observation_id
	 * @param uncertaintyId
	 *            uncertainty_id
	 * @throws IOException
	 */
	private void importIntoObsUnc(int observationId, int uncertaintyId) throws IOException {

		SimpleFeatureSource featureSource = Repository.getDataStore(jdbcUrl).getFeatureSource("obs_unc");

		final SimpleFeatureType TYPE = featureSource.getSchema();

		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);

		featureBuilder.add(observationId);
		featureBuilder.add(uncertaintyId);
		featureBuilder.add(params.get("gml_identifier"));

		if(featureSource.getQueryCapabilities().isUseProvidedFIDSupported()) {
			/**
			 * SimpleFeatureSource allows creating own feature ID's.
			 */
			featureBuilder.featureUserData(Hints.USE_PROVIDED_FID, true);
		}

		SimpleFeature feature = featureBuilder.buildFeature(observationId + "." + uncertaintyId);

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

}
