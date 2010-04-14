package com.freshdirect.smartstore.external;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.CompositeTypeService;
import com.freshdirect.cms.application.service.xml.FlexContentHandler;
import com.freshdirect.cms.application.service.xml.XmlContentService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.aspects.ScoreFactorGlobalNameAspect;
import com.freshdirect.fdstore.aspects.ScoreFactorPersonalNameAspect;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.dsl.CompileException;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.smartstore.scoring.DataAccess;
import com.freshdirect.smartstore.scoring.DataGenerator;
import com.freshdirect.smartstore.scoring.DataGeneratorCompiler;

public class ExternalRecommenderTest extends TestCase {
	private static final class TestScoreProvider extends ScoreProvider {
		public TestScoreProvider() {
			super(false);
		}
	}

	protected static class Recommender1 implements ExternalRecommender {
		boolean change;
		
		public void setChange(boolean change) {
			this.change = change;
		}
		
		@Override
		public List<RecommendationItem> recommendItems(ExternalRecommenderRequest request) throws ExternalRecommenderCommunicationException {
			// smoke test
			if ("123".equals(request.getCustomerId()))
				return new ArrayList<RecommendationItem>();
			else if ("456".equals(request.getCustomerId())) {
				List<RecommendationItem> items = new ArrayList<RecommendationItem>(5);
				if (change)
					items.add(new RecommendationItem("b1"));
				items.add(new RecommendationItem("a1"));
				items.add(new RecommendationItem("a2"));
				items.add(new RecommendationItem("e1"));
				items.add(new RecommendationItem("unknown1"));
				return items;
			} else
				throw new ExternalRecommenderCommunicationException("communication error when trying with unknown user");
		}
	}

	protected static class Recommender2 extends Recommender1 {
	}

	protected static class Recommender3 implements ExternalRecommender {
		@Override
		public List<RecommendationItem> recommendItems(ExternalRecommenderRequest request) throws ExternalRecommenderCommunicationException {
			Set<RecommendationItem> items = new HashSet<RecommendationItem>();
			if (request.getItems().contains(new RecommendationItem("a1"))) {
				items.add(new RecommendationItem("a1"));
				items.add(new RecommendationItem("a2"));
				items.add(new RecommendationItem("a3"));
			}
			if (request.getItems().contains(new RecommendationItem("e1"))) {
				items.add(new RecommendationItem("e1"));
				items.add(new RecommendationItem("e2"));
				items.add(new RecommendationItem("e3"));
			}
			List<RecommendationItem> list = new ArrayList<RecommendationItem>(items);
			Collections.sort(list, new Comparator<RecommendationItem>() {
				@Override
				public int compare(RecommendationItem o1, RecommendationItem o2) {
					return o1.getId().compareTo(o2.getId());
				}
			});
			return list;
		}
	}

	private class MockDataAccess implements DataAccess {
		@Override
		public boolean addPrioritizedNode(ContentNodeModel model) {
			fail("should not be called");
			return false;
		}

		@Override
		public List<ContentNodeModel> getDatasource(SessionInput input, String name) {
			fail("should not be called");
			return null;
		}

		@Override
		public List<ContentNodeModel> getPrioritizedNodes() {
			fail("should not be called");
			return null;
		}

		@Override
		public double[] getVariables(String userId, PricingContext pricingContext, ContentNodeModel contentNode, String[] variables) {
                    fail("should not be called");
                    return null;
		}
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		// initialize CMS from XML with a sample set of products (a1, a2, a3,
		// b1, etc.)
		List<ContentTypeServiceI> list = new ArrayList<ContentTypeServiceI>();

		list.add(new XmlTypeService("classpath:/com/freshdirect/cms/resource/CMSStoreDef.xml"));

		CompositeTypeService typeService = new CompositeTypeService(list);

		XmlContentService service = new XmlContentService(typeService, new FlexContentHandler(), "classpath:/com/freshdirect/cms/fdstore/content/simple2.xml");

		CmsManager.setInstance(new CmsManager(service, null));

		AspectSystem aspectSystem = TestUtils.createAspectSystem();
        aspectSystem.add(new ScoreFactorGlobalNameAspect(Collections.EMPTY_SET));
        aspectSystem.add(new ScoreFactorPersonalNameAspect(Collections.EMPTY_SET));
	}

	public void testRegistry() throws NoSuchExternalRecommenderException {
		try {
			ExternalRecommenderRegistry.getInstance(null, ExternalRecommenderType.PERSONALIZED);
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
			assertEquals("providerName cannot be null", e.getMessage());
		}
		try {
			ExternalRecommenderRegistry.getInstance("", ExternalRecommenderType.PERSONALIZED);
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
			assertEquals("providerName cannot be null", e.getMessage());
		}
		try {
			ExternalRecommenderRegistry.getInstance("foo", null);
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
			assertEquals("recommenderType cannot be null", e.getMessage());
		}
		try {
			ExternalRecommenderRegistry.getInstance("foo", ExternalRecommenderType.PERSONALIZED);
			fail("should throw NoSuchExternalRecommenderException");
		} catch (NoSuchExternalRecommenderException e) {
		}
		try {
			ExternalRecommenderRegistry.registerRecommender("bar", ExternalRecommenderType.PERSONALIZED, null);
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
		}
		try {
			ExternalRecommenderRegistry.registerRecommender(null, ExternalRecommenderType.PERSONALIZED, new Recommender1());
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
		}
		try {
			ExternalRecommenderRegistry.registerRecommender("bar", null, new Recommender1());
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
		}

		// registering new recommender should return true status
		boolean status = ExternalRecommenderRegistry.registerRecommender("foo", ExternalRecommenderType.PERSONALIZED, new Recommender1());
		assertEquals(true, status);

		// new registration should overwrite the previous
		status = ExternalRecommenderRegistry.registerRecommender("foo", ExternalRecommenderType.PERSONALIZED, new Recommender2());
		assertEquals(false, status);

		// unregister should succeed
		status = ExternalRecommenderRegistry.unregisterRecommender("foo", ExternalRecommenderType.PERSONALIZED);
		assertEquals(true, status);
		try {
			ExternalRecommenderRegistry.getInstance("foo", ExternalRecommenderType.PERSONALIZED);
			fail("should throw NoSuchExternalRecommenderException");
		} catch (NoSuchExternalRecommenderException e) {
		}
		try {
			ExternalRecommenderRegistry.unregisterRecommender("bar", null);
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
		}
		try {
			ExternalRecommenderRegistry.unregisterRecommender(null, ExternalRecommenderType.PERSONALIZED);
			fail("cannot accept illegal argument");
		} catch (IllegalArgumentException e) {
		}

		// unregister non-registered should fail
		status = ExternalRecommenderRegistry.unregisterRecommender("foo", ExternalRecommenderType.PERSONALIZED);
		assertEquals(false, status);
	}

	public void testPersonalized() {
		// clean up registry and register foo
		ExternalRecommenderRegistry.unregisterRecommender("foo", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("bar", ExternalRecommenderType.PERSONALIZED);
		Recommender1 recommender;
		ExternalRecommenderRegistry.registerRecommender("foo", ExternalRecommenderType.PERSONALIZED, recommender = new Recommender1(), 10, 2000);

		DataGeneratorCompiler compiler = new DataGeneratorCompiler(new String[0]);
		try {
			compiler.createDataGenerator("ext1", "PersonalItems_bar()");
			fail("cannot accept non-registered recommender (bar)");
		} catch (CompileException e) {
		}
		try {
			compiler.createDataGenerator("ext2", "PersonalizedItems_foo(currentNode)");
			fail("this function should not accept any parameter");
		} catch (CompileException e) {
		}
		try {
			DataGenerator generator = compiler.createDataGenerator("ext3", "PersonalizedItems_foo()");
			SessionInput input = new SessionInput("123", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			List<ContentNodeModel> nodes = generator.generate(input, new MockDataAccess());
			assertEquals(0, nodes.size());

			// should return different result for different user
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(3, nodes.size());
			assertEquals("a1", nodes.get(0).getContentKey().getId());
			assertEquals("a2", nodes.get(1).getContentKey().getId());
			assertEquals("e1", nodes.get(2).getContentKey().getId());
			
			// cache test
			recommender.setChange(true);
			
			// change should take effect only after timeout
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(3, nodes.size());
			assertEquals("a1", nodes.get(0).getContentKey().getId());
			assertEquals("a2", nodes.get(1).getContentKey().getId());
			assertEquals("e1", nodes.get(2).getContentKey().getId());

			try {
				Thread.sleep(2200);
			} catch (InterruptedException e) {
				fail("thread interrupted while asleep");
			}
			// now it should really take effect
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(4, nodes.size());
			assertEquals("b1", nodes.get(0).getContentKey().getId());
			assertEquals("a1", nodes.get(1).getContentKey().getId());
			assertEquals("a2", nodes.get(2).getContentKey().getId());
			assertEquals("e1", nodes.get(3).getContentKey().getId());
			

			// should return no results for unknown users (even if the
			// communication fails internally)
			input = new SessionInput("unknown", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(0, nodes.size());
		} catch (CompileException e) {
			fail("should compile registered recommender properly");
		}
	}

	public void testRelated() {
		// clean up registry and register baz
		ExternalRecommenderRegistry.unregisterRecommender("baz", ExternalRecommenderType.RELATED);
		ExternalRecommenderRegistry.registerRecommender("baz", ExternalRecommenderType.RELATED, new Recommender3());
		DataGeneratorCompiler compiler = new DataGeneratorCompiler(new String[0]);
		try {
			compiler.createDataGenerator("ext4", "RelatedItems_bar()");
			fail("cannot accept non-registered recommender (bar)");
		} catch (CompileException e) {
		}
		try {
			compiler.createDataGenerator("ext5", "RelatedItems_baz()");
			fail("this function should have exactly one parameter");
		} catch (CompileException e) {
		}
		try {
			compiler.createDataGenerator("ext6", "RelatedItems_baz(currentNode, explicitList)");
			fail("this function should have exactly one parameter");
		} catch (CompileException e) {
		}
		try {
			// testing currentNode and explicitList combined into a single list
			DataGenerator generator = compiler.createDataGenerator("ext7", "RelatedItems_baz(toList(currentNode,explicitList))");
			SessionInput input = new SessionInput("123", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			input.setCurrentNode(ContentFactory.getInstance().getContentNode("e1"));
			List<ContentNodeModel> nodes = generator.generate(input, new MockDataAccess());
			assertEquals(3, nodes.size());
			assertEquals("e1", nodes.get(0).getContentKey().getId());
			assertEquals("e2", nodes.get(1).getContentKey().getId());
			assertEquals("e3", nodes.get(2).getContentKey().getId());

			// should return the same for different user
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			input.setCurrentNode(ContentFactory.getInstance().getContentNode("e1"));
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(3, nodes.size());
			assertEquals("e1", nodes.get(0).getContentKey().getId());
			assertEquals("e2", nodes.get(1).getContentKey().getId());
			assertEquals("e3", nodes.get(2).getContentKey().getId());

			// no result if no input
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(0, nodes.size());

			// works for items coming from explicit list
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			List<ContentNodeModel> explicitList = new ArrayList<ContentNodeModel>();
			explicitList.add(ContentFactory.getInstance().getContentNode("e1"));
			input.setExplicitList(explicitList);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(3, nodes.size());
			assertEquals("e1", nodes.get(0).getContentKey().getId());
			assertEquals("e2", nodes.get(1).getContentKey().getId());
			assertEquals("e3", nodes.get(2).getContentKey().getId());

			// works for multiple items coming from various sources
			input = new SessionInput("456", EnumServiceType.CORPORATE, PricingContext.DEFAULT);
			input.setCurrentNode(ContentFactory.getInstance().getContentNode("e1"));
			explicitList = new ArrayList<ContentNodeModel>();
			explicitList.add(ContentFactory.getInstance().getContentNode("a1"));
			input.setExplicitList(explicitList);
			nodes = generator.generate(input, new MockDataAccess());
			assertEquals(6, nodes.size());
			assertEquals("a1", nodes.get(0).getContentKey().getId());
			assertEquals("a2", nodes.get(1).getContentKey().getId());
			assertEquals("a3", nodes.get(2).getContentKey().getId());
			assertEquals("e1", nodes.get(3).getContentKey().getId());
			assertEquals("e2", nodes.get(4).getContentKey().getId());
			assertEquals("e3", nodes.get(5).getContentKey().getId());
		} catch (CompileException e) {
			fail("should compile registered recommender properly");
		}
	}
	
	public void testScoring() {
		ExternalRecommenderRegistry.unregisterRecommender("foo", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.unregisterRecommender("bar", ExternalRecommenderType.PERSONALIZED);
		ExternalRecommenderRegistry.registerRecommender("foo", ExternalRecommenderType.PERSONALIZED, new Recommender1(), 10, 20000);
		ScoreProvider.setInstance(new TestScoreProvider());
		String[] factors = { "Personalized_foo" };
		double[] x = ScoreProvider.getInstance().getVariables("456", null, ContentFactory.getInstance().getContentNode("a1"), factors);
		assertEquals(4.0, x[0]);
		x = ScoreProvider.getInstance().getVariables("456", null, ContentFactory.getInstance().getContentNode("a2"), factors);
		assertEquals(3.0, x[0]);
		x = ScoreProvider.getInstance().getVariables("456", null, ContentFactory.getInstance().getContentNode("e1"), factors);
		assertEquals(2.0, x[0]);
		x = ScoreProvider.getInstance().getVariables("456", null, ContentFactory.getInstance().getContentNode("x1"), factors);
		assertEquals(0.0, x[0]);
	}
}
