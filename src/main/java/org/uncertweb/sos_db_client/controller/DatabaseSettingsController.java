package org.uncertweb.sos_db_client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.uncertweb.sos_db_client.event.ErrorMessageEvent;
import org.uncertweb.sos_db_client.event.InformationMessageEvent;
import org.uncertweb.sos_db_client.event.TabbedPaneEvent;
import org.uncertweb.sos_db_client.event.UpdateEvent;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.view.DatabaseSettingsView;

/**
 * Implement controller.
 * 
 * @author Benedikt Klus
 */
public class DatabaseSettingsController implements IController {

	private static final Logger LOG = LogManager.getLogger(DatabaseSettingsController.class);

	private DatabaseSettingsView view;

	private Map<String, String> params;

	/**
	 * Create controller.
	 * 
	 * @param view
	 *            view to be associated with
	 */
	public DatabaseSettingsController(DatabaseSettingsView view) {

		this.view = view;
		addListener();

	}

	/**
	 * Add listener to view.
	 */
	@Override
	public void addListener() {

		view.addBtnConnectListener(new BtnConnectListener());
		view.addMandatoryTextFieldsListener(new MandatoryTextFieldsListener());

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class BtnConnectListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {

				@Override
				protected Boolean doInBackground() throws Exception {

					params = view.getDatabaseSettingsParams();

					/**
					 * Initialize temporary JDBC URL to check if a database connection can be established before writing the parameters to
					 * the appropriate settings file.
					 */
					String _jdbcUrl = "jdbc:postgresql://" + params.get("host") + ":" + params.get("port") + "/" + params.get("database")
							+ "?user=" + params.get("user") + "&password=" + params.get("passwd");

					/**
					 * Add data store to repository.
					 */
					Repository.getDataStore(_jdbcUrl);

					return true;

				}

				@Override
				protected void done() {

					boolean isDone = false;

					try {
						isDone = get();
					} catch(InterruptedException e) {
						LOG.error(e.getLocalizedMessage(), e);
						EventBus.publish(new ErrorMessageEvent(e));
					} catch(ExecutionException e) {
						LOG.error(e.getLocalizedMessage(), e);
						EventBus.publish(new ErrorMessageEvent(e));
					}

					if(isDone) {

						/**
						 * Write parameters first to prevent any kind of RuntimeException.
						 */
						ApplicationProperties.setHost(params.get("host"));
						ApplicationProperties.setPort(params.get("port"));
						ApplicationProperties.setDatabase(params.get("database"));
						ApplicationProperties.setUser(params.get("user"));
						ApplicationProperties.setPasswd(params.get("passwd"));

						if(view.IsCheckBoxSaveSettingsSelected()) {
							ApplicationProperties.writeDatabaseSettingsProperties();
						}

						EventBus.publish(new UpdateEvent());
						EventBus.publish(new InformationMessageEvent("Database connection established."));
						EventBus.publish(new TabbedPaneEvent(true));

					}

					else {
						EventBus.publish(new TabbedPaneEvent(false));
					}

				}

			};

			swingWorker.execute();

		}

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class MandatoryTextFieldsListener implements DocumentListener {

		public void changedUpdate(DocumentEvent event) {

			view.IsMandatoryFieldEmpty();

		}

		public void insertUpdate(DocumentEvent event) {

			view.IsMandatoryFieldEmpty();

		}

		public void removeUpdate(DocumentEvent event) {

			view.IsMandatoryFieldEmpty();

		}

	}

}
