package io.github.nx.utils;

import java.io.*;

public class FileUtils {
    public static void copyFile(File source, File target) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(target)) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
    
    public static boolean isFileEmpty(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    return false;
                }
            }
        } catch (IOException e) {
        	Logger.log("Could not read file: " + e.getMessage(), Logger.ERROR);
        }
        return true;
    }
}

