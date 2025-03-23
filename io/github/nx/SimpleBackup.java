package io.github.nx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SimpleBackup {

	public static void main(String[] args) {
        String inputFile = "list.txt";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd--HH_mm_ss");
        String formattedNow = now.format(formatter);
        try {
            System.out.println("Reading '" + inputFile + "'");
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                File directory = new File(line.trim());
                if (directory.exists() && directory.isDirectory()) {
                    String zipFileName = "backup_"+directory.getName() +"_" + formattedNow + ".zip";
                    System.out.println("Creating a backup from " + directory.getAbsolutePath());
                    File path = new File("backups/");
                    path.mkdir();
                    zipDirectory(directory, "backups/"+zipFileName);
                } else {
                    System.out.println("Skipping invalid directory: " + line);
                }
            }
            reader.close();
            long curTime = System.nanoTime()/100000000/60/60;
            System.out.println("Done! " + curTime+"ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static void zipDirectory(File folder, String zipFileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipDirectoryRecursive(folder, folder.getName(), zos);
            System.out.println("Successfully created a backup from " + folder.getAbsolutePath());
        }
    }

    private static void zipDirectoryRecursive(File folder, String parentFolderName, ZipOutputStream zos) throws IOException {
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectoryRecursive(file, parentFolderName + "/" + file.getName(), zos);
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    String zipEntryName = parentFolderName + "/" + file.getName();
                    ZipEntry zipEntry = new ZipEntry(zipEntryName);
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                }
            }
        }
    }

}
