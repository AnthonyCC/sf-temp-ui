/**
 * @author ekracoff
 * Created on Jan 10, 2005*/

package com.freshdirect.cms.listeners;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.slide.authenticate.SecurityToken;
import org.apache.slide.common.Domain;
import org.apache.slide.common.NamespaceAccessToken;
import org.apache.slide.common.ServiceAccessException;
import org.apache.slide.common.SlideToken;
import org.apache.slide.content.Content;
import org.apache.slide.content.ContentImpl;
import org.apache.slide.content.NodeRevisionContent;
import org.apache.slide.content.NodeRevisionDescriptor;
import org.apache.slide.content.NodeRevisionNumber;
import org.apache.slide.content.RevisionContentNotFoundException;
import org.apache.slide.content.RevisionNotFoundException;
import org.apache.slide.event.ContentEvent;
import org.apache.slide.event.EventCollection;
import org.apache.slide.event.EventCollectionListener;
import org.apache.slide.event.MacroEvent;
import org.apache.slide.event.VetoException;
import org.apache.slide.event.EventCollection.Event;
import org.apache.slide.lock.ObjectLockedException;
import org.apache.slide.security.AccessDeniedException;
import org.apache.slide.structure.LinkedObjectNotFoundException;
import org.apache.slide.structure.ObjectNotFoundException;
import org.apache.slide.structure.Structure;
import org.apache.slide.structure.SubjectNode;
import org.apache.slide.webdav.event.WebdavEvent;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.fdstore.ProductBulkLoader;
import com.freshdirect.cms.validation.ContentValidationException;
import com.freshdirect.framework.conf.FDRegistry;

public class CMSEventCollectionListener implements EventCollectionListener {
	
	private MediaEventHandlerI handler = (MediaEventHandlerI) FDRegistry.getInstance().getService(MediaEventHandlerI.class);
	private final static String HISTORY_PREFIX = "/history";
	private final static String BASE_PREFIX = "/files";

	public void vetoableCollected(EventCollection eventCollection) throws VetoException {

		try {
			List events = eventCollection.getCollection();

			Event firstEvent = (Event) events.get(0);
			String evtName = firstEvent.getMethod().getName();

			if (WebdavEvent.MOVE.getName().equals(evtName)) {
				doMove(handler, events);

			} else if (WebdavEvent.PUT.getName().equals(evtName) || (WebdavEvent.MKCOL.getName().equals(evtName))) {
				if (((Event) events.get(1)).getMethod().getName().equals(ContentEvent.STORE.getName())) {
					doUpdate(handler, events);
				} else {
					doCreate(handler, events);
				}

			} else if (WebdavEvent.DELETE.getName().equals(evtName)) {
				doDelete(handler, events);

			} else if (findEvent(MacroEvent.COPY.getName(), events) != null) {
				//if the webDav event does not exists, look for a copy macro
				doCopy(handler, events);
			}

		} catch (ContentValidationException e) {
			throw new CmsRuntimeException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new VetoException("Maintaining media table failed, so veto");
		}
	}

	public void collected(EventCollection eventCollection) {

	}

	private void doUpdate(MediaEventHandlerI handler, List events) throws IOException {
		ContentEvent event = (ContentEvent) findEvent(ContentEvent.STORE.getName(), events);
		if (!event.getUri().startsWith(HISTORY_PREFIX)) {
			Media media = convertMedia(event);
			String userId = getUserId(event);

			if (handler.isBulkload())
				doBulkLoad(event, userId);

			handler.update(media, userId);
		}
	}

	private void doCopy(MediaEventHandlerI handler, List events) {
		MacroEvent event = (MacroEvent) findEvent(MacroEvent.COPY.getName(), events);
		handler.copy(trimUri(event.getSourceURI()), trimUri(event.getTargetURI()), getUserId(event));
	}

	private void doMove(MediaEventHandlerI handler, List events) throws ContentValidationException {
		MacroEvent event = (MacroEvent) findEvent(MacroEvent.MOVE.getName(), events);
		handler.move(trimUri(event.getSourceURI()), trimUri(event.getTargetURI()), getUserId(event));
	}

	private void doDelete(MediaEventHandlerI handler, List events) {
		MacroEvent event = (MacroEvent) findEvent(MacroEvent.DELETE.getName(), events);
		handler.delete(trimUri(event.getTargetURI()), getUserId(event));
	}

	private void doCreate(MediaEventHandlerI handler, List events) throws ContentValidationException, IOException {
		ContentEvent event = (ContentEvent) findEvent(ContentEvent.CREATE.getName(), events);

		if (!event.getUri().startsWith(HISTORY_PREFIX)) {

			Media media = convertMedia(event);
			String userId = getUserId(event);

			if (handler.isBulkload())
				doBulkLoad(event, userId);

			handler.create(media, userId);
		}

	}

	private void doBulkLoad(ContentEvent event, String userId) {
		if (event.getRevisionDescriptor().getContentType().equals("application/vnd.ms-excel")
			&& trimUri(event.getUri()).toLowerCase().startsWith("/bulkloader")) {
			
			StringBuffer report = new StringBuffer();
			List successes = new ArrayList();
			Map failures = new HashMap();
			
			report.append("BulkLoader\n");
			report.append("Started load at ").append(new Date()).append("\n");
			report.append("----------------------------------------------------------------------------\n");
			report.append("\n");
			
			try {
				ProductBulkLoader.XLSBulkLoad(retrieveContent(event).streamContent(), userId, successes, failures);
			} catch(Exception e) {
				report.append("Exception: " + e);
			} finally {
				if (successes.size() > 0) {
					report.append("There were " + successes.size() + " successful insertions:\n");
					for(Iterator i = successes.iterator(); i.hasNext(); ) report.append(i.next()).append('\n');
				} else {
					report.append("No successful insertions :(.\n");
				}
				
				if (failures.size() > 0) {
					report.append("There were " + failures.size() + " failures:\n" );
					for(Iterator i = failures.entrySet().iterator(); i.hasNext(); ) {
						Map.Entry ie = (Map.Entry)i.next();
						report.append(ie.getKey()).append(' ').append(ie.getValue()).append('\n');
					}
				} else {
					report.append("No unsuccessful insertions :)!\n");
				}
			}
			
			createOutput(event.getUri(), event.getToken(), report.toString(), userId);
		}
	}

	private EventObject findEvent(String name, List events) {
		for (Iterator i = events.iterator(); i.hasNext();) {
			Event event = (Event) i.next();
			if (event.getMethod().getName().equals(name) && event.getMethod().getGroup() != WebdavEvent.GROUP) {
				return event.getEvent();
			}
		}
		return null;
	}

	private Media convertMedia(ContentEvent event) throws IOException {
		String mimeType = event.getRevisionDescriptor().getContentType();
		String uri = event.getUri();
		NodeRevisionContent content = null;
		if (Media.determineType(uri, mimeType).equals(FDContentTypes.IMAGE)) {
			content = retrieveContent(event);
		}
		return Media.convertEventToMedia(trimUri(uri), mimeType, content != null ? content.getContentBytes() : null);
	}

	private NodeRevisionContent retrieveContent(ContentEvent event) {
		ContentImpl content = (ContentImpl) event.getSource();
		try {
			NodeRevisionContent theContent = content.retrieve(event.getToken(), event.getRevisionDescriptors(), event
				.getRevisionDescriptor());
			return theContent;

		} catch (ObjectNotFoundException e1) {
			throw new CmsRuntimeException(e1);
		} catch (AccessDeniedException e1) {
			throw new CmsRuntimeException(e1);
		} catch (LinkedObjectNotFoundException e1) {
			throw new CmsRuntimeException(e1);
		} catch (RevisionNotFoundException e1) {
			throw new CmsRuntimeException(e1);
		} catch (RevisionContentNotFoundException e1) {
			throw new CmsRuntimeException(e1);
		} catch (ObjectLockedException e1) {
			throw new CmsRuntimeException(e1);
		} catch (ServiceAccessException e1) {
			throw new CmsRuntimeException(e1);
		} catch (VetoException e1) {
			throw new CmsRuntimeException(e1);
		}
	}

	private String trimUri(String uri) {
		return uri.startsWith(BASE_PREFIX) ? uri.substring(BASE_PREFIX.length()) : uri;
	}

	private String getUserId(EventObject event) {
		Principal p;
		if (event instanceof ContentEvent) {
			p = ((ContentEvent) event).getToken().getCredentialsToken().getPrincipal();
		} else {
			p = ((MacroEvent) event).getToken().getCredentialsToken().getPrincipal();
		}
		return p == null ? "SlideUnknown" : p.getName();

	}

	private void createOutput(String file_uri, SlideToken slideToken, String report, String userId) {
		try {
			boolean update = false;
			
			String uri = file_uri.substring(0, file_uri.lastIndexOf("/"))
				+ "/logs/"
				+ file_uri.substring(file_uri.lastIndexOf("/") + 1, file_uri.length())
				+ ".log";

			NamespaceAccessToken nat = Domain.accessNamespace(new SecurityToken(""), "slide");
			Structure struct = nat.getStructureHelper();
			Content content = nat.getContentHelper();

			NodeRevisionNumber lastRevision = null;
			try {
				struct.retrieve(slideToken, uri);
				lastRevision = content.retrieve(slideToken, uri).getLatestRevision();
				System.out.println("Found resource with latest revision number "
					+ (lastRevision == null ? "none" : lastRevision.toString()));
			} catch (ObjectNotFoundException e) {
				struct.create(slideToken, new SubjectNode(uri), uri);
			}

			if (lastRevision != null){
				lastRevision = new NodeRevisionNumber(lastRevision, false);
				update = true;
			} else {
				lastRevision = new NodeRevisionNumber();
			}
			// create node revision descriptor
			NodeRevisionDescriptor revisionDescriptor = new NodeRevisionDescriptor(0);


			NodeRevisionContent rc = new NodeRevisionContent();
			rc.setContent(report.toCharArray());

			revisionDescriptor.setResourceType("");
			revisionDescriptor.setProperty("revision", lastRevision.toString());
			revisionDescriptor.setContentLength(report.length());
		
            content.create(slideToken, uri, revisionDescriptor, rc);


			Media media = new Media(trimUri(uri), FDContentTypes.HTML, "text/plain", new Date());
			if (update) {
				handler.update(media, userId);
			} else {
				handler.create(media, userId);
			}

		} catch (Exception e) {
			throw new CmsRuntimeException("Couldnt write out report", e);
		}
	}

}
