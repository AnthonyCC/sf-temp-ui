package com.freshdirect.transadmin.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ResourceBundle;



public class TransAdminSystemException extends RuntimeException {
	public static final String messageFileName="messages";
	public static final String ERROR_PREFIX="sys.error.";
    private Throwable causedException;
    private String errorCode=null;

	public TransAdminSystemException(String errorCode,Throwable e){
		this(e);
		this.errorCode=errorCode;
	}


	public TransAdminSystemException(Throwable e){
		this.causedException=e;
	}


	public String getMessage(){
		StringBuffer msgBuffer=new StringBuffer();
		if(errorCode!=null)
		{
			ResourceBundle bundle=ResourceBundle.getBundle(messageFileName);
	        String message=bundle.getString(ERROR_PREFIX+errorCode);
	        msgBuffer.append(message);
	        msgBuffer.append("\n");
		}
		return msgBuffer.toString();

	}


	public String getCausedException(){
		StringWriter writer=null;
		PrintWriter pw=null;

		if(this.causedException!=null){
			try {
			writer=new StringWriter();
			pw=new PrintWriter(writer);
			this.causedException.printStackTrace(pw);
//			System.out.println("asdasd:"+writer.toString());
			return writer.toString();
			} finally {
				 try {
				   if(writer != null)  writer.close();
				   if(pw != null)  pw.close();
				 } catch (IOException ignore) {}
			}
		}
		return "";
	}

}
