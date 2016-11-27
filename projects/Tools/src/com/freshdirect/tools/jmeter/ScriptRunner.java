package com.freshdirect.tools.jmeter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.framework.util.StringUtil;

import java.util.LinkedList;

/**
 * 
 * @author istvan 
 */
public class ScriptRunner {
	
	
	private static String fileSep = System.getProperty("file.separator", "/");
	
	/**
	 * Context of data columns.
	 * @author istvan
	 *
	 */
	private static class DataContext {
		
		// Map<Integer,List<Double>>
		private Map columns = new TreeMap();
		
		
		/**
		 * Get a column.
		 * @param column
		 * @return List<Double>
		 */
		public List getColumn(int column) {
			return (List)columns.get(new Integer(column));
		}
		
		/**
		 * Add a column.
		 * @param c column
		 * @param values (List<Double>)
		 */
		public void addColumn(int c, List values) {
			columns.put(new Integer(c), values);
		}
	}
	
	
	/**
	 * Expressions involving data columns.
	 * @author istvan
	 *
	 */
	public static abstract class Expression {
	
		/** Evaluate expression in context. */
		public abstract double evaluate(DataContext context);
		
		/** Is expression runtime evaluable. */
		public abstract boolean isConstant();
		
		/** Get referenced columns. */
		public abstract Set getColumns();
	}
	
	
	/**
	 * Expand variable references.
	 * A variable reference has the form <tt>$(var)</tt>, where <tt>var</tt> refers to a system property.
	 * @param in
	 * @return expanded string
	 */
	private static String expandReference(String in) {
		if (in == null) return null;
		StringBuffer res = new StringBuffer(2*in.length());
		for(int i=0; i < in.length(); ++i) {
			if (in.charAt(i) == '$') {
				if (in.charAt(++i) != '(') throw new RuntimeException("( expected after $ in " + in);
				int begin = ++i;
				int end = in.indexOf(')',begin);
				if (end == -1) throw new RuntimeException("missing ) in " + in);
				String propName = in.substring(begin,end);
				i = end;
				String propValue = System.getProperty(propName);
				if (propValue == null) {
					System.err.println("Reference to undefined property " + propName);
					propValue = "";
				}
				
				res.append(propValue);
			} else res.append(in.charAt(i));
		}
		return res.toString();
	}
	
	/**
	 * Wrapper for a function over a list of doubles (ie a column).
	 * @author istvan
	 *
	 */
	private static abstract class FunctionEvaluator {
		
		private String name;
		
		protected FunctionEvaluator(String name) {
			this.name = name;
		}
		
		protected double getValue(Object o) {
			return ((Double)o).doubleValue();
		}
		
		public String getName() { return name; }
		
		public abstract double evaluate(List values);
	}
	
	
	/**
	 * A function over a list of doubles.
	 * @author istvan
	 *
	 */
	private static class ColumnFunction extends Expression {
		private int column;
		private FunctionEvaluator evaluator;
		
		protected ColumnFunction(FunctionEvaluator function, int column) {
			this.evaluator = function;
			this.column = column;
		}
		
		
		public String toString() {
			return new StringBuffer().
				append(evaluator.getName()).
				append('(').append('$').append(column).append(')').toString();
		}
		
		public double evaluate(DataContext context)  {
			return evaluator.evaluate(context.getColumn(column));
		}
		
		public boolean isConstant() { return false; }
		
		public Set getColumns() {
			Set colset = new HashSet(1);
			colset.add(new Integer(column));
			return colset;
		}
	}
	
	/**
	 * Parenthesis for forced evaluation order.
	 * @author istvan
	 *
	 */
	private static class ParenthesizedExpression extends Expression {
		private Expression operand;
		private ParenthesizedExpression(Expression operand) {
			this.operand = operand;
		}
		
		public double evaluate(DataContext context) {
			return operand.evaluate(context);
		}
		
		public String toString() {
			return new StringBuffer('(').append(operand).append(')').toString();
		}
		
		public boolean isConstant() {
			return operand.isConstant();
		}
		
		public Set getColumns() {
			return operand.getColumns();
		}
	}
	
	/**
	 * Expression wrapper for a constant.
	 * @author istvan
	 */
	private static class ConstantExpression extends Expression {
		private double value;
		private ConstantExpression(double value) {
			this.value = value;
		}
		
		public double evaluate(DataContext context) {
			return value;
		}
		
		public String toString() {
			return Double.toString(value);
		}
		
		public boolean isConstant() {
			return true;
		}
		
