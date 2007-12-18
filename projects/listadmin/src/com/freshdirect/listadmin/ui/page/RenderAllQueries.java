package com.freshdirect.listadmin.ui.page;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.hibernate.Session;

import com.freshdirect.listadmin.core.ListadminDaoFactory;
import com.freshdirect.listadmin.db.ListadminDao;
import com.freshdirect.listadmin.db.StoredQuery;
import com.freshdirect.listadmin.query.MapQueryContext;
import com.freshdirect.listadmin.render.QueryRendererI;
import com.freshdirect.listadmin.render.sql.CmsQueryRenderer;

public class RenderAllQueries extends AppPage implements IExternalPage {

	public void activateExternalPage(Object[] arg0, IRequestCycle arg1) {
		
	}

//FIXME: commented out at the migration to tapestry 4. did anything use this?
//	public IMarkupWriter getResponseWriter(OutputStream out) {
//		return new HTMLWriter("text/xml","UTF-8",out);
//	}
	
	public String getData() {
		StringBuffer buffy = new StringBuffer();
		
		
		buffy.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffy.append("<Content>");
		buffy.append("<CmsQueryFolder id=\"queries\">");
		buffy.append("<name>OCF Queries</name>");
		buffy.append("<queries>");
		
		
		ListadminDao 	dao      = ListadminDaoFactory.getInstance().getListadminDao();
		Session      	sess     = dao.currentSession();
		List 		 	queries  = sess.createCriteria(StoredQuery.class).list();
		QueryRendererI 	renderer = new CmsQueryRenderer();
		MapQueryContext empty    = new MapQueryContext();
		
		for(Iterator it=queries.iterator();it.hasNext();) {
			buffy.append(renderer.renderQuery((StoredQuery) it.next(),empty));
		}
		buffy.append("</queries>");
		
		buffy.append("</CmsQueryFolder>");
		buffy.append("</Content>");

		return buffy.toString();
	}
}
