package org.uncertweb.sos_db_client.store;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implement application properties.
 * 
 * @author Benedikt Klus
 */
public class ApplicationProperties {

	private static final Logger LOG = LogManager.getLogger(ApplicationProperties.class);

	private static final String FILE_PATH = System.getProperty("user.home") + Resource.FILE_SEPARATOR + ".sosdbclient.properties";

	private static String host, port, database, user, passwd;

	/**
	 * Set host
	 * 
	 * @param host
	 *            host to be set
	 */
	public static void setHost(String host) {

		ApplicationProperties.host = host;

	}

	/**
	 * Set port.
	 * 
	 * @param port
	 *            port to be set
	 */
	public static void setPort(String port) {

		ApplicationProperties.port = port;

	}

	/**
	 * Set database.
	 * 
	 * @param database
	 *            database to be set
	 */
	public static void setDatabase(String database) {

		ApplicationProperties.database = database;

	}

	/**
	 * Set user.
	 * 
	 * @param user
	 *            user to be set
	 */
	public static void setUser(String user) {

		ApplicationProperties.user = user;

	}

	/**
	 * Set password.
	 * 
	 * @param passwd
	 *            password to be set
	 */
	public static void setPasswd(String passwd) {

		ApplicationProperties.passwd = passwd;

	}

	/**
	 * Read application properties from external properties file.
	 * 
	 * @return application properties
	 */
	public static Map<String, String> readApplicationProperties() {

		Properties properties = new Properties();

		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(FILE_PATH));
			properties.load(inputStream);
			inputStream.close();
		} catch(FileNotFoundException e) {
			LOG.error(e.getLocalizedMessage(), e);
		} catch(IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}

		Map<String, String> params = new HashMap<String, String>();

		params.put("name", properties.getProperty("applicationName"));
		params.put("version", properties.getProperty("applicationVersion"));
		params.put("buildid", properties.getProperty("applicationBuildId"));

		return params;

	}

	/**
	 * Read database settings properties from external properties file.
	 * 
	 * @return database settings properties
	 */
	public static Map<String, String> readDatabaseSettingsProperties() {

		Properties properties = new Properties();

		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream(new FileInputStream(FILE_PATH));
			properties.load(inputStream);
			inputStream.close();
		} catch(FileNotFoundException e) {
			LOG.error(e.getLocalizedMessage(), e);
		} catch(IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}

		Map<String, String> params = new HashMap<String, String>();

		params.put("host", properties.getProperty("databaseSettingsHost"));
		params.put("port", properties.getProperty("databaseSettingsPort"));
		params.put("database", properties.getProperty("databaseSettingsDatabase"));
		params.put("user", properties.getProperty("databaseSettingsUser"));
		params.put("passwd", properties.getProperty("databaseSettingsPasswd"));

		return params;

	}

	/**
	 * Write database settings properties to external properties file.
	 */
	public static void writeDatabaseSettingsProperties() {

		Properties properties = new Properties();

		properties.setProperty("databaseSettingsHost", host);
		properties.setProperty("databaseSettingsPort", port);
		properties.setProperty("databaseSettingsDatabase", database);
		properties.setProperty("databaseSettingsUser", user);
		properties.setProperty("databaseSettingsPasswd", passwd);

		BufferedOutputStream outputStream = null;

		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(FILE_PATH));
			properties.store(outputStream, null);
			outputStream.close();
		} catch(FileNotFoundException e) {
			LOG.error(e.getLocalizedMessage(), e);
		} catch(IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}

	}

	/**
	 * Get JDBC URL.
	 * 
	 * @return JDBC URL
	 */
	public static String getJdbcUrl() {

		String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + passwd;

		return jdbcUrl;

	}

}
