<%@ page import="java.net.*,java.io.*"%>  
<%  
        try {  
				String sku = request.getParameter("sku");
                URL url = new URL("http://dev.freshdirect.com/test/freemarker_testing/all_info.jsp?sku2url=true&sku=" + sku);  
                URLConnection conn = url.openConnection();  
          
                conn.setDoInput(true);  
                conn.setDoOutput(false);  
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
                StringBuffer sb= new StringBuffer();                
				String inputLine;
				while ((inputLine = br.readLine()) != null) 
					sb.append(inputLine);
                br.close(); 
				out.write(sb.toString());
        } catch(Exception e) {  
                e.printStackTrace();  
        }  
%>  