package io.github.nx.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupHistory {
    public static final String HISTORY_FILE = "backup_history.log";

    public static void logBackup(String fileName) {
        try (FileWriter fw = new FileWriter(HISTORY_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " - Backup created: " + fileName);
            bw.newLine();
        } catch (IOException e) {
        	Logger.log("Failed to write history: " + e.getMessage(), Logger.ERROR);
        }
    }
}