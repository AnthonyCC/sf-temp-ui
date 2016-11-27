package com.freshdirect.transadmin.parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import au.com.bytecode.opencsv.CSVReader;

import com.freshdirect.transadmin.service.AssetManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;

public class AssetLoader implements SynchronousParserClient {

	private IParser parser;

	private IAssetProcessor validator;

	private String filename;

	private String destination;
	
	private String fileBackupDirectory;

	private final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
		
	private int count = 0;
	private int invalids = 0;
	private int valids = 0;
	private int invalidExceptions = 0;
	private int total = 0;
	private int realTotal = 0;
	private int numThreads = 1;
	private int connReset = 1000;	
	private boolean done;
	private int maxRecords = 10000;

	private long startTime;
	
	private AssetManagerI assetManagerService;
		
	public AssetManagerI getAssetManagerService() {
		return assetManagerService;
	}

	public void setAssetManagerService(AssetManagerI assetManagerService) {
		this.assetManagerService = assetManagerService;
	}
	
	private DomainManagerI domainManagerService;
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}


	public AssetLoader(String filename, String destination) {
		
		parser = new AssetCSVParser();
		validator = new AssetCSVProcessor();
		
		parser.setClient(this);

		this.filename = filename;
		this.destination = destination;	
		//this.fileBackupDirectory = fileBackupDirectory;
		total = countLines();
		
	}

	@SuppressWarnings("unchecked")
	public void load() {
		try {
			// Initialize if any
			
			System.out.println("\n----- Asset File processing starting -----");
			startTime = System.currentTimeMillis();

			for (int i = 0; i < numThreads; i++) {
				new Thread(new Consumer(queue)).start();
			}
			validator.initialize(assetManagerService, domainManagerService);
			parser.parseFile(filename);

			if (parser.getExceptions().size() > 0) {
				Iterator<BadDataException> exIter = parser.getExceptions().iterator();
				while (exIter.hasNext()) {
					BadDataException bde = exIter.next();
					System.out.println("Bad Data:"+ bde);
				}
			}

			while (true) {
				// all parsed records were tested and all valid records were				
				if (count == realTotal) {
					System.out.println("writing out results...");
					//validator.flushResults(destination);
					done = true;
					break;
				} else {
					Thread.sleep(100);
				}
			}
			System.out.println("\n----- Asset File processing ended -----");
			
			//Now backup the file by moving it from the source location to the file backup location. Move and rename file with timestamp
			/*File sourceFile = new File(filename);
			String newFileFullPath = fileBackupDirectory + File.separatorChar+ getNewFileNameWithTimestamp(sourceFile.getName());
			boolean isMoved = sourceFile.renameTo(new File(newFileFullPath));
			File destinationFile = new File(newFileFullPath);
			destinationFile.setLastModified(Calendar.getInstance().getTimeInMillis());
			if (!isMoved)
			{
				//Unable to move/rename the file. Log the error
				String message = "Unable to move/rename the file :"+filename+" to : "+ newFileFullPath;
			}*/	
			
		} catch (Exception e) {
			e.printStackTrace();
			printExceptionToGUI(e);
		}
	}

	private String getNewFileNameWithTimestamp(String oldFileNameWithExtn) {
		int dotTokenIndex= oldFileNameWithExtn.lastIndexOf(".");
		String fileNameWithOutExtn=oldFileNameWithExtn.substring(0,dotTokenIndex);
		String fileExtn = oldFileNameWithExtn.substring(dotTokenIndex+1,oldFileNameWithExtn.length());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String newFileName = fileNameWithOutExtn + "-"+ df.format(Calendar.getInstance().getTime())+ "." + fileExtn;
		return newFileName;
	}

	public void accept(Object o) {
		try {
			//some light memory management
			if (queue.size() >= maxRecords) {
				Thread.sleep(1000);
			}
			queue.put(o);
			realTotal++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void printExceptionToGUI(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		System.out.println(e.getMessage() + "\n" + sw.toString());
	}

	private int countLines() {
		
		FileInputStream inputStream = null;
		int lines = 0;
		try  {
			inputStream = new FileInputStream(filename);
			CSVReader reader = new CSVReader(new InputStreamReader(inputStream));			
			String[] line;
			while ((line = reader.readNext()) != null) {
				if (!AssetCSVParser.isEmpty(line[0])) {
					lines++;
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return lines;
	}

	public synchronized int getCount() {
		return count;
	}

	public int getTotal() {
		return total;
	}

	public int getInvalids() {
		return invalids;
	}

	public int getInvalidExceptions() {
		return invalidExceptions;
	}

	public int getValids() {
		return valids;
	}
	
	public long getElapsedTime() {
		return System.currentTimeMillis() - startTime;
	}

	public boolean isDone() {
		return done;
	}

	private synchronized void incrementCount() {
		count++;
	}

	private synchronized void incrementValids() {
		valids++;
	}

	private synchronized void incrementInvalids() {
		invalids++;
	}

	private synchronized void incrementExceptions() {
		invalidExceptions++;
	}
	
	public int getConnReset() {
		return connReset;
	}

	private class Consumer implements Runnable {
		private Connection conn;
		private final BlockingQueue<Object> queue;
		private int localCount = 0;

		public Consumer(BlockingQueue<Object> q) {
			queue = q;
		}

		public void run() {
			try {
				while (!isDone()) {
					if (localCount % getConnReset() == 0) {
						// refresh connection every 10000 records
						if (conn != null)
							conn.close();
						//conn = OracleDAOFactory.createConnection();
					}
					consume(queue.take());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}
		}

		private void consume(Object object) {
			try {
				boolean result = validator.processAssetRecord(object);
				if (result) {
					incrementValids();
				} else {
					incrementInvalids();
				}
			} catch (Exception e) {
				e.printStackTrace();
				incrementExceptions();
			} finally {
				incrementCount();
				localCount++;
				System.out.println(getCount() + " records of " + total
						+ " processed...");
			}
		}
	}
}
