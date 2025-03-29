package io.github.nx.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import io.github.nx.SimpleBackup;

public class UpdateChecker {
	
	@SuppressWarnings("deprecation")
	public static void checkUpdate() {
		URL url;
		
		try {
			url = new URL(SimpleBackup.VERSIONURL);
			Scanner sc;
			sc = new Scanner(url.openStream());
			StringBuffer sb = new StringBuffer();
			while (sc.hasNext()) {
				sb.append(sc.next());
			}
			String result = sb.toString();
			result = result.replaceAll("<[^>]*>", "");
			if (!result.equals(SimpleBackup.VERSION)) {
				Logger .log("A newer version is available: ("+result+")", Logger.WARNING);
			} else {
				Logger.log("Latest version installed!", Logger.INFO);
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
