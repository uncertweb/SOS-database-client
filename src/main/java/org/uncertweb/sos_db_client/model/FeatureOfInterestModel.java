package org.uncertweb.sos_db_client.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement model.
 * 
 * @author Benedikt Klus
 */
public class FeatureOfInterestModel {

	private static final Logger LOG = LogManager.getLogger(FeatureOfInterestModel.class);

	/**
	 * Import multiple data sets into database.
	 * 
	 * @param fileUris
	 *            file URI's
	 * @param params
	 *            parameters to be imported
	 * @throws IOException
	 * @throws FactoryException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("resource")
	public void importIntoDatabase(List<String> fileUris, List<String> params) throws IOException, FactoryException, SQLException,
			ClassNotFoundException {

		String jdbcUrl = ApplicationProperties.getJdbcUrl();

		for(String fileUri : fileUris) {

			String typeName = fileUri.substring(fileUri.lastIndexOf(Resource.FILE_SEPARATOR) + 1, fileUri.lastIndexOf('.'));

			SimpleFeatureSource featureSource = Repository.getDataStore(fileUri).getFeatureSource(typeName);

			/**
			 * Helper variables for calculating the progress of the current task.
			 */
			int count = featureSource.getCount(Query.ALL);
			int index = 0;

			SimpleFeatureIterator featureIterator = featureSource.getFeatures().features();

			/**
			 * Write attribute values to the appropriate table column.
			 */
			try {

				while(featureIterator.hasNext()) {

					/**
					 * Update progress bar.
					 */
					index++;
					int currentValue = (int) (100.0 / count * index);
					String currentText = "Importing data set " + index + "/" + count;
					EventBus.publish(new ProgressEvent(false, currentValue, currentText));

					SimpleFeature feature = featureIterator.next();

					/**
					 * GeoTools obviously writes the assigned feature type CRS to geometry columns, which are called "the_geom" as
					 * conventionally defined in PostGIS, only, thus need for using the PostgreSQL JDBC driver directly.
					 */
					Class.forName("org.postgresql.Driver");

					Connection connection = null;
					PreparedStatement preparedStatement = null;

					try {

						connection = DriverManager.getConnection(jdbcUrl);

						/**
						 * Check if feature already exists in the database.
						 */
						Object featureOfInterestId = feature.getAttribute(params.get(0));

						String statement = "SELECT * FROM feature_of_interest WHERE feature_of_interest_id = '" + featureOfInterestId
								+ "';";

						preparedStatement = connection.prepareStatement(statement);

						/**
						 * executeUpdate() cannot be used when issuing a statement that returns a result, thus need for using
						 * executeQuery().
						 */
						ResultSet resultSet = preparedStatement.executeQuery();

						if(resultSet.next()) {

							LOG.warn("Feature '" + featureOfInterestId + "' already exists in the database.");

						}

						else {

							List<Object> featureAttributes = new ArrayList<Object>();

							for(String param : params) {

								if(param != null) {

									/**
									 * Check if parameter is attribute name or value.
									 */
									if(feature.getAttribute(param) == null) {
										featureAttributes.add(param);
									} else {
										featureAttributes.add(feature.getAttribute(param));
									}

								} else {
									featureAttributes.add(null);
								}

							}

							CoordinateReferenceSystem crs = featureSource.getSchema().getCoordinateReferenceSystem();
							int epsgCode = CRS.lookupEpsgCode(crs, true);
							Object geom = featureAttributes.get(3);

							statement = "INSERT INTO feature_of_interest(feature_of_interest_id, feature_of_interest_name, feature_of_interest_description, geom, feature_type, schema_link) VALUES (?, ?, ?, GeometryFromText('"
									+ geom + "', " + epsgCode + "), ?, ?)";

							preparedStatement = connection.prepareStatement(statement);
							preparedStatement.setObject(1, featureAttributes.get(0));
							preparedStatement.setObject(2, featureAttributes.get(1));
							preparedStatement.setObject(3, featureAttributes.get(2));
							preparedStatement.setObject(4, featureAttributes.get(4));
							preparedStatement.setObject(5, featureAttributes.get(5));
							preparedStatement.executeUpdate();

						}

					} finally {

						if(preparedStatement != null) {
							preparedStatement.close();
						}

						if(connection != null) {
							connection.close();
						}

					}

				}

			} finally {

				if(featureIterator != null) {
					featureIterator.close();
				}

			}

		}

	}

}