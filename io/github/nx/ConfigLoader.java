package io.github.nx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class ConfigLoader {
	public static String target;
	public static boolean isUploadAllowed;

	public static void loadConfig(String configFile) {
		
		File file = new File(configFile);
        if (!file.exists()) {
        	createDefaultConfig(file);
        }
		
		try (FileInputStream fis = new FileInputStream(configFile)) {
			Properties properties = new Properties();
			properties.load(fis);

			target = properties.getProperty("target", target);
			isUploadAllowed = Boolean.parseBoolean(properties.getProperty("isUploadAllowed", "true"));

			System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Loading 'config.cfg'");
		} catch (IOException e) {
			System.err.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[ERROR] Failed to load config: " + e.getMessage());
			System.exit(1);
		}
	}
	
	private static void createDefaultConfig(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("target=Z:/Backup/\n");
            writer.write("isUploadAllowed=false\n");
            System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Default config file created: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[ERROR] Could not create default config: " + e.getMessage());
            System.exit(1);
        }
    }
	
}
