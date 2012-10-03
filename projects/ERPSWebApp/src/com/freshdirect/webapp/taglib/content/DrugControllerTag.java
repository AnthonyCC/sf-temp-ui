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
import javax.servlet.jsp.JspException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.freshdirect.content.nutrition.DrugConstantsEnum;
import com.freshdirect.content.nutrition.NutritionDrugItem;
import com.freshdirect.content.nutrition.NutritionDrugPanel;
import com.freshdirect.content.nutrition.NutritionDrugSection;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

public class DrugControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1519469814297740033L;

	public int doStartTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ObjectMapper mapper = new ObjectMapper();
		NutritionDrugPanel panel = null;

		try {

			if ("GET".equalsIgnoreCase(request.getMethod())) {

				String skuCode = (String) request.getParameter("skuCode");

				if (skuCode != null) {

					Writer writer = null;

					panel = ErpFactory.getInstance().getDrugPanel(skuCode);

					if (panel == null) {
						//throw new JspException("No NutritionModel found");
						panel = createProtoPanel(skuCode);
					}
					
					writer = new StringWriter();
					mapper.writeValue(writer, panel);

					pageContext.setAttribute("panel", writer.toString());
				} else {
					throw new JspException("No skuCode found");
				}
				
			} else if ("POST".equalsIgnoreCase(request.getMethod())) {
				
				if(request.getParameter("delete")!=null){
					
					ErpFactory.getInstance().deleteDrugPanel((String)request.getParameter("delete"));
					
				}else{
					
					String panelJson = (String) request.getParameter("panel");			
					panel = mapper.readValue(panelJson, NutritionDrugPanel.class);
					ErpFactory.getInstance().saveDrugPanel(panel);
					
					pageContext.setAttribute("panel", panelJson);
				}

			}

		} catch (FDResourceException e) {
			throw new JspException(e.getMessage());
		} catch (JsonGenerationException e) {
			throw new JspException(e.getMessage());
		} catch (JsonMappingException e) {
			throw new JspException(e.getMessage());
		} catch (IOException e) {
			throw new JspException(e.getMessage());
		}

		return EVAL_BODY_BUFFERED;

	}
	
	private NutritionDrugPanel createProtoPanel(String skuCode){
		
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
		
		public int compare(NutritionDrugSection o1, NutritionDrugSection o2) {
			return o1.getPosition() < o2.getPosition() ? -1 : o1.getPosition() == o2.getPosition() ? 0 : 1;
		}
		
	}
	
	class DrugItemComparator implements Comparator<NutritionDrugItem>{
		
		public int compare(NutritionDrugItem o1, NutritionDrugItem o2) {
			return o1.getPosition() < o2.getPosition() ? -1 : o1.getPosition() == o2.getPosition() ? 0 : 1;
		}
		
	}

}
