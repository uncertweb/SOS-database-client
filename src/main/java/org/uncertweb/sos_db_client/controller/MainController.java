package org.uncertweb.sos_db_client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;

import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bushe.swing.event.EventBus;
import org.uncertweb.sos_db_client.event.InformationMessageEvent;
import org.uncertweb.sos_db_client.event.UpdateEvent;
import org.uncertweb.sos_db_client.store.ApplicationProperties;
import org.uncertweb.sos_db_client.view.MainView;

/**
 * Implement controller.
 * 
 * @author Benedikt Klus
 */
public class MainController implements IController {

	private MainView view;

	/**
	 * Create controller.
	 * 
	 * @param view
	 *            view to be associated with
	 */
	public MainController(MainView view) {

		this.view = view;
		addListener();

	}

	/**
	 * Add listener to view.
	 */
	@Override
	public void addListener() {

		// view.addTabbedPaneListener(new TabbedPaneListener());
		view.addMainFrameListener(new MainFrameListener());
		view.addBtnAboutListener(new BtnAboutListener());
		view.addBtnCloseListener(new BtnCloseListener());

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class TabbedPaneListener implements ChangeListener {

		public void stateChanged(ChangeEvent event) {

			SwingWorker<Void, Void> swingWorker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {

					String title = view.getTabbedPaneSelectedTab();

					if(title.equals("Relationship")) {
						EventBus.publish(new UpdateEvent());
					}

					if(title.equals("Observation")) {
						EventBus.publish(new UpdateEvent());
					}

					return null;

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
	class MainFrameListener implements WindowListener {

		public void windowActivated(WindowEvent event) {
			// TODO Auto-generated method stub

		}

		public void windowClosed(WindowEvent event) {
			// TODO Auto-generated method stub

		}

		public void windowClosing(WindowEvent event) {

			view.close();

		}

		public void windowDeactivated(WindowEvent event) {
			// TODO Auto-generated method stub

		}

		public void windowDeiconified(WindowEvent event) {
			// TODO Auto-generated method stub

		}

		public void windowIconified(WindowEvent event) {
			// TODO Auto-generated method stub

		}

		public void windowOpened(WindowEvent event) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class BtnAboutListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			Map<String, String> params = ApplicationProperties.readApplicationProperties();

			EventBus.publish(new InformationMessageEvent("<html>Version: " + params.get("version") + "<br />Build-ID: "
					+ params.get("buildid") + "</html>"));

		}

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class BtnCloseListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			view.close();

		}

	}

}
