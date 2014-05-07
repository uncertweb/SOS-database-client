package org.uncertweb.sos_db_client.model;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Implement files filter.
 * 
 * @author Benedikt Klus
 */
public class SupportedFilesFilter extends FileFilter {

	@Override
	public boolean accept(File file) {

		if (file.isDirectory()) {
			return true;
		}

		/**
		 * Get the file extension.
		 */
		String extension = null;

		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');

		if (i > 0 && i < fileName.length() - 1) {
			extension = fileName.substring(i + 1).toLowerCase();
		}

		if (extension != null) {
			if (extension.equals("csv") || extension.equals("shp")) {
				return true;
			} else {
				return false;
			}
		}

		return false;

	}

	@Override
	public String getDescription() {

		return "Supported files (*.csv, *.shp)";

	}

}
