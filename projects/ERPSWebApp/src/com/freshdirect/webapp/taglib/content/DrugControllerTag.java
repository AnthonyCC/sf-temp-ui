package com.freshdirect.webapp.taglib.content;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.content.nutrition.DrugConstantsEnum;
import com.freshdirect.content.nutrition.NutritionDrugItem;
import com.freshdirect.content.nutrition.NutritionDrugPanel;
import com.freshdirect.content.nutrition.NutritionDrugSection;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

public class DrugControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static final long serialVersionUID = -1519469814297740033L;
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private String redirectUrl;
	private String skuCode;

	@Override
	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		try {
			skuCode = request.getParameter("skuCode");			
			if (skuCode == null) {
				return SKIP_BODY;
			}

			if ( "GET".equalsIgnoreCase( request.getMethod() ) ) {

				doGetInternal();
				
			} else if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {
				
				String deleteSku = request.getParameter( "delete" );
				String panelJson = request.getParameter( "panel" );
				
				if ( deleteSku != null ) {

					ErpFactory.getInstance().deleteDrugPanel( deleteSku );
					((HttpServletResponse) pageContext.getResponse()).sendRedirect(this.redirectUrl + "?skuCode=" + skuCode);

				} else if ( panelJson != null ) {
					
					NutritionDrugPanel panel = mapper.readValue( panelJson, NutritionDrugPanel.class );
					ErpFactory.getInstance().saveDrugPanel( panel );

					pageContext.setAttribute( "panel", panelJson );
					((HttpServletResponse) pageContext.getResponse()).sendRedirect(this.redirectUrl + "?skuCode=" + skuCode);
					
				} else {
					doGetInternal();
				}

			}

		} catch (FDResourceException e) {
			throw new JspException(e);
		} catch (JsonGenerationException e) {
			throw new JspException(e);
		} catch (JsonMappingException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}

		return EVAL_BODY_BUFFERED;

	}
	
	private void doGetInternal() throws JspException, FDResourceException {
		if (skuCode == null) {
			throw new JspException("No skuCode found");
		}

		Writer writer = null;

		NutritionDrugPanel panel = ErpFactory.getInstance().getDrugPanel(skuCode);

		pageContext.setAttribute("protoPanel", false);
		if (panel == null) {
			panel = createProtoPanel();
			pageContext.setAttribute("protoPanel", true);
		}
		
		writer = new StringWriter();
		try {
			mapper.writeValue(writer, panel);
		} catch (JsonProcessingException e) {
			throw new JspException(e);
		} catch ( IOException e ) {
			throw new JspException(e);
		}

		pageContext.setAttribute("panel", writer.toString());
		pageContext.setAttribute("skuCode", skuCode);
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	private NutritionDrugPanel createProtoPanel() {
		
		NutritionDrugPanel panel = new NutritionDrugPanel();
		panel.setSkuCode(skuCode);
		
		List<NutritionDrugSection> sections = new ArrayList<NutritionDrugSection>();
		panel.setSections(sections);
		
		//create sections first
		Map<DrugConstantsEnum, NutritionDrugSection> sectionHelper = new HashMap<DrugConstantsEnum, NutritionDrugSection>();
		for(DrugConstantsEnum constant : DrugConstantsEnum.values()){
			if(constant.getType()!=null){
				NutritionDrugSection section = new NutritionDrugSection();
				section.setTitle(constant.getText());
				section.setType(constant.getType());
				section.setPosition(constant.getPosition());
				section.setImportance(constant.getImportance());
				sectionHelper.put(constant, section);
			}
		}
		
		outter:
		for(DrugConstantsEnum constant : DrugConstantsEnum.values()){
			if("item".equals(constant.getItemType()) || "separator".equals(constant.getItemType())){
				//check if there is item in the same position
				NutritionDrugSection section = sectionHelper.get(constant.getParent());
				for(NutritionDrugItem item : section.getItems()){
					if(item.getPosition() == constant.getPosition()){
						item.setValue2(constant.getText());
						continue outter;
					}
				}
				
				NutritionDrugItem item = new NutritionDrugItem();
				item.setPosition(constant.getPosition());
				item.setSeparator("separator".equals(constant.getItemType()));
				item.setValue1(constant.getText());
				item.setNewline(constant.getNewline());
				item.setImportant(constant.getImportance() > 0);
				section.getItems().add(item);
				
			}
		}
		
		for(DrugConstantsEnum key : sectionHelper.keySet()){
			NutritionDrugSection section = sectionHelper.get(key);
			Collections.sort(section.getItems(), new DrugItemComparator());
			sections.add(section);
		}
		
		Collections.sort(sections, new DrugSectionComparator());
		
		return panel;
	}
	
	class DrugSectionComparator implements Comparator<NutritionDrugSection>{
		
		@Override
		public int compare(NutritionDrugSection o1, NutritionDrugSection o2) {
			return o1.getPosition() < o2.getPosition() ? -1 : o1.getPosition() == o2.getPosition() ? 0 : 1;
		}
		
	}
	
	class DrugItemComparator implements Comparator<NutritionDrugItem>{
		
		@Override
		public int compare(NutritionDrugItem o1, NutritionDrugItem o2) {
			return o1.getPosition() < o2.getPosition() ? -1 : o1.getPosition() == o2.getPosition() ? 0 : 1;
		}
		
	}

}
