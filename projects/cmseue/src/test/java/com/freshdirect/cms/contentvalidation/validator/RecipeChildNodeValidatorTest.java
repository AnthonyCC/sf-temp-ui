package com.freshdirect.cms.contentvalidation.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.freshdirect.cms.core.service.ContentProviderService;
import com.freshdirect.cms.validation.ValidationResults;
import com.google.common.base.Optional;

@RunWith(MockitoJUnitRunner.class)
@Category(UnitTest.class)
public class RecipeChildNodeValidatorTest {

    @InjectMocks
    private RecipeChildNodeValidator underTest;

    @Mock
    private ContentProviderService contentProviderService;

    @Test
    public void testValidateWhenEverythingIsOkayWithRecipe() {
        ContentKey recipeKey = ContentKeyFactory.get(ContentType.Recipe, "recipeForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        final ContentKey defaultRecipeVariant = ContentKeyFactory.get(ContentType.RecipeVariant, "recipeForTest_default");
        List<ContentKey> recipeVariants = new ArrayList<ContentKey>();
        recipeVariants.add(defaultRecipeVariant);
        Optional<Object> recipeVariantsOptional = Optional.fromNullable((Object) recipeVariants);

        Mockito.when(contentProviderService.getAttributeValue(recipeKey, ContentTypes.Recipe.variants)).thenReturn(recipeVariantsOptional);

        ValidationResults validationResults = underTest.validate(recipeKey, attributesWithValues, contentProviderService);

        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenEverythingIsOkayWithRecipeVariant() {
        ContentKey recipeKey = ContentKeyFactory.get(ContentType.RecipeVariant, "recipeVariantForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        final ContentKey mainRecipeSection = ContentKeyFactory.get(ContentType.RecipeSection, "recipeVariantForTest_main");
        List<ContentKey> recipeSections = new ArrayList<ContentKey>();
        recipeSections.add(mainRecipeSection);
        Optional<Object> recipeSectionsOptional = Optional.fromNullable((Object) recipeSections);

        Mockito.when(contentProviderService.getAttributeValue(recipeKey, ContentTypes.RecipeVariant.sections)).thenReturn(recipeSectionsOptional);

        ValidationResults validationResults = underTest.validate(recipeKey, attributesWithValues, contentProviderService);

        Assert.assertEquals(0, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenNoDefaultVariantForRecipe() {
        ContentKey recipeKey = ContentKeyFactory.get(ContentType.Recipe, "recipeForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        Optional<Object> recipeVariantsOptional = Optional.fromNullable(null);

        Mockito.when(contentProviderService.getAttributeValue(recipeKey, ContentTypes.Recipe.variants)).thenReturn(recipeVariantsOptional);

        ValidationResults validationResults = underTest.validate(recipeKey, attributesWithValues, contentProviderService);

        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }

    @Test
    public void testValidateWhenNoMainSectionForVariant() {
        ContentKey recipeKey = ContentKeyFactory.get(ContentType.RecipeVariant, "recipeVariantForTest");
        Map<Attribute, Object> attributesWithValues = new HashMap<Attribute, Object>();

        Optional<Object> recipeSectionsOptional = Optional.fromNullable(null);

        Mockito.when(contentProviderService.getAttributeValue(recipeKey, ContentTypes.RecipeVariant.sections)).thenReturn(recipeSectionsOptional);

        ValidationResults validationResults = underTest.validate(recipeKey, attributesWithValues, contentProviderService);

        Assert.assertEquals(1, validationResults.getValidationResults().size());
    }
}
