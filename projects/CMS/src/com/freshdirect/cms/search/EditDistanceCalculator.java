package com.freshdirect.cms.search;

/**
 * Calculate the edit distance between two strings.
 * @author istvan
 */
public interface EditDistanceCalculator {
	
	/** Calculate edit distance.
	 * 
	 * @param s1 string one
	 * @param s2 string two
	 * @return edit distance
	 */
	public int calculate(String s1, String s2);
	
	/** Get a trace of the last calculation.
	 * 
	 *  This feature is predominantly for debugging.
	 * 
	 * @return a string representation of the trace of calculation
	 */
	public abstract String getTrace();
	
	/** Helper for edit distance calculators which use Dynamic Programming.
	 * 
	 */
	public abstract class Helper implements EditDistanceCalculator {
		
		/**
		 * Used as a work buffer to calculate edit distance.
		 *  
		 * This table can adjust itself to be reused for calculating edit distance for
		 * different strings. 
		 * 
		 */
		protected static class DTable  {
			
			private int[] array; // rep 
			private int rows;
			private int columns;
			
			private String s1 = "", s2 = ""; // strings
			
			/**
			 * Constructor.
			 * Empty table.
			 */
			public DTable() {
				this(0,0);
			}
			
			/**
			 * Constructor.
			 * @param rows number of
			 * @param columns number of
			 */
			public DTable(int rows, int columns) {
				this.array = new int[rows*columns];
				this.rows = rows;
				this.columns = columns;
			}
			
			/**
			 * Whether capable to perform calculation without adjustment.
			 * @param rows would be number of
			 * @param columns n would be number of
			 * @return whether can calculate without adjustment
			 */
			public boolean isCompatible(int rows, int columns) {
				return (this.rows * this.columns) >= (rows*columns);
			}
			
			/**
			 * Get cell value.
			 * @param i row index
			 * @param j column index
			 * @return cell value
			 */
			public int get(int i, int j) {
				return array[i*columns + j];
			}
			
			/**
			 * Get number of rows.
			 * @return number of rows
			 */
			public int getRows() { 
				return rows;
			}
			
			/**
			 * Get number of columns.
			 * @return number of columns
			 */
			public int getColumns() {
				return columns;
			}
			
			/**
			 * Set cell value.
			 * @param i row index
			 * @param j column index
			 * @param v new cell value
			 */
			public void set(int i, int j, int v) {
				array[i*columns + j] = v;
			}
			
			// Adjust table size, if not compatible.
			private void adjust(int newRows, int newColumns) {
				if (!isCompatible(newRows,newColumns)) array = new int[newRows * newColumns];
				rows = newRows;
				columns = newColumns;
			}
			
			/**
			 * Adjust table size to calculate distance for given strings.
			 * 
			 * @param s1 string 1
			 * @param s2 string 2
			 */
			public void adjust(String s1, String s2) {
				adjust(s1.length()+1,s2.length()+1);
				this.s1 = s1;
				this.s2 = s2;
			}

			// these assist in producing readable output
			private static String nl = System.getProperty("line.separator", "\n");
			
			private static int minDecimalDisplayWidth(int v) {
				return v == 0 ? 1 : (int)Math.floor(Math.log(v)/Math.log(10)) +1;
			}
			
			private void appendValue(StringBuffer buff, int v) {
				int needs = minDecimalDisplayWidth(getColumns() - 1);
				int has = minDecimalDisplayWidth(v);
				for(int i=0; i< needs-has+1; ++i) buff.append(' ');
				buff.append(v);
			}
			
			private void appendChar(StringBuffer buff, char c) {
				int needs = minDecimalDisplayWidth(getColumns() - 1);
				for(int i=0; i< needs; ++i) buff.append(' ');
				buff.append(c);
			}
			
			/**
			 * Produces the Dynamic Programming table with the latest values and strings aligned.
			 */
			public String toString() {
				if (s1.length() == 0 || s2.length() == 0) return "";
				
				StringBuffer buff = new StringBuffer();

				appendChar(buff, '*');
				for(int j=0; j< getColumns(); ++j) appendChar(buff, j > 0 ? s2.charAt(j-1) : ' ');
				buff.append(nl);
				for(int i=0; i< getRows(); ++i) {
					appendChar(buff,i > 0 ? s1.charAt(i-1) : ' ');
					for(int j=0; j< getColumns(); ++j) appendValue(buff, get(i,j));
					buff.append(nl);
				}
				
				return buff.toString();
			}
		}
	
		// Dynamic programming table
		protected DTable T = new DTable();
	
		/**
		 * Dynamic programming table with the latest values.
		 * @return table
		 */
		public String getTrace() {
			return T.toString();
		}
		
	}
	
	/**
	 * Calculates the Levenshtein distance.
	 * 
	 * Each insertion, deletion and substitution by a different charcter is penalized by weight 1.
	 * The value is the minimum edit distance.
	 * @author istvan
	 *
	 */
	public static class Levenshtein extends Helper {

		public int calculate(String s1, String s2) {
			T.adjust(s1,s2);
			
			for(int i = 0; i<= s1.length(); ++i) T.set(i,0,i);
			for(int j = 0; j<= s2.length(); ++j) T.set(0,j,j);
			
			for(int i = 0; i < s1.length(); ++i) {
				for(int j = 0 ; j <  s2.length(); ++j) {
					T.set(
						i+1,
						j+1,
						Math.min(
							Math.min(T.get(i,j+1), T.get(i+1,j)) + 1, // insert or delete
							T.get(i,j) + (s1.charAt(i) == s2.charAt(j) ? 0 : 1) // substitute
						)
					);
				}
			}
			
			return T.get(s1.length(), s2.length());
		}
	}
	
	/**
	 * Calculates the Longest Common Subsequence.
	 * 
	 * Strictly speaking LCSS is an inverse distance (i.e. the larger the value the closer the strings are),
	 * thus it is normalized such that {@link #calculate(String, String) calculate} returns
	 * Math.min(s1.length(), s2.length() - {@link #getLCSSValue(String, String) getLCSSValue(s1,s2)} and thus
	 * can be thought of as a measure how much the shorter string is embedded in the longer.
	 * 
	 * @author istvan
	 *
	 */
	public static class LCSS extends Helper {
		
		public int getLCSSValue(String s1, String s2) {
			T.adjust(s1,s2);
			
			for(int i = 0; i<= s1.length(); ++i) 
				T.set(i,0, i > 0 && i <= s2.length() && (s1.charAt(i-1) == s2.charAt(i-1)) ? 1 : 0);
			for(int j = 0; j<= s2.length(); ++j) 
				T.set(0,j, j > 0 && j <= s1.length() && (s1.charAt(j-1) == s2.charAt(j-1)) ? 1 : 0);
			
			for(int i = 0; i < s1.length(); ++i) {
				for(int j = 0 ; j <  s2.length(); ++j) {
					T.set(
						i+1,
						j+1,
						Math.max(
							Math.max(T.get(i,j+1), T.get(i+1,j)), // insert or delete
							T.get(i,j) + ((s1.charAt(i) == s2.charAt(j) ? 1 : 0)) // change
						)
					);
				}
			}
			
			return T.get(s1.length(), s2.length());
		}
		
		public int calculate(String s1, String s2) {
			int v = getLCSSValue(s1, s2);
			return Math.min(s1.length(), s2.length()) - v;
		}
	}
	
	/**
	 * Calculates a modified Levenshtein distance.
	 * 
	 * Same as {@link Levenshtein} but swapping adjacent charactes are penelized by 1 (instead of 2).
	 * @author istvan
	 *
	 */
	public static class ModifiedLevenshtein extends Helper {
		
		private static boolean adjacentSwap(String s1, String s2, int i, int j) {
			return 
				i > 0 && j > 0 &&
				s1.charAt(i-1) == s2.charAt(j) &&
				s1.charAt(i) == s2.charAt(j-1);
		}
		
		public int calculate(String s1, String s2) {
			T.adjust(s1,s2);
			
			for(int i = 0; i<= s1.length(); ++i) T.set(i,0,i);
			for(int j = 0; j<= s2.length(); ++j) T.set(0,j,j);
			
			for(int i = 0; i < s1.length(); ++i) {
				for(int j = 0 ; j <  s2.length(); ++j) {
					T.set(
						i+1,
						j+1,
						Math.min(
							Math.min(T.get(i,j+1), T.get(i+1,j)) + 1, // insert or delete
							Math.min(
								T.get(i,j) + (s1.charAt(i) == s2.charAt(j) ? 0 : 1), // substitute
								T.get(i,j) + (adjacentSwap(s1, s2, i, j) ? 0 : 1) // swap adjacent
							)
						)
					);
				}
			}
			
			return T.get(s1.length(), s2.length());
		}
	}
	
	
	

}
