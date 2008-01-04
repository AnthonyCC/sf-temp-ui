package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.survey.FDSurveyResponse;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.framework.webapp.ui.PCheckboxGroup;
import com.freshdirect.framework.webapp.ui.PElement;
import com.freshdirect.framework.webapp.ui.PForm;
import com.freshdirect.framework.webapp.ui.PRadioGroup;
import com.freshdirect.framework.webapp.ui.PSelect;
import com.freshdirect.framework.webapp.ui.PTextArea;
import com.freshdirect.framework.webapp.ui.PTextField;

public class SurveyControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private final static Category LOGGER = LoggerFactory.getInstance(SurveyControllerTag.class);

	private String successPage;
	private String resultName;
	private String formName;

	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ActionResult result = new ActionResult();
		SignupSurveyForm form = new SignupSurveyForm();
		String action = "";
		// This is to avoid javascript in page to set the value of action 'cause there are multiple submit images on page
		// and Netscape does not execute onclick if the input types are images
		for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
			String controlName = (String) e.nextElement();
			if ("submit_survey.x".equalsIgnoreCase(controlName) || "submit_survey.y".equalsIgnoreCase(controlName)) {
				action = "filled_survey";
				break;
			}
			if ("skip_survey.x".equalsIgnoreCase(controlName) || "skip_survey.y".equalsIgnoreCase(controlName)) {
				action = "skip_survey";
				break;
			}
		}

		if ("POST".equalsIgnoreCase(request.getMethod())) {
			HttpSession session = pageContext.getSession();
			FDSessionUser user = (FDSessionUser) session.getAttribute(USER);
			FDIdentity identity = user.getIdentity();
			try {

				if ("filled_survey".equalsIgnoreCase(action)) {
					form.initialize(request);
					form.validate(result);
					if (result.isSuccess()) {
						FDSurveyResponse survey = form.getFormValues(user.getIdentity(), "second_order_survey");

						boolean promoAvailable = PromotionFactory.getInstance().getPromotion("2ND_ORDER_SURVEY") != null;
						BigInteger id = new BigInteger(user.getIdentity().getFDCustomerPK());
						BigInteger modVal = new BigInteger("4");
						//Commented as part of PERF-22.
						//ErpSaleInfo firstOrder = user.getOrderHistory().getLastSale();
						Date firstOrderRequestedDate = user.getOrderHistory().getLastOrderDlvDate();
						Calendar startPromo = Calendar.getInstance();
						startPromo.set(2005, Calendar.MAY, 1, 0, 0, 0);
						
						FDCustomerModel customer = FDCustomerFactory.getFDCustomer(user.getIdentity());
					    boolean isManualRetention = "true".equals(customer.getProfile().getAttribute("ManualRetention-$5")) ;
						//if (promoAvailable && (id.mod(modVal).intValue() == 0 || isManualRetention) && firstOrder.getRequestedDate().after(startPromo.getTime())) {
						if (promoAvailable && (id.mod(modVal).intValue() == 0 || isManualRetention) && firstOrderRequestedDate.after(startPromo.getTime())) {
							FDCustomerManager.setProfileAttribute(identity, "second_order_promo", "true");
						}

						FDCustomerManager.storeSurvey(survey);

						user.setSurveySkipped(false);
						
						user.invalidateCache();

						user.updateUserState();
					}
				}
				if ("skip_survey".equalsIgnoreCase(action)) {
					FDCartModel cart = user.getShoppingCart();

					FDCustomerManager.setProfileAttribute(identity, "second_order_survey", "SKIP");

					user.setSurveySkipped(true);
					
					user.invalidateCache();
					
					user.updateUserState();

					user.setShoppingCart(cart);
					session.setAttribute(USER, user);
				}

			} catch (FDResourceException re) {
				LOGGER.warn(re);
				result.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));
			}
			if (result.isSuccess() && successPage != null) {
				HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
				try {
					response.sendRedirect(response.encodeRedirectURL(successPage));
					JspWriter writer = pageContext.getOut();
					writer.close();
					return SKIP_BODY;
				} catch (IOException ioe) {
					throw new JspException("Error redirecting " + ioe.getMessage());
				}
			}
		}

		pageContext.setAttribute(resultName, result);
		pageContext.setAttribute(formName, form);
		return EVAL_BODY_BUFFERED;
	}

	public class SignupSurveyForm extends PForm {
		public SignupSurveyForm() {
			super();
			this.makeForm();
		}

		private void makeForm() {

			PRadioGroup.Option[] aboutFDOptions = {
				new PRadioGroup.Option("4", "</td><td align=center>"),
				new PRadioGroup.Option("3", "</td><td align=center>"),
				new PRadioGroup.Option("2", "</td><td align=center>"),
				new PRadioGroup.Option("1", "")};
			PRadioGroup.Option[] aboutGrayFDOptions = {
				new PRadioGroup.Option("4", "</td><td align=center bgcolor='#eeeeee'>"),
				new PRadioGroup.Option("3", "</td><td align=center bgcolor='#eeeeee'>"),
				new PRadioGroup.Option("2", "</td><td align=center bgcolor='#eeeeee'>"),
				new PRadioGroup.Option("1", "")};
			//1
			this.addElement(new PRadioGroup("question1_prices", aboutFDOptions, true));
			this.addElement(new PRadioGroup("question1_selection", aboutGrayFDOptions, true));
			this.addElement(new PRadioGroup("question1_quality", aboutFDOptions, true));
			this.addElement(new PRadioGroup("question1_convenience", aboutGrayFDOptions, true));
			this.addElement(new PRadioGroup("question1_customerservice", aboutFDOptions, true));
			//2
			this.addElement(new PRadioGroup("question2_professional", aboutFDOptions, true));
			this.addElement(new PRadioGroup("question2_prompt", aboutGrayFDOptions, true));
			//3
			this.addElement(new PRadioGroup("question3_easeofuse", aboutFDOptions, true));
			this.addElement(new PRadioGroup("question3_information", aboutGrayFDOptions, true));
			this.addElement(new PRadioGroup("question3_speed", aboutFDOptions, true));

			PRadioGroup.Option[] spendingOptions = {
				new PRadioGroup.Option("Under $50", "Under $50<br>"),
				new PRadioGroup.Option("$51 - $75", "$51 - $75<br>"),
				new PRadioGroup.Option("$76 - $100", "$76 - $100<br>"),
				new PRadioGroup.Option("$101 - $125", "$101 - $125<br>"),
				new PRadioGroup.Option("$126 - $150", "$126 - $150<br>"),
				new PRadioGroup.Option("$151 - $175", "$151 - $175<br>"),
				new PRadioGroup.Option("$175+", "$175+<br>")};

			PRadioGroup.Option[] numberOptions = {
				new PRadioGroup.Option("1", "1<br>"),
				new PRadioGroup.Option("2", "2<br>"),
				new PRadioGroup.Option("3", "3<br>"),
				new PRadioGroup.Option("4", "4<br>"),
				new PRadioGroup.Option("5+", "5+<br>")};

			PCheckboxGroup.Option[] cuisineOptions = {
				new PCheckboxGroup.Option("Italian", "Italian<br>"),
				new PCheckboxGroup.Option("French", "French<br>"),
				new PCheckboxGroup.Option("Comfort Food", "Comfort Food<br>"),
				new PCheckboxGroup.Option("Asian", "Asian<br>"),
				new PCheckboxGroup.Option("Caribbean", "Caribbean<br>"),
				new PCheckboxGroup.Option("Mexican", "Mexican<br>"),
				new PCheckboxGroup.Option("Mediterranean", "Mediterranean<br>"),
				new PCheckboxGroup.Option("other", "other: ")};
			//4
			this.addElement(new PCheckboxGroup("question4_favoritecuisine", cuisineOptions, true));

			this.addElement(new PTextField("question4_favoritecuisine_other", false));

			PCheckboxGroup.Option[] cookAttitudeOptions = {
				new PCheckboxGroup.Option("I never cook", "I never cook<br>"),
				new PCheckboxGroup.Option("I cook infrequently", "I cook infrequently<br>"),
				new PCheckboxGroup.Option("I mostly cook for special occasions", "I mostly cook for special occasions<br>"),
				new PCheckboxGroup.Option("I cook often", "I cook often<br>"),
				new PCheckboxGroup.Option("I love to try new foods", "I love to try new foods<br>"),
				new PCheckboxGroup.Option("I am an expert cook", "I am an expert cook<br>"),
				new PCheckboxGroup.Option("I only cook simple dishes", "I only cook simple dishes<br>"),
				new PCheckboxGroup.Option("I would like to cook more than I do", "I would like to cook more than I do<br>"),
				new PCheckboxGroup.Option("I like to use recipes", "I like to use recipes<br>")};
			//5
			this.addElement(new PCheckboxGroup("question5_cookingattitude", cookAttitudeOptions, true));

			PCheckboxGroup.Option[] nutritionOptions = {
				new PCheckboxGroup.Option("Vegetarian", "Vegetarian<br>"),
				new PCheckboxGroup.Option("Kosher", "Kosher<br>"),
				new PCheckboxGroup.Option("Organic", "Organic<br>"),
				new PCheckboxGroup.Option("high fiber", "high fiber<br>"),
				new PCheckboxGroup.Option("low calorie", "low calorie<br>"),
				new PCheckboxGroup.Option("low carb", "low carb<br>"),
				new PCheckboxGroup.Option("low cholesterol", "low cholesterol<br>"),
				new PCheckboxGroup.Option("low fat", "low fat<br>"),
				new PCheckboxGroup.Option("fat free", "fat free<br>"),
				new PCheckboxGroup.Option("low sodium", "low sodium<br>"),
				new PCheckboxGroup.Option("MSG-free", "MSG-free<br>"),
				new PCheckboxGroup.Option("sugar free (diabetes friendly)", "sugar free (diabetes friendly)<br>"),
				new PCheckboxGroup.Option("lactose free high calcium", "lactose free high calcium<br>"),
				new PCheckboxGroup.Option("heart healthy", "heart healthy<br>"),
				new PCheckboxGroup.Option("trans-fat free", "trans-fat free<br>"),
				new PCheckboxGroup.Option("whole grains", "whole grains<br>"),
				new PCheckboxGroup.Option("no special preferences", "no special preferences<br>"),
				new PCheckboxGroup.Option("other", "other: ")};
			//6
			this.addElement(new PCheckboxGroup("question6_importantnutrition", nutritionOptions, true));

			this.addElement(new PTextField("question6_importantnutrition_other", false));

			PCheckboxGroup.Option[] allergyOptions = {
				new PCheckboxGroup.Option("milk", "milk<br>"),
				new PCheckboxGroup.Option("egg", "egg<br>"),
				new PCheckboxGroup.Option("peanut", "peanut<br>"),
				new PCheckboxGroup.Option("tree nut (walnut, cashew, etc.)", "tree nut (walnut, cashew, etc.)<br>"),
				new PCheckboxGroup.Option("fish", "fish<br>"),
				new PCheckboxGroup.Option("shellfish", "shellfish<br>"),
				new PCheckboxGroup.Option("soy", "soy<br>"),
				new PCheckboxGroup.Option("wheat", "wheat<br>"),
				new PCheckboxGroup.Option("no allergies", "no allergies<br>"),
				new PCheckboxGroup.Option("other", "other: ")};

			//7
			this.addElement(new PCheckboxGroup("question7_foodallergy", allergyOptions, true));

			this.addElement(new PTextField("question7_foodallergy_other", false));

			PRadioGroup.Option[] householdOptions = {
				new PRadioGroup.Option("Single", "Single<br>"),
				new PRadioGroup.Option("Couple", "Couple<br>"),
				new PRadioGroup.Option("Family with Children", "Family with Children<br>"),
				new PRadioGroup.Option("Roommate(s)", "Roommate(s)<br>"),
				new PRadioGroup.Option("Office", "Office<br>"),
				new PRadioGroup.Option("Caterer/Person Chef", "Caterer/Person Chef<br>")};
			
			//8
			this.addElement(new PRadioGroup("question8_weeklyspend_store", spendingOptions, true));

			this.addElement(new PRadioGroup("question8_weeklyspend_restaurant", spendingOptions, true));
			
			//9
			this.addElement(new PRadioGroup("question9_numpeopleshopfor", numberOptions, true));
			
			//10
			this.addElement(new PRadioGroup("question10_household", householdOptions, true));

			PSelect.Option[] dayOptions = {
				new PSelect.Option("", "Day"),
				new PSelect.Option("1", "1"),
				new PSelect.Option("2", "2"),
				new PSelect.Option("3", "3"),
				new PSelect.Option("4", "4"),
				new PSelect.Option("5", "5"),
				new PSelect.Option("6", "6"),
				new PSelect.Option("7", "7"),
				new PSelect.Option("8", "8"),
				new PSelect.Option("9", "9"),
				new PSelect.Option("10", "10"),
				new PSelect.Option("11", "11"),
				new PSelect.Option("12", "12"),
				new PSelect.Option("13", "13"),
				new PSelect.Option("14", "14"),
				new PSelect.Option("15", "15"),
				new PSelect.Option("16", "16"),
				new PSelect.Option("17", "17"),
				new PSelect.Option("18", "18"),
				new PSelect.Option("19", "19"),
				new PSelect.Option("20", "20"),
				new PSelect.Option("21", "21"),
				new PSelect.Option("22", "22"),
				new PSelect.Option("23", "23"),
				new PSelect.Option("24", "24"),
				new PSelect.Option("25", "25"),
				new PSelect.Option("26", "26"),
				new PSelect.Option("27", "27"),
				new PSelect.Option("28", "28"),
				new PSelect.Option("29", "29"),
				new PSelect.Option("30", "30"),
				new PSelect.Option("31", "31")};

			PSelect.Option[] monthOptions = {
				new PSelect.Option("", "Month"),
				new PSelect.Option("1", "Jan"),
				new PSelect.Option("2", "Feb"),
				new PSelect.Option("3", "Mar"),
				new PSelect.Option("4", "Apr"),
				new PSelect.Option("5", "May"),
				new PSelect.Option("6", "Jun"),
				new PSelect.Option("7", "Jul"),
				new PSelect.Option("8", "Aug"),
				new PSelect.Option("9", "Sep"),
				new PSelect.Option("10", "Oct"),
				new PSelect.Option("11", "Nov"),
				new PSelect.Option("12", "Dec")};

			PSelect.Option[] yearOptions = new PSelect.Option[84];
			yearOptions[0] = new PSelect.Option("", "Year");

			for (int i = 1; i < yearOptions.length; i++) {
				String s = String.valueOf(i + 1904);
				yearOptions[i] = new PSelect.Option(s, s);
			}
			//11
			this.addElement(new PSelect("question11_birthday", dayOptions, false));
			this.addElement(new PSelect("question11_birthmonth", monthOptions, false));
			this.addElement(new PSelect("question11_birthyear", yearOptions, false));
			
			PRadioGroup.Option[] genderOptions = {
				new PRadioGroup.Option("Female", "Female&nbsp;&nbsp;&nbsp;"),
				new PRadioGroup.Option("Male", "Male")};
			
			//12
			this.addElement(new PRadioGroup("question12_gender", genderOptions, false));
			
			PRadioGroup.Option[] ratingOptions = {
				new PRadioGroup.Option("1", "</td><td align=center>"),
				new PRadioGroup.Option("2", "</td><td align=center>"),
				new PRadioGroup.Option("3", "</td><td align=center>"),
				new PRadioGroup.Option("4", "</td><td align=center>"),
				new PRadioGroup.Option("5", "</td><td align=center>")};
			
			//13
			this.addElement(new PRadioGroup("question13_recommend", ratingOptions, true));
			
			//14
			this.addElement(new PTextArea("question14_additional_comments", false));
			
			//15 COS
			this.addElement(new PTextField("questionCOS1_company", false));
			this.addElement(new PTextField("questionCOS2_contact", false));

		}

		public FDSurveyResponse getFormValues(FDIdentity identity, String name) {
			FDSurveyResponse survey = new FDSurveyResponse(identity, name);
			for (Iterator i = this.getValues().iterator(); i.hasNext();) {
				PElement element = (PElement) i.next();
				String[] values = element.getValues();

				if ("question14_additional_comments".equals(element.getName()) && values[0].length() > 1000) {
					values[0] = values[0].toString().substring(0, 1000);
					survey.addAnswer(element.getName(), values);
				} else {
					survey.addAnswer(element.getName(), values);
				}
			}
			return survey;
		}
	}
}