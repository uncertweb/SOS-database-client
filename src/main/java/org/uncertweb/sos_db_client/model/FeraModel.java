package org.uncertweb.sos_db_client.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bushe.swing.event.EventBus;
import org.uncertweb.sos_db_client.event.ProgressEvent;
import org.uncertweb.sos_db_client.store.Resource;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * Implement model.
 * 
 * @author Benedikt Klus
 */
public class FeraModel implements IModel {

	private static final Logger LOG = LogManager.getLogger(FeraModel.class);

	/**
	 * Process climate data for U-SOS import.
	 * 
	 * @param files
	 *            files
	 * @param targetDir
	 *            target directory
	 * @param firstYearOfRun
	 *            first year of run
	 * @throws IOException
	 */
	public void processClimateDataUSOS(File[] files, File targetDir, int firstYearOfRun) throws IOException {

		/**
		 * Helper variables for calculating the progress of the current task.
		 */
		int count = files.length;
		int index = 0;

		/**
		 * Iterate through files.
		 */
		for(File file : files) {

			/**
			 * Update progress bar.
			 */
			index++;
			int currentValue = (int) (100.0 / count * index);
			String currentText = "Writing file " + index + "/" + count;
			EventBus.publish(new ProgressEvent(false, currentValue, currentText));

			/**
			 * Determine target file path.
			 */
			String fileName = file.getName();

			String targetFilePath = targetDir.getPath() + Resource.FILE_SEPARATOR + fileName.substring(0, fileName.lastIndexOf("."))
					+ "_prep.csv";

			CSVReader reader = new CSVReader(new FileReader(file));

			CSVWriter writer = new CSVWriter(new FileWriter(targetFilePath));

			try {

				String[] nextLine;

				/**
				 * Write column headers.
				 */
				String[] entries = { "time_stamp", "day_count", "transition", "precip_dtotal", "temp_dmin", "temp_dmax",
						"vapourpressure_dmean", "relhum_dmean", "sunshine_dtotal", "diffradt_dtotal", "dirradt_dtotal", "pet_dmean" };

				writer.writeNext(entries);

				/**
				 * Iterate through file with nextLine[] representing an array of values from the current line.
				 */
				while((nextLine = reader.readNext()) != null) {

					/**
					 * Convert year, month and day to format "yyyy-MM-dd'T'HH:mm:ssZ", and then save it to the first index. Left pad month
					 * and day with zero if required.
					 */
					int _year = firstYearOfRun + Integer.parseInt(nextLine[0]) - 3001;
					int _month = Integer.parseInt(nextLine[1]);
					int _day = Integer.parseInt(nextLine[2]);

					/**
					 * Uncomment code block if monthly instead of daily timestamps are needed.
					 */
					// if (_day != 1) {
					// continue;
					// }

					String year = String.valueOf(_year);
					String month = String.format("%02d", _month);
					String day = String.format("%02d", _day);

					entries[0] = year + "-" + month + "-" + day + " 00:00:00";

					for(int i = 3; i < nextLine.length; i++) {
						entries[i - 2] = nextLine[i];
					}

					/**
					 * Write line to file.
					 */
					writer.writeNext(entries);

				}

			} finally {

				if(reader != null) {
					reader.close();
				}

				if(writer != null) {
					writer.close();
				}

			}

		}

	}

