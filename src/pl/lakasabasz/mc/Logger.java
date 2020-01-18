package pl.lakasabasz.mc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Logger {
	private static FileWriter log = null;
	private static File logFile = null;
	
	public Logger() {
		checkIfFilenameIsValid();
	}

	public static void logInfo(String txt) {
		checkIfFilenameIsValid();
		try {
			log.write(LocalTime.now().toString() + " INFO: " + txt + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void logWarning(String txt) {
		checkIfFilenameIsValid();
		try {
			log.write(LocalTime.now().toString() + " WARNING: " + txt + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void logError(String txt) {
		checkIfFilenameIsValid();
		try {
			log.write(LocalTime.now().toString() + " ERROR: " + txt + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setLog(FileWriter log) {
		Logger.log = log;
	}
	
	private static void checkIfFilenameIsValid() {
		if(logFile == null || !logFile.getName().equals(LocalDate.now().toString() + ".log")) {
			String filename = LocalDate.now().toString() + ".log";
			logFile = new File(Main.getThisPlugin().getDataFolder() + "/" + filename);
			try {
				log.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				// It is possible, just ignore it
			}
			try {
				log = new FileWriter(Main.getThisPlugin().getDataFolder() + "/" + filename, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String getPath() {
		return logFile.getAbsolutePath();
	}
}
