
<%@page import="com.freshdirect.webapp.util.FDEventUtil"%>

<%@page import="java.io.InputStream"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Random"%>

<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>

<%@page import="com.freshdirect.framework.util.CSVUtils"%>
<%@page import="com.freshdirect.framework.util.DiscreteRandomSamplerWithReplacement"%>


<%@page import="com.freshdirect.webapp.util.FDEventUtil"%>

<%@page import="com.freshdirect.smartstore.Variant"%>

<%@page import="com.freshdirect.cms.ContentKey"%>

<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>



<h2>Impression Logger Test</h2>

<%

String action = request.getParameter("action");


//
// CVS File upload
//
if (ServletFileUpload.isMultipartContent(request)) {

	int events = 10000;
	int threads = 1;
	int burst = 5;
	double mu = 0.2;
	double std = 0.1;
	DiscreteRandomSamplerWithReplacement sampler = new DiscreteRandomSamplerWithReplacement();

	try {

		// Create a factory for disk-based file items
		FileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List /* FileItem */ items = upload.parseRequest(request);

		for(Iterator i = items.iterator(); i.hasNext(); ) {
			FileItem fileItem = (FileItem)i.next();
			if ("events".equals(fileItem.getFieldName())) {
				events = Integer.parseInt(fileItem.getString());
			} else if ("threads".equals(fileItem.getFieldName())) {
				threads = Integer.parseInt(fileItem.getString());
			} else if ("burst".equals(fileItem.getFieldName())) {
				burst = Integer.parseInt(fileItem.getString());
			} else if ("mu".equals(fileItem.getFieldName())) {
				mu = Double.parseDouble(fileItem.getString());
			} else if ("std".equals(fileItem.getFieldName())) {
				std = Double.parseDouble(fileItem.getString());
			}
		}


		for(Iterator i = items.iterator(); i.hasNext(); ) {
			FileItem fileItem = (FileItem)i.next();
			if ("csvfile".equals(fileItem.getFieldName())) {
				List distro = CSVUtils.parse(fileItem.getInputStream(),false,false);
				for(Iterator di=distro.iterator(); di.hasNext(); ) {
					List row = (List)di.next();
					sampler.addValue(row.get(0),Long.parseLong(row.get(1).toString()));
				}
			}
		}
%>


Threads started:
<ul>
	<li>Threads: <%= threads %></li>
	<li>Content ids: <%= sampler.getItemCount() %></li>
	<li>Total mass: <%= sampler.getTotalFrequency() %></li>
	<li>Burst size: <%= burst %></li>
	<li>Timer mean: <%= mu %></li>
	<li>Standard deviation: <%= std %></li>
</ul>

<%
		final DiscreteRandomSamplerWithReplacement samplerRef = sampler;
		final int eventsRef = events;

		final double muRef = mu;
		final double stdRef = std;

		final int burstRef = burst;

		for(int t = 0; t < threads; ++t) {
			new Thread(
				new Runnable() {
					private Random R = new Random();

					private String[] variants = new String[] { 
						"V1",
						"V2",
						"V3",
						"V4"
					};

					private double waitTime() {
						double g = R.nextGaussian();
						double nG = g*Math.sqrt(stdRef) + muRef;
						return nG < 0 ? 0 : nG;
					}

					public void run() {
						for(int i = 0; i < eventsRef; ++i) {
							if ((burstRef > 1) && (i % burstRef == (burstRef - 1))) {
								try {
									int millisecs = (int)(waitTime() * 1000.0);
									Thread.sleep(millisecs);
								} catch (InterruptedException e) {}
							}
							String skuCode = samplerRef.getRandomItem(R).toString();
							FDEventUtil.logRecommendationImpression(
								variants[R.nextInt(variants.length)],
								new ContentKey(FDContentTypes.SKU,skuCode)
							);
						}
					}
				}
			).start();
		}
	} catch (Exception e) {
%>
Valami baj van: <%= e %>
<%
	}
} else {
%>


<form name="cvsform" method="post" enctype="multipart/form-data">
	<table>
	<tr>
		<td>Content distribution (CSV file):</td><td><input type="file" name="csvfile"/></td>
	</tr>
	<tr>
		<td>Threads:</td><td><input type="text" name="threads" value="1"/></td>
	</tr>
	<tr>
		<td>Events to produce (by thread)</td><td><input type="text" name="events" value="10000"/></td>
	</tr>
	<tr>
		<td>Burst size</td><td><input type="text" name="burst" value="5"/></td>
	</tr>
	<tr>
		<td>Mean burst generation time (in sec)</td><td><input type="text" name="mu" value="0.2"/></td>
	</tr>
	<tr>
		<td>Standard deviation (in sec)</td><td><input type="text" name="std" value="0.1"/></td>
	</tr>
	<tr>
		<td colspan="2"><a href="impression_start_stop.jsp">Manage timed event flushes</a></td>
	</tr>
	<tr>
		<td colspan="2"><input type="submit" value="go" style="margin-top: 20px"/></td>
	</tr>

	</table>
</form>


<h2>What is this?</h2>

This tool generates impression events. The content distribution needs to be uploaded as a CSV file. It must have two columns, where the
first is the content id (uninterpreted) and the second is the frequency. Several generators can be started simoultaneosly in their own
threads. Each will draw the specified number of samples to compile a histogram and will generate the specified number of events from this
distribution. Events will be generated in bursts; that is, the specified burst size number of impressions will be generated at once. This
is followed by a <a href="http://en.wikipedia.org/wiki/Gaussian_function">Gaussian</a> pause having the specified mean and standard 
deviation.

<%
}


%>