	/**
	 * Process climate data for Emulator import.
	 * 
	 * The columns hold the values for the monthly averages of minimum temperature, maximum temperature and evapotranspiration as well as
	 * the monthly totals of precipitation starting with all four properties of the first month in the stated order.
	 * 
	 * The rows hold the values for realisation 1 to 1000 of all years starting with all 1000 realisations of the first year.
	 * 
	 * @param realisations
	 *            unprepared source input files
	 * @param targetDir
	 *            target directory
	 * @throws IOException
	 */
	public void processClimateDataEmulator(File[] realisations, File targetDir) throws IOException {

		String targetFilePath = targetDir.getPath() + Resource.FILE_SEPARATOR + "climate_data_input.csv";

		CSVReader reader = null;

		CSVWriter writer = new CSVWriter(new FileWriter(targetFilePath));

		/**
		 * Year one of each run is 3001 in the spreadsheet.
		 */
		int firstYearOfRun = 3001;

		/**
		 * Helper variables for calculating the progress of the current task.
		 */
		int count = 29;
		int index = 0;

		try {

			/**
			 * Iterate through years. Each run has to span a 30 year interval. Can calculate values for 29 years only due to crop year shift
			 * of calendar year.
			 */
			for(int currentYear = firstYearOfRun; currentYear < firstYearOfRun + 29; currentYear++) {

				/**
				 * Update progress bar.
				 */
				index++;
				int currentValue = (int) (100.0 / count * index);
				String currentText = "Calculating values of crop year " + index + "/" + count;
				EventBus.publish(new ProgressEvent(false, currentValue, currentText));

				/**
				 * Iterate through realisations.
				 */
				for(File realisation : realisations) {

					reader = new CSVReader(new FileReader(realisation));

					String[] nextLine = reader.readNext();

					/**
					 * Get line of January 1 of current year.
					 */
					while(!nextLine[0].equals(String.valueOf(currentYear))) {
						nextLine = reader.readNext();
					}

					/**
					 * Get line of October 1 of current year.
					 */
					while(!nextLine[1].equals("10")) {
						nextLine = reader.readNext();
					}

					/**
					 * Four properties per month to calculate makes 48 properties per year.
					 */
					List<Double> entries = new ArrayList<Double>();

					/**
					 * Crop year runs from 1 October of the current year to 30 September of the following year, and thus does not coincide
					 * with the calendar year.
					 */
					String[] cropYear = { "10", "11", "12", "01", "02", "03", "04", "05", "06", "07", "08", "09" };

					for(String month : cropYear) {

						List<Double> minTemp = new ArrayList<Double>();
						List<Double> maxTemp = new ArrayList<Double>();
						List<Double> precip = new ArrayList<Double>();
						List<Double> pet = new ArrayList<Double>();

						/**
						 * Add daily values.
						 */
						while(nextLine[1].equals(month)) {

							minTemp.add(Double.parseDouble(nextLine[6]));
							maxTemp.add(Double.parseDouble(nextLine[7]));
							precip.add(Double.parseDouble(nextLine[5]));
							pet.add(Double.parseDouble(nextLine[13]));

							nextLine = reader.readNext();

						}

						/**
						 * Calculate monthly average of minimum temperature.
						 */
						double sumMinTemp = 0;

						for(double value : minTemp) {
							sumMinTemp += value;
						}

						entries.add(sumMinTemp / minTemp.size());

						/**
						 * Calculate monthly average of maximum temperature.
						 */
						double sumMaxTemp = 0;

						for(double value : maxTemp) {
							sumMaxTemp += value;
						}

						entries.add(sumMaxTemp / maxTemp.size());

						/**
						 * Calculate total of precipitation.
						 */
						double totalPrecip = 0;

						for(double value : precip) {
							totalPrecip += value;
						}

						entries.add(totalPrecip);

						/**
						 * Calculate monthly average of potential evapotranspiration.
						 */
						double sumPet = 0;

						for(double value : pet) {
							sumPet += value;
						}

						entries.add(sumPet / pet.size());

					}

					String[] _entries = new String[entries.size()];

					int _index = 0;

					for(Double entry : entries) {
						_entries[_index] = String.valueOf(entry);
						_index++;
					}

					writer.writeNext(_entries);

				}

			}

		} finally {

			if(reader != null) {
				reader.close();
			}

			if(writer != null) {
				writer.close();
			}

		}

	}