		public Set getColumns() {
			return Collections.EMPTY_SET;
		}
	}
	
	/**
	 * An operator that takes two doubles.
	 * 
	 * Must provide precedence (eg multiplication has higher precedence than addition).
	 * @author istvan
	 *
	 */
	private static abstract class BinaryEvaluator {
		protected char symbol;
		protected int precedence;
		
		protected BinaryEvaluator(char symbol, int precedence) {
			this.symbol = symbol;
			this.precedence = precedence;
		}
		protected abstract double evaluate(double lhs, double rhs);
		protected char getSymbol() { return symbol; }
		protected int getPrecedence() { return precedence; }
	}
	
	/**
	 * Expressions chained with binary operators.
	 * @author istvan
	 *
	 */
	private static class ExpressionStack extends Expression {
		
		private List operators;
		private List expressions;
		private boolean constant;
		
		private ExpressionStack(Expression e) {
			operators = new LinkedList();
			expressions = new LinkedList();
			expressions.add(e);
			constant = e.isConstant();
		}
		
		private ExpressionStack(Expression lhs, Expression rhs, BinaryEvaluator op) {
			operators = Collections.singletonList(op);
			expressions = new ArrayList(2);
			expressions.add(lhs);
			expressions.add(rhs);
			constant = lhs.isConstant() && rhs.isConstant();
		}
		
		
		private void append(BinaryEvaluator op, Expression e) {
			operators.add(op);
			expressions.add(e);
			if (!e.isConstant()) constant = false;
		}
		
		/**
		 * Evaluate in left-right order; assumes precedences have been fixed.
		 */
		public double evaluate(DataContext context)  {
			if (expressions.size() == 0) return 0;
			Iterator ei = expressions.iterator();
			double v = ((Expression)ei.next()).evaluate(context);
			
			for(Iterator oi = operators.iterator(); oi.hasNext(); ) {
				double vrhs = ((Expression)ei.next()).evaluate(context);
				BinaryEvaluator evaluator = (BinaryEvaluator)oi.next();
				v = evaluator.evaluate(v, vrhs);
			}
			return v;
		}
		
		public String toString() {
			StringBuffer string = new StringBuffer().append('{');
			Iterator ei = expressions.iterator();
			string.append(ei.next());
			for(Iterator oi = operators.iterator(); oi.hasNext(); ) {
				string.append(((BinaryEvaluator)oi.next()).getSymbol()).append(ei.next());
			}
			return string.append('}').toString();
		}
		
		/**
		 * Pretty dumb precedence fix.
		 * Recursively group highest precedences and evaluate parts if context free.
		 */
		public void fixPrecedences() {
			if (operators.size() == 0) return;
			int max = -1;
			for(Iterator i = operators.iterator(); i.hasNext();) {
				BinaryEvaluator evaluator = (BinaryEvaluator)i.next();
				if (evaluator.getPrecedence() > max) max = evaluator.getPrecedence();
			}
			
			
			for(int i = 0; i< operators.size(); ++i) {
				BinaryEvaluator evaluator = (BinaryEvaluator)operators.get(i);
				if (evaluator.getPrecedence() == max) {
					operators.remove(i);
					Expression lhs = (Expression)expressions.remove(i);
					Expression rhs = (Expression)expressions.get(i);
					if (!lhs.isConstant() || !rhs.isConstant()) {
						expressions.set(i, new ExpressionStack(lhs,rhs,evaluator));
					} else {
						expressions.set(i, new ConstantExpression(evaluator.evaluate(lhs.evaluate(null), rhs.evaluate(null))));
					}
					--i;
				}
			}
			
			fixPrecedences();
		}
		
		public boolean isConstant() {
			return constant;
		}
		
		public Set getColumns() {
			Set columns = new HashSet();
			for(Iterator i = expressions.iterator(); i.hasNext();) {
				columns.addAll(((Expression)i.next()).getColumns());
			}
			return columns;
		}
		
	}
	
	private static FunctionEvaluator countFunction = new FunctionEvaluator("count") {

		public double evaluate(List values) {
			return (double)values.size();
		}
	};
	
	private static FunctionEvaluator sumFunction = new FunctionEvaluator("sum") {

		public double evaluate(List values) {
			double sum = 0;
			for(Iterator i = values.iterator(); i.hasNext();) {
				sum += ((Double)i.next()).doubleValue();
			}
			return sum;
		}
	};
	
	private static FunctionEvaluator aveFunction = new FunctionEvaluator("ave") {
		
		public double evaluate(List values) {
			return values.size() == 0 ? 0 : sumFunction.evaluate(values)/(double)values.size();
		}
	};
	
	private static FunctionEvaluator stdFunction = new FunctionEvaluator("std") {
		public double evaluate(List values) {
			if (values.size() == 0) return 0;
			double ave = aveFunction.evaluate(values);
			double ss = 0;
			for(Iterator i = values.iterator(); i.hasNext();) {
				double d = ((Double)i.next()).doubleValue() - ave;
				ss += d*d;
			}
			return Math.sqrt(ss/(double)values.size());
		}
	};
	
	private static FunctionEvaluator medianFunction = new FunctionEvaluator("median") {
		
		private double get(List values, int i) {
			return ((Double)values.get(i)).doubleValue();
		}
		
		public double evaluate(List values) {
			if (values.size() == 0) return 0;
			List copy = new ArrayList(values.size());
			copy.addAll(values);
			Collections.sort(copy);
			int h = values.size()/2;
			return values.size() % 2 == 0 ? (get(copy,h) + get(copy,h+1))/2.0 : get(copy,h);
		}
	};
	
	private static FunctionEvaluator minFunction = new FunctionEvaluator("min") {
		public double evaluate(List values) {
			if (values.size() == 0) return 0;
			Iterator i = values.iterator();
			double m = getValue(i.next());
			while(i.hasNext()) {
				double v = getValue(i.next());
				if (v < m) m = v;
			}
			return m;
		}
	};
	
	private static FunctionEvaluator maxFunction = new FunctionEvaluator("max") {
		public double evaluate(List values) {
			if (values.size() == 0) return 0;
			Iterator i = values.iterator();
			double m = getValue(i.next());
			while(i.hasNext()) {
				double v = getValue(i.next());
				if (v > m) m = v;
			}
			return m;
		}
	};
	
	
	private static BinaryEvaluator additionOperator = new BinaryEvaluator('+',10) {
		protected double evaluate(double lhs, double rhs) {
			return lhs + rhs;
		}
	};
	
	private static BinaryEvaluator subtractionOperator = new BinaryEvaluator('-',10) {
		protected double evaluate(double lhs, double rhs) {
			return lhs - rhs;
		}
	};
	
	private static BinaryEvaluator multiplicationOperator = new BinaryEvaluator('*',20) {
		protected double evaluate(double lhs, double rhs) {
			return lhs * rhs;
		}
	};
	
	private static BinaryEvaluator divisionOperator = new BinaryEvaluator('/',20) {

		protected double evaluate(double lhs, double rhs) {
			return rhs == 0 ? 0 : lhs/rhs;
		}
		
	};
	
	// Map<String,FunctionEvaluator>
	private static Map functions = new TreeMap();
	
	static {
		functions.put(countFunction.getName(),countFunction);
		functions.put(sumFunction.getName(),sumFunction);
		functions.put(aveFunction.getName(),aveFunction);
		functions.put(stdFunction.getName(),stdFunction);
		functions.put(medianFunction.getName(), medianFunction);
		functions.put(minFunction.getName(), minFunction);
		functions.put(maxFunction.getName(), maxFunction);
	}
	
	private static Map binaryOperators = new TreeMap();
	
	static {
		binaryOperators.put(new Character('+'),additionOperator);
		binaryOperators.put(new Character('-'),subtractionOperator);
		binaryOperators.put(new Character('*'),multiplicationOperator);
		binaryOperators.put(new Character('/'),divisionOperator);
	}
	
	
	/** Parse an expression from stream.
	 * 
	 * @param is input stream
	 * @return expression
	 * @throws IOException
	 */
	public static Expression parseExpression(InputStream is) throws IOException {
		StreamTokenizer tokenizer = new StreamTokenizer(new InputStreamReader(is));
		
		tokenizer.parseNumbers();
		
		tokenizer.whitespaceChars(' ', ' ');
		tokenizer.whitespaceChars('\t', '\t');
		
		tokenizer.ordinaryChar('$');
		tokenizer.ordinaryChar('(');
		tokenizer.ordinaryChar(')');
		tokenizer.ordinaryChar('+');
		tokenizer.ordinaryChar('-');
		tokenizer.ordinaryChar('*');
		tokenizer.ordinaryChar('/');
		
		tokenizer.wordChars('a', 'z');
		tokenizer.wordChars('A', 'Z');
		tokenizer.wordChars('_', '_');
		
		tokenizer.eolIsSignificant(false);
		
		return parseExpression(tokenizer);
	}
	
