package com.freshdirect.framework.template;

import java.io.IOException;
import java.io.Writer;

public class FreemarkerWriter extends Writer {
	
	private Writer root = null;
	public FreemarkerWriter(Writer root) {
		this.root = root;
	}
	
	@Override
	public void close() throws IOException {
		root.close();
		
	}

	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		root.write(cbuf, off, len);
		
	}
	

}
