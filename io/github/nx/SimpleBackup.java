package io.github.nx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SimpleBackup {

	public static String target = "Z:/Backup/";
	public static boolean isUploadAllowed = false;
	private static String listFile = "list.txt";
	private static final String version = "v0.7.1_2025";

	public static void main(String[] args) {
		System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] "+ "[INFO] Launching SimpleBackup "+  version);
		ConfigLoader.loadConfig("config.cfg");

		String target = ConfigLoader.target;
		boolean isUploadAllowed = ConfigLoader.isUploadAllowed;

		String dateFormattedNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		try {

			System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Reading '" + listFile + "'");
			BufferedReader reader = new BufferedReader(new FileReader(listFile));
			String line;
			while ((line = reader.readLine()) != null) {
				File directory = new File(line.trim());
				if (directory.exists() && directory.isDirectory()) {
					String zipFileName = directory.getName() + "_" + dateFormattedNow + ".zip";
					System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Creating a backup from " + directory.getAbsolutePath());
					File path = new File("backups/");
					path.mkdir();
					zipDirectory(directory, "backups/" + zipFileName);
					if (isUploadAllowed) {
						System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Copying " + zipFileName + " to " + target);
						Files.copy(Paths.get(new File("backups/" + zipFileName).getAbsolutePath()),
								Paths.get(target + "/" + zipFileName), StandardCopyOption.REPLACE_EXISTING);
						System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Succesfully copied!");
					} else {
						System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Skipping uploading to NAS");
					}
				} else {
					System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Skipping invalid directory: " + line);
				}
			}
			reader.close();
			System.out.println(
					"[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Done!");
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		System.exit(0);
	}

	private static void zipDirectory(File folder, String zipFileName) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFileName); ZipOutputStream zos = new ZipOutputStream(fos)) {
			zipDirectoryRecursive(folder, folder.getName(), zos);
			System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + "[INFO] Successfully created a backup from " + folder.getAbsolutePath());
		}
	}

	private static void zipDirectoryRecursive(File folder, String parentFolderName, ZipOutputStream zos)
			throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {

			if (file.getName().equalsIgnoreCase("desktop.ini")) {
				continue;
			}

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