	/**
	 * Parse a unary expression.
	 * Do everything except binary ops.
	 * @param tokenizer
	 * @return unary expression
	 * @throws IOException
	 */
	private static Expression parseUnaryExpression(StreamTokenizer tokenizer) throws IOException {
		int tt = tokenizer.nextToken();
		if (tt == StreamTokenizer.TT_EOF) throw new EOFException();
		else if (tt == StreamTokenizer.TT_NUMBER) return new ConstantExpression(tokenizer.nval);
		else if (tt == StreamTokenizer.TT_WORD) {
			FunctionEvaluator evaluator = (FunctionEvaluator)functions.get(tokenizer.sval);
			if (evaluator == null) throw new IOException("Unrecognized function " + tokenizer.sval);
			if (tokenizer.nextToken() != '(') throw new IOException("( expected");
			if (tokenizer.nextToken() != '$') throw new IOException("$ expected");
			if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) throw new IOException("Column expected");
			if ((Math.floor(tokenizer.nval) != tokenizer.nval)) throw new IOException("Invalid column index: " + tokenizer.nval);
			if (tokenizer.nextToken() != ')') throw new IOException(") expected");
			int column = (int)tokenizer.nval;
			return new ColumnFunction(evaluator,column);
		} else if (tt == '(') {
			Expression par = new ParenthesizedExpression(parseExpression(tokenizer));
			if (tokenizer.nextToken() != ')') throw new IOException(") expected");
			return par;
		} else throw new IOException("Invalid start of expression: " + tokenizer.sval);

	}
	
	/**
	 * Parse expression.
	 * Take a unary and see if followed by binary op chain.
	 * @param tokenizer
	 * @return parsed expression
	 * @throws IOException
	 */
	private static Expression parseExpression(StreamTokenizer tokenizer) throws IOException {
		ExpressionStack e = new ExpressionStack(parseUnaryExpression(tokenizer));
	
		for(;;) {
			int tt = tokenizer.nextToken();
			if (tt == ')') {
				tokenizer.pushBack();
				break;
			} else if (tt == StreamTokenizer.TT_EOF) break;
			Character op = new Character((char)tt);
			BinaryEvaluator evaluator = (BinaryEvaluator)binaryOperators.get(op);
			if (evaluator == null) throw new IOException("Unrecognized binary operator: " + tokenizer.sval);
			e.append(evaluator, parseUnaryExpression(tokenizer));
		}
		e.fixPrecedences();
		return e;
	}
	
	/**
	 * An aggregate formula involving columns.
	 * @author istvan
	 *
	 */
	public static class Formula {
		private String outputFile;
		private Expression expression;
		
		/**
		 * Constructor.
		 * @param directory output directory
		 * @param file output file
		 * @param expression formula to evaluate
		 */
		public Formula(String directory, String file, Expression expression) {
			outputFile = directory + fileSep + file;
			this.expression = expression;
		}
		
		/**
		 * Evaluate formula and write result.
		 * @param context
		 * @throws IOException
		 */
		public void evaluate(DataContext context) throws IOException {
			double v = expression.evaluate(context);
			FileOutputStream os = new FileOutputStream(outputFile);
			try {
				os.write(("YVALUE=" + v).getBytes());
				os.write(System.getProperty("line.separator","\n").getBytes());
			} finally {
				os.close();
			}
		}
		
		public Set getColumns() {
			return expression.getColumns();
		}
	}
	
	/**
	 * An aggregate statistic.
	 *
	 * Consists of regular expressions that select rows and formulae that aggregate the results.
	 * @author istvan
	 *
	 */
	public static class Aggregate {
		private String inputFile;
		
		private List matches = new ArrayList();
		private List formulae = new ArrayList();
		
		/**
		 * A regular expression over column values.
		 * @author istvan
		 *
		 */
		public static class Match {
			private int column;
			private Pattern regex;
			
			private boolean negated = false;
			
			public Match(int column, Pattern regex) {
				this.column = column;
				this.regex = regex;
			}
			
			public void negate() {
				negated = true;
			}
			
			public boolean isNegated() {
				return negated;
			}
			
			/**
			 * Remove rows that do not match.
			 * @param table
			 * @return
			 */
			public List match(List table) {
				
				List results = new ArrayList(table.size());
				
				for(Iterator i = table.iterator(); i.hasNext();) {
					List row = (List)i.next();
					Matcher matcher = regex.matcher(row.get(column).toString());
					boolean matches = matcher.matches();
					
					if ((matches && !negated) || (!matches && negated)) results.add(row);
				}
				
				return results;
			}
			
			
		}
		
		/**
		 * Constructor.
		 * @param directory output directory
		 * @param file output file
		 */
		public Aggregate(String directory, String file) {
			inputFile = directory + fileSep + file;	
		}
		
		public void addMatch(Match m) {
			matches.add(m);
		}
		
		public void addFormula(Formula formula) {
			formulae.add(formula);
		}
		
		public void evaluate() throws IOException {
			FileInputStream is = new FileInputStream(inputFile);
			
			try {
				// parse jmeter output file
				List input = CSVUtils.parse(is, true, false);
				
				// remove rows that do not match
				for(Iterator i = matches.iterator(); i.hasNext();) {
					Match m = (Match)i.next();
					input = m.match(input);
				}
				
				// get relevant columns
				Set columns = new HashSet();
				for(Iterator i = formulae.iterator(); i.hasNext();) {
					Formula formula = (Formula)i.next();
					columns.addAll(formula.getColumns());
				}
				
				DataContext context = new DataContext();
				
				// turn them into doubles
				for(Iterator i = columns.iterator(); i.hasNext();) {
					int c = ((Integer)i.next()).intValue();
					List values = new ArrayList(input.size());
					for(Iterator r = input.iterator(); r.hasNext();) {
						List row = (List)r.next();
						values.add(new Double(row.get(c).toString()));
					}
					context.addColumn(c, values);
				}
				
				// evaluate formulae
				for(Iterator i = formulae.iterator(); i.hasNext();) {
					Formula formula = (Formula)i.next();
					formula.evaluate(context);
				}
				
			} finally {
				is.close();
			}
		}
	}
	
	/**
	 * Name value pair.
	 * @author istvan
	 *
	 */
	public static class Parameter {
		private String name;
		private String value;
		
		public Parameter(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	/**
	 * A JMeter test instance.
	 * 
	 * May entail obtaining CMS data, providing parameters to JMeter and aggregating the results.
	 * 
	 * @author istvan
	 *
	 */
	public static class JMeterTest {
		
		private abstract static class DataRetrieval {
			protected String server;
			protected int port;
			protected String type;
			protected String file;
			protected StringBuffer url = null;
			protected List params = new ArrayList();
			
			public String getFile() {
				return file;
			}
			
			
			public void addQueryParameter(String name, String value) {
				url = null;
				params.add(new Parameter(name, expandReference(value)));
			}
			
			public DataRetrieval(String server, int port, String file, String type) {
				this.server = server;
				this.port = port;
				this.type = type;
				this.file = file;
			}
			
			public abstract String getUrl();
		}
		
		private static class CMSDataRetrieval extends DataRetrieval {
			
			public String getUrl() {
				if (url == null) {
					url = new StringBuffer(512);
					url.
						append("http://").
						append(server);
					if (port != -1) url.append(':').append(port);
					
					url.append("/test/data/data.jsp?type=");
					
					if ("product".equals(type)) url.append("products");
					else if ("category".equals(type)) url.append("categories");
					else if ("department".equals(type)) url.append("departments");
					else if ("recipe".equals(type)) url.append("recipes");
					else if ("all".equals(type)) url.append("all");
						
					url.append("&produce=true&output=csv");
					for(Iterator i = params.iterator(); i.hasNext();) {
						Parameter param = (Parameter)i.next();
						url.
							append('&').
							append(param.getName()).
							append('=').
							append(StringUtil.escapeUri(param.getValue()));
					}
				}
				return url.toString();
			}
			
			public CMSDataRetrieval(String server, int port, String file, String type) {
				super(server,port,file,type);
			}
		}
		
		private static class DyfCustomerDataRetrieval extends DataRetrieval {
			
			public DyfCustomerDataRetrieval(String server, int port, String file, String type) {
				super(server,port,file,type);
			}
			
			public String getUrl() {
				if (url == null) {
					url = new StringBuffer(128);
					url.
						append("http://").
						append(server);
					if (port != -1) url.append(':').append(port);
					url.append("/test/smartstore/dyf_cust_ids.jsp");
				}
				return url.toString();
			}
		}
		
		private static class SearchTermsRetrieval extends DataRetrieval {
			public SearchTermsRetrieval(String server, int port, String file, String type) {
				super(server,port,file,type);
			}
			
			public String getUrl() {
				if (url == null) {
					url = new StringBuffer(128);
					url.
						append("http://").
						append(server);
					if (port != -1) url.append(':').append(port);
					url.append("/test/search/search_terms.jsp");
				}
				return url.toString();
			}
		}
		
		private String jmxDirectory;
		
		private String jmeterJar;
		private String jmxScript;
		
		private boolean enabled = true;
		
		private List dataRetrievals = new ArrayList();
	
		private List JMeterParameters = new ArrayList();
		
		private List aggregates = new ArrayList();
		
		/**
		 * Constructor.
		 * @param jmeterJar Jar file of JMeter 
		 * @param jmxDirectory script directory
		 * @param jmxScript script name
		 */
		public JMeterTest(String jmeterJar, String jmxDirectory, String jmxScript) {
			this.jmeterJar = jmeterJar;
			this.jmxDirectory = jmxDirectory;
			this.jmxScript = jmxScript;
		}
		
		public void addJMeterParameter(String name, String value) {
			JMeterParameters.add(new Parameter(name,expandReference(value)));
		}
		
		public void addDataRetrieval(DataRetrieval dataRetrieval) {
			dataRetrievals.add(dataRetrieval);
		}
		
		public void addAggregate(Aggregate aggregate) {
			aggregates.add(aggregate);
		}
		
		public void disable() {
			enabled = false;
		}
		
		public void enable() {
			enabled = true;
		}
		
		public boolean isEnabled() {
			return enabled;
		}
		
		public String getName() {
			return jmxScript;
		}
		
		/**
		 * Run the test.
		 * 
		 * Retrieve CMS data (if applicable), run JMeter and aggregate the results.
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public void evaluate() throws IOException, InterruptedException {
			// find jmxFile
			String jmxFile = jmxDirectory + fileSep + jmxScript;
			
			// get the runtime
			Runtime runtime = Runtime.getRuntime();
			
			
			// retrieve data
			for(Iterator i = dataRetrievals.iterator(); i.hasNext();) {
				DataRetrieval dataRetrieval = (DataRetrieval)i.next();
				StringBuffer wgetCommand = new StringBuffer(512);
				
				String tmpFile = dataRetrieval.getFile() + ".tmp";
				
				wgetCommand.
					append("wget -O ").
					append(tmpFile).
					append(' ').
					append(dataRetrieval.getUrl());
				
				System.out.println("Executing " + wgetCommand);
				Process wget = runtime.exec(wgetCommand.toString());
				int wrc = wget.waitFor();
				if (wrc != 0) throw new RuntimeException("Command " + wgetCommand + " failed (rc = " + wrc + ')');
				BufferedReader is = 
					new BufferedReader(new InputStreamReader(new FileInputStream(tmpFile)));
				
				System.out.println("Removing redundant lines.");
				FileOutputStream os = new FileOutputStream(dataRetrieval.getFile());
				for(;;) {
					String line = is.readLine();
					if (line == null) {
						os.flush();
						os.close();
						is.close();
						File f = new File(tmpFile);
						f.delete();
						break;
					}
					line = line.trim();
					if (line.length() == 0) continue;
					os.write(line.getBytes());
					os.write('\n');	
				}
			}
			
			
			
			StringBuffer jmeterCommand = new StringBuffer(512);
			jmeterCommand.
					append("sh ").
					append(jmxDirectory).
					append(fileSep).
					append("jmeter.sh ").
					append(" -jar ").
					append(jmeterJar).
					append(" -n -t ").
					append(jmxFile);
			for(Iterator i= JMeterParameters.iterator(); i.hasNext();) {
				Parameter entry = (Parameter)i.next();
				jmeterCommand.
					append(" -J").
					append(entry.getName()).
					append('=').
					append(entry.getValue());
			}
			
			System.out.println("Executing " + jmeterCommand);
			Process jmeter = runtime.exec(jmeterCommand.toString());
			int rc = jmeter.waitFor();
			if (rc != 0) throw new RuntimeException("Command " + jmeterCommand + " failed.");
			
		
			System.out.println("Aggregating results");
			for(Iterator i = aggregates.iterator(); i.hasNext();) {
				Aggregate aggregate = (Aggregate)i.next();
				aggregate.evaluate();
			}
			
			System.out.println("Done.");
		}
		
		
	}

	/**
	 * XML SAX Parser for runner file.
	 * 
	 * @author istvan
	 *
	 */
	private class Handler extends DefaultHandler {
		
		
		private String jarFile = null;
		private String scriptDirectory = null;
		private String resultsDirectory = null;
		
		private List tests = new ArrayList();
		
		
		private JMeterTest currentTest;
		private JMeterTest.DataRetrieval currentRetrieval;
		private String dataSection = null;
		private String currentParameterName;
		private StringBuffer currentValue = new StringBuffer();
		private Aggregate currentAggregate;
		private int currentMatchColumn;
		private boolean currentMatchNegated;
		private String currentResultsFile;
		
		public void error(SAXParseException e) {
			throw new RuntimeException(e);
		}
		
		private void checkAttribute(String qName, String name, Attributes attributes) {
			if (attributes.getValue(name) == null) 
				throw new RuntimeException("Attribute \"" + name + "\" missing in " + qName);
		}
		
		private String getAttribute(String name, Attributes attributes) {
			return expandReference(attributes.getValue(name));
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			if (qName.equals("JMeterTests")) {
				
				checkAttribute(qName, "dir", attributes);
				checkAttribute(qName, "jar", attributes);
				
				scriptDirectory = getAttribute("dir",attributes);
				jarFile = getAttribute("jar",attributes);
				
				if (!(new File(scriptDirectory)).exists()) {
					throw new RuntimeException("Cannot access script directory " + scriptDirectory + '.');
				}
				
				if (!(new File(jarFile)).exists()) {
					throw new RuntimeException("Cannot access jmeter jar file " + jarFile + '.');
				}
				
			} else if (qName.equals("JMeterScript")) {
				
				checkAttribute(qName,"file",attributes);
				
				String script = getAttribute("file", attributes);
				String scriptPath = scriptDirectory +fileSep + script;
				if (!(new File(scriptPath)).exists()) {
					throw new RuntimeException("Cannot access script file " + script + '.');
				}
				
				currentTest = new JMeterTest(jarFile,scriptDirectory,script);
				
				String enabledStr = getAttribute("enabled",attributes);
				if (enabledStr != null) {
					if ("false".equals(enabledStr)) currentTest.disable();
					else if ("true".equals(enabledStr)) currentTest.enable();
					else throw new RuntimeException("false or true expected for attribute enabled instead of " + enabledStr);
				}
			} else if (qName.equals("CMSData")) {
				
				dataSection = "CMS";
			} else if (qName.equals("CustomerData")) {
				dataSection = "Customer";
			} else if (qName.equals("StaticData")) {
				dataSection = "Static";
			} else if (qName.equals("Retrieve")) {
				
				if (!"CMS".equals(dataSection) && !"Customer".equals(dataSection) && !"Static".equals(dataSection)) {
					throw new RuntimeException("Retrieve only expected in CMSData, CustomerData and Static sections");
				}
				
				checkAttribute(qName,"dir",attributes);
				checkAttribute(qName,"server", attributes);
				checkAttribute(qName,"file",attributes);
				checkAttribute(qName,"type",attributes);
				
				String outputDirectory = getAttribute("dir", attributes);
				
				File directoryInstance = new File(outputDirectory);
				
				if (!directoryInstance.exists()) directoryInstance.mkdirs();
				
				String server = getAttribute("server",attributes);
				
				int port = -1;
				if (attributes.getValue("port") != null) {
					String portString = getAttribute("port",attributes);
					try {
						port = Integer.parseInt(portString);
					} catch(NumberFormatException e) {
						throw new RuntimeException("Invalid value for port: " + portString);
					}
				}
				
				String type = getAttribute("type", attributes);
				
				
				String file = getAttribute("file", attributes);
				
				
				
				if ("CMS".equals(dataSection)) {

					if (!"product".equals(type) &&
						!"category".equals(type) &&
						!"recipe".equals(type) &&
						!"department".equals(type) &&
						!"all".equals(type)) {
						throw new RuntimeException("Expected product, category, recipe, department or all, instead of " + type + '.');
					}
				
					currentRetrieval = new JMeterTest.CMSDataRetrieval(server,port,outputDirectory + fileSep + file,type);
				} else if ("Customer".equals(dataSection)) {
					if ("dyfcustid".equals(type)) {
						currentRetrieval = new JMeterTest.DyfCustomerDataRetrieval(server,port,outputDirectory + fileSep + file, type);
					} else {
						throw new RuntimeException("Unknown customer retrieval type: " + type);
					}
				} else if ("Static".equals(dataSection)) {
					if ("searchterms".equals(type)) {
						currentRetrieval = new JMeterTest.SearchTermsRetrieval(server,port,outputDirectory + fileSep + file,type);
					} else {
						throw new RuntimeException("Unknown static retrieval type: " + type);
					}
				}
				
			} else if ("Parameter".equals(qName)) {
				checkAttribute(qName,"name", attributes);
				currentParameterName = getAttribute("name", attributes);
			} else if ("Results".equals(qName)) {
				checkAttribute(qName,"dir",attributes);
				resultsDirectory = getAttribute("dir", attributes);
				File resultsDirectoryInstance = new File(resultsDirectory);
				if (!resultsDirectoryInstance.exists()) resultsDirectoryInstance.mkdirs();
			} else if ("Aggregate".equals(qName)) {
				checkAttribute(qName, "file", attributes);
				String file = getAttribute("file", attributes);
				currentAggregate = new Aggregate(resultsDirectory,file);
			} else if ("Match".equals(qName)) {
				checkAttribute(qName, "column", attributes);
				String columnStr = getAttribute("column", attributes);
				try {
					currentMatchColumn = Integer.parseInt(columnStr);
				} catch(NumberFormatException e) {
					throw new RuntimeException("Invalid column value: " + columnStr);
				} 
				
				currentMatchNegated = false;
				String negatedStr = getAttribute("negate",attributes);
				if (negatedStr != null) {
					if ("true".equals(negatedStr)) currentMatchNegated = true;
					else if (!"false".equals(negatedStr)) 
						throw new RuntimeException("true or false expected for negated, not " + negatedStr);
				}
			} else if ("Formula".equals(qName)) {
				checkAttribute(qName, "file", attributes);
				currentResultsFile = getAttribute("file", attributes);
				
			
			}
			
			currentValue.setLength(0);
		}
		
		public void endElement(String uri, String localName, String qName) {
			if ("Retrieve".equals(qName)) {
				currentTest.addDataRetrieval(currentRetrieval);
				currentRetrieval = null;
			} else if ("Parameter".equals(qName)) {
				if (currentRetrieval == null) { // jmeter parameter
					currentTest.addJMeterParameter(currentParameterName, currentValue.toString().trim());
					// TODO
				} else {
					currentRetrieval.addQueryParameter(currentParameterName, currentValue.toString().trim());
				}
			} else if ("Aggregate".equals(qName)) {
				currentTest.addAggregate(currentAggregate);
			} else if ("Match".equals(qName)) {
				Pattern pattern;
				try {
					pattern = Pattern.compile(currentValue.toString().trim());
				} catch(Exception e) {
					throw new RuntimeException("Could not compile regexp: " + currentValue);
				}
				Aggregate.Match m = new Aggregate.Match(currentMatchColumn,pattern);
				if (currentMatchNegated) m.negate();
				currentAggregate.addMatch(m);
			} else if ("Formula".equals(qName)) {
				Expression expression;
				try {
					expression = parseExpression(new ByteArrayInputStream(currentValue.toString().getBytes()));
				} catch (Exception e) {
					throw new RuntimeException("Could not parse expression: " + currentValue + " (" + e.getMessage() + ')');
				}
				Formula formula = new Formula(resultsDirectory, currentResultsFile, expression);
				currentAggregate.addFormula(formula);
			} else if ("JMeterScript".equals(qName)) {
				tests.add(currentTest);
			}
		}
		
		public void warning(SAXParseException e) {
		}
		
		public void characters(char[] ch, int start, int length) throws SAXException {
			currentValue.append(ch,start,length);
		}
	}
	
	
	/**
	 * Parse config XML.
	 * 
	 * @param is
	 * @return List<JMeterTest>
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public List parse(InputStream is) throws IOException, SAXException, ParserConfigurationException {
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		
		Handler handler = new Handler();
		
		parser.parse(new InputSource(is),handler);
		return handler.tests;
	}
	
	/**
	 * 
	 * Run from command line.
	 * Runs the tests whose file names are given in the command line.
	 * 
	 * Provide <tt>-Djmx.directory</tt> for the directory of JMeter script files.
	 * 
	 * @param argv command line arguments
	 * @throws Exception
	 */
	public static void main(String[] argv) {
		String jmeterDir = System.getProperty("jmx.directory");
		if (jmeterDir == null) {
			throw new RuntimeException("System property jmx.directory undefined");
		}
		
		ScriptRunner runner = new ScriptRunner();
		
		
		for(int i=0; i< argv.length; ++i) {
			try {
				List tests = runner.parse(new FileInputStream(jmeterDir + fileSep + argv[i]));
				for(Iterator t = tests.iterator(); t.hasNext(); ) {
					JMeterTest test = (JMeterTest)t.next();
					if (!test.isEnabled()) continue;
					try {
						test.evaluate();
					} catch (Exception e) {
						System.err.println("In " + test.getName() + ":   " + e);
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.err.println(e);
			}
		}
	}
}
