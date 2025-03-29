package io.github.nx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	public static boolean zipDirectory(File folder, String zipFileName) {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            return zipDirectoryRecursive(folder, folder.getName(), zos);
        } catch (IOException e) {
        	Logger.log("Failed to create ZIP file " + e.getMessage(), Logger.ERROR);
            return false;
        }
    }

    public static boolean zipDirectoryRecursive(File folder, String parentFolderName, ZipOutputStream zos) {
        boolean allFilesZipped = true;
        File[] files = folder.listFiles();
        if (files == null) return false;

        for (File file : files) {
            if (file.getName().equalsIgnoreCase("desktop.ini")) continue;
            
            try {
                if (file.isDirectory()) {
                    if (!zipDirectoryRecursive(file, parentFolderName + "/" + file.getName(), zos)) {
                        allFilesZipped = false;
                    }
                } else {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(parentFolderName + "/" + file.getName());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[8192];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                }
            } catch (IOException e) {
            	Logger.log("Failed to zip: " + e.getMessage(), Logger.ERROR);
                allFilesZipped = false;
            }
        }
        return allFilesZipped;
    }
}
