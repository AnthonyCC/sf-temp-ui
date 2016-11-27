package com.freshdirect.mobileapi.controller;

import static com.freshdirect.mobileapi.model.Product.wrap;
import static java.lang.Double.valueOf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.MethodRetryHandler;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.RecipeTagModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.mobileapi.controller.data.Image;
import com.freshdirect.mobileapi.controller.data.Product;
import com.freshdirect.mobileapi.controller.data.ProductQuantity;
import com.freshdirect.mobileapi.controller.data.request.RecipeGetAllRequest;
import com.freshdirect.mobileapi.controller.data.response.Idea;
import com.freshdirect.mobileapi.controller.data.response.RecipeDetailResponse;
import com.freshdirect.mobileapi.controller.data.response.RecipeDetailResponse.Ingredient;
import com.freshdirect.mobileapi.controller.data.response.RecipeListResponse;
import com.freshdirect.mobileapi.controller.data.response.RecipeTagsResponse;
import com.freshdirect.mobileapi.exception.JsonException;
import com.freshdirect.mobileapi.exception.ModelException;
import com.freshdirect.mobileapi.exception.NoSessionException;
import com.freshdirect.mobileapi.model.SessionUser;
import com.freshdirect.mobileapi.service.ServiceException;

public class RecipesController extends BaseController {

	private static final Pattern IMAGE_SIZE = Pattern.compile("^.*/img/(\\d+)x(\\d+)/.*$");

	private static final String ACTION_GET_TAGS = "getTags";
	private static final String ACTION_GET_ALL = "getAll";
	private static final String ACTION_GET_DETAIL = "getDetail";
	private static final String ACTION_SEARCH = "search";

	private static final String RECIPYURL_API = "http://api.getpopcart.com/v1";
		
	private static final String FOODILY_API = "http://api.foodily.com/v1";
	private static final String POPCART_API = "http://api.getpopcart.com/v1";
	

	private static final AtomicReference<String> RECIPY_API_TOKEN = new AtomicReference<String>(null);
	
	@Override
	protected ModelAndView processRequest(final HttpServletRequest request,
			final HttpServletResponse response, final ModelAndView model, final String action,
			SessionUser user) throws JsonException, FDException,
			ServiceException, NoSessionException {
		if (user == null) {
			user = fakeUser(request.getSession());
		}

		if (ACTION_GET_TAGS.equals(action)) {
			return getTags(model, user);
		} else if (ACTION_GET_ALL.equals(action)) {
			updateToken();
			return getAll(request, model, user);
		} else if (ACTION_GET_DETAIL.equals(action)) {
			updateToken();
			final String recipeId = request.getParameter("recipeId");
			return getDetail(recipeId, model, user);
		} else if (ACTION_SEARCH.equals(action)) {
			return search(model, user);
		}
		throw new UnsupportedOperationException();
	}

