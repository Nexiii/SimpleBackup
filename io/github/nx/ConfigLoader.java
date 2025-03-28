package io.github.nx;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class ConfigLoader {
	public static String target = "Z:/Backup/";
	public static boolean isUploadAllowed = true;

	public static void loadConfig(String configFile) {
		
		File file = new File(configFile);
        if (!file.exists()) {
        	System.err.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "Failed to load config");
			System.exit(1);
        }
		
		try (FileInputStream fis = new FileInputStream(configFile)) {
			Properties properties = new Properties();
			properties.load(fis);

			target = properties.getProperty("target", target); // Fallback to default if missing
			isUploadAllowed = Boolean.parseBoolean(properties.getProperty("isUploadAllowed", "true"));

			System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "Loading 'config.cfg'");
		} catch (IOException e) {
			System.err.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "Failed to load config: " + e.getMessage());
			System.exit(1);
		}
	}
}