	/**
	 * Aggregate climate data. Choose the algorithm to be used. Initialize with 0 in case the monthly values for each phenomenon should be
	 * calculated (results in a separate output for each realisation), initialize with 1 in case the monthly averages of the monthly values
	 * should be calculated (results in one output for all realisations).
	 * 
	 * @param realisations
	 *            unprepared source input files
	 * @param targetDir
	 *            target directory
	 * @throws IOException
	 */
	public void aggregateClimateData(File[] realisations, File targetDir, int algorithm) throws IOException {

		if(algorithm == 0) {

			/**
			 * Helper variables for calculating the progress of the current task.
			 */
			int count = realisations.length;
			int index = 0;

			/**
			 * Iterate through realisations.
			 */
			for(File realisation : realisations) {

				/**
				 * Update progress bar.
				 */
				index++;
				int currentValue = (int) (100.0 / count * index);
				String currentText = "Calculating monthly values for realisation " + index + "/" + count;
				EventBus.publish(new ProgressEvent(false, currentValue, currentText));

				String targetFilePath = targetDir.getPath() + Resource.FILE_SEPARATOR + "climate_data_agg0_" + index + ".csv";

				CSVReader reader = new CSVReader(new FileReader(realisation));

				CSVWriter writer = new CSVWriter(new FileWriter(targetFilePath));

				/** Write header information. */
				String[] header = { "temp_dmin_ma", "temp_dmax_ma", "precip_dtotal_mt", "pet_dmean_ma" };
				writer.writeNext(header);

				/**
				 * Year one of each run is 3001 in the spreadsheet.
				 */
				int firstYearOfRun = 3001;

				try {

					/**
					 * Iterate through years. Each run has to span a 30 year interval.
					 */
					for(int currentYear = firstYearOfRun; currentYear < firstYearOfRun + 30; currentYear++) {

						String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };

						/** Iterate through months. */
						for(String month : months) {

							/**
							 * Monthly average for minimum temperature, maximum temperature and evapotranspiration as well as monthly total
							 * for precipitation for the current month.
							 */
							List<Double> entries = new ArrayList<Double>();

							String[] nextLine = reader.readNext();

							/**
							 * Get line of current month of current year.
							 */
							while(!(nextLine[0].equals(String.valueOf(currentYear)) && nextLine[1].equals(month))) {
								nextLine = reader.readNext();
							}

							List<Double> minTemp = new ArrayList<Double>();
							List<Double> maxTemp = new ArrayList<Double>();
							List<Double> precip = new ArrayList<Double>();
							List<Double> pet = new ArrayList<Double>();

							/**
							 * Add daily values.
							 */
							while(nextLine != null && nextLine[1].equals(month)) {

								minTemp.add(Double.parseDouble(nextLine[6]));
								maxTemp.add(Double.parseDouble(nextLine[7]));
								precip.add(Double.parseDouble(nextLine[5]));
								pet.add(Double.parseDouble(nextLine[13]));

								nextLine = reader.readNext();

							}

							/**
							 * Calculate monthly average of minimum temperature for the current realisation.
							 */
							double sumMinTemp = 0;

							for(double value : minTemp) {
								sumMinTemp += value;
							}

							entries.add(sumMinTemp / minTemp.size());

							/**
							 * Calculate monthly average of maximum temperature for the current realisation.
							 */
							double sumMaxTemp = 0;

							for(double value : maxTemp) {
								sumMaxTemp += value;
							}

							entries.add(sumMaxTemp / maxTemp.size());

							/**
							 * Calculate monthly total of precipitation for the current realisation.
							 */
							double sumPrecip = 0;

							for(double value : precip) {
								sumPrecip += value;
							}

							entries.add(sumPrecip);

							/**
							 * Calculate monthly average of potential evapotranspiration for the current realisation.
							 */
							double sumPet = 0;

							for(double value : pet) {
								sumPet += value;
							}

							entries.add(sumPet / pet.size());

							String[] _entries = new String[entries.size()];

							int _index = 0;

							for(Double entry : entries) {
								_entries[_index] = String.valueOf(entry);
								_index++;
							}

							writer.writeNext(_entries);

						}

					}

				} finally {

					if(reader != null) {
						reader.close();
					}

					if(writer != null) {
						writer.close();
					}

				}

			}

		} else if(algorithm == 1) {

			String targetFilePath = targetDir.getPath() + Resource.FILE_SEPARATOR + "climate_data_agg1.csv";

			CSVReader reader = null;

			CSVWriter writer = new CSVWriter(new FileWriter(targetFilePath));

			/** Write header information. */
			String[] header = { "temp_dmin_ma", "temp_dmax_ma", "precip_dtotal_mt", "pet_dmean_ma" };
			writer.writeNext(header);

			/**
			 * Helper variables for calculating the progress of the current task.
			 */
			int count = 30;
			int index = 0;

			/**
			 * Year one of each run is 3001 in the spreadsheet.
			 */
			int firstYearOfRun = 3001;

			try {

				/**
				 * Iterate through years. Each run has to span a 30 year interval.
				 */
				for(int currentYear = firstYearOfRun; currentYear < firstYearOfRun + 30; currentYear++) {

					/**
					 * Update progress bar.
					 */
					index++;
					int currentValue = (int) (100.0 / count * index);
					String currentText = "Calculating values of year " + index + "/" + count;
					EventBus.publish(new ProgressEvent(false, currentValue, currentText));

					String[] months = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };

					/** Iterate through months. */
					for(String month : months) {

						/**
						 * Monthly averages for minimum temperature, maximum temperature and evapotranspiration as well as monthly totals
						 * for precipitation of all selected realisations for the current month.
						 */
						List<Double> minTempAvg = new ArrayList<Double>();
						List<Double> maxTempAvg = new ArrayList<Double>();
						List<Double> precipTotal = new ArrayList<Double>();
						List<Double> petAvg = new ArrayList<Double>();

						/**
						 * Iterate through realisations.
						 */
						for(File realisation : realisations) {

							reader = new CSVReader(new FileReader(realisation));

							String[] nextLine = reader.readNext();

							/**
							 * Get line of current month of current year.
							 */
							while(!(nextLine[0].equals(String.valueOf(currentYear)) && nextLine[1].equals(month))) {
								nextLine = reader.readNext();
							}

							List<Double> minTemp = new ArrayList<Double>();
							List<Double> maxTemp = new ArrayList<Double>();
							List<Double> precip = new ArrayList<Double>();
							List<Double> pet = new ArrayList<Double>();

							/**
							 * Add daily values.
							 */
							while(nextLine != null && nextLine[1].equals(month)) {

								minTemp.add(Double.parseDouble(nextLine[6]));
								maxTemp.add(Double.parseDouble(nextLine[7]));
								precip.add(Double.parseDouble(nextLine[5]));
								pet.add(Double.parseDouble(nextLine[13]));

								nextLine = reader.readNext();

							}

							/**
							 * Calculate monthly average of minimum temperature for the current realisation.
							 */
							double sumMinTemp = 0;

							for(double value : minTemp) {
								sumMinTemp += value;
							}

							minTempAvg.add(sumMinTemp / minTemp.size());

							/**
							 * Calculate monthly average of maximum temperature for the current realisation.
							 */
							double sumMaxTemp = 0;

							for(double value : maxTemp) {
								sumMaxTemp += value;
							}

							maxTempAvg.add(sumMaxTemp / maxTemp.size());

							/**
							 * Calculate monthly total of precipitation for the current realisation.
							 */
							double sumPrecip = 0;

							for(double value : precip) {
								sumPrecip += value;
							}

							precipTotal.add(sumPrecip);

							/**
							 * Calculate monthly average of potential evapotranspiration for the current realisation.
							 */
							double sumPet = 0;

							for(double value : pet) {
								sumPet += value;
							}

							petAvg.add(sumPet / pet.size());

						}

						List<Double> entries = new ArrayList<Double>();

						/**
						 * Calculate monthly average of minimum temperature of all selected realisations for the current month.
						 */
						double sumMinTempAvg = 0;

						for(double value : minTempAvg) {
							sumMinTempAvg += value;
						}

						entries.add(sumMinTempAvg / minTempAvg.size());

						/**
						 * Calculate monthly average of maximum temperature of all selected realisations for the current month.
						 */
						double sumMaxTempAvg = 0;

						for(double value : maxTempAvg) {
							sumMaxTempAvg += value;
						}

						entries.add(sumMaxTempAvg / maxTempAvg.size());

						/**
						 * Calculate monthly average of precipitation of all selected realisations for the current month.
						 */
						double sumPrecipAvg = 0;

						for(double value : precipTotal) {
							sumPrecipAvg += value;
						}

						entries.add(sumPrecipAvg / precipTotal.size());

						/**
						 * Calculate monthly average of potential evapotranspiration of all selected realisations for the current month.
						 */
						double sumPetAvg = 0;

						for(double value : petAvg) {
							sumPetAvg += value;
						}

						entries.add(sumPetAvg / petAvg.size());

						String[] _entries = new String[entries.size()];

						int _index = 0;

						for(Double entry : entries) {
							_entries[_index] = String.valueOf(entry);
							_index++;
						}

						writer.writeNext(_entries);

					}

				}

			} finally {

				if(reader != null) {
					reader.close();
				}

				if(writer != null) {
					writer.close();
				}

			}

		} else {

			LOG.info("Please enter either 0 or 1 for the algorithm to be executed.");

		}

	}

}