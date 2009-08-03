/**
 * @author ekracoff
 * Created on Feb 1, 2005*/

package com.freshdirect.cms.listeners;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.CmsRequest;
import com.freshdirect.cms.application.CmsUser;
import com.freshdirect.cms.application.service.DbService;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.changecontrol.ContentNodeChange;
import com.freshdirect.cms.changecontrol.EnumContentNodeChangeType;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.framework.conf.FDRegistry;

public class MediaEventHandler extends DbService implements MediaEventHandlerI {
	private static final Logger LOGGER = Logger.getLogger(MediaEventHandler.class);

	private MediaAssociator associator = new MediaAssociator();
	{
		associator.addRule(null, "c", FDContentTypes.PRODUCT, "PROD_IMAGE");
		associator.addRule(null, "cx", FDContentTypes.PRODUCT, "PROD_IMAGE_CONFIRM");
		associator.addRule(null, "p", FDContentTypes.PRODUCT, "PROD_IMAGE_DETAIL");
		associator.addRule(null, "f", FDContentTypes.PRODUCT, "PROD_IMAGE_FEATURE");
		associator.addRule(null, "cr", FDContentTypes.PRODUCT, "PROD_IMAGE_ROLLOVER");
		associator.addRule(null, "z", FDContentTypes.PRODUCT, "PROD_IMAGE_ZOOM");
		associator.addRule(null, "desc", FDContentTypes.PRODUCT, "PROD_DESCR");
		
		associator.addRule("bd", "l", FDContentTypes.BRAND, "BRAND_LOGO");
		associator.addRule("bd", "m", FDContentTypes.BRAND, "BRAND_LOGO_MEDIUM");
		associator.addRule("bd", "s", FDContentTypes.BRAND, "BRAND_LOGO_SMALL");
		
		associator.addRule("rec", "c", FDContentTypes.RECIPE, "titleImage");
		associator.addRule("rec", "p", FDContentTypes.RECIPE, "photo");
		associator.addRule("rec", "hn", FDContentTypes.RECIPE, "description");
		associator.addRule("rec", "in", FDContentTypes.RECIPE, "ingredientsMedia");
		associator.addRule("rec", "pr", FDContentTypes.RECIPE, "preparationMedia");
		associator.addRule("rec", "cr", FDContentTypes.RECIPE, "copyrightMedia");
		
		associator.addRule("cbk", "c", FDContentTypes.RECIPE_SOURCE, "image");
		associator.addRule("cbk", "p", FDContentTypes.RECIPE_SOURCE, "zoomImage");
		associator.addRule("cbk", "cl", FDContentTypes.RECIPE_SOURCE, "leftContent");
		associator.addRule("cbk", "ct", FDContentTypes.RECIPE_SOURCE, "topContent");
		associator.addRule("cbk", "cb", FDContentTypes.RECIPE_SOURCE, "bottomContent");
	}
	
	private boolean autoAssociation = true;

	private MediaDao dao = new MediaDao();
	private ChangeLogServiceI changeLogger = (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);

	public void setAutoAssociation(boolean autoAssociation) {
		this.autoAssociation = autoAssociation;
	}

	public boolean isAutoAssociation() {
		return autoAssociation;
	}
	
	private boolean bulkload = true;
	
	public void setBulkload(boolean bulkload) {
		this.bulkload = bulkload;
	}
	
	public boolean isBulkload() {
		return bulkload;
	}

	public void create(final Media media, final String userId) {
    	LOGGER.debug("-->create()");
		new DbTxCommand() {
			protected void run(Connection conn) throws SQLException {
				Media m;
				try {
					m = dao.insert(conn, media);
					logChange(m.getContentKey(), EnumContentNodeChangeType.ADD, "initial checkin", userId);
					associate(m, userId);
				} catch (SQLException e) {
					if (e.getMessage().indexOf("unique constraint") >= 0) {
						LOGGER.debug("create failed for '" + media.getUri() + "', trying update");
						dao.update(conn, media);
						m = dao.lookupByUri(conn, media.getUri());
						logChange(m.getContentKey(), EnumContentNodeChangeType.MODIFY, "updated " + media.getUri(), userId);
					} else {
						throw e;
					}
				}
			}
		}.execute();
    	LOGGER.debug("<--create()");
	}

