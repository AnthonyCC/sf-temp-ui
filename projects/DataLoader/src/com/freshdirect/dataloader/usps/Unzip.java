/**
 * @author ekracoff
 * Created on Jun 2, 2005*/

package com.freshdirect.dataloader.usps;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class Unzip {

	public static void unzip(File zip, File destDir) throws ZipException, IOException {
		Enumeration entries;
		ZipFile zipFile = new ZipFile(zip);

		entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();

			if (entry.isDirectory()) {
				// Assume directories are stored parents first then children.
				System.out.println("Extracting directory: " + entry.getName());
				// This is not robust, but works
				(new File(entry.getName())).mkdir();
				continue;
			}

			System.out.println("Extracting file: " + entry.getName());
			File destFile = new File(destDir.getAbsolutePath() + File.separator + entry.getName());
			copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(destFile)));
		}

		zipFile.close();
	}

	private static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);

		in.close();
		out.close();
	}
}