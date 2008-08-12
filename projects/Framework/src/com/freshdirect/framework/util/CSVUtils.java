package com.freshdirect.framework.util;

import java.util.List;
import java.util.ArrayList;

import java.io.PushbackInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.EOFException;

import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Utilities for parsing CSV files and escaping values.
 *
 * @author istvan
 *
 */
public class CSVUtils {

	/** White space token type. */
	public final static int TT_WS = 1;

	/** Comma space token type. */
	public final static int TT_COMMA = 2;

	/** Column value token type. */
	public final static int TT_VALUE = 3;

	/** End of line token type. */
	public final static int TT_EOLN = 4;

	/** End of file token type. */
	public final static int TT_EOF = 5;

	/**
	 * Exception marking line and character numbers.
	 */
	public static class FormatException extends RuntimeException {

		private static final long serialVersionUID = -6845928217941803876L;
		private int lineno;
		private int charno;

		private FormatException(String description, int lineno, int charno) {
			super(description);
			this.lineno = lineno;
			this.charno = charno;
		}

		/** Get line number of exception.
		 * @return line number where exception occured
		 */
		public int getLineNumber() {
			return lineno;
		}

		/** Get character position of exception.
		 * @return character position (in {@link #getLineNumber()} where exception occured
		 */
		public int getCharactedPosition() {
			return charno;
		}

		/** String representation.
		 * @return as string
		 */
		public String toString() {
			return super.toString() + " in line " + lineno + " at character " + charno;
		}
        }

	/**
	 * Parse a CSV file one token at a time.
	 */
	public static class Tokenizer {

		private static final String CRLF = "\r\n";
		private static final String LF = "\n";

		private int lineno = 1;
		private int charno = 1;

		private PushbackInputStream is;

		private String token = null;
		private int type = -1;
		private boolean CR = false;

		private int read() throws IOException {
			++charno;
			return is.read();
		}

		private void putback(int c) throws IOException {
			if (c == -1) return;
			--charno;
			is.unread(c);
		}

		// apparently the only white spaces in CSV are SPACE and TAB
		private void skipWs() throws IOException {
			for(;;) {
				int c = read();
				if (c == ' ' || c == '\t') continue;
				putback(c);
				return;
			}	
		}

		/** Constructor.
		 * @param is CSV stream to parse
		 */
		public Tokenizer(InputStream is) {
			this.is = new PushbackInputStream(is);
		}

		private void throwFormatException(String description) {
			throw new FormatException(description,lineno,charno);
		}

		// everything between a pair of double quotes is a single value
		// quotes are escaped by quotes
		private void readQuotedValue() throws IOException {
			StringBuffer value = new StringBuffer();
			int c = read();
			int startline = lineno;
			int startchar = charno;
			if (c != '\"') throwFormatException("\" expected");
			for(;;) {
				c = read();
				if (c == '\"') {
					c = read();
					if (c != '\"') {
						putback(c);
						break;
					}
				} else if (c == -1) throw new EOFException("Unterminated quoted string started in line " + startline + " at character " + startchar); 
				value.append((char)c);
			}
			token = value.toString();
			type = TT_VALUE;
		}

		// unquoted strings may include everything including white space
		// but trailing white space must be removed
		// the value is terminated by a COMMA, EOLN or EOF
		private void readUnquotedValue() throws IOException {
			StringBuffer value = new StringBuffer();
			for(;;) {
				int c = read();
				if (c == -1) break;
				else if (c == ',') {
					putback(c);
					break;
				} else if (c == '\"') {
					throwFormatException("Unexpected quote");
				} else if (c == '\r') {
					c = read();
					putback(c);
					if (c == '\n') {
						read();
						CR = true;
						putback('\n');
						break;
					}
					value.append('\r');
				} else if (c == '\n') {
					putback(c);
					break;
				} else value.append((char)c);
			}
			for(int i=value.length()-1; i>=0; --i) {
				if (value.charAt(i) == ' ' || value.charAt(i) == '\t') value.deleteCharAt(i);
				else break;
			}
			token = value.toString();
			type = TT_VALUE;
		}

		// read a separator, which is COMMA, EOLN or EOF
		private void readSep() throws IOException {
			skipWs();
			int c = read();
			if (c == -1) {	
				token = null;
				type = TT_EOF;
			} else if (c == ',') {
				token = ",";
				type = TT_COMMA;
			} else if (c == '\n') {
				type = TT_EOLN;
				token = CR ? CRLF : LF;
				CR = false;
			} else if (c == '\r') {	
				c = read();
				if (c == '\n') {
					type = TT_EOLN;
					token = CRLF;
					CR = false;
				} else throwFormatException("Unexpected character (int)" + c);
			}  else throwFormatException("Unexpected character (int)" + c);
			
		}

		// read a column value, which may be quoted. 
		// the interpretation of EOF depends on what was on the stream before
		private void readValue() throws IOException {
			skipWs();
			int c = read();
			if (c == -1) {
				if (type == TT_EOLN) {
					type = TT_EOF;
					token = null;
					read();
				} else {
					token = "";
					type = TT_VALUE;
				}
				return;
			} 
			putback(c);
			if(c == '\"') readQuotedValue();
			else readUnquotedValue();
		}

		/**
		 * Parse next token.
		 * The next token is removed from the stream. {@link getType()} will return
		 * its type and {@link getToken()} the actual value. 
		 * @throws IOException for IO errors
		 * @throws EOFException if called after an EOF had been read previously
		 */
		public void nextToken() throws IOException {
			switch(type) {
				case TT_EOF:  throw new EOFException();
				case TT_EOLN:
					++lineno;
					charno = 0;
				case TT_COMMA:
				case -1:
					readValue();
					break;
				case TT_VALUE:
					readSep();
					break;
			}
		}

		/**
		 * Get token value.
		 * The token value "unescapes" quoted strings. For the separating comma
		 * "," is returned and end of line CR or CRLF depending on what was 
		 * actually there. {@link #getType())} can be used to distinguish 
		 * quoted commas and end of line characters from the markup characters.
		 * @return token value
		 * @throws EOFException if called after EOF has been read
		 * @throws NoSuchElementException if no token has been read
		 */
		public String getToken() throws EOFException {
			if (type == TT_EOF) throw new EOFException();
			if (token == null) throw new NoSuchElementException();
			return token;
		}

		/**
		 * Get token type.
		 * @return token type (one of TT_EOF, TT_COMMA, TT_VALUE or TT_EOLN)
		 * @throws NoSuchElementException if no token has been read
		 */
		public int getType() {
			if (type == -1) throw new NoSuchElementException();
			return type;
		}

		/** 
		 * Get current line number.
		 * @return line number
		 */
		public int getLineNumber() { return lineno; }

		/** 
		 * Get current character position.
		 * @return character position in line returned by {@link #getLineNumber()}
		 */
		public int getCharacterPosition() { return charno; }
	}

	/** Get an iterator over a CSV file.
	 *
	 * The iterator will return both the values and the markup as subsequent tokens.
	 * Commas are returned as the string "," and end of line characters as "\n" or "\r\n"
	 * depending on the actual line separators. There is no other way of distinguishing
	 * escaped markup from actual markup than keeping track of where they occur.
	 * 
	 * @return iterator
	 * @throws NoSuchElementException when {@link java.util.Iterator#next()} is called with no next
	 * @throws FormatException when the file format is invalid
	 */
	public static Iterator iterator(final InputStream is) {
		return new Iterator() {

			private boolean moved = false;
			private Tokenizer tokenizer = new Tokenizer(is);
			
			public Object next() {
				if (!hasNext()) throw new NoSuchElementException();
				moved = false;
				try {
					return tokenizer.getToken();
				} catch (EOFException e) {
					throw new NoSuchElementException();
				}
			}
			
			public boolean hasNext() {
				try {
					if (!moved) tokenizer.nextToken();
					moved = true;
					return tokenizer.getType() != TT_EOF;
				} catch(IOException e) {
					throw new FormatException(e.getMessage(),tokenizer.getLineNumber(),tokenizer.getCharacterPosition());
				} 
					
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/** Escape a string so it can be used as a CSV column value.
	 * @param s string to escape
	 * @return the string quoted or itself if no escaping is needed
	 */
	public static String escape(String s) {
		if (s == null) return "";
		if (s.length() == 0) return s;
		int newContentLength = 0;
		boolean hasComma = false;

		for(int i=0; i< s.length(); ++i) {
			switch(s.charAt(i)) {
				case '\"':
					++newContentLength;
				case ',':
					hasComma = true;
				default:
					++newContentLength;
			}
		}

		if (newContentLength > s.length() || 
			hasComma || 
			s.charAt(0) == ' ' || s.charAt(0) == '\t' ||
			s.charAt(s.length() - 1) == ' ' || s.charAt(s.length() - 1) == '\t') {
			StringBuffer newString = new StringBuffer(newContentLength + 2).append('\"');
			for(int i=0; i < s.length(); ++i) {
				if (s.charAt(i) == '\"') newString.append('\"');
				newString.append(s.charAt(i));
			}
			return newString.append('\"').toString();
		} else return s;
	}

	private static boolean emptyRow(List row) {
		return row.size() == 0 || (row.size() == 1 && row.get(0).equals(""));
		
	}
	/** Parse a CSV file.
	 *
	 * @param is input stream representing the CSV file
	 * @param keepEmptyTrailingColumns whether to keep trailing empty columns
	 * @param keepEmptyRows whether to keep empty rows 
	 * @return List of rows (actually List<List<String>>)
	 * @throws IOException on read errors
	 * @throws FormatException if the file has invalid format
	 */
	public static List parse(InputStream is, boolean keepEmptyTrailingColumns, boolean keepEmptyRows) throws IOException {
		Tokenizer tokenizer = new Tokenizer(is);
		List result = new ArrayList();
		List row = new ArrayList();
		DONE: for(;;) {
			tokenizer.nextToken();
			switch(tokenizer.getType()) {
				case TT_EOF: 
					if (keepEmptyRows || !emptyRow(row)) result.add(row);
					break DONE;
				case TT_COMMA: break;
				case TT_EOLN:
					if (!keepEmptyTrailingColumns) {
						for(int i=row.size() - 1; i >= 0; --i) {
							if ("".equals(row.get(i))) row.remove(i);
							else break;
						}
						if (keepEmptyRows || !emptyRow(row)) result.add(row);
					} else if (keepEmptyRows || !emptyRow(row)) result.add(row);
					row = new ArrayList();
					break;
				case TT_VALUE:
					row.add(tokenizer.getToken());
					break;
			}
		}
		return result;
	}
}
