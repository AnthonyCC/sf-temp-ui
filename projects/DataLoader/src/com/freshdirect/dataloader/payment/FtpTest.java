package com.freshdirect.dataloader.payment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpTest {
    
    public static void main(String[] args) {
        FtpTest ftp = new FtpTest();
        ftp.go();
    }
    
    FTPClient client = null;
    
    /** Creates new FtpTest */
    public FtpTest() {
        client = new FTPClient();
		client.setDefaultTimeout(30000);
		client.setDataTimeout(30000);
    }
    
    public void go() {
        
        try {
            boolean ok = true;
            System.out.println("connecting...");
            client.connect("ems1.nyc1.freshdirect.com");
            int reply = client.getReplyCode();
            ok = FTPReply.isPositiveCompletion(reply);
            if (ok) {
                System.out.println("logging in...");
                ok = client.login("bmadmin", "sun1ray");
            }
            if (ok) {
                System.out.println("retreiving file...");
                FileOutputStream ofs = new FileOutputStream(new File("d:/junk_" + System.currentTimeMillis() + ".txt"));
                ok = client.retrieveFile("install.log", ofs);
                ofs.close();
            }
            if (ok) {
                System.out.println("logging out...");
                client.logout();
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (client.isConnected()) {
                try {
                    System.out.println("disconnecting...");
                    client.disconnect();
                } catch(IOException ioe) {
                }
            }
            System.out.println("done...");
        }
        
    }
    
}