	private ModelAndView search(final ModelAndView model, final SessionUser user) {
		// TODO Auto-generated method stub
		return null;
	}
	private static GetMethod getMethodRecp(String recipeId){
		final HttpClient http = new HttpClient();
		http.setConnectionTimeout(5000);
		GetMethod get = new GetMethod(POPCART_API + "/recipes/" + recipeId);
		get.addRequestHeader("Authorization","Bearer " + RECIPY_API_TOKEN.get());
			
		try {
			http.executeMethod(get);
			if(200 == get.getStatusCode()){
				return get;
			} else {
				get = new GetMethod(FOODILY_API + "/recipes/" + recipeId);
				get.addRequestHeader("Authorization","Bearer " + RECIPY_API_TOKEN.get());
				http.executeMethod(get);
				return get;
				
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return get;

		}
	private ModelAndView getDetail(final String recipeId, final ModelAndView model, final SessionUser user) {
	
		
		GetMethod get = getMethodRecp(recipeId);
		try {
			
			if (200 == get.getStatusCode()) {
				final String json = get.getResponseBodyAsString();
				final ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				final Map<String, Object> response = mapper.readValue(json, Map.class);
				final RecipeDetailResponse recipe = new RecipeDetailResponse();

				readFoodilyRecipe(response, recipe);

				fillIngredients(recipe, user.getFDSessionUser());
				fillFoodilyIngredients(response, recipe);

				//				recipe.setTags(tags);

				setResponseMessage(model, recipe, user);
			}
		} catch (final HttpException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		} catch (final JsonException e) {
			throw new RuntimeException(e);
		}
		return model;
	}

	private void fillFoodilyIngredients(Map<String, Object> json,
			RecipeDetailResponse recipe) {
		@SuppressWarnings("unchecked")
		final Map<String, Object> a = (Map<String, Object>) json.get("ingredients");
		@SuppressWarnings("unchecked")
		final List<Map<String, Object>> ingredientsJson = (List<Map<String, Object>>) a.get("list");
		List<Ingredient> ingredients = new ArrayList<Ingredient>(ingredientsJson.size());
		for (Map<String, Object> ingredientJson : ingredientsJson) {
			Ingredient ingredient = new Ingredient();
			ingredient.setName(ingredientJson.get("name").toString());
			ingredient.setQuantity(valueOf(ingredientJson.get("quantity").toString()));
			ingredient.setUom(ingredientJson.get("uom").toString());
			ingredient.setText(ingredientJson.get("text").toString());
			ingredients.add(ingredient);
		}
		recipe.setFoodilyIngredients(ingredients);
	}

	
	@SuppressWarnings("unchecked")
	private void fillIngredients(
			RecipeDetailResponse recipe, FDUserI user)  {

		
		try {
			GetMethod get = returnMethod(recipe);
			if (get.getStatusCode() == 200) {
				final String json = get.getResponseBodyAsString();				
				final ObjectMapper mapper = new ObjectMapper();
				final Map<String, Object> response = mapper.readValue(json, Map.class);
				final List<Map<String, Object>> recs = (List<Map<String, Object>>) response.get("recommendations");
				final Map<String, Object> rec = recs.get(0);				
				final List<Map<String, Object>> ings = (List<Map<String, Object>>) rec.get("products");
				List<List<Product>> ingredients = new ArrayList<List<Product>>(ings.size());
				
				//APPDEV-4238 -- Ingredients you may already have at hand
				List<List<Product>> ingredientsYmah = new ArrayList<List<Product>>();
				
				
				for (Map<String, Object> ing : ings) {
				
					final List<Map<String, Object>> prods = (List<Map<String, Object>>) ing.get("products");
					List<Product> suggestions = new ArrayList<Product>(prods.size());
					List<Product> youMayAlreadyHave = new ArrayList<Product>();
					
					for (Map<String, Object> prod : prods) {
						
						//APPDEV-4238 -- Ingredients you may already have at hand
						//Check flag that is set for items you may have on hand
						String isYmah = "";
						if(prod.containsKey("isPantryItem")) {
							Object object = prod.get("isPantryItem");
							isYmah = object.toString();
						}
						
						//Is FreshDirect storing a JSON object as their vendor ID? -- 
						final String vendorJson = prod.get("vendorId").toString();
						final Map<String, Object> vendorData = mapper.readValue(vendorJson, Map.class);
						final String skuId = vendorData.get("skuCode").toString();
						final SkuModel skuModel = (SkuModel) ContentFactory.getInstance().getContentNode(skuId);
						if(skuModel!=null){
						final ProductModel productModel = skuModel.getProductModel();
						try {
							final Product product = new Product(wrap(productModel, user));
							
							//DOOR3 FD-iPad FDIP-738
							//Currently, there is an error in the Foodily API (it doesn't return quantity and uom).
							//Therefore, querying for these values returns null. I have negotiated with Ahmed
							//Abdullah to pass -1.0 back to the client when this occurs. Setting foodilyUnitOfMeasureObject
							//equal to an empty string instead of null ensures that the object's JSON data gets populated.
							//The reason -1.0 is chosen is because -1 is an invalid value, and the client maps this as a 
							//double; hence the decimal.
							//Followup: the Foodily API returns a field called "cartQty". Ahmed has requested this be passed back
							//to the client in the interim.
							Object foodilyQuantityObject = prod.get("quantity");
							if( foodilyQuantityObject == null )
							{
								foodilyQuantityObject = prod.get("cartQty");
							}
							final String foodilyProductQuantity = foodilyQuantityObject == null ? "-1.0" : foodilyQuantityObject.toString();
							Object foodilyUnitOfMeasureObject = prod.get("uom");
							final String foodilyProductUnitofMeasure = foodilyUnitOfMeasureObject == null ? "" : foodilyUnitOfMeasureObject.toString();
							ProductQuantity pq = new ProductQuantity();
							pq.setQuantityText(foodilyProductQuantity);
							pq.setSalesUnitLabel(foodilyProductUnitofMeasure);
							product.setQuantity(pq);
							//*************************************************************************
							suggestions.add(product);
							
							//APPDEV-4238 -- Ingredients you may already have at hand
							if(isYmah.equalsIgnoreCase("true")){
								youMayAlreadyHave.add(product);
							}
						} catch (ModelException e) {
							recipe.addWarningMessage("SKU: " + vendorJson + ": " + traceFor(e));
						}
						}
					}
					ingredients.add(suggestions);
					ingredientsYmah.add(youMayAlreadyHave);
						
				}
				recipe.setIngredients(ingredients);
				recipe.setIngredientsYmah(ingredientsYmah);
				
			}
		} catch (HttpException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	private GetMethod returnMethod(RecipeDetailResponse recipe) throws HttpException{
		HttpClient http = new HttpClient();
		GetMethod get = new GetMethod(RECIPYURL_API + "/shopping?recipes=" +recipe.getRecipeId() + "&stores=freshdirect&inStockOnly=false&fields=*(recipe(ingredients(list(*))),products(ingredient,products(*)))");
		get.addRequestHeader("Authorization","Bearer " + RECIPY_API_TOKEN.get());
		try {
			http.executeMethod(get);
			if(get.getStatusCode() == 200){
				return get;
			}else {
				get = new GetMethod(FOODILY_API + "/shopping?recipes=" +recipe.getRecipeId() + "&stores=freshdirect&inStockOnly=false&fields=*(recipe(ingredients(list(*))),products(ingredient,products(*)))");
				get.addRequestHeader("Authorization","Bearer " + RECIPY_API_TOKEN.get());
				http.executeMethod(get);
				return get;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	private void readFoodilyRecipe(final Map<String, Object> json,
			final RecipeDetailResponse recipe) {
		recipe.setRecipeTitle(json.get("name").toString());

		@SuppressWarnings("unchecked")
		final String source = ((Map<String, Object>) json.get("sourceRecipe")).get("url").toString();
		recipe.setRecipeUrl(source);
		recipe.setRecipeId(json.get("id").toString());

		try {
            String instructions = "";
            @SuppressWarnings("unchecked")
            final List<Map<String, String>> steps = (List<Map<String, String>>) ((Map<String, Object>) json.get("instructions")).get("list");
            for (final Map<String, String>	 step :steps) {
            	instructions += '\n' + step.get("text");
            }
            recipe.setRecipeText(instructions.trim());
        } catch (NullPointerException e) {
            // No instructions
        }

		@SuppressWarnings("unchecked")
		final List<Map<String, String>> images = (List<Map<String, String>>) ((Map<String, Object>) json.get("images")).get("list");
		
		if (images!=null && !images.isEmpty()) {
			Collections.reverse(images);
			for (final Map<String, String> image : images) {
				final String url = image.get("largeUrl");
				int width = 0;
				int height = 0;
				try {
					final Matcher matcher = IMAGE_SIZE.matcher(url);
					if (matcher.groupCount() == 2) {
						final String w = matcher.group(1);
						final String h = matcher.group(2);
						width = Integer.valueOf(w, 10);
						height = Integer.valueOf(h, 10);
						break;
					}
				} catch (final Exception e) {
					width = 0;
					height = 0;
				} finally {
					recipe.setRecipeImage(new Image(url, height, width));
				}
			}
		}
	}

	private ModelAndView getAll(final HttpServletRequest request, final ModelAndView model, final SessionUser user) throws JsonException {
		try {
			final HttpClient http = new HttpClient();

			String searchTerm = null;

			try {
				BufferedReader reader = request.getReader();
				if(reader != null){
					ObjectMapper om = new ObjectMapper();
					
					String rTest = urlDecode(reader.readLine()).substring(5);
					
					RecipeGetAllRequest recipeGetAllRequest = (RecipeGetAllRequest)
							om.readValue(rTest, RecipeGetAllRequest.class);
					
					searchTerm = recipeGetAllRequest.getTag();
				}
			} 
			catch (JsonParseException e) { 
				e.printStackTrace();
			}	
			catch (JsonMappingException e) { 
				e.printStackTrace();			
			}
	
		
			final GetMethod get = getMethodURL( searchTerm);
			if (200 == get.getStatusCode()) {
				final String json = get.getResponseBodyAsString();
				final ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				final
				Map<String, Object> res = mapper.readValue(json, Map.class);

				@SuppressWarnings("unchecked")
				final
				List<Map<String, Object>> recipeJsons = (List<Map<String, Object>>) res.get("recipes");
				final List<RecipeDetailResponse> recipes = new ArrayList<RecipeDetailResponse>(recipeJsons.size());
				for (final Map<String, Object> recipeJson : recipeJsons) {
					final RecipeDetailResponse recipe = new RecipeDetailResponse();
					readFoodilyRecipe(recipeJson, recipe);
					recipes.add(recipe);
				}

				final RecipeListResponse response = new RecipeListResponse();
				response.setRecipes(recipes);
				setResponseMessage(model, response, user);
			}
		} catch (final HttpException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return model;
	}
	
	private GetMethod getMethodURL(String searchTerm) throws HttpException, IOException{
		final HttpClient http = new HttpClient();
		String url = RECIPYURL_API + "/recipes?expand=recipes";
		if (searchTerm != null) {
			url += "&q=" + urlEncode(searchTerm);
		}
		
		GetMethod get = new GetMethod(url);
		get.addRequestHeader("Authorization","Bearer " + RECIPY_API_TOKEN.get());
		http.executeMethod(get);
		if(200 == get.getStatusCode()){
			return get;
		}else {
			String url1 = FOODILY_API + "/recipes?expand=recipes";
			if (searchTerm != null) {
				url1 += "&q=" + urlEncode(searchTerm);
			}
		get = new GetMethod(url1);
		get.addRequestHeader("Authorization","Bearer " + RECIPY_API_TOKEN.get());
		http.executeMethod(get);
		return get;
		}
	}

	private String urlEncode(final String query) {
		try {
			return URLEncoder.encode(query, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			return "";
		}
	}
	

	private String urlDecode(final String query) {
		try {
			return URLDecoder.decode(query, "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			return "";
		}
	}

	private ModelAndView getTags(final ModelAndView model, final SessionUser user) throws JsonException {
//		final Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(ContentType.get("RecipeTag"));
		List <RecipeTagModel> recipeTags = ContentFactory.getInstance().getStore().getTabletIdeasRecipeTags();
		final RecipeTagsResponse response = new RecipeTagsResponse();
		final List<Idea> ideas = new ArrayList<Idea>();
		for (final RecipeTagModel recipeTagModel : recipeTags) {
			final Idea idea = new Idea();
			idea.setDestinationSection("recipe");
			idea.setFeatureImage(new Image(recipeTagModel.getTabletImage()));
			idea.setDestinationId(recipeTagModel.getTagId());
			ideas.add(idea);
		}
		response.setRecipeTags(ideas);
		setResponseMessage(model, response, user);
		return model;
	}

	private static String updateToken() {
		final String previousToken = RECIPY_API_TOKEN.get();
		String newToken = "";
		final HttpClient http = new HttpClient();
		final PostMethod auth = new PostMethod(RECIPYURL_API + "/token");
		auth.addRequestHeader("Authorization", "Basic ZnJlc2hkaXJlY3Q6SjNJZ3A5T3ZHZm5qcWpu");
		auth.addParameter("grant_type", "client_credentials");
		try {
			http.executeMethod(auth);
			if (200 == auth.getStatusCode()) {
				newToken = processsRequest(auth,previousToken);
			}else{
				final PostMethod authFood = new PostMethod(FOODILY_API + "/token");
				authFood.addRequestHeader("Authorization", "Basic ZnJlc2hkaXJlY3Q6SjNJZ3A5T3ZHZm5qcWpu");
				authFood.addParameter("grant_type", "client_credentials");			
				http.executeMethod(authFood);
				if (200 == authFood.getStatusCode()) 
					newToken = processsRequest(authFood,previousToken);
			}
		} catch (final HttpException e) {
			throw new RuntimeException(e);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return newToken;
	}
	private static String processsRequest(PostMethod auth,String previousToken) throws JsonParseException, JsonMappingException, IOException{
		String newToken = "";
		final String json = auth.getResponseBodyAsString();
		final ObjectMapper mapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		final Map<String, Object> response = mapper.readValue(json, Map.class);
		final String access_token = response.get("access_token").toString();
		if (RECIPY_API_TOKEN.compareAndSet(previousToken, access_token)) {
			newToken = access_token;
		} else {
			newToken = RECIPY_API_TOKEN.get();
		}
	
		return newToken;
	}

	@Override
	protected boolean validateUser() {
		return false; // guest browsing
	}
}
