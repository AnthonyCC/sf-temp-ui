/*
 * $Workfile:$
 *
 * $Date:$
 *
 * Copyright (c) 2003 FreshDirect
 *
 */
 
package com.freshdirect.dataloader.email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ListBuilder
 *
 * @version    $Revision:$
 * @author     $Author:$
 */
public class ListBuilder {

	/**
	 * 
	 */
	public ListBuilder() {
		super();
	}
	
	
	public List<MailInfo> parseEmailList(File file) {
		List<MailInfo> emailAddresses = new ArrayList<MailInfo>();

		System.out.println("Reading " + file + "...");
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			//
			// LOOP THROUGH EACH LINE IN THE FILE,
			// AND ADD EACH TOKEN (EMAIL ADDRESS) TO THE LIST
			//
			String line = in.readLine();
			while (line != null) {
				String trimmed = line.trim();
				if ("".equals(trimmed)) {
					line = in.readLine();
					continue;
				}
				MailInfo mi = parseLine(line);
				if (mi != null) {
					emailAddresses.add(mi);
				}
				line = in.readLine();
			}

		} catch (FileNotFoundException ex) {
			System.out.println("Cannot find email address list: " + file);
			System.exit(1);
		} catch (IOException ex) {
			System.out.println("IOException reading list");
			System.exit(1);
		}
		return emailAddresses;
	}
    
	private MailInfo parseLine(String line) {
		if (line == null) return null;

		StringTokenizer tokenizer = new StringTokenizer(line, "\t");
		int c = 0;
		MailInfo mi = new MailInfo();
		while(tokenizer.hasMoreTokens()) {
			String part = tokenizer.nextToken().trim();
			if (c == 0) {
				if (com.freshdirect.mail.EmailUtil.isValidEmailAddress(part)) {
					mi.setEmail(part);
				} else {
					System.out.println("discarding ("+ line +") - invalid email address ");
					return null;
				}
			} else if (c == 1) {
				part = part.toLowerCase();
				part = part.substring(0, 1).toUpperCase() + part.substring(1);
				mi.setFirstName(part);
			} else if (c == 2) {
				mi.isHtmlEmail(!"X".equalsIgnoreCase(part));
			}
			c++;
		}
		return mi;

	}

}