	public void move(final String sourceUri, final String targetUri, final String userId) {
    	LOGGER.debug("-->move()");
		new DbTxCommand() {
			protected void run(Connection conn) throws SQLException {
				Media media = dao.lookupByUri(conn, sourceUri);
				dao.move(conn, sourceUri, targetUri);
				if (media != null)
					logChange(media.getContentKey(), EnumContentNodeChangeType.MODIFY,
							"moved " + sourceUri + " to " + targetUri, userId);
			}
		}.execute();
    	LOGGER.debug("<--move()");
	}

	public void copy(final String sourceUri, final String targetUri, final String userId) {
    	LOGGER.debug("-->copy()");
		new DbTxCommand() {
			protected void run(Connection conn) throws SQLException {
				dao.copy(conn, sourceUri, targetUri);
				Media media = dao.lookupByUri(conn, sourceUri);
				logChange(media.getContentKey(), EnumContentNodeChangeType.ADD, "copied " + sourceUri + " to " + targetUri, userId);
			}
		}.execute();
    	LOGGER.debug("<--copy()");
	}

	public void delete(final String sourceUri, final String userId) {
    	LOGGER.debug("-->delete()");
		new DbTxCommand() {
			protected void run(Connection conn) throws SQLException {
				Media media = dao.lookupByUri(conn, sourceUri);
				dao.delete(conn, sourceUri);
				if (media != null) logChange(media.getContentKey(), EnumContentNodeChangeType.DELETE, "deleted " + sourceUri, userId);
			}
		}.execute();
    	LOGGER.debug("<--delete()");
	}

	public void update(final Media media, final String userId) {
    	LOGGER.debug("-->update()");
		new DbTxCommand() {
			protected void run(Connection conn) throws SQLException {
				dao.update(conn, media);
				Media m = dao.lookupByUri(conn, media.getUri());
				logChange(m.getContentKey(), EnumContentNodeChangeType.MODIFY, "updated " + media.getUri(), userId);
			}
		}.execute();
    	LOGGER.debug("<--update()");
	}

	private void associate(Media media, String userId) {
    	LOGGER.debug("-->associate()");
		if (!isAutoAssociation()) {
	    	LOGGER.debug("<--associate() auto association disabled");
			return;
		}
		
		MediaAssociation assoc = associator.getAssociation(media.getUri());
		if (assoc == null) {
	    	LOGGER.debug("<--associate() not an association");
			return;
		}
		
		ContentNodeI assocNode = assoc.getContentKey().lookupContentNode();
		if (assocNode == null) {
	    	LOGGER.debug("<--associate() no content node found");
			return;
		}
		assocNode = assocNode.copy();

		AttributeI attrib = assocNode.getAttribute(assoc.getAttributeName());
		if (attrib == null) {
	    	LOGGER.debug("<--associate() no attribute found");
			return;
		}
		if (!(attrib instanceof RelationshipI)) {
	    	LOGGER.debug("<--associate() attribute not a relationship");
			return;
		}
		ContentNodeUtil.addRelationshipKey((RelationshipI) attrib, media.getContentKey());
		
		CmsRequest req = new CmsRequest(new CmsUser(userId));
		req.addNode(assocNode);
    	LOGGER.debug("---associate()-->CmsManager.handle() start");
		CmsManager.getInstance().handle(req);
    	LOGGER.debug("<--associate()");
	}

	private void logChange(ContentKey contentKey, EnumContentNodeChangeType changeType, String note, final String userId) {
    	LOGGER.debug("-->logChange()");
		ContentNodeChange nodeChange = new ContentNodeChange();
		nodeChange.setContentKey(contentKey);
		nodeChange.setChangeType(changeType);

		ChangeSet changeSet = new ChangeSet();
		changeSet.setUserId(userId);
		changeSet.setNote(note);
		changeSet.setModifiedDate(new Date());
		changeSet.addChange(nodeChange);

		changeLogger.storeChangeSet(changeSet);
    	LOGGER.debug("<--logChange()");
	}

}