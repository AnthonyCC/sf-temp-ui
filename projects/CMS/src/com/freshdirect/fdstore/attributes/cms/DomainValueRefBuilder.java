/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.DomainValueRef;

/**
 * @author mrose
 *
 */
public class DomainValueRefBuilder extends AbstractAttributeBuilder {
    
    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.DOMAINVALUEREF;
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        ContentNodeI cNode = (ContentNodeI) value;
 //  H A C K ! ! ! !    H A C K ! ! ! ! //
        /*
        String domainValuePortion = (String)cNode.getAttribute("Label").getValue();
		if (domainValuePortion!=null ) {
			String newDmvPortion = domainValuePortion.replaceAll(" ","_");
			int posDmv = cNode.getKey().getId().indexOf(newDmvPortion);
			String domainID = posDmv>-1 ? cNode.getKey().getId().substring(0,posDmv-1) : null;
			
			//** that failed...if it has one underscore  then use stuff before first underscore **
			if (domainID==null && StringUtils.countMatches(cNode.getKey().getId(),"_")==1) {
				posDmv = cNode.getKey().getId().indexOf("_");
				domainID = posDmv>-1 ? cNode.getKey().getId().substring(0,posDmv) : null;				
			}
			//** that failed...Find the first UpperCase or numeric  char preceded by an underscore**
			if (domainID==null ){
				String upperID = cNode.getKey().getId().toUpperCase();
				StringBuffer sb = new StringBuffer();
				boolean keepGoing=true;
				for (int x=0; x<upperID.length() && keepGoing; x++) {
					if ("-".equals(upperID.substring(x,x+1)) ||
						!cNode.getKey().getId().substring(x,x+1).equals(upperID.substring(x,x+1)) ) {
						 sb.append(cNode.getKey().getId().substring(x,x+1));
					} else if (!"-".equals(upperID.substring(x,x+1)) ) {
						keepGoing=false;
					}
				}
					
				domainID =sb.length()>0 ? sb.toString() : null;	
				if (domainID!=null && domainID.endsWith("_")) {
					domainID = sb.toString().substring(0,sb.length());
				}
			}
			
			System.out.println("** Hack Returning Domain "+domainID+"   node key id: "+cNode.getKey().getId());
			
			return (domainID==null ? null : new DomainValueRef(domainID, cNode.getKey().getId()) );
		} else {
			System.out.println("**** RETurning NUll ***");
			return null;
		}
//  E N D   O F   H A C K  !!!!! */
		
		/*  using hack above until getParentKeys is implemented */
//System.out.println("DomainValueBuilder getting:"+cNode.getKey());
        ContentNodeI domainNode = findBestParent(cNode);
        if (domainNode != null) {
			return new DomainValueRef(domainNode.getKey().getId(), cNode.getKey().getId());
		} else {
		    return null;
		} 

    }	
}
