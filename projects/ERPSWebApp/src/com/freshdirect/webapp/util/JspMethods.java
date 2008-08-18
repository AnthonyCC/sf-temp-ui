/*
 * JspMethods.java
 *
 * Created on March 13, 2002, 11:42 AM
 */
package com.freshdirect.webapp.util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.attributes.Attribute;
import com.freshdirect.fdstore.attributes.MultiAttribute;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryRef;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeI;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentRef;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainRef;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.DomainValueRef;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRef;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;

/**
 *
 * @author  rgayle
 * @version 
 */
public class JspMethods {
    
   private static Category LOGGER = LoggerFactory.getInstance( JspMethods.class );

   public final static ProductModel.PriceComparator priceComp = new ProductModel.PriceComparator();
    
   public final static ProductModel.RatingComparator ratingComp = new ProductModel.RatingComparator();
   
   
    public static String leadZeroes(double dblItem,int maxLength) {
        long dblToLng = Math.round(dblItem * 100);
        return JspMethods.leadZeroes(dblToLng+"",maxLength);
    }

    public static String leadZeroes(int intItem,int maxLength) {
        return JspMethods.leadZeroes(intItem+"",maxLength);
    }

    public static String leadZeroes(String ItemToPad,int maxLength) {
            String stringItem = ItemToPad;
            StringBuffer leadString = new StringBuffer();
            int lenStringThing = stringItem.length();
            for(int i = (maxLength  - lenStringThing); i > 0; i--) {
                    leadString.append("0");
            }
            leadString.append(stringItem);
            return leadString.toString();
    }

    public static String getImageDimensions(Image imgItem) {

        int height = -1;
        int width = -1;
        StringBuffer imgDimString = new StringBuffer();
        if (imgItem != null) {
            width = imgItem.getWidth();
            height = imgItem.getHeight();
            if (width !=-1  && height != -1) {
                imgDimString.append(" width=\"");
                imgDimString.append(width);
                imgDimString.append("\" ");

                imgDimString.append(" height=\"");
                imgDimString.append(height);
                imgDimString.append("\" ");
            }
        } else imgDimString.append(""); 
        return imgDimString.toString();
    }


    public static String getProductNameToUse(ContentNodeModel topFolder){
        Attribute attribute = topFolder.getAttribute("LIST_AS");
        String useName = attribute==null?"":(String)attribute.getValue();
        String displayAttribute = null;
        if ("nav".equalsIgnoreCase(useName)) {
            displayAttribute = "nav";
        } else  if ("glance".equalsIgnoreCase(useName)) {
            displayAttribute = "glance";
        } else {
            displayAttribute = "full";
        }
        return displayAttribute;
    }

    public static String getDisplayName(ContentNodeModel content_node,ProductModel prodNode) {
         if (content_node==null || prodNode ==null || !(ContentNodeI.TYPE_CATEGORY.equals(content_node.getContentType())) ) return "";
         String nameToUse = JspMethods.getProductNameToUse(content_node);
         if (nameToUse==null || nameToUse.equalsIgnoreCase("full")) return prodNode.getFullName();
         if (nameToUse!=null && nameToUse.equalsIgnoreCase("nav")) return prodNode.getNavName();
         if (nameToUse!=null && nameToUse.equalsIgnoreCase("glance")) return prodNode.getGlanceName();
         return prodNode.getFullName();
    }
    public static String getDisplayName(ProductModel prodNode,String nameToUse) {
         if (prodNode ==null) return "";
         if (nameToUse==null || nameToUse.equalsIgnoreCase("full")) return prodNode.getFullName();
         if (nameToUse!=null && nameToUse.equalsIgnoreCase("nav")) return prodNode.getNavName();
         if (nameToUse!=null && nameToUse.equalsIgnoreCase("glance")) return prodNode.getGlanceName();
         return prodNode.getFullName();
    }

    public static double getPrice(ProductModel theProduct) throws JspException{
        List skus = theProduct.getSkus(); 
        SkuModel sku = null;
        //remove the unavailable sku's
        for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
            sku = (SkuModel)li.next();
            if ( sku.isUnavailable() ) {
               li.remove();
            }
        }

        FDProductInfo productInfo = null;
        //ProductModel.PriceComparator priceComp = new ProductModel.PriceComparator();
        double prodPrice = 0.0;
        if (skus.size()==0) return prodPrice;  // skip this item..it has no skus.  Hmmm?
        if (skus.size()==1) {
            sku = (SkuModel)skus.get(0);  // we only need one sku
        }
        else {
            sku = (SkuModel) Collections.min(skus, priceComp);
        }
        if (sku!=null && sku.getSkuCode() != null) {
            //
            // get the FDProductInfo from the FDCachedFactory
            //
            try {
                productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode());
                prodPrice = productInfo.getDefaultPrice();
            } catch (FDResourceException fdre) {
                LOGGER.warn("FDResourceException occured", fdre);
                throw new JspException("JspMethods.getPrice method caught an FDResourceException");
            } catch (FDSkuNotFoundException fdsnfe) {
                LOGGER.warn("FDSkuNotFoundException occured", fdsnfe);
                throw new JspException("JspMethods.getPrice method caught an FDSkuNotFoundException");
            }
        }
        return prodPrice;
    }
    
    public static String getProductRating(ProductModel theProduct)throws JspException{
    	   //System.out.println("inside getProductRating :"+theProduct);
    	   
    	   String rating="";
    	
//    	   if(!FDStoreProperties.IsProduceRatingEnabled()){
//    		   return rating; 
//    	   }
    	   
    	   List skus = theProduct.getSkus(); 
           SkuModel sku = null;
           //remove the unavailable sku's
           for (ListIterator li=skus.listIterator(); li.hasNext(); ) {
               sku = (SkuModel)li.next();
               if ( sku.isUnavailable() ) {
                  li.remove();
               }
           }

           FDProductInfo productInfo = null;
           
           //ProductModel.PriceComparator priceComp = new ProductModel.PriceComparator();           
           if (skus.size()==0) return rating;  // skip this item..it has no skus.  Hmmm?
           if (skus.size()==1) {
               sku = (SkuModel)skus.get(0);  // we only need one sku
           }
           else {
               sku = (SkuModel) Collections.max(skus, ratingComp);
           }
           if (sku!=null && sku.getSkuCode() != null) {
               //
               // get the FDProductInfo from the FDCachedFactory
               //
               try {
            	   
            	   if(sku.getSkuCode().startsWith("FRU") || sku.getSkuCode().startsWith("VEG") || sku.getSkuCode().startsWith("YEL"))
            	   {
	                   productInfo = FDCachedFactory.getProductInfo( sku.getSkuCode());
	                   
	                   //System.out.println(" Rating productInfo :"+productInfo);    	
	                   
	                   String tmpRating = productInfo.getRating();
	                   
	                   if(tmpRating!=null && tmpRating.trim().length()>0){
	                	 
	                	   EnumOrderLineRating enumRating=EnumOrderLineRating.getEnumByStatusCode(tmpRating);
	                	   
	                	   //System.out.println(" enumRating :"+enumRating);
	                	   
	                	   if(enumRating!=null && enumRating.isEligibleToDisplay()){
	                		   rating=enumRating.getStatusCodeInDisplayFormat();
	                		   //System.out.println(" rating in display format  :"+rating);
	                	   }
	                   }
            	   } 
                   
               } catch (FDResourceException fdre) {
                   LOGGER.warn("FDResourceException occured", fdre);
                   throw new JspException("JspMethods.getPrice method caught an FDResourceException");
               } catch (FDSkuNotFoundException fdsnfe) {
                   LOGGER.warn("FDSkuNotFoundException occured", fdsnfe);
                   throw new JspException("JspMethods.getPrice method caught an FDSkuNotFoundException");
               }
           }
           return rating;
    }
    
    
    
    public static String getAttributeValue(ProductModel theProduct,String domainName,String maName)  {
        MultiAttribute prodSortMAttrib = (MultiAttribute)theProduct.getAttribute(maName);
        List prodDomainValues = null;
        String prodDomainValue = "";

        if (prodSortMAttrib !=null) {
            prodDomainValues = prodSortMAttrib.getValues();
        }else prodDomainValues = new ArrayList();

        boolean foundDomain = false;
       // get the matching domainvalue off the prod for this Domain.
        for(Iterator dvItr = prodDomainValues.iterator();dvItr.hasNext() && !foundDomain ;) {
            Object obj = dvItr.next();
            if (!(obj instanceof DomainValueRef)) {
            	continue;
            }
            DomainValue dmv = ((DomainValueRef)obj).getDomainValue(); 
            Domain dom = dmv.getDomain();
            if (dom.getName().equalsIgnoreCase(domainName)) {
                prodDomainValue = dmv.getValue();
                foundDomain = true;
            }
        }
        return prodDomainValue;
    }
    
	public static class ContentNodeComparator implements Comparator {

		public int compare(Object obj1, Object obj2) {
			if (obj1 instanceof ContentNodeModel && !(obj2 instanceof ContentNodeModel)) {
				return -1;
			} else  if (obj2 instanceof ContentNodeModel && !(obj1 instanceof ContentNodeModel)) {
				return 1;
			}
			String name1 = ((ContentNodeModel)obj1).getContentType()+":"+((ContentNodeModel)obj1).getFullName();
			String name2 = ((ContentNodeModel)obj2).getContentType()+":"+((ContentNodeModel)obj2).getFullName();
			return name1.compareToIgnoreCase(name2);
		}
	}

    public static class DomainNameComparator implements Comparator {
        // handles Domains or DomainRefs, DomainValue and DomainValueRefs
        public int compare (Object obj1, Object obj2) {
            String name1 = null;
            String name2 = null;
            if(obj1 instanceof Domain) {
                name1 = ((Domain)obj1).getName();
            }
            if(obj1 instanceof DomainRef) {
                name1 = ((DomainRef)obj1).getDomainName();
            }
            if(obj1 instanceof DomainValue) {
                name1 = ((DomainValue)obj1).getDomain().getName();
            }
            if(obj1 instanceof DomainValueRef) {
                name1 = ((DomainValueRef)obj1).getDomainName();
            }

            if(obj2 instanceof Domain) {
                name2 = ((Domain)obj1).getName();
            }
            if(obj2 instanceof DomainRef) {
                name2 = ((DomainRef)obj2).getDomainName();
            }
            if(obj2 instanceof DomainValue) {
                name2 = ((DomainValue)obj2).getDomain().getName();
            }
            if(obj2 instanceof DomainValueRef) {
                name2 = ((DomainValueRef)obj2).getDomainName();
            }
            
            if (name1==null || name2==null) return 0;
            return name1.compareToIgnoreCase(name2);
        }
    }
	
	public static Comparator domainValueComp = new Comparator() {
		public int compare(Object obj1, Object obj2) {
			DomainValue dv1 = (DomainValue) obj1;
			DomainValue dv2 = (DomainValue) obj2;
			return dv1.getValue().compareTo(dv2.getValue());
		}
	};
   
    public static class MyAttributeComparator implements Comparator {
        private String attribName = null;
        private boolean reverseOrder = false;
        private boolean includeCategory = true;
        private String MultiAttribName = null;
        public MyAttributeComparator(String attribName,String maName) {
            this.attribName=attribName;
            this.MultiAttribName = maName;
        }
        public void setReverseOrder(boolean flag) {
            this.reverseOrder = flag;
        }
        public void setIncludeCategory(boolean flag) {
            this.includeCategory = flag;
        }

        public int compare(Object obj1, Object obj2) {
            String sortField1 = "";
            String sortField2 = "";
            String attribValue1 = "";
            String attribValue2 = "";
            CategoryModel parentCategory = null;
            ContentNodeModel cn1 = (ContentNodeModel) obj1;
            ContentNodeModel cn2 = (ContentNodeModel) obj2;
            if (ContentNodeI.TYPE_CATEGORY.equals(cn1.getContentType())) {
                sortField1 = leadZeroes(((CategoryModel)cn1).getPriority(),6)+cn1.getFullName()+":";
            } else {
                parentCategory = (CategoryModel)((ProductModel)cn1).getParentNode();
                attribValue1=cn1.getFullName();
                if(!"name".equalsIgnoreCase(attribName) && !"price".equalsIgnoreCase(attribName)) {
                    attribValue1 = getAttributeValue((ProductModel)cn1,attribName,MultiAttribName);
                } else if("price".equalsIgnoreCase(attribName)) {
                    if(((ProductModel)cn1).isUnavailable()) {
                      attribValue1 = JspMethods.leadZeroes("9999.99",8);
                    } else { 
                        try {
                            attribValue1 = JspMethods.leadZeroes(JspMethods.getPrice((ProductModel)cn1),8);
                        } catch (JspException je) {
                            LOGGER.warn("JspException occured", je);
                            attribValue1="xxxxx";
                        }
                    }
                }
                if (includeCategory) {
                    sortField1 = leadZeroes(parentCategory.getPriority(),6)+parentCategory.getFullName()+":";
                } else {
                    sortField1 = "";
                }
            }

            if (ContentNodeI.TYPE_CATEGORY.equals(cn2.getContentType())) {
                sortField2 = JspMethods.leadZeroes(((CategoryModel)cn2).getPriority(),6)+cn2.getFullName()+":";
            } else {
                parentCategory = (CategoryModel)((ProductModel)cn2).getParentNode();
                attribValue2=cn2.getFullName();
                if(!"name".equalsIgnoreCase(attribName) && !"price".equalsIgnoreCase(attribName)) {
                    attribValue2 = getAttributeValue((ProductModel)cn2,attribName,MultiAttribName);
                } else if("price".equalsIgnoreCase(attribName)) {
                    if(((ProductModel)cn2).isUnavailable()) {
                      attribValue2 = JspMethods.leadZeroes("9999.99",8);
                    } else {
                        try {
                            attribValue2 = JspMethods.leadZeroes(JspMethods.getPrice((ProductModel)cn2),8);
                        } catch (JspException je) {
                            LOGGER.warn("JspException occured", je);
                            attribValue2="xxxxx";
                        }
                    }
                }
                if (includeCategory) {
                sortField2 = JspMethods.leadZeroes(parentCategory.getPriority(),6)+parentCategory.getFullName()+":";
                } else {
                    sortField2 = "";
                }
            }
            if ("".equals(attribValue1)) {
                attribValue1="zzzzzzzzz";
                if (reverseOrder) {
                    attribValue1="";
                }
            }
            if ("".equals(attribValue2)) {
                attribValue2="zzzzzzzzz";
                if (reverseOrder) {
                    attribValue2="";
                }
            }

            if (reverseOrder) {
                if (attribValue1.compareTo(attribValue2)!=0) {
                    String tmpx = attribValue2;
                    attribValue2 = attribValue1;
                    attribValue1 = tmpx;
                }
            }
            sortField1 = sortField1+attribValue1;
            sortField2 = sortField2+attribValue2;
            return sortField1.compareTo(sortField2);
        }
    }

    public static List sorter(java.util.Collection CatsAndProds,String orderBy) {
        return JspMethods.sorter(CatsAndProds,orderBy,true,true);
    }


    public static List sorter(java.util.Collection CatsAndProds,String orderBy,boolean reverseOrder,boolean includeFolder) {
        //sort the items by folder + (specified-attribute | Name | price)
        List sortedList = new ArrayList(CatsAndProds);
         MyAttributeComparator sortByRatingAttribute = new MyAttributeComparator(orderBy,"RATING");
         sortByRatingAttribute.setReverseOrder(reverseOrder);
         sortByRatingAttribute.setIncludeCategory(includeFolder);
         Collections.sort(sortedList,sortByRatingAttribute);
        //}
        return sortedList;
    }


	/* utility method */
    public static void dumpRequest(HttpServletRequest request){
        Enumeration rpn = request.getParameterNames();
        while( rpn.hasMoreElements() ) {
            String paramName = (String) rpn.nextElement();
            String paramValues[] = request.getParameterValues(paramName);
            if (paramValues == null){
                LOGGER.debug("Param name="+paramName+",  Value=[is null]");
            } else if (paramValues.length == 1  && paramValues[0].length()==0){
                LOGGER.debug("Param name="+paramName+",  Value=[Empty String]");
            } else if(paramValues.length == 1){
                LOGGER.debug("Param name="+paramName+",  Value="+paramValues[0]);
            } else if (paramValues.length > 1) {
                 LOGGER.debug("Param name="+paramName);
                 for(int j=0;j<paramValues.length;j++){
                     LOGGER.debug("   Value #"+j+1+"="+paramValues[j]);
                 }
             }
        }
    }
    
//**** moved these methods from CatLayoutManager.jspf ***/
  public static String displayFAProducts(LinkedList productLinks, boolean showPrices,boolean listIsUnavailable){
	 return  displayFAProducts(productLinks,null, showPrices, listIsUnavailable);
  }

//	*******************************************************
  public static String displayFAProducts(LinkedList productLinks, LinkedList productPrices, boolean showPrices,boolean listIsUnavailable){
	  if (productLinks.size()<1) return "";
	  int productItemsToDisplay = productLinks.size();
	  int columnCutoff = productItemsToDisplay / 2;
	  StringBuffer outputRows = new StringBuffer(2000);
	  if (productItemsToDisplay % 2 != 0) columnCutoff++;  //adjust for odd number of items

	  if (showPrices==true) {
			  for (int k = 0; k < productItemsToDisplay; k++) {
					  if (k!=0){
						outputRows.append("<tr>");
					  }
					  else{
							  outputRows.append("<td width=\"290\"><table border=\"0\" width=\"290\"><tr>");
							  if (listIsUnavailable) {
								  outputRows.append("<td  width=\"260\"><font color=\"#999999\"><b>Currently Unavailable<B></font></td><tr>");
							  }
					  }
					  outputRows.append("<TD width=\"290\">");
					  outputRows.append(productLinks.get(k));
					  //if (productPrices!=null ) {
					 //     outputRows.append(productPrices.get(k));
					 // }

					  outputRows.append("</td><TD width=\"5\">");
					  if (k==0) {  //Since this cell is in the first row use the image tag to set the spacing
							  outputRows.append("<IMG SRC=\"");
							  outputRows.append("/media_stat/images/layout/clear.gif");
							  outputRows.append("\" ALT=\"\" WIDTH=\"5\" HEIGHT=\"1\" BORDER=\"0\">");
					  }
					  else {
							  outputRows.append("&nbsp;");
					  }
					  outputRows.append("</td><TD width=\"70\" class=\"text11bold\">");
					  if (productPrices!=null) {
						  outputRows.append(productPrices.get(k));
					  } outputRows.append("&nbsp;"); 

					  outputRows.append("</td></tr>");
			  }
			  outputRows.append("</table></td>");
	  }
	  else {
			  for (int k = 0; k < columnCutoff; k++) {
					  if (k!=0){
						outputRows.append("<tr>");
					  }
					  else{
							  outputRows.append("<td width=\"290\"><table width=\"290\"><tr>");
							 if (listIsUnavailable) {
								  outputRows.append("<td colspan=\"3\" width=\"260\"><br><font color=\"#999999\"><b>Currently Unavailable</b></font></td><tr>");
							  }
					  }
					  outputRows.append("<TD valign=\"top\" width=\"140\">");
					  outputRows.append(productLinks.get(k));
					  outputRows.append("</td><TD width=\"10\">");
					  if (k==0) {  //Since this cell is in the first row use the image tag to set the spacing
							  outputRows.append("<IMG SRC=\"");
							  outputRows.append("/media_stat/images/layout/clear.gif");
							  outputRows.append("\" ALT=\"\" WIDTH=\"10\" HEIGHT=\"1\" BORDER=\"0\">");
					  }
					  else {
							  outputRows.append("&nbsp;");
					  }
					  outputRows.append("</td><TD valign=\"top\" width=\"140\">");
					  if ((columnCutoff+k) < productLinks.size()) {
							  outputRows.append(productLinks.get(columnCutoff+k));
					  }
					  else {
							  outputRows.append("&nbsp;");
					  }
					  outputRows.append("</td></tr>");
			  }
			  outputRows.append("</table></td>");
	  }
//	<TD width="90"><%=col1.toString()%//></td>
//	<%// =outputRows.toString()%//>

	  return outputRows.toString();
  }
  public static void dumpErrors(ActionResult _result) {
	  if (!_result.isSuccess()){
		  java.util.Collection errs = _result.getErrors();
		  Iterator itr = errs.iterator();
		  for (;itr.hasNext();){
			 System.out.println(((ActionError)itr.next()).getDescription());
		  }
	  }
  }


  public static DisplayObject loadLayoutDisplayStrings(HttpServletResponse response, String categoryId, ContentNodeModel displayThing,String productNameAttribute) throws JspException{
	  return  loadLayoutDisplayStrings(response,  categoryId, displayThing,productNameAttribute,true, false,null,false);
  }

  public static DisplayObject loadLayoutDisplayStrings(HttpServletResponse response, String categoryId, ContentNodeModel displayThing,String productNameAttribute,boolean showPrice) throws JspException{
	  return  loadLayoutDisplayStrings(response,  categoryId, displayThing,productNameAttribute,showPrice, false,null,false);
  }
  public static DisplayObject loadLayoutDisplayStrings(HttpServletResponse response, String categoryId, ContentNodeModel displayThing,
			   String productNameAttribute,boolean showPrice, boolean gotoPrimaryHome, String trkCode) throws JspException{
				return  loadLayoutDisplayStrings(response,  categoryId, displayThing,productNameAttribute,showPrice, gotoPrimaryHome,trkCode,false);
  }			   	

  public static DisplayObject loadLayoutDisplayStrings(HttpServletResponse response, String categoryId, ContentNodeModel displayThing,
			  String productNameAttribute,boolean showPrice, boolean gotoPrimaryHome, String trkCode,boolean useAltImage) throws JspException{
	 // String itemImage = "";
	  DisplayObject displayObj = new DisplayObject();
	  String trackingCode = (trkCode==null || "".equals(trkCode.trim()))
	  		? 	 ""
	  		:	 "&trk="+trkCode;
	  		
	  String itemName = "";
	  int imageWidth = 0;
	  int imageHeight = 0;
	  Attribute attribute = null;
	  String itemURL = "";
	  String itemAltText = "";
	  String rolloverImage = "";
	  StringBuffer indicators = new StringBuffer(300);
	  StringBuffer rolloverText = new StringBuffer(300);

//	*** load the variables with the appropriate stuff for use by Hoizontal & generic
	  String organicIndicator = "/media_stat/images/template/icon_organic.gif";
	  String inSeasonIndicator = "/media_stat/images/template/icon_in_season.gif";
	  String imagePath = null;
	  ContentNodeModel displayFolder=null;
	  ProductModel displayProduct = null;
	  String imgName = "ro_img_"+displayThing.getContentName();
	  rolloverText.setLength(0);
	  rolloverImage = "";
	  Image itemImage = null;
	  indicators.setLength(0);	
	  SkuModel dfltSku = null;
	  if(displayThing.getContentType().equals(ContentNodeI.TYPE_PRODUCT)) {
			 displayProduct = (ProductModel) displayThing;
			 dfltSku = displayProduct.getDefaultSku();
			 String thisProdBrandLabel=null;
			 try {
			 	if (dfltSku !=null && showPrice) {
					  FDProductInfo pi = FDCachedFactory.getProductInfo( dfltSku.getSkuCode() );
					  //pi.getAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), pi.getDefaultPriceUnit().toLowerCase())
					  displayObj.setPrice( currencyFormatter.format(pi.getDefaultPrice())+"/"+ pi.getDisplayableDefaultPriceUnit().toLowerCase());
                      String salesUnitDescr =  FDCachedFactory.getProduct( pi).getSalesUnits()[0].getDescription();
                      if (!"nm".equalsIgnoreCase(salesUnitDescr) && !"ea".equalsIgnoreCase(salesUnitDescr) && !"".equalsIgnoreCase(salesUnitDescr)) { 
                      	displayObj.setSalesUnitDescription(salesUnitDescr);
                      } else { displayObj.setSalesUnitDescription(""); }
			 	}
			   thisProdBrandLabel = displayProduct.getPrimaryBrandName();
			} catch (FDResourceException fde) {
				  throw new JspException(fde);
			} catch (FDSkuNotFoundException sknf) {
				  throw new JspException(sknf);
			}
			// get the produce Rating
			
			displayObj.setRating(JspMethods.getProductRating(displayProduct));
						
			if (useAltImage) {
				Attribute altImgAttrib = displayProduct.getAttribute("ALTERNATE_IMAGE");
				if (altImgAttrib !=null) {
					itemImage = (Image)altImgAttrib.getValue();
				}
			}
			if (itemImage==null) {
				itemImage = displayProduct.getCategoryImage();//displayProduct.getContent("ATR_image_product").getString("url");
			}
			imagePath = itemImage.getPath();
			StringBuffer tmpName = new StringBuffer();
			if ("nav".equalsIgnoreCase(productNameAttribute)){
				tmpName.append(displayProduct.getNavName());
			} else if ("glance".equalsIgnoreCase(productNameAttribute)){
				tmpName.append(displayProduct.getGlanceName());
			} else {
				if (thisProdBrandLabel!=null && thisProdBrandLabel.length()>0 
				  && displayProduct.getFullName().startsWith(thisProdBrandLabel)) {
					tmpName.append("<FONT CLASS=\"text10bold\">");
					tmpName.append(thisProdBrandLabel);
					tmpName.append("</font><BR>");
					tmpName.append(displayProduct.getFullName().substring(thisProdBrandLabel.length()).trim());
				} else {
					tmpName.append(displayProduct.getFullName());
				}
			}
			
			itemName = tmpName.toString(); 
			  //itemName =displayProduct.getString(productNameAttribute,"no"+productNameAttribute);
			  imageWidth = itemImage.getWidth();//displayProduct.getContent("ATR_image_product").getString("ATR_image_width");
			  imageHeight = itemImage.getHeight();//displayProduct.getContent("ATR_image_product").getString("ATR_image_height");
			  itemAltText = displayProduct.getFullName();//getString("ATR_full_name", itemName);
			  if (gotoPrimaryHome) {
				  Attribute attrib =displayProduct.getAttribute("PRIMARY_HOME");
				  if (attrib !=null ) {
					  CategoryRef catRef = (CategoryRef)attrib.getValue();
					  itemURL = response.encodeURL("product.jsp?productId=" + displayProduct + "&catId=" + catRef.getRefName()+trackingCode);
				  }
			  } else {
				  itemURL = response.encodeURL("product.jsp?productId=" + displayProduct + "&catId=" + categoryId+trackingCode);
			  }
			  attribute = displayProduct.getAttribute("PROD_IMAGE_ROLLOVER");
			  rolloverImage = attribute==null?"":((Image)attribute.getValue()).getPath();
			  if (!rolloverImage.equals("")) {
					   rolloverText.append("onMouseover='");
					  rolloverText.append("swapImage(\""+imgName+"\",\""+rolloverImage+"\")");
					   //rolloverText.append(rolloverImage);
					   rolloverText.append(";return true;'  onMouseout='");
					   rolloverText.append("swapImage(\""+imgName+"\",\""+imagePath+"\")");
					   rolloverText.append(";return true;'");
			  }
			  if (false==true) { //(displayProduct.getBoolean("ATR_in_season", false)) {
					  indicators.append("<IMG SRC=\"");
					  indicators.append(inSeasonIndicator);
					  indicators.append("\" width=\"9\" height=\"8\" hspace=\"3\" vspace=\"4\" border=\"0\"");
					  indicators.append(" ALT=\"\">");
			  }
			  else{
					  indicators.append("&nbsp;");
			  }

			  /*if (false==true) { //displayProduct.getBoolean("ATR_u_organic", false)) {
					  indicators.append("<IMG SRC=\"");
					  indicators.append(organicIndicator);
					  indicators.append("\" width=\"5\" height=\"5\" hspace=\"0\" vspace=\"5\" border=\"0\"");
					  indicators.append(" ALT=\"\">");
			  } 
			  else{ */
					  indicators.append("&nbsp;");
			  //}
	  } else if(ContentNodeI.TYPE_CATEGORY.equals(displayThing.getContentType())) {
			  displayFolder = (CategoryModel) displayThing;
			  attribute = displayFolder.getAttribute("CAT_PHOTO");
			  itemImage = attribute==null?null:(Image)attribute.getValue();
			  imagePath = attribute==null?"":itemImage.getPath();//getContent("ATR_image_category_photo").getString("url");
			  itemName = displayFolder.getFullName();
			  imageWidth = itemImage==null?0:itemImage.getWidth();   //displayFolder.getContent("ATR_image_category_photo").getString("ATR_image_width");
			  imageHeight = itemImage==null?0:itemImage.getHeight(); //displayFolder.getContent("ATR_image_category_photo").getString("ATR_image_height");
			  itemAltText = displayFolder.getAltText(); //(getString("ATR_alt_text", itemName);
			  //if (pageContext.getRequest().getRequestURI().toLowerCase().endsWith("department.jsp")){
			  //if (request.getRequestURI().toLowerCase().endsWith("department.jsp")){
			  //itemURL = response.encodeURL("/category.jsp?catId="+displayFolder+trkCode);
			  //} else {
			  itemURL = response.encodeURL("/category.jsp?catId="+displayFolder+trackingCode);
			  //} 
			  
			  if (displayFolder.getAttribute("ALIAS") !=null) {
					  ContentRef contentRef = (ContentRef)displayFolder.getAttribute("ALIAS").getValue();
					  if (ContentNodeI.TYPE_CATEGORY.equals(contentRef.getType())){
						  itemURL = response.encodeURL("/category.jsp?catId="+contentRef.getRefName()+trackingCode);
					 } else if (ContentNodeI.TYPE_DEPARTMENT.equals(contentRef.getType())) {
						  itemURL = response.encodeURL("/department.jsp?catId="+contentRef.getRefName()+trackingCode);
					 }
			   }

			  if (false==true) {//!!! need another way to do this(displayFolder.getBoolean("ATR_contains_organic", false)) {
					  indicators.append("<IMG SRC=\"");
					  indicators.append(organicIndicator);
					  indicators.append("\" width=\"5\" height=\"5\" hspace=\"0\" vspace=\"5\" border=\"0\"");
					  indicators.append(" ALT=\"\">");
			  }
			  else{
					  indicators.append("&nbsp;");
			  }
	  } else if(displayThing.getContentType().equals(ContentNodeI.TYPE_DEPARTMENT)) {
			  displayFolder = (DepartmentModel) displayThing;
			  attribute = displayFolder.getAttribute("DEPT_PHOTO");
			  itemImage = attribute==null?null:(Image)attribute.getValue();
			  imagePath = itemImage==null?"":itemImage.getPath();
			  itemName = displayFolder.getFullName();
			  imageWidth = itemImage==null?0:itemImage.getWidth();  
			  imageHeight = itemImage==null?0:itemImage.getHeight(); 
			  itemAltText = displayFolder.getAltText(); 
			  itemURL = response.encodeURL("category.jsp?catId="+displayFolder+trkCode);
			  if (false==true) {//!!! need another way to do this(displayFolder.getBoolean("ATR_contains_organic", false)) {
					  indicators.append("<IMG SRC=\"");
					  indicators.append(organicIndicator);
					  indicators.append("\" width=\"5\" height=\"5\" hspace=\"0\" vspace=\"5\" border=\"0\"");
					  indicators.append(" ALT=\"\">");
			  }
			  else{
					  indicators.append("&nbsp;");
			  }
	  }
	  displayObj.setItemName(itemName);
	  displayObj.setItemURL(itemURL);
	  displayObj.setImageName(imgName);
	  displayObj.setImagePath(imagePath);
	  displayObj.setImageWidth(imageWidth+"");
	  displayObj.setImageHeight(imageHeight+"");
	  displayObj.setAltText(itemAltText);
	  displayObj.setRolloverString(rolloverText.toString());
	  displayObj.setIndicators(indicators.toString());
	  return displayObj;

  }
  public static ProductModel getFeaturedProduct(ContentRef cr) {
	  if (!cr.getType().equals(ContentNodeI.TYPE_PRODUCT)) return null;
	  return getFeaturedProduct((ProductRef)cr);
  }


  public static ProductModel getFeaturedProduct(ProductRef pf) {
	  // the content ref must have a category and Product Id in it
	 if (pf==null || !pf.getType().equals(ContentNodeI.TYPE_PRODUCT)) {
		  return null;
	 }
	 ContentFactory contentFactory = ContentFactory.getInstance();
	 ContentNodeModel cnm = null;

	 if (pf.getRefName()==null || pf.getRefName2()==null){
		  return null;
	 }
	 ProductModel product = null;
	 product = pf.lookupProduct();
	 if (product==null) {
		 cnm = pf.lookupCategory();
		 if (cnm==null ) {
			  return null;
		 }
		  //great..get this category's subcategories
		 product = null;
		 List subFolders = ((CategoryModel)cnm).getSubcategories();
		 for(int idx = 0;idx < subFolders.size() && product==null;idx++) {
			 CategoryModel subFldr = (CategoryModel)subFolders.get(idx);
			  product = contentFactory.getProductByName(subFldr.toString(),pf.getRefName2());
		 }

		 if (product==null) {
		 }
	 }
	 return product;
  }


 	  public static java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);

	  public static Integer[][] displayPattern;
	
	  static {
		  int[][] tempDisplayPattern = {
			  {1, 0, 0, 0, 0}, {2, 0, 0, 0, 0}, {3, 0, 0, 0, 0},
			  {4, 0, 0, 0, 0}, {3, 2, 0, 0, 0}, {4, 2, 0, 0, 0},
			  {4, 3, 0, 0, 0}, {4, 4, 0, 0, 0}, {4, 2, 3, 0, 0},
			  {4, 3, 3, 0, 0}, {4, 3, 4, 0, 0}, {4, 4, 4, 0, 0},
			  {4, 3, 4, 2, 0}, {4, 3, 4, 3, 0}, {4, 4, 4, 3, 0},
			  {4, 4, 4, 4, 0}, {4, 3, 4, 3, 3}, {4, 3, 4, 3, 4},
			  {4, 4, 4, 4, 3}, {4, 4, 4, 4, 4} };
	
		  displayPattern = new Integer[tempDisplayPattern.length][5];
		  //transform the int array into a two dimensional Interger array...for esthetic purposes
		  for (int x=0; x<displayPattern.length; x++) {
			  for (int y=0; y<5; y++) {
				  displayPattern[x][y]=new Integer(tempDisplayPattern[x][y]);
			  }
		  }
	  }
	  
	public static String removeChars(String source, String charsToRemove) {
			if (source==null || charsToRemove==null || source.length()==0 || charsToRemove.length()==0) return source;
			String uSource = source.toUpperCase();
			String uctr = charsToRemove.toUpperCase();
			StringBuffer workBuffer = new StringBuffer();
			for (int idx =0; idx < uSource.length();idx++){
				char[] chr = {uSource.charAt(idx) };
				if (uctr.indexOf(new String(chr))==-1) {
					workBuffer.append(chr);
				}
			}
			if (workBuffer.length()!=0) {
				return workBuffer.toString();
			}
			return "";
	}

}
