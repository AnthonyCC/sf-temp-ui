<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.freshdirect.cms.application.CmsImport"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import='org.apache.commons.fileupload.servlet.ServletFileUpload,org.apache.commons.fileupload.FileItemFactory,org.apache.commons.fileupload.FileItem,org.apache.commons.fileupload.disk.DiskFileItemFactory' %>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>CMS Data Upload</title>
	<link rel="stylesheet" type="text/css" href="/test/search/config.css" />
</head>
<%
    String uploadError = "";
    String message = "";

    if (ServletFileUpload.isMultipartContent(request)) {
        // Create a factory for disk-based file items
        FileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        FileItem cmsContent = null;
        // Parse the request
        List /* FileItem */items = upload.parseRequest(request);
        for (Iterator i = items.iterator(); i.hasNext();) {
            FileItem item = (FileItem) i.next();
            if ("cmsContent".equals(item.getFieldName())) {
                cmsContent = item;
            }
        }

        if (cmsContent == null || cmsContent.getSize() == 0) {
            uploadError += "<li>Missing CMS file!</li>";
        } else {
            message += "<li>Uploaded <i>" + cmsContent.getName() + "</i> , size: " + cmsContent.getSize() + "</li>";
            try {
                CmsImport imp = new CmsImport(cmsContent.getName(), cmsContent.getInputStream());
                imp.run();
                if (imp.getResponse() != null) {
                    message += "<li>Changeset id :" + imp.getResponse().getChangeSetId() + "</li>";
                }
                if (imp.getException() != null) {
                    uploadError += "<li>"+imp.getException().getMessage()+"</li>";
                }
            } finally {
                cmsContent.delete();
            }

        }

    }
%>
<body>
<%
    if (!"".equals(uploadError)) {
%>
	<ul class="error">
		<%=uploadError%>
	</ul> 
<%
     }
     if (!"".equals(message)) {
 %>
	<ul class="error">
		<%=message%>
	</ul> 
<%
     }
 %>
 	<ul>
 		Total Memory : <%= Runtime.getRuntime().totalMemory() / 1024 / 1024 %> MB
 	</ul>
 	<ul>
 		Free Memory : <%= Runtime.getRuntime().freeMemory() / 1024 / 1024 %> MB
 	</ul>
 	<ul>
 		Max Memory : <%= Runtime.getRuntime().maxMemory() / 1024 / 1024 %> MB
 	</ul>
<form method="post" enctype="multipart/form-data">
<fieldset style="margin-bottom: 10px;width: 974px;">
<legend>Upload CMS Content</legend>
	<div><label for="cmsContent">CMS content:</label><input type="file" id="cmsContent" name="cmsContent"></div>
	<div><input type="submit" value="Upload file"></div>
</fieldset>
</form>

<legend>Full restore</legend>
<div>
	<li>
		Create backup from the CMS schema : 
	There is a <i><a href=" https://svn.freshdirect.com/appdev/FreshDirect/trunk/release/db/CMS_Backup/create_cms_backup_tables.sql">release/db/CMS_Backup/create_cms_backup_tables.sql</a></i> 
	which creates the necessary <b>backup</b> tables, and <i><a href="https://svn.freshdirect.com/appdev/FreshDirect/trunk/release/db/CMS_Backup/store_backup.sql">release/db/CMS_Backup/store_backup.sql</a></i> script, 
	which copies the relevant tables to the backup tables.
	</li> 
	<li>
		Delete rows from the CMS.MEDIA table, before the <i>Media.xml.gz</i> is imported.
	</li>	
	<li>
		Delete rows from the CMS.CONTENTNODE, CMS.ATTRIBUTE, CMS.RELATIONSHIP table, before the <i>Store.xml.gz</i> is imported.
	</li>	

</div>
</body>
</html>