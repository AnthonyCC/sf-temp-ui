<%@ page import='java.lang.reflect.*' %>
<%@ page import='java.util.*' %>
<%!

final static String[] EXPAND_CLASS = { "com.freshdirect" };
final static String[] PRIMITIVE_CLASS = { "java.lang" };

public void debugObject(Set visitedObjects, JspWriter out, String title, Object obj) throws IOException {
	
	out.println("<table width=100% border=0 cellspacing=1 cellpadding=2 bgcolor=#ffffff><tr bgcolor=#cccccc><td colspan=2><b>");
	out.println("<A name='" + obj.hashCode() + "'>");
	out.println( title );
	out.println("</A></b></th></tr>");

	if (visitedObjects.contains(obj)) {
		out.println("<tr><td colspan=2 bgcolor=#f0f0f0>");
		out.println("<A href='#" + obj.hashCode() + "'>Object " + obj.hashCode() + "</A>");
		out.println("</td></tr></table>");
		return;
	}

	visitedObjects.add(obj);

	String className = obj.getClass().getName();
	for (int x=0; x<PRIMITIVE_CLASS.length; x++) {
		if (className.startsWith(PRIMITIVE_CLASS[x])) {
			out.println("<tr><td colspan=2 bgcolor=#f0f0f0>");
			out.println(obj);
			out.println("</td></tr></table>");
			return;
		}
	}

	Method[] methods = obj.getClass().getMethods();
	for (int i=0; i<methods.length; i++) {
		Method method = methods[i];
		if (method.getName().startsWith("get") && method.getParameterTypes().length==0) {
			out.println("<tr><td bgcolor=#dddddd valign=top>");
			out.println(method.getName());
			out.println("</td><td bgcolor=#f0f0f0>");
			try {
				Object o = method.invoke( obj, new Object[0] );
				if (o==null) {
					out.println("<i>null</i>");

				} else if (o instanceof Collection) {
					int z=0;
					for (Iterator ci=((Collection)o).iterator(); ci.hasNext(); z++) {
						debugObject(visitedObjects, out, z+". element", ci.next());
					}
				} else {
					String name = o.getClass().getName();
					boolean expanded=false;
					for (int x=0; x<EXPAND_CLASS.length; x++) {
						if (name.startsWith(EXPAND_CLASS[x])) {
							debugObject(visitedObjects, out, o.toString(), o);
							expanded=true;
							break;
						}
					}
					if (!expanded) {
						out.println( o.toString() );
					}
				}
			} catch (InvocationTargetException ex) {
				out.println("<font color=#ff0000>" + ex.getTargetException() + "</font>");
			} catch (Exception ex) {
				out.println("<font color=#ff0000>Unable to invoke (" + ex + ")</font>");
			}
			out.println("</td></tr>");
		}
	}
	out.println("</table>");
}
%>
<html>
<body bgcolor=#ffffff>
<h2>Debug session objects</h2>

<%
Set visitedObjects = new HashSet();
Enumeration e = session.getAttributeNames();
while (e.hasMoreElements()) {
  String name = (String)e.nextElement();
  debugObject(visitedObjects, out, "Session object <i>"+name+"</i>", session.getAttribute(name));
  out.println("<br>");
}
%>


</body>
</html>