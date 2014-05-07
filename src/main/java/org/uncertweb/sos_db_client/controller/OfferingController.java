package org.uncertweb.sos_db_client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.uncertweb.sos_db_client.event.ErrorMessageEvent;
import org.uncertweb.sos_db_client.event.InformationMessageEvent;
import org.uncertweb.sos_db_client.event.UpdateEvent;
import org.uncertweb.sos_db_client.model.MainModel;
import org.uncertweb.sos_db_client.view.OfferingView;

/**
 * Implement controller.
 * 
 * @author Benedikt Klus
 */
public class OfferingController implements IController {

	private static final Logger LOG = LogManager.getLogger(OfferingController.class);

	private OfferingView view;

	private MainModel model;

	/**
	 * Create controller.
	 * 
	 * @param view
	 *            view to be associated with
	 * @param model
	 *            model to be associated with
	 */
	public OfferingController(OfferingView view, MainModel model) {

		this.view = view;
		this.model = model;
		addListener();

	}

	/**
	 * Add listener to view.
	 */
	@Override
	public void addListener() {

		view.addBtnImportListener(new BtnImportListener());
		view.addMandatoryTextFieldsListener(new MandatoryTextFieldsListener());

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class BtnImportListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {

				@Override
				protected Boolean doInBackground() throws Exception {

					List<String> params = view.getOfferingParams();

					model.importIntoDatabase("offering", params);

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
						EventBus.publish(new InformationMessageEvent("Offering imported."));
						EventBus.publish(new UpdateEvent());
					}

					view.reset();

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
