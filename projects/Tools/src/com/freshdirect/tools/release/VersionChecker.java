/*
 * VersionChecker.java
 *
 * Created on January 24, 2003, 4:52 PM
 */

package com.freshdirect.tools.release;

import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 *
 * @author  mrose
 * @version
 */
public class VersionChecker {
    
    /** Creates new VersionChecker */
    public VersionChecker() {
        super();
    }
    
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Please provide a directory containing the FreshDirect JAR files to check");
            System.exit(-1);
        }
        
        VersionChecker vc = new VersionChecker();
        vc.checkJarsInDir(args[0]);
    }
    
    private void checkJarsInDir(String directory) {
        try {
            File theDir = new File(directory);
            if (!theDir.isDirectory()) {
                System.err.println("\"" + directory + "\" is not a directory.");
                return;
            }
            
            File[] jarFiles = theDir.listFiles(
                new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".jar");
                    }
                }
            );
            
            for (int i=0; i<jarFiles.length; i++) {
                checkJarVersion(jarFiles[i]);
            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void checkJarVersion(File file) throws IOException {
        System.out.println();
        System.out.println(file.getAbsolutePath());
        
        JarFile jarFile = new JarFile(file);
        Manifest man = jarFile.getManifest();
        Attributes attrs = man.getMainAttributes();
        
        Attributes.Name vendor = Attributes.Name.IMPLEMENTATION_VENDOR;
        System.out.println("\t" + vendor + " : " + attrs.get(vendor));
        
        Attributes.Name title = Attributes.Name.IMPLEMENTATION_TITLE;
        System.out.println("\t" + title + " : " + attrs.get(title));
        
        Attributes.Name version = Attributes.Name.IMPLEMENTATION_VERSION;
        System.out.println("\t" + version + " : " + attrs.get(version));        
    }
    
    
}
