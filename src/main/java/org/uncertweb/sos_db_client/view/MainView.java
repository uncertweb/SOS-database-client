package org.uncertweb.sos_db_client.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.uncertweb.sos_db_client.controller.MainController;
import org.uncertweb.sos_db_client.event.ErrorMessageEvent;
import org.uncertweb.sos_db_client.event.InformationMessageEvent;
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.event.TabbedPaneEvent;
import org.uncertweb.sos_db_client.model.Utility;
import org.uncertweb.sos_db_client.store.Repository;
import org.uncertweb.sos_db_client.store.Resource;

/**
 * Implement view.
 * 
 * @author Benedikt Klus
 */
public class MainView implements IView {

	private static final Logger LOG = LogManager.getLogger(MainView.class);

	private JTabbedPane tabbedPane;

	private JFrame mainFrame;

	private JProgressBar progressBar;

	private JButton btnAbout;

	private JButton btnClose;

	/**
	 * Create view.
	 */
	public MainView() {

		initComponents();
		registerController();

	}

	/**
	 * Initialize components.
	 */
	@Override
	public void initComponents() {

		/*
		 * Register this object on EventBus.
		 */
		AnnotationProcessor.process(this);

		ImageIcon aboutIcon = new ImageIcon(getClass().getResource(Resource.IMAGE_PATH + "About16.gif"));
		ImageIcon stopIcon = new ImageIcon(getClass().getResource(Resource.IMAGE_PATH + "Stop16.gif"));

		/**
		 * Initialize child views.
		 */
		DatabaseSettingsView databaseSettingsView = new DatabaseSettingsView();
		ProcedureView procedureView = new ProcedureView();
		FeatureOfInterestView featureOfInterestView = new FeatureOfInterestView(this);
		PhenomenonView phenomenonView = new PhenomenonView();
		OfferingView offeringView = new OfferingView();
		RelationshipView relationshipView = new RelationshipView();
		ObservationView observationView = new ObservationView(this);
		UncertaintyView uncertaintyView = new UncertaintyView(this);
		FeraView feraView = new FeraView(this);

		tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		/**
		 * Add tabs.
		 */
		tabbedPane.addTab("Database Settings", new JScrollPane(databaseSettingsView.getRootComponent()));
		tabbedPane.addTab("Procedure", new JScrollPane(procedureView.getRootComponent()));
		tabbedPane.addTab("Feature of Interest", new JScrollPane(featureOfInterestView.getRootComponent()));
		tabbedPane.addTab("Phenomenon", new JScrollPane(phenomenonView.getRootComponent()));
		tabbedPane.addTab("Offering", new JScrollPane(offeringView.getRootComponent()));
		tabbedPane.addTab("Relationship", new JScrollPane(relationshipView.getRootComponent()));
		tabbedPane.addTab("Observation", new JScrollPane(observationView.getRootComponent()));
		tabbedPane.addTab("Uncertainty", new JScrollPane(uncertaintyView.getRootComponent()));
		tabbedPane.addTab("FERA Add-On", new JScrollPane(feraView.getRootComponent()));

		EventBus.publish(new TabbedPaneEvent(false));

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setString("Ready");
		progressBar.setStringPainted(true);

		btnAbout = new JButton("About", aboutIcon);
		btnAbout.setToolTipText("About");

		btnClose = new JButton("Close", stopIcon);
		btnClose.setToolTipText("Close");

		JPanel statusBar = new JPanel();
		statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.LINE_AXIS));
		statusBar.add(progressBar);
		statusBar.add(Box.createHorizontalStrut(5));
		statusBar.add(Utility.getVerticalSeparator());
		statusBar.add(Box.createHorizontalStrut(5));
		statusBar.add(btnAbout);
		statusBar.add(btnClose);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		contentPane.add(statusBar, BorderLayout.SOUTH);

		mainFrame = new JFrame();
		mainFrame.setTitle("SOS Database Client");
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setMinimumSize(new Dimension(640, 480));
		mainFrame.setBounds(100, 100, 640, 480);
		mainFrame.setContentPane(contentPane);

	}

	/**
	 * Register controller.
	 */
	@Override
	public void registerController() {

		new MainController(this);

	}

	/**
	 * Get root component.
	 * 
	 * @return root component
	 */
	@Override
	public JFrame getRootComponent() {

		return mainFrame;

	}

	/**
	 * Call this method whenever an object of the associated event type is published over EventBus.
	 * 
	 * @param event
	 *            triggered event
	 */
	@EventSubscriber(eventClass = ProgressEvent.class)
	public void setProgressBarValue(ProgressEvent event) {

		progressBar.setIndeterminate(event.setIndeterminate());
		progressBar.setValue(event.getCurrentValue());
		progressBar.setString(event.getCurrentText());

	}

	/**
	 * Call this method whenever an object of the associated event type is published over EventBus.
	 * 
	 * @param event
	 *            triggered event
	 */
	@EventSubscriber(eventClass = TabbedPaneEvent.class)
	public void setTabbedPaneEnabled(TabbedPaneEvent event) {

		for(int i = 1; i < tabbedPane.getTabCount(); i++) {
			tabbedPane.setEnabledAt(i, event.isEnabled());
		}

	}

	/**
	 * Call this method whenever an object of the associated event type is published over EventBus.
	 * 
	 * @param event
	 *            triggered event
	 */
	@EventSubscriber(eventClass = InformationMessageEvent.class)
	public void showInformationMessageDialog(InformationMessageEvent event) {

		JOptionPane.showMessageDialog(mainFrame, event.getMessage(), "Message", JOptionPane.INFORMATION_MESSAGE);

	}

	/**
	 * Call this method whenever an object of the associated event type is published over EventBus.
	 * 
	 * @param event
	 *            triggered event
	 */
	@EventSubscriber(eventClass = ErrorMessageEvent.class)
	public void showErrorMessageDialog(ErrorMessageEvent event) {

		Exception e = event.getException();

		final JTextArea textArea = new JTextArea(5, 40);
		textArea.setEditable(false);
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));
		textArea.setText(writer.toString());

		/**
		 * Set caret position after appending the text.
		 */
		textArea.setCaretPosition(0);

		JOptionPane.showMessageDialog(mainFrame, new JScrollPane(textArea), "Error", JOptionPane.ERROR_MESSAGE);

	}

	/**
	 * Close view.
	 */
	public void close() {

		try {
			Repository.disposeDataStores();
		} catch(IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
			EventBus.publish(new ErrorMessageEvent(e));
		}

		System.exit(0);

	}

	/**
	 * Get selected tab.
	 * 
	 * @return selected tab
	 */
	public String getTabbedPaneSelectedTab() {

		String title = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
		return title;

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addTabbedPaneListener(ChangeListener l) {

		tabbedPane.addChangeListener(l);

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addMainFrameListener(WindowListener l) {

		mainFrame.addWindowListener(l);

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addBtnAboutListener(ActionListener l) {

		btnAbout.addActionListener(l);

	}

	/**
	 * Add listener.
	 * 
	 * @param l
	 *            listener to be added
	 */
	public void addBtnCloseListener(ActionListener l) {

		btnClose.addActionListener(l);

	}

	/**
	 * Launch application.
	 * 
	 * @param args
	 *            command-line arguments
	 */
	public static void main(String[] args) {

		/**
		 * Make the program use the System L&F.
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(ClassNotFoundException e) {
			LOG.error(e.getLocalizedMessage(), e);
		} catch(InstantiationException e) {
			LOG.error(e.getLocalizedMessage(), e);
		} catch(IllegalAccessException e) {
			LOG.error(e.getLocalizedMessage(), e);
		} catch(UnsupportedLookAndFeelException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}

		MainView mainView = new MainView();
		mainView.getRootComponent().setVisible(true);

	}

}
