package org.uncertweb.sos_db_client.store;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.csv.CSVDataStore;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Implement repository for managing data stores.
 * 
 * @author Benedikt Klus
 */
public class Repository {

	private static Map<String, Object> repository = new HashMap<String, Object>();

	private static List<String> dataStoreIds = new ArrayList<String>();

	/**
	 * Get data store by ID.
	 * 
	 * @param id
	 *            data store ID, must be the URL for files and webservices, the
	 *            JDBC URL if using a database
	 * @return data store
	 * @throws IOException
	 */
	public static synchronized DataStore getDataStore(String id)
			throws IOException {

		/**
		 * Handle escaped URL's properly.
		 * 
		 * @see <a
		 *      href="http://stackoverflow.com/questions/2166039/java-how-to-get-a-file-from-an-escaped-url"></a>
		 */
		id = URLDecoder.decode(id, "UTF-8");

		DataStore dataStore = (DataStore) repository.get(id);

		/**
		 * Add data store to repository if non existing.
		 */
		if (dataStore == null) {

			Map<String, Object> params = new HashMap<String, Object>();

			/**
			 * Handle files and webservices.
			 */
			if (id.startsWith("file")) {
				params.put("url", id);
			}

			/**
			 * Handle databases.
			 */
			else if (id.startsWith("jdbc")) {

				/**
				 * Split JDBC URL of format
				 * "jdbc:postgresql://host:port/database?user=userName&password=pass"
				 * by ":", "/", "?", "=" and "&" delimiter.
				 */
				String[] _segs = id.split(":|/|\\?|=|&");

				/**
				 * Set empty strings to null.
				 */
				for (int i = 0; i < _segs.length; i++) {
					if (_segs[i].isEmpty()) {
						_segs[i] = null;
					}
				}

				/**
				 * Copy JDBC URL segments to new string array of length 11 to
				 * prevent ArrayIndexOutOfBoundsException if no password is
				 * submitted.
				 */
				String[] segs = new String[11];

				for (int i = 0; i < _segs.length; i++) {
					segs[i] = _segs[i];
				}

				/**
				 * Set data store parameter "dbtype" to specify the type of
				 * database to be connected to, "Expose primary keys" to true to
				 * expose primary key columns as attributes.
				 */
				params.put("dbtype", "postgis");
				params.put("host", segs[4]);
				params.put("port", segs[5]);
				params.put("database", segs[6]);
				params.put("user", segs[8]);
				params.put("passwd", segs[10]);
				params.put("Expose primary keys", true);

			}

			dataStore = DataStoreFinder.getDataStore(params);

			/**
			 * Exception handling if DataStoreFinder.getDataStore(params) does
			 * not find CSVDataStore. The GeoTools CSV plugin is unsupported and
			 * still under development.
			 * 
			 * @see <a
			 *      href="http://docs.geotools.org/latest/userguide/library/data/csv.html"></a>
			 */
			if (id.endsWith(".csv")) {

				/**
				 * ID is of format "file:/path/to/file.name", use substring() to
				 * trim ID.
				 */
				String filePath = id.substring(5);
				dataStore = new CSVDataStore(new File(filePath));

			}

			if (dataStore == null) {
				throw new RuntimeException("Data store is null.");
			}

			/**
			 * DataStoreFinder.getDataStore(params) does not throw an exception
			 * if passing wrong parameters, thus need for invoking a method on
			 * data store to check if it is "working" as intended.
			 * 
			 * @see <a
			 *      href="http://sourceforge.net/mailarchive/message.php?msg_id=27659342"></a>
			 */
			else {
				try {
					dataStore.getTypeNames();
				} catch (RuntimeException e) {
					/**
					 * Clean up any internal connections or memory used by the
					 * data store to prevent temporary loss of database
					 * connections.
					 */
					dataStore.dispose();
					dataStore = null;

					throw new RuntimeException(e.getLocalizedMessage());
				}
			}

			repository.put(id, dataStore);
			dataStoreIds.add(id);

		}

		return dataStore;

	}

	/**
	 * Get attribute names.
	 * 
	 * @param dataStoreId
	 *            data store ID
	 * @param typeName
	 *            type name
	 * @return attribute names
	 * @throws IOException
	 */
	public static synchronized List<String> getAttributeNames(
			String dataStoreId, String typeName) throws IOException {

		/**
		 * Handle escaped URL's properly.
		 * 
		 * @see <a
		 *      href="http://stackoverflow.com/questions/2166039/java-how-to-get-a-file-from-an-escaped-url"></a>
		 */
		dataStoreId = URLDecoder.decode(dataStoreId, "UTF-8");

		List<String> attributeNames = new ArrayList<String>();

		List<AttributeDescriptor> _attributeNames = getDataStore(dataStoreId)
				.getFeatureSource(typeName).getSchema()
				.getAttributeDescriptors();

		for (AttributeDescriptor _attributeName : _attributeNames) {
			attributeNames.add(_attributeName.getLocalName());
		}

		return attributeNames;

	}

	/**
	 * Get data set ID's.
	 * 
	 * @param dataStoreId
	 *            data store ID
	 * @param typeName
	 *            type name
	 * @return data set ID's
	 * @throws IOException
	 */
	public static synchronized List<String> getDataSetIds(String dataStoreId,
			String typeName) throws IOException {

		/**
		 * Handle escaped URL's properly.
		 * 
		 * @see <a
		 *      href="http://stackoverflow.com/questions/2166039/java-how-to-get-a-file-from-an-escaped-url"></a>
		 */
		dataStoreId = URLDecoder.decode(dataStoreId, "UTF-8");

		List<String> dataSetIds = new ArrayList<String>();

		SimpleFeatureIterator featureIterator = getDataStore(dataStoreId)
				.getFeatureSource(typeName).getFeatures().features();

		try {
			while (featureIterator.hasNext()) {
				SimpleFeature feature = featureIterator.next();
				/**
				 * getID() returns feature ID of format "typename.ID", use
				 * substring() to trim ID.
				 */
				dataSetIds
						.add(feature.getID().substring(typeName.length() + 1));
			}
		} finally {

			if (featureIterator != null) {
				featureIterator.close();
			}

		}

		return dataSetIds;

	}

	/**
	 * Clean up any internal connections or memory used by the data stores to
	 * prevent temporary loss of database connections.
	 * 
	 * @throws IOException
	 */
	public static void disposeDataStores() throws IOException {

		for (String id : dataStoreIds) {
			Repository.getDataStore(id).dispose();
		}

	}

}
