package io.github.nx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.github.nx.utils.BackupHistory;
import io.github.nx.utils.ConfigLoader;
import io.github.nx.utils.FileUtils;
import io.github.nx.utils.Logger;
import io.github.nx.utils.UpdateChecker;
import io.github.nx.utils.ZipUtils;

public class SimpleBackup {

	private static final String LIST_FILE = "list.txt";
	private static final String CONFIG_FILE = "config.cfg";
	public static final String VERSION = "v0.8.1_2025";
	public static final String VERSIONURL = "https://raw.githubusercontent.com/Nexiii/SimpleBackup/refs/heads/main/version.txt";

	public static void main(String[] args){
		Logger.log("Starting SimpleBackup " +VERSION+"!", Logger.INFO);
		setCmdTitle("SimpleBackup " + VERSION + " made by Nexiii / Malte");
		UpdateChecker.checkUpdate();
        ConfigLoader.loadConfig(CONFIG_FILE);

        String target = ConfigLoader.target;
        boolean isUploadToNasAllowed = ConfigLoader.isUploadToNasAllowed;
        String dateFormattedNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        
        if(!new File(LIST_FILE).exists()) {
        	try {
    			new File(LIST_FILE).createNewFile();
    			Logger.log("Created 'list.txt' file!", Logger.INFO);
    			Logger.log("Please add a directory to the 'list.txt' and restart the program.", Logger.INFO);
    			System.exit(1);
    		} catch (IOException e) {
    			Logger.log("Could not create 'list.txt': " + e.getMessage(), Logger.ERROR);
    		}
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(LIST_FILE))) {
        	Logger.log("Reading '"+ LIST_FILE +"'.", Logger.INFO);   
        	if (FileUtils.isFileEmpty(LIST_FILE)) {
                Logger.log("'list.txt' is empty", Logger.ERROR);
            	System.exit(1);
            }
            String line;
            while ((line = reader.readLine()) != null) {
            	File directory = new File(line.trim());
                if (directory.exists() && directory.isDirectory()) {
                    String zipFileName = directory.getName() + "_" + dateFormattedNow + ".zip";
                    Logger.log("Creating backup for: " + directory.getAbsolutePath(), Logger.INFO);
                    File backupFolder = new File("backups/");
                    backupFolder.mkdir();

                    boolean success = ZipUtils.zipDirectory(directory, "backups/" + zipFileName);
                    if (success) {
                    	Logger.log("Successfully created!", Logger.INFO);
                        BackupHistory.logBackup(zipFileName);
                        if (isUploadToNasAllowed) {
                        	Logger.log("Copying " + zipFileName + " to " + target, Logger.INFO);
                            FileUtils.copyFile(new File("backups/" + zipFileName), new File(target + "/" + zipFileName));
                            Logger.log("Successfully copied!", Logger.INFO);
                        }
                    } else {
                    	Logger.log("Skipping upload due to ZIP errors!", Logger.ERROR);
                    }
                } else {
                	Logger.log("Skipping invalid directory: " +line, Logger.ERROR);
                }
            }
        } catch (IOException e) {
        	Logger.log("Error processing backup:" + e.getMessage(), Logger.ERROR);
        }
        Logger.log("Done!", Logger.INFO);
        System.exit(1);
    }
	
	public static void setCmdTitle(String title) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "title " + title).inheritIO().start();
            } else {
            	Logger.log("CMD title change is only supported on Windows.", Logger.WARNING);
            }
        } catch (Exception e) {
        	Logger.log("Failed to change CMD title:" + e.getMessage(), Logger.ERROR);
        }
    }
}
