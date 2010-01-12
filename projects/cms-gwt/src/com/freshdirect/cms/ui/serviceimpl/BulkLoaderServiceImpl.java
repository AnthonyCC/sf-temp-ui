package com.freshdirect.cms.ui.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState;
import com.freshdirect.cms.ui.model.bulkload.BulkLoadReverseRelationship;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.freshdirect.cms.ui.service.BulkLoaderService;
import com.freshdirect.cms.ui.service.ServerException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class BulkLoaderServiceImpl extends RemoteServiceServlet implements BulkLoaderService {
	private static final long serialVersionUID = 3988173646720452184L;

	@Override
	public GwtBulkLoadHeader getPreviewHeader() {
		GwtBulkLoadHeader header = (GwtBulkLoadHeader) getThreadLocalRequest().getSession().getAttribute("previewHeader");

		if (header == null)
			throw new FDRuntimeException("Bulk load preview header have not been loaded correctly");

		return header;
	}

	@Override
	public PagingLoadResult<GwtBulkLoadRow> getPreviewRows(PagingLoadConfig config) {
		List<GwtBulkLoadRow> rows = getBulkLoadRows();

		List<GwtBulkLoadRow> sublist = new ArrayList<GwtBulkLoadRow>();
		int start = config.getOffset();
		int limit = rows.size();
		if (config.getLimit() > 0) {
			limit = Math.min(start + config.getLimit(), limit);
		}
		for (int i = config.getOffset(); i < limit; i++) {
			sublist.add(rows.get(i));
		}
		return new BasePagingLoadResult<GwtBulkLoadRow>(sublist, config.getOffset(), rows.size());
	}

	@Override
	public boolean hasAnyError() {
		List<GwtBulkLoadRow> rows = getBulkLoadRows();

		for (GwtBulkLoadRow row : rows) {
			for (GwtBulkLoadCell cell : row.getCells()) {
				if (cell.getStatus().getState().isError())
					return true;
			}
		}

		return false;
	}

	private List<GwtBulkLoadRow> getBulkLoadRows() {
		@SuppressWarnings("unchecked")
		List<GwtBulkLoadRow> rows = (List<GwtBulkLoadRow>) getThreadLocalRequest().getSession().getAttribute("previewRows");

		if (rows == null)
			throw new FDRuntimeException("Bulk load preview rows have not been loaded correctly");
		return rows;
	}

	@Override
	public GwtSaveResponse save() throws ServerException {
		HttpServletRequest request = getThreadLocalRequest();
		GwtUser user = ContentServiceImpl.getUserFromRequest(request);
		List<GwtBulkLoadRow> rows = getBulkLoadRows();

		try {
			Map<ContentKey, ContentNodeI> nodes = new HashMap<ContentKey, ContentNodeI>();
			Map<String, ContentNodeI> rrNodes = new HashMap<String, ContentNodeI>();
			Map<String, String> rrAttributes = new HashMap<String, String>();
			for (GwtBulkLoadRow row : rows) {
				GwtBulkLoadCell cell = row.getCells().get(0);
				if ("_K".equals(cell.getAttributeType())) {
					ContentNodeI node = null;
					if (cell.getStatus().getState() == BulkLoadPreviewState.CREATE) {
						ContentKey key = ContentKey.decode(cell.getParsedValue().toString());
						node = CmsManager.getInstance().createPrototypeContentNode(key);
						nodes.put(node.getKey(), node);
					} else if (cell.getStatus().getState() == BulkLoadPreviewState.UPDATE) {
						ContentKey key = ContentKey.decode(cell.getParsedValue().toString());
						node = CmsManager.getInstance().getContentNode(key).copy();
						nodes.put(node.getKey(), node);
					}
					row.getRowStatus().setNode(node);
				}
			}
			for (GwtBulkLoadRow row : rows) {
				ContentNodeI node = (ContentNodeI) row.getRowStatus().getNode();
				if (node != null) {
					for (int i = 1; i < row.getCells().size(); i++) {
						GwtBulkLoadCell cell = row.getCells().get(i);
						if (cell.getStatus().getState().isOperation()) {
							if ("R".equals(cell.getAttributeType())) {
								if (node.getDefinition().getAttributeDef(cell.getAttributeName()).getCardinality() == EnumCardinality.ONE) {
									@SuppressWarnings("unchecked")
									ContentKey key = cell.getParsedValue() != null
											? ContentKey.decode(((Collection<String>) cell.getParsedValue()).iterator().next())
											: null;
									node.setAttributeValue(cell.getAttributeName(), key);
								} else /* EnumCardinality.MANY */{
									@SuppressWarnings("unchecked")
									Collection<String> keys = (Collection<String>) cell.getParsedValue();
									if (keys == null) {
										node.setAttributeValue(cell.getAttributeName(), null);
									} else {
										ArrayList<ContentKey> nodeKeys = new ArrayList<ContentKey>(keys.size());
										for (String key : keys) {
											ContentKey nodeKey = ContentKey.decode(key);
											nodeKeys.add(nodeKey);
										}
										node.setAttributeValue(cell.getAttributeName(), nodeKeys);
									}
								}
							} else if ("_RR".equals(cell.getAttributeType())) {
								String attributeName = cell.getAttributeName();
								ContentNodeI rrNode = rrNodes.get(attributeName);
								String rrAttribute = rrAttributes.get(attributeName);
								if (rrNode == null) {
									int dotIndex = attributeName.indexOf('.');
									String keyPart = attributeName.substring(0, dotIndex);
									ContentKey key = ContentKey.decode(keyPart);
									rrNode = nodes.get(key);
									if (rrNode == null) {
										rrNode = CmsManager.getInstance().getContentNode(key).copy();
									}
									rrNodes.put(attributeName, rrNode);
									nodes.put(rrNode.getKey(), rrNode);

									rrAttribute = attributeName.substring(dotIndex + 1);
									rrAttributes.put(attributeName, rrAttribute);
								}
								@SuppressWarnings("unchecked")
								Collection<ContentKey> value = (Collection<ContentKey>) rrNode.getAttributeValue(rrAttribute);
								if (value == null) {
									value = new ArrayList<ContentKey>();
								}

								BulkLoadReverseRelationship change = (BulkLoadReverseRelationship) cell.getParsedValue();
								if (change == BulkLoadReverseRelationship.ADD) {
									if (!value.contains(node.getKey())) {
										value.add(node.getKey());
									}
								} else /* BulkLoadReverseRelationship.REMOVE */{
									value.remove(node.getKey());
								}

								if (!value.isEmpty()) {
									rrNode.setAttributeValue(rrAttribute, value);
								} else {
									rrNode.setAttributeValue(rrAttribute, null);
								}
								Object o = rrNode.getAttributeValue(rrAttribute);
								System.out.println(o);
							} else {
								node.setAttributeValue(cell.getAttributeName(), cell.getParsedValue());
							}
						}
					}
				}
			}

			GwtSaveResponse response = ContentServiceImpl.saveNodes(user, new ArrayList<ContentNodeI>(nodes.values()));
			return response;
		} catch (RuntimeException e) {
			throw new ServerException("error saving changes", e);
		}
	}
}
