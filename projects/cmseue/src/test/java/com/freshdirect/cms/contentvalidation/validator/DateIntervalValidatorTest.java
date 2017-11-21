package com.freshdirect.cms.contentvalidation.validator;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.validation.ValidationResults;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class DateIntervalValidatorTest {

    @InjectMocks
    private DateIntervalValidator underTest;

    @Test
    public void testValidateWhenEverythingIsOkay() {
        ContentKey scheduleKey = ContentKeyFactory.get(ContentType.Schedule, "scheduleForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.Schedule.StartDate, new Date());
        attributesWithValues.put(ContentTypes.Schedule.EndDate, new Date());

        ValidationResults validationResults = underTest.validate(scheduleKey, attributesWithValues, null);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenStartDateIsNull() {
        ContentKey scheduleKey = ContentKeyFactory.get(ContentType.Schedule, "scheduleForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.Schedule.StartDate, null);
        attributesWithValues.put(ContentTypes.Schedule.EndDate, new Date());

        ValidationResults validationResults = underTest.validate(scheduleKey, attributesWithValues, null);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenEndDateIsNull() {
        ContentKey scheduleKey = ContentKeyFactory.get(ContentType.Schedule, "scheduleForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.Schedule.StartDate, new Date());
        attributesWithValues.put(ContentTypes.Schedule.EndDate, null);

        ValidationResults validationResults = underTest.validate(scheduleKey, attributesWithValues, null);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenStartDateAndEndDateIsNull() {
        ContentKey scheduleKey = ContentKeyFactory.get(ContentType.Schedule, "scheduleForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.Schedule.StartDate, null);
        attributesWithValues.put(ContentTypes.Schedule.EndDate, null);

        ValidationResults validationResults = underTest.validate(scheduleKey, attributesWithValues, null);
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenEndDateIsBeforeStartDate() {
        ContentKey scheduleKey = ContentKeyFactory.get(ContentType.Schedule, "scheduleForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();
        attributesWithValues.put(ContentTypes.Schedule.EndDate, new Date(System.currentTimeMillis() - 1000));
        attributesWithValues.put(ContentTypes.Schedule.StartDate, new Date(System.currentTimeMillis()));

        ValidationResults validationResults = underTest.validate(scheduleKey, attributesWithValues, null);
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }
}
