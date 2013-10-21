package com.freshdirect.transadmin.parser;

import java.io.File;

import javax.swing.JFileChooser;

public class AssetLoaderClient {

	private static String CSV_FILETYPE = "Microsoft Office Excel Comma Separated Values File";
	
	private static AssetLoader loader;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String fileDirectory = "C:\\Development\\upload";
		
		try {
			
			boolean fileFound = doesFileExist(fileDirectory);					
						
			System.out.println("fileFound:"+ fileFound);
						
				if (fileFound){
					System.out.println("Files found to process:"+ fileFound);
					File fileDir = new File(fileDirectory);
					File[] filesList = fileDir.listFiles();
					for (int index = 0; index < filesList.length; index++) {
						File file = filesList[index];
						if (file.isFile()) {
							JFileChooser chooser = new JFileChooser();
							String fileTypeName = chooser.getTypeDescription(file);
							if(fileTypeName != null && CSV_FILETYPE.equalsIgnoreCase(fileTypeName)) {
								loader = new AssetLoader(
															fileDirectory
																	+ File.separatorChar
																	+ file.getName().substring(0, file.getName().lastIndexOf("."))
																	+ ".csv"
															, fileDirectory
																	+ File.separatorChar
																	+ file.getName().substring(0, file.getName().lastIndexOf("."))
																	+ "_results.csv"
															);
								loader.load();
								System.out.println("Processing file Completed." );
							}
						}
					}
				}
		} catch ( Exception e ) {
			e.printStackTrace();	       	
		}

	}
	
	private static boolean doesFileExist(String fileDirectory) {
		boolean fileExists = false;
		System.out.println("Started checking if file exits..");
		File fileDir = new File(fileDirectory);		
		File[] filesList = fileDir.listFiles();
		if(fileDir != null) {
			for (int index = 0; index < filesList.length; index++) {
				File file = filesList[index];
				if (file.isFile()) {
					JFileChooser chooser = new JFileChooser();
					String fileTypeName = chooser.getTypeDescription(file);
					if (fileTypeName != null
							&& CSV_FILETYPE.equalsIgnoreCase(fileTypeName)) {
						fileExists = true;
						break;
					}
				}
			}
		}
		System.out.println("Completed checking if file exits..");
		return fileExists;
	}
	


}
