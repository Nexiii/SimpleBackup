package io.github.nx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
	public static String target;
	public static boolean isUploadToNasAllowed;

	public static void loadConfig(String configFile) {
		
		File file = new File(configFile);
        if (!file.exists()) {
        	createDefaultConfig(file);
        }
		
		try (FileInputStream fis = new FileInputStream(configFile)) {
			Properties properties = new Properties();
			properties.load(fis);

			target = properties.getProperty("target", target);
			isUploadToNasAllowed = Boolean.parseBoolean(properties.getProperty("isUploadToNasAllowed", "true"));

			Logger.log("Loading 'config.cfg'.", Logger.INFO);
		} catch (IOException e) {
			Logger.log("Failed to load config: " + e.getMessage(), Logger.ERROR);
			System.exit(1);
		}
	}
	
	private static void createDefaultConfig(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("target=Z:/Backup/\n");
            writer.write("isUploadToNasAllowed=false\n");
            Logger.log("Default config file created: " + file.getAbsolutePath(), Logger.INFO);
        } catch (IOException e) {
        	Logger.log("Could not create default config: " + e.getMessage(), Logger.ERROR);
            System.exit(1);
        }
    }
	
}
