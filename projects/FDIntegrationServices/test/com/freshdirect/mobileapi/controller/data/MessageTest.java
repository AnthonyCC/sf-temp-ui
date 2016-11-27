package com.freshdirect.mobileapi.controller.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;

public class MessageTest extends TestCase {

    /**
     * Common method to handling getting file content into a string for testing.
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    protected String getFileContentAsString(String filename) throws IOException {
        InputStream in = getClass().getResourceAsStream(filename);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder data = new StringBuilder();
        String line = "";
        while (true) {
            line = reader.readLine();
            if (line == null) {
                break;
            }
            data.append(line);
        }
        reader.close();
        return data.toString();
    }

    protected InputStream getFileContentAsStream(String filename) throws IOException {
        InputStream in = getClass().getResourceAsStream(filename);
        return in;
    }

}
