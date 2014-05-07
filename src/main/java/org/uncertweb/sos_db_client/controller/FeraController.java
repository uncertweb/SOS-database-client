package org.uncertweb.sos_db_client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;
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
import org.uncertweb.sos_db_client.model.FeraModel;
import org.uncertweb.sos_db_client.model.TupelHolder;
import org.uncertweb.sos_db_client.view.FeraView;
import org.uncertweb.sos_db_client.view.FileChooserView;
import org.uncertweb.sos_db_client.view.FileSaverView;
import org.uncertweb.sos_db_client.view.IView;

/**
 * Implement controller.
 * 
 * @author Benedikt Klus
 */
public class FeraController implements IController {

	private static final Logger LOG = LogManager.getLogger(FeraController.class);

	private IView parentView;

	private FeraView view;

	private FeraModel model;

	private File[] files;

	/**
	 * Create controller.
	 * 
	 * @param view
	 *            view to be associated with
	 * @param model
	 *            model to be associated with
	 */
	public FeraController(FeraView view, FeraModel model) {

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
		view.addBtnExportListener(new BtnExportListener());
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

				SwingWorker<TupelHolder<Boolean, String>, Void> swingWorker = new SwingWorker<TupelHolder<Boolean, String>, Void>() {

					@Override
					protected TupelHolder<Boolean, String> doInBackground() throws Exception {

						files = fileChooserView.getRootComponent().getSelectedFiles();
						String fileNames = "";

						for(File file : files) {
							fileNames += "\"" + file.getName() + "\" ";
						}

						return new TupelHolder<Boolean, String>(true, fileNames);

					}

					@Override
					protected void done() {

						boolean isDone = false;

						try {
							isDone = get().getFirst();
							view.setTextFieldInputFileText(get().getSecond());
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
	class BtnExportListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			final FileSaverView fileSaverView = new FileSaverView(parentView);

			if(fileSaverView.getReturnVal() == JFileChooser.APPROVE_OPTION) {

				SwingWorker<Boolean, Void> swingWorker = new SwingWorker<Boolean, Void>() {

					@Override
					protected Boolean doInBackground() throws Exception {

						File targetDir = fileSaverView.getRootComponent().getSelectedFile();

						Map<String, String> params = view.getFeraParams();
						int firstYearOfRun = Integer.parseInt(params.get("firstYearOfRun"));

						model.processClimateDataUSOS(files, targetDir, firstYearOfRun);

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
							EventBus.publish(new InformationMessageEvent("File(s) exported."));
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
