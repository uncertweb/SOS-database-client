package org.uncertweb.sos_db_client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.model.RelationshipModel;
import org.uncertweb.sos_db_client.view.RelationshipView;

/**
 * Implement controller.
 * 
 * @author Benedikt Klus
 */
public class RelationshipController implements IController {

	private static final Logger LOG = LogManager.getLogger(RelationshipController.class);

	private RelationshipView view;

	private RelationshipModel model;

	/**
	 * Create controller.
	 * 
	 * @param view
	 *            view to be associated with
	 * @param model
	 *            model to be associated with
	 */
	public RelationshipController(RelationshipView view, RelationshipModel model) {

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
		view.addMandatoryComboBoxesListener(new MandatoryComboBoxesListener());
		view.addMandatoryTextFieldsListener(new MandatoryTextFieldsListener());

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class BtnImportListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {

			SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {

				@Override
				protected Boolean doInBackground() throws Exception {

					List<String> foiOffParams = view.getFoiOffParams();
					List<String> phenOffParams = view.getPhenOffParams();
					List<String> procFoiParams = view.getProcFoiParams();
					List<String> procOffParams = view.getProcOffParams();
					List<String> procPhenParams = view.getProcPhenParams();
					List<String> procProcParams = view.getProcProcParams();

					model.importIntoDatabase("foi_off", foiOffParams, true);
					model.importIntoDatabase("phen_off", phenOffParams);
					model.importIntoDatabase("proc_foi", procFoiParams, false);
					model.importIntoDatabase("proc_off", procOffParams);
					model.importIntoDatabase("proc_phen", procPhenParams);
					model.importIntoDatabase("proc_proc", procProcParams);

					return true;

				}

				@Override
				protected void done() {

					/**
					 * Update progress bar.
					 */
					EventBus.publish(new ProgressEvent(false, 0, "Ready"));

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
						EventBus.publish(new InformationMessageEvent("Relationship(s) imported."));
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
	class MandatoryComboBoxesListener implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent event) {

			view.IsMandatoryFieldEmpty();

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
