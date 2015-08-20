package com.freshdirect.cms.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.context.FulfillmentContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.Recommender;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.RecommendationServiceTestBase;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.scoring.HelperFunctions;
import com.freshdirect.smartstore.scoring.MockFilterFactory;

public class SmartCategoryRecommenderTest extends RecommendationServiceTestBase {
	@Override
	public void setUp() throws Exception {
		super.setUp();

		// Turn off availability filtering in SmartStore layer
		FilterFactory.mockInstance(new MockFilterFactory());
	}

	@Override
	protected void initAspects(AspectSystem aspectSystem) {
	}

	@Override
	protected String getCmsXmlName() {
		return "classpath:/com/freshdirect/cms/fdstore/content/SmartCategory.xml";
	}

	public void testSmartCategory() {
		// create test user
		// final MockPageContext ctx =
		// TestUtils.createMockPageContext(TestUtils.createUser("pk1", "erp1",
		// "fd1"));

		Recommender test = (Recommender) ContentFactory.getInstance()
				.getContentNode(FDContentTypes.RECOMMMENDER, "testRec");

		assertNotNull(test);

		List<ContentNodeModel> expList = new ArrayList<ContentNodeModel>(1);
		expList.add(ContentFactory.getInstance().getContentNode(
				FDContentTypes.CATEGORY, "smcat1"));

		SessionInput input = new SessionInput.Builder().setCustomerId("fd1")
				.setServiceType(EnumServiceType.HOME)
				.setPricingContext(PricingContext.DEFAULT)
				.setFulfillmentContext(new FulfillmentContext())
				// .setCurrentNode(ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY,
				// "smcat1"))
				.setExplicitList(expList).build();

		List<? extends ContentNodeModel> result = HelperFunctions
				.getSmartCategoryRecommendation(input);

		assertNotNull(result);
		assertEquals(3, result.size());
	}
}
