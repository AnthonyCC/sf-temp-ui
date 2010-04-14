package com.freshdirect.smartstore.fdstore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;

/**
 * Test distributions.
 * 
 * @author istvan
 *
 */
public class SamplingTest extends SamplingTestsBase {

	
	private int N = 30000;
	private double maximumAverageError = 15;
	
	private static Map<ContentKey, Integer> indexMap = new HashMap<ContentKey, Integer>();
	
	static {
		indexMap.put(BANAN, new Integer(0));
		indexMap.put(CITROM, new Integer(1));
		indexMap.put(EPER, new Integer(2));
		indexMap.put(CSERESZNYE, new Integer(3));
		indexMap.put(MEGGY, new Integer(4));
		indexMap.put(ZOLDALMA, new Integer(5));
		indexMap.put(EGRES, new Integer(6));
		indexMap.put(CITROMIZUBANAN, new Integer(7));		
	}
	
	public void testDeterministic() {
		MockedImpressionSampler sampler = MockedImpressionSampler.create("deterministic");
		
		List<ContentKey> keys = sampler.sample(sampler.getCandidates(), false, Collections.EMPTY_SET);
		
		assertTrue(keys.size() == 8);
		assertEquals(getLabel(keys.get(0)),getLabel(CITROMIZUBANAN));
		assertEquals(getLabel(keys.get(1)),getLabel(EGRES));
		assertEquals(getLabel(keys.get(2)),getLabel(ZOLDALMA));
		assertEquals(getLabel(keys.get(3)),getLabel(MEGGY));
		assertEquals(getLabel(keys.get(4)),getLabel(CSERESZNYE));
		assertEquals(getLabel(keys.get(5)),getLabel(EPER));
		assertEquals(getLabel(keys.get(6)),getLabel(CITROM));
		assertEquals(getLabel(keys.get(7)),getLabel(BANAN));
	}
	
	private String format(String s, int l) {
		if (s.length()>=l) return s;
		else {
			StringBuffer buff = new StringBuffer(l);
			for(int i=0; i< l-s.length(); ++i) buff.append(' ');
			buff.append(s);
			return buff.toString();
		}
	}
	
	private String format(int i, int l) {
		NumberFormat nf = new DecimalFormat("#,###,###");
		return format(nf.format(i),l);
	}
	
	private boolean assertWithinPercent(String strategy, int[][] expected) {
		System.out.println();
		System.out.println("--- SAMPLING STRATEGY: " + strategy + " ---");
		System.out.println();
		System.out.println("C: Calculated (or drawn), E: Expected");
		System.out.println("Average error limit " + maximumAverageError + '%');
		System.out.println();
		
		
		MockedImpressionSampler sampler = MockedImpressionSampler.create(strategy);
		
		int [][] X = new int [expected.length][expected.length];
		
		String[] stuff = new String[expected.length];
		
		for(int x=0; x< N; ++x) {
			List<ContentKey> keys = sampler.sample(sampler.getCandidates(), false, Collections.EMPTY_SET);
			
			int c = 0;
			for(Iterator<ContentKey> i = keys.iterator(); i.hasNext();++c) {
				ContentKey key = i.next();
				int ind = ((Number)indexMap.get(key)).intValue();
				++X[ind][c];
				stuff[ind] = key.getId();
			}
		}
		
		for(int i=0; i< expected.length; ++i) {
			System.out.print(format(stuff[i],17));
			System.out.print(": ");
			for(int j=0; j< expected[i].length; ++j) {
				System.out.print(" C: ");
				System.out.print(format(X[i][j],6));
				System.out.print(" E: ");
				System.out.print(format(expected[i][j],6));
				System.out.print(' ');
			}
			System.out.println();
		}
		
		double averageError[] = new double[expected[0].length];
		double maxError = 0;
		for(int j=0; j< expected[0].length; ++j) {
			averageError[j] = 0;
			for(int i=0; i< expected.length; ++i) {
				double error = 
					((double)Math.abs(X[i][j] - expected[i][j]))/(expected[i][j] == 0 ? 1.0 : (double)expected[i][j]);
				if (error > maxError) maxError = error;
				averageError[j] += error;
			}
			averageError[j] /= (double)(expected.length);
		}
		
		double averageAverageError = 0;
		for(int j=0; j< averageError.length; ++j) averageAverageError += averageError[j];
		averageAverageError /= (double)averageError.length;
		
		System.out.println();
		System.out.println("Maximum Error: " + (int)(maxError*100.0) + '%');
		
		if (averageError.length > 1) {
			for(int j=0; j< averageError.length; ++j) {
				System.out.println("Average Error (chosen as " + (j+1) + "th): " + (int)(averageError[j]*100.0) + '%');
			}
		}
		System.out.println("Average Error: " + (int)(averageAverageError*100.0) + '%');
		
		if (100.0*averageAverageError > maximumAverageError) {
			fail("Average error " + (int)(100.0*averageAverageError) + "% larger than " + maximumAverageError + "%");
		}
		
		return true;
	}
	
	public void testUniform() {
		int [][] E = new int [8][8];
		
		for(int i=0; i< 8; ++i) {
			for(int j=0; j< 8; ++j) {
				E[i][j] = N/8;
			}
		}
		
		assertWithinPercent("uniform", E);		
	}
	
	public void testLinear() {
		int [][] E = new int [8][1];
		
		int t = 0;
		for(int i=0; i<8; ++i) {
			E[i][0] = i+1;
			t += E[i][0];
		}
		
		for(int i=0; i< 8; ++i) {
			E[i][0] = (E[i][0]*N)/t;
		}
		
		assertWithinPercent("linear", E);
	}
	
	public void testQuadratic() {
		int [][] E = new int [8][1];
		
		int t = 0;
		for(int i=0; i<8; ++i) {
			E[i][0] = (i+1)*(i+1);
			t += E[i][0];
		}
		
		for(int i=0; i< 8; ++i) {
			E[i][0] = (E[i][0]*N)/t;
		}
		
		assertWithinPercent("quadratic", E);
	}
	
	public void testCubic() {
		int [][] E = new int [8][1];
		
		int t = 0;
		for(int i=0; i<8; ++i) {
			E[i][0] = (i+1)*(i+1)*(i+1);
			t += E[i][0];
		}
		
		for(int i=0; i< 8; ++i) {
			E[i][0] = (E[i][0]*N)/t;
		}
		
		assertWithinPercent("cubic", E);
	}
	
	
	public void testPower() {
		int [][] E = new int [8][1];
		int [] T = new int[8];
		
		int t = 0;
		for(int i=0; i< 8; ++i) {
			T[i] = (int)(10000.0*(Math.pow(i+1.0,exponent))/(Math.pow(7, exponent) - Math.pow(6,exponent)));
			E[i][0] = i == 0 ? T[i] : T[i] - T[i-1];
			t += E[i][0];
		}
		
		
		for(int i=0; i< 8; ++i) {
			E[i][0] = ((E[i][0]*N)/t);
		}
		
		for(int i=0; i<= 4; ++i) {
			int tmp = E[i][0];
			E[i][0] = E[7-i][0];
			E[7-i][0] = tmp;
		}
		
		assertWithinPercent("power", E);
	}
	
	public void testSQRT() {
		int [][] E = new int [8][1];
		int [] T = new int[8];
		
		int t = 0;
		for(int i=0; i< 8; ++i) {
			T[i] = (int)((100.0*(Math.sqrt(i+1.0)))/(Math.sqrt(8) - Math.sqrt(7)));
			E[i][0] = i == 0 ? T[i] : T[i] - T[i-1];
			t += E[i][0];
		}
		
		for(int i=0; i< 8; ++i) {
			E[i][0] = ((E[i][0]*N)/t);
		}
		
		for(int i=0; i<= 4; ++i) {
			int tmp = E[i][0];
			E[i][0] = E[7-i][0];
			E[7-i][0] = tmp;
		}
		
		assertWithinPercent("sqrt", E);
	}
	
	public void testHarmonic() {
		int [][] E = new int [8][1];
		int [] T = new int[8];
		
		int t = 0;
		for(int i=0; i< 8; ++i) {
			T[i] = (int)(10000.0*(Math.log(i+1) + 1.57721)/(Math.log(8) - Math.log(7)));
			E[i][0] = i == 0 ? T[i] : T[i] - T[i-1];
			t += E[i][0];
		}
		
		
		for(int i=0; i< 8; ++i) {
			E[i][0] = (int)(((long)E[i][0]*(long)N)/(long)t);
		}
		
		for(int i=0; i<= 4; ++i) {
			int tmp = E[i][0];
			E[i][0] = E[7-i][0];
			E[7-i][0] = tmp;
		}
		
		assertWithinPercent("harmonic", E);
	}
	
	
	private class ContentKeyPath {
		private ContentKey key;
		private ContentKeyPath parent;
		private int l;
		double p;
		
		private ContentKeyPath(ContentKey key, ContentKeyPath parent, double p) {
			this.key = key;
			this.parent = parent;
			this.l = parent == null ? -1 : parent.level() + 1;
			this.p = p < 0 ? 0 : p;
		}
		
		public ContentKeyPath() {
			this(null,null,1.0);
		}
		
		public boolean isSeen(ContentKey child) {
			if (child.equals(key)) return true;
			ContentKeyPath up = parent;
			while(up != null) {
				if (child.equals(up.getKey())) {
					return true;
				}
				up = up.getParent();
			}
			return false;
		}
		
		public ContentKey getKey() {
			return key;
		}
		
		public int level() {
			return l;
		}
		
		public ContentKeyPath getParent() {
			return parent;
		}
		
		public double getP() {
			return p;
		}
		
		public List getChildren() {
			ArrayList children = new ArrayList(8 - level());
			double t = 0;
			for(Iterator i = scoreMap.entrySet().iterator(); i.hasNext();) {
				Map.Entry e = (Map.Entry)i.next();
				ContentKey cand = (ContentKey)e.getKey();
				if (isSeen(cand)) continue;
				double score = ((Number)e.getValue()).doubleValue();
				t += score;
				children.add(new ContentKeyPath(cand,this,score));
			}
			for(Iterator i = children.iterator(); i.hasNext();) {
				ContentKeyPath path = (ContentKeyPath)i.next();
				path.p = (this.p * path.p)/t;
			}
			
			return children;
		}
	}
	
	public void testComplicated() {
		
		int [][] E = new int[8][8];
		double [][] P = new double [8][8];
		
		List S = new LinkedList(new ContentKeyPath().getChildren());
		
		while(!S.isEmpty()) {
			ContentKeyPath me = (ContentKeyPath)S.remove(0);
			P[((Number)indexMap.get(me.getKey())).intValue()][me.level()] += me.getP();
			for(Iterator i = me.getChildren().iterator(); i.hasNext();) {
				S.add(0,i.next());
			}
		}
			
		for(int i=1; i< 8; ++i) {
			for(int j=0; j< 7; ++j) {
				E[i][j] = (int)(P[i][j]*N);
			}
		}
		
		for(int i=0; i< 8; ++i) {
			E[0][i] = E[i][7] = 0;
		}
		E[0][7] = N;
		
		assertWithinPercent("complicated", E);
	}
	
}
