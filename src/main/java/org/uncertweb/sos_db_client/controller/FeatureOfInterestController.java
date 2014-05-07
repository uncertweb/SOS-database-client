package org.uncertweb.sos_db_client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.uncertweb.sos_db_client.event.ErrorMessageEvent;
import org.uncertweb.sos_db_client.event.InformationMessageEvent;
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.event.UpdateEvent;
import org.uncertweb.sos_db_client.model.FeatureOfInterestModel;
import org.uncertweb.sos_db_client.model.TripelHolder;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.store.Resource;
import org.uncertweb.sos_db_client.view.FeatureOfInterestView;
import org.uncertweb.sos_db_client.view.FileChooserView;
import org.uncertweb.sos_db_client.view.IView;

/**
 * Implement controller.
 * 
 * @author Benedikt Klus
 */
public class FeatureOfInterestController implements IController {

	private static final Logger LOG = LogManager.getLogger(FeatureOfInterestController.class);

	private IView parentView;

	private FeatureOfInterestView view;

	private FeatureOfInterestModel model;

	private List<String> fileUris;

	/**
	 * Create controller.
	 * 
	 * @param view
	 *            view to be associated with
	 * @param model
	 *            model to be associated with
	 */
	public FeatureOfInterestController(FeatureOfInterestView view, FeatureOfInterestModel model) {

		this.view = view;
		this.model = model;
		this.parentView = view.getParentView();
		addListener();

	}

	/**
	 * Add listener to view.
	 */
	@Override
	public void addListener() {

		view.addBtnAddListener(new BtnAddListener());
		view.addBtnImportListener(new BtnImportListener());
		view.addMandatoryComboBoxesListener(new MandatoryComboBoxesListener());
		view.addMandatoryTextFieldsListener(new MandatoryTextFieldsListener());

	}

	/**
	 * Implement listener.
	 * 
	 * @author Benedikt Klus
	 */
	class BtnAddListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			final FileChooserView fileChooserView = new FileChooserView(parentView);

			if(fileChooserView.getReturnVal() == JFileChooser.APPROVE_OPTION) {

				SwingWorker<TripelHolder<Boolean, String, List<String>>, Void> swingWorker = new SwingWorker<TripelHolder<Boolean, String, List<String>>, Void>() {

					@Override
					protected TripelHolder<Boolean, String, List<String>> doInBackground() throws Exception {

						File[] files = ((JFileChooser) fileChooserView.getRootComponent()).getSelectedFiles();
						String fileNames = "";
						fileUris = new ArrayList<String>();

						for(File file : files) {
							fileUris.add(file.toURI().toString());
							fileNames += "\"" + file.getName() + "\" ";
						}

						String fileUri = fileUris.get(0);
						String typeName = fileUri.substring(fileUri.lastIndexOf(Resource.FILE_SEPARATOR) + 1, fileUri.lastIndexOf('.'));
						List<String> attributeNames = Repository.getAttributeNames(fileUri, typeName);

						return new TripelHolder<Boolean, String, List<String>>(true, fileNames, attributeNames);

					}

					@Override
					protected void done() {

						boolean isDone = false;

						try {
							isDone = get().getFirst();
							view.setTextFieldInputFileText(get().getSecond().toString());
							view.addInputFileComboBoxItems(get().getThird());
						} catch(InterruptedException e) {
							LOG.error(e.getLocalizedMessage(), e);
							EventBus.publish(new ErrorMessageEvent(e));
						} catch(ExecutionException e) {
							LOG.error(e.getLocalizedMessage(), e);
							EventBus.publish(new ErrorMessageEvent(e));
						}

						if(isDone) {
							EventBus.publish(new InformationMessageEvent("File(s) added."));
						}

						view.reset();

					}

				};

				swingWorker.execute();

			}

		}

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

					List<String> params = view.getFeatureOfInterestParams();

					model.importIntoDatabase(fileUris, params);

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
						EventBus.publish(new InformationMessageEvent("Feature(s) of Interest imported."));
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
