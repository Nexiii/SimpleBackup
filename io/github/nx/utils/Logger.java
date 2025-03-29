package io.github.nx.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

	public static final String INFO = "[INFO] ";
	public static final String ERROR = "[ERROR] ";
	public static final String WARNING = "[WARNING] ";
	
	public Logger() {
		log("Initializing logger...", INFO);
	}
	
	public static void log(String text, String TYPE) {
		if(TYPE.equals(INFO)) {
			System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + INFO + text);
		} else if (TYPE.equals(ERROR)) {
			System.err.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + ERROR + text);
		} else if (TYPE.equals(WARNING)) {
			System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + WARNING + text);
		}
	}
	
}
