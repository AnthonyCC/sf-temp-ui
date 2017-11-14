package com.freshdirect.cms.validation.validator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.freshdirect.cms.category.UnitTest;
import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.validation.ValidationResultLevel;
import com.freshdirect.cms.validation.ValidationResults;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class TypeValidatorTest {

    @Mock
    private ContentTypeInfoService contentTypeInfoService;

    @InjectMocks
    private TypeValidator underTest;

    @Test
    public void testValidate() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Product))
                .thenReturn(new HashSet<Attribute>(Arrays.asList(new Attribute[] {ContentTypes.Product.FULL_NAME, ContentTypes.Product.NAV_NAME})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Product, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Product.FULL_NAME, "Test full name");
        attributes.put(ContentTypes.Product.NAV_NAME, "Test nav name");

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithRequired() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Image))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.Image.height, ContentTypes.Image.width, ContentTypes.Image.lastmodified, ContentTypes.Image.path})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Image, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Image.height, new Integer(252));
        attributes.put(ContentTypes.Image.width, new Integer(252));
        attributes.put(ContentTypes.Image.lastmodified, createDate(2017, 2, 12));
        attributes.put(ContentTypes.Image.path, "/media/test/i/dont/know");

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithMissingRequired() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Synonym))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.Synonym.word, ContentTypes.Synonym.synonymValue, ContentTypes.Synonym.bidirectional})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Synonym, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Synonym.word, "TestWord");
        attributes.put(ContentTypes.Synonym.bidirectional, Boolean.TRUE);

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
        Assert.assertEquals(contentKey, validationResults.getValidationResults().get(0).getValidatedObject());
        Assert.assertEquals(ValidationResultLevel.ERROR, validationResults.getValidationResults().get(0).getFailureLevel());
    }

    @Test
    public void testValidateWithNotExistingAttribute() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Image))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.Image.height, ContentTypes.Image.width, ContentTypes.Image.lastmodified, ContentTypes.Image.path})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Image, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Image.height, new Integer(252));
        attributes.put(ContentTypes.Image.width, new Integer(252));
        attributes.put(ContentTypes.Image.lastmodified, createDate(2017, 2, 12));
        attributes.put(ContentTypes.Image.path, "/media/test/i/dont/know");
        attributes.put(ContentTypes.Product.FULL_NAME, "this is a product attribute");

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWithWrongTypeAttribute() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Image))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.Image.height, ContentTypes.Image.width, ContentTypes.Image.lastmodified, ContentTypes.Image.path})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Image, "test");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Image.height, new Integer(252));
        attributes.put(ContentTypes.Image.width, new Integer(252));
        attributes.put(ContentTypes.Image.lastmodified, new Integer(252));
        attributes.put(ContentTypes.Image.path, "/media/test/i/dont/know");

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateEnumerated() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Recipe))
                .thenReturn(new HashSet<Attribute>(Arrays.asList(new Attribute[] {ContentTypes.Recipe.theme_color, ContentTypes.Recipe.productionStatus})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Recipe, "testRecipe");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Recipe.theme_color, "DEFAULT");

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateEnumeratedOutsideOfSet() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Recipe))
                .thenReturn(new HashSet<Attribute>(Arrays.asList(new Attribute[] {ContentTypes.Recipe.theme_color, ContentTypes.Recipe.productionStatus})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Recipe, "testRecipe");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Recipe.productionStatus, "NOT_IN_ENUM");

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateEnumeratedWithWrongType() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.Recipe))
                .thenReturn(new HashSet<Attribute>(Arrays.asList(new Attribute[] {ContentTypes.Recipe.theme_color, ContentTypes.Recipe.productionStatus})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.Recipe, "testRecipe");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.Recipe.productionStatus, new Integer(242));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(2, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateSingleRelationship() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.DonationOrganization))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.DonationOrganization, "donationOrganization");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL, ContentKeyFactory.get(ContentType.Image, "donOrgImage"));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateSingleRelationshipWithWrongType() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.DonationOrganization))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.DonationOrganization, "donationOrganization");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL, ContentKeyFactory.get(ContentType.Html, "thisShouldNotBeAllowed"));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateSingleRelationshipWithMultiTarget() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.DonationOrganization))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.DonationOrganization, "donationOrganization");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL, Arrays.asList(ContentKeyFactory.get(ContentType.Image, "testOrgImage")));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateMultiRelationship() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.DonationOrganization))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.DonationOrganization, "donationOrganization");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.DonationOrganization.GIFTCARD_TYPE, Arrays.asList(ContentKeyFactory.get(ContentType.DomainValue, "domainValue")));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertFalse(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateMultiRelationshipWithSingleValue() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.DonationOrganization))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.DonationOrganization, "donationOrganization");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentKeyFactory.get(ContentType.DomainValue, "domainValue"));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateMultiRelationshipContainingWrongType() {
        Mockito
            .when(contentTypeInfoService.selectAttributes(ContentType.DonationOrganization))
                .thenReturn(new HashSet<Attribute>(
                        Arrays.asList(new Attribute[] {ContentTypes.DonationOrganization.GIFTCARD_TYPE, ContentTypes.DonationOrganization.ORGANIZATION_LOGO_SMALL})));

        ContentKey contentKey = ContentKeyFactory.get(ContentType.DonationOrganization, "donationOrganization");
        Map<Attribute, Object> attributes = new HashMap<Attribute, Object>();
        attributes.put(ContentTypes.DonationOrganization.GIFTCARD_TYPE,
                Arrays.asList(ContentKeyFactory.get(ContentType.DomainValue, "domainValue"), ContentKeyFactory.get(ContentType.Image, "image")));

        ValidationResults validationResults = underTest.validate(contentKey, attributes, null);
        Assert.assertTrue(validationResults.hasError());
        Assert.assertFalse(validationResults.hasInfo());
        Assert.assertFalse(validationResults.hasWarning());
        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    private Date createDate(int year, int month, int day) {

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }
}
