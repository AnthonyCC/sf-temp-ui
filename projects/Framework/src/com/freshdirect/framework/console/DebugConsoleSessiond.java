/*****************************************************************************
 *                                                                           *
 *  This file is part of the BeanShell Java Scripting distribution.          *
 *  Documentation and updates may be found at http://www.beanshell.org/      *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is BeanShell. The Initial Developer of the Original    *
 *  Code is Pat Niemeyer. Portions created by Pat Niemeyer are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  Patrick Niemeyer (pat@pat.net)                                           *
 *  Author of Learning Java, O'Reilly & Associates                           *
 *  http://www.pat.net/~pat/                                                 *
 *                                                                           *
 *****************************************************************************/

package com.freshdirect.framework.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.log4j.Category;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.UtilEvalError;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
	BeanShell remote session server.
	Starts instances of bsh for client connections.
	Note: the sessiond effectively maps all connections to the same interpreter
	(shared namespace).
*/
public class DebugConsoleSessiond extends Thread
{
	private static final Category LOGGER = LoggerFactory.getInstance(DebugConsoleSessiond.class);
	
	private ServerSocket ss;
	NameSpace globalNameSpace;
	private boolean doExit;
	
	private DebugConsole console;


	public DebugConsoleSessiond(DebugConsole console, NameSpace globalNameSpace, int port) throws IOException
	{
		this.console = console;
		ss = new ServerSocket(port,1,Inet4Address.getByName("127.0.0.1"));
		this.globalNameSpace = globalNameSpace;
	}

	public void run()
	{
		try
		{
			ss.setSoTimeout(1000);
			globalNameSpace.setVariable("session", this, false);
			globalNameSpace.setVariable("console", console, false);
			while(!doExit) {
				try {
					Socket cls = ss.accept();
					new SessiondConnection(console, this, globalNameSpace, cls).start();
				} catch (SocketTimeoutException ste) {
					// Let's go again
				}
			}
		}
		catch(IOException e) { 
			LOGGER.warn("Exception when running session", e);
		} catch (UtilEvalError e) {
			LOGGER.warn("Exception when setting variable", e);
		} 
		finally {
			try {
				ss.close();
			} catch (IOException e) {
				LOGGER.warn("Exception when closing socket", e);
			}
			LOGGER.info("DebugConsoleSession exited");
		}
	}
	
	public void exitSession() {
		LOGGER.info("Schedule DebugConsoleSession exit");
		doExit = true;
	}
}

class SessiondConnection extends Thread
{
	private static final Category LOGGER = LoggerFactory.getInstance(SessiondConnection.class);

	NameSpace globalNameSpace;
	Socket client;
	DebugConsoleSessiond sessiond;
	DebugConsole console;

	SessiondConnection(DebugConsole console, DebugConsoleSessiond sessiond, NameSpace globalNameSpace, Socket client)
	{
		this.sessiond = sessiond;
		this.console = console;
		this.client = client;
		this.globalNameSpace = globalNameSpace;
	}

	public void run()
	{
		try
		{
			InputStream ins = client.getInputStream();
			PrintStream out = new PrintStream(client.getOutputStream());
			InputStreamReader in = new InputStreamReader(ins); 
			BufferedReader br = new BufferedReader(in);
			out.print("Login: ");
			String login = br.readLine();
			out.print("Password: ");
			String pw = br.readLine();
			String thePw = console.getPassword(login);
			if (thePw == null || !thePw.equals(pw)) {
				out.println("Authentication failed!");
				return ;
			}
			Interpreter i = new Interpreter( 
					br, out, out, true, globalNameSpace);
			i.setExitOnEOF( false ); // don't exit interp
			i.eval("setAccessibility(true)");
			i.eval("bsh.system.shutdownOnExit = false");
			i.run();				
		}
		catch(IOException e) { 
			LOGGER.warn("Exception while handling connection", e);
		} 
		catch (EvalError e) {
			LOGGER.warn("Exception while evaluating initial expressions", e);
		} 
		finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.warn("Exception while closing connection", e);
			}
		}
	}
	
	
}

