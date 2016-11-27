package com.freshdirect.dashboard.drools;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.freshdirect.dashboard.drools.decisiontable.ProjectedUtilizationRuleDT;
import com.freshdirect.dashboard.model.ProjectedUtilizationBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "classpath:capacity-rule-context.xml" })
public class ProjectedUtilizationRuleDTTest {

	@Autowired
	ProjectedUtilizationRuleDT ruleRunner;

	@Test
	public void testExecuteExample() throws Exception {
        ProjectedUtilizationBase pu = new ProjectedUtilizationBase();
        pu.setZone("400");
        pu.setHoursToCutoff(5.0);
        pu.setConfirmedUtilization(76);
        pu.setProjectedUtilization(105);
       
		assertTrue( ruleRunner.executeSuggestedAction(pu) == "A");
	}
}
