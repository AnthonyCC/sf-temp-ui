<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.*'%>
<%@ page import='com.freshdirect.fdstore.FDReservation'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>

<%@ page import='com.freshdirect.fdstore.content.ContentFactory' %>
<%@ page import='com.freshdirect.fdstore.content.ProductModel' %>
<%@ page import='com.freshdirect.fdstore.content.Domain' %>
<%@ page import='com.freshdirect.fdstore.content.Html' %>
<%@ page import='com.freshdirect.fdstore.content.CategoryModel' %>
<%@ page import='com.freshdirect.fdstore.content.DepartmentModel' %>

<%@ page import='java.util.List' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>

<HTML>
<HEAD>
<TITLE>WINE ATTRIBUTES</TITLE>
</HEAD>
<BODY>
<%
    String contentId=request.getParameter("contentNodeId");
    System.out.println("contentId :"+contentId);
  
%>

<form name="form1">
<TABLE WIDTH="50%">
<TR>
<TD align="right">PRODUCT ID :</TD>
<TD><input type="text" name="contentNodeId" value=""></TD>
<TD><input type="submit" name="submit" value="  FETCH  "></TD>
<TD>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TD>
</TR>

</TABLE>
<BR><BR>
<%

  if(contentId!=null){
        ContentNodeModel model1=ContentFactory.getInstance().getContentNode(contentId);                
        String parentName=model1.getParentNode().getContentName();
        System.out.println("model :"+model1+" parentName :"+parentName);
        
		ProductModel model= ContentFactory.getInstance().getProduct(parentName, contentId);								
		System.out.println("model new1 :"+model);	
        
   
%>

<TABLE width="70%" border="1">
<TR>
<TH bgColor="gray"><B>WINE ATTRIBUTES[CATEGORY]</B></TH>
<TH bgColor="gray"><B>VALUES</B></TH>
</TR>
<TR>
<TD><b>Wine Sorting Domain Values :</b></TD>
<TD>
<%
	    CategoryModel catModel1=(CategoryModel) ContentFactory.getInstance().getContentNode(parentName);
        
        List wineSortList=catModel1.getWineSortCriteria();		
		if(wineSortList!=null && wineSortList.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineSortList.size();i++){
				
			   DomainValue dValue=(DomainValue)wineSortList.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
                }
%>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+":</font><b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>       
</TR>
<TR>
<TD><b>Wine Filter Domain :</b></TD>
<TD>
<%
        List wineFilterList=catModel1.getWineFilterCriteria();		
		if(wineFilterList!=null && wineFilterList.size()>0){                
			for(int i=0;i<wineFilterList.size();i++){
				
			   Domain dName=(Domain)wineFilterList.get(i);
%>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+dName+"</font>"%>;
		<%	}			
		}
       %> 
</TD>       
</TR>
<TR>
<TD><b>Wine Side Nav Full List Domain :</b></TD>
<TD>
<%
	List wineSideNavFullList=catModel1.getWineSideNavFullList();
		if(wineSideNavFullList!=null && wineSideNavFullList.size()>0)
        {
			String domainName=null;			
			for(int i=0;i<wineSideNavFullList.size();i++){				
			   Domain dName=(Domain)wineSideNavFullList.get(i);
%>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+dName+"</font>"%>;
		<%	}			
		}
       %> 
</TD>       
</TR>
<TR>
<TD><b>Wine Side Nav Sections Domain Values :</b></TD>
<TD>
<%	         
            List wineSectionsList=catModel1.getWineSideNavSections();
			if(wineSectionsList!=null && wineSectionsList.size()>0)
            {
				String domainName=null;				
				for(int i=0;i<wineSectionsList.size();i++)
                {					
				   DomainValue dValue=(DomainValue)wineSectionsList.get(i);
					if(i==0){
						domainName=dValue.getDomain().getName();
					}
%>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+":</font><b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	   }
            }        
		
       %> 
</TD>       
</TR>
<TR>
<TD><b>Template Path:</b></TD>
<TD><font color='blue' size='4'><%=catModel1.getContentTemplatePath()%></font></TD>
</TR>
</TABLE>



<TABLE width="70%" border="1">
<TR>
<TH bgColor="gray"><B>WINE ATTRIBUTES[PRODUCT]</B></TH>
<TH bgColor="gray"><B>VALUES</B></TH>
</TR>
<BR>
<TR>
<TD><b>Wine Classifications Text:</b></TD>
<TD><font color='blue' size='4'><%=model.getWineClassification()%></font></TD>
</TR>
<TR>
<TD><b>Wine Aging Text:</b></TD>
<TD><font color='blue' size='4'><%=model.getWineAging()%></font></TD>
</TR>
<TR>
<TD><b>Wine Alchohol Content Text:</b></TD>
<TD><font color='blue' size='4'><%=model.getWineAlchoholContent()%></font></TD>
</TR>
<TR>
<TD><b>Wine Importer Text:</b></TD>
<TD><font color='blue' size='4'><%=model.getWineImporter()%></font></TD>
</TR>
<TR>
<TD><b>Wine City:</b></TD>
<TD><font color='blue' size='4'><%=model.getWineCity()%></font></TD>
</TR>
<TR>
<TD><b>Wine Type Domain Values:</b></TD>
<TD><%
    List wineTypeList=model.getNewWineType();
      	if(wineTypeList!=null && wineTypeList.size()>0){
			String domainName=null;			
			for(int i=0;i<wineTypeList.size();i++){				
			   DomainValue dValue=(DomainValue)wineTypeList.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>
<TR>
<TD><b>Wine Region Domain Values:</b></TD>
<TD><%
		List wineRegionList=model.getNewWineRegion();		
		if(wineRegionList!=null && wineRegionList.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineRegionList.size();i++){
				
			   DomainValue dValue=(DomainValue)wineRegionList.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();

				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>
<TR>
<TD><b>Wine Vintage Domain Values:</b></TD>
<TD><%
		List wineVintage=model.getWineVintage();		
		if(wineVintage!=null && wineVintage.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineVintage.size();i++){
				
			   DomainValue dValue=(DomainValue)wineVintage.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>

<TR>
<TD><b>Wine Verietal Domain Values:</b></TD>
<TD><%
 
        List wineVarietal=model.getWineVarietal();		
		if(wineVarietal!=null && wineVarietal.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineVarietal.size();i++){
				
			   DomainValue dValue=(DomainValue)wineVarietal.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>


<TR>
<TD><b>Wine Country Domain Value:</b></TD>
<TD><%

DomainValue value=model.getWineCountry();
		if(value!=null){
			   System.out.println("WineCountry domainName:"+value.getDomain().getFullName()+":"+value.getLabel());
 %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+value.getDomain().getName()+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+value.getLabel()+"</font>"%>;
		<%				
		}
       %> 
</TD>
</TR>

<TR>
<TD><b>Wine Rating1 Domain Values:</b></TD>
<TD><%
        List wineRating1=model.getWineRating1();		
		if(wineRating1!=null && wineRating1.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineRating1.size();i++){
				
			   DomainValue dValue=(DomainValue)wineRating1.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>
<TR>
<TD><b>Wine Rating2 Domain Values:</b></TD>
<TD><%
        List wineRating2=model.getWineRating2();		
		if(wineRating2!=null && wineRating2.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineRating2.size();i++){
				
			   DomainValue dValue=(DomainValue)wineRating2.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>
<TR>
<TD><b>Wine Rating3 Domain Values:</b></TD>
<TD><%
        List wineRating3=model.getWineRating3();		
		if(wineRating3!=null && wineRating3.size()>0){
			String domainName=null;
			
			for(int i=0;i<wineRating3.size();i++){
				
			   DomainValue dValue=(DomainValue)wineRating3.get(i);
				if(i==0){
					domainName=dValue.getDomain().getName();
				} %>
			   <%="<b>DomainName:</b><font color='blue' size='4'>"+domainName+"</font>:<b>domainValue :</b><font color='blue' size='4'>"+dValue.getLabel()+"</font>"%>;
		<%	}			
		}
       %> 
</TD>
</TR>
<TR>
<TD><b>Wine Review1 :</b></TD>
<TD>
<%
        Html review1=model.getWineReview1();
        String htmlStr1="";
        if(review1 instanceof TitledMedia){
        	TitledMedia tReview1=(TitledMedia)review1;        
        	htmlStr1="<b>MediaTitle :</b><font color='blue' size='4'>"+tReview1.getMediaTitle()+"</font><b>: size:</b><font color='blue' size='4'>"+tReview1.getPopupSize()+"</font><b>: path :</b><font color='blue' size='4'>"+tReview1.getPath()+"</font>";
        }
        else if(review1!=null)
        {
        	htmlStr1="<b> MediaType :</b><font color='blue' size='4'>"+review1.getMediaType()+"</font><b>: Path:</b><font color='blue' size='4'>"+review1.getPath()+"</font>";
        }

%>
<%=htmlStr1%>
</TD>
</TR>
<TR>
<TD><b>Wine Review2 :</b></TD>
<TD>
<%
        Html review2=model.getWineReview2();
        String htmlStr2="";
        if(review2 instanceof TitledMedia){
        	TitledMedia tReview2=(TitledMedia)review2;        
        	htmlStr2="<b>MediaTitle :</b><font color='blue' size='4'>"+tReview2.getMediaTitle()+"</font><b>: size:</b><font color='blue' size='4'>"+tReview2.getPopupSize()+"</font><b>: path :</b><font color='blue' size='4'>"+tReview2.getPath()+"</font>";
        }
        else if(review2!=null)
        {
        	htmlStr2="<b> MediaType :</b><font color='blue' size='4'>"+review2.getMediaType()+"</font><b>: Path:</b><font color='blue' size='4'>"+review2.getPath()+"</font>";
        }

%>
<%=htmlStr2%>
</TD>
</TR>
<TR>
<TD><b>Wine Review3 :</b></TD>
<TD>
<%
        Html review3=model.getWineReview3();
        String htmlStr3="";
        if(review3 instanceof TitledMedia){
        	TitledMedia tReview3=(TitledMedia)review3;        
        	htmlStr3="<b>MediaTitle :</b><font color='blue' size='4'>"+tReview3.getMediaTitle()+"</font><b>: size:</b><font color='blue' size='4'>"+tReview3.getPopupSize()+"</font><b>: path :</b><font color='blue' size='4'>"+tReview3.getPath()+"</font>";
        }
        else if(review3!=null)
        {
        	htmlStr3="<b> MediaType :</b><font color='blue' size='4'>"+review3.getMediaType()+"</font><b>: Path:</b><font color='blue' size='4'>"+review3.getPath()+"</font>";
        }

%>
<%=htmlStr3%>
</TD>
</TR>

<TR>
<TD><b>Product Bottom Media :</b></TD>
<TD>
<%
        Html bottomMedia1=model.getProductBottomMedia();
        String mediaStr="";
        if(bottomMedia1 instanceof TitledMedia){
        	TitledMedia tMedia=(TitledMedia)bottomMedia1;        
        	mediaStr="<b>MediaTitle :</b><font color='blue' size='4'>"+tMedia.getMediaTitle()+"</font><b>: size:</b><font color='blue' size='4'>"+tMedia.getPopupSize()+"</font><b>: path :</b><font color='blue' size='4'>"+tMedia.getPath()+"</font>";
        }
        else if(bottomMedia1!=null)
        {
        	mediaStr="<b> MediaType :</b><font color='blue' size='4'>"+bottomMedia1.getMediaType()+"</font><b>: Path:</b><font color='blue' size='4'>"+bottomMedia1.getPath()+"</font>";
        }

%>
<%=mediaStr%>
</TD>
</TR>

<TR>
<TD><b>Wine Perfect Pair :</b></TD>
<%
	    CategoryModel catModel =model.getPerfectPair();
	    String catStr="";
	    if(catModel!=null){
	    	catStr="<b> Category Id:</b><font color='blue' size='4'>"+catModel.getContentKey().getId()+"</font><b>: Category Name :</b><font color='blue' size='4'>"+catModel.getFullName()+"</font><B>: Department :</B><font color='blue' size='4'>"+catModel.getDepartment().getFullName()+"</font>";
	    }

%>
<TD><%=catStr%></TD>
</TR>
</TABLE><br><br><br>


<% 
	
  }
%>
</FORM>
</BODY>
</HTML>