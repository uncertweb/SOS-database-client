package org.uncertweb.sos_db_client.model;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

/**
 * Implement utility.
 * 
 * @author Benedikt Klus
 */
public class Utility {

	/**
	 * Set empty strings to null.
	 * 
	 * @param list
	 *            list to be processed
	 */
	public static void setEmptyStringsToNull(List<String> list) {

		int index = 0;

		for(String entry : list) {
			if(entry.isEmpty()) {
				list.set(index, null);
			}
			index++;
		}

	}

	/**
	 * Set empty strings to null.
	 * 
	 * @param map
	 *            map to be processed
	 */
	public static void setEmptyStringsToNull(Map<String, String> map) {

		for(Map.Entry<String, String> entry : map.entrySet()) {
			if(entry.getValue().isEmpty()) {
				entry.setValue(null);
			}
		}

	}

	/**
	 * Get vertical separator. JSeparator has unbounded maximum size in both directions, thus limiting maximum width to preferred width
	 * while keeping maximum height unchanged since preferred height is 0.
	 * 
	 * @return vertical separator
	 */
	public static JSeparator getVerticalSeparator() {

		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		Dimension dimension = new Dimension(separator.getPreferredSize().width, separator.getMaximumSize().height);
		separator.setMaximumSize(dimension);

		return separator;

	}

	/**
	 * Add horizontal scroll bar to combo box drop down list. Place method after combo box items insertion code.
	 * 
	 * @param comboBox
	 *            combo box to be adjusted
	 */
	public static void adjustScrollBar(JComboBox comboBox) {

		if(comboBox.getItemCount() == 0) {
			return;
		}

		Object comp = comboBox.getUI().getAccessibleChild(comboBox, 0);

		if(!(comp instanceof JPopupMenu)) {
			return;
		}

		JPopupMenu popup = (JPopupMenu) comp;

		JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
		scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	}

}
