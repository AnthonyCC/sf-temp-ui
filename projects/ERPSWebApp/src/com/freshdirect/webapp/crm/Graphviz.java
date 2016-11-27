package com.freshdirect.webapp.crm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Graphviz {

	public static void writePNG(String input, OutputStream out) throws IOException {
		invokeDot("-Tpng", input, out);
	}

	private static void invokeDot(String params, String input, OutputStream out) throws IOException {
		Process proc = Runtime.getRuntime().exec("dot " + params);

		PrintWriter dot = new PrintWriter(proc.getOutputStream());
		dot.print(input);
		dot.close();

		InputStream in = proc.getInputStream();
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
	}

}
