package com.freshdirect.cms.ui.serviceimpl;

import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.CREATE;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.DELETE;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.ERROR_CELL;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.ERROR_COLUMN;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.ERROR_ROW;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.ERROR_SHEET;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.IGNORE_CELL;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.IGNORE_COLUMN;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.NO_CHANGE;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.UNKNOWN;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.UPDATE;
import static com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState.WARNING;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.CmsException;
import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.EnumAttributeType;
import com.freshdirect.cms.EnumCardinality;
import com.freshdirect.cms.EnumDefI;
import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.MediaServiceI;
import com.freshdirect.cms.meta.RelationshipDef;
import com.freshdirect.cms.ui.model.bulkload.BulkLoadReverseRelationship;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeaderCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadPreviewStatus;
import com.freshdirect.framework.conf.FDRegistry;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BulkLoaderXlsUploadServlet extends FileUploadServlet {
	private static final long serialVersionUID = 782726764466391767L;

	private static final Logger LOGGER = LoggerFactory.getInstance(BulkLoaderXlsUploadServlet.class);

	private static final DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	private static Set<String> contentTypeNames;

	private static MediaServiceI mediaService;

	private static synchronized void initMediaService() {
		if (mediaService == null) {
			try {
				mediaService = (MediaServiceI) FDRegistry.getInstance().getService(MediaServiceI.class);
			} catch (RuntimeException e) {
				LOGGER.warn("Failed to retrieve media service -- CMS instance not in DB mode", e);
			}
		}
	}

	private static synchronized void initContentTypeNames() {
		if (contentTypeNames == null) {
			contentTypeNames = new HashSet<String>();

			Set<ContentType> contentTypes = CmsManager.getInstance().getTypeService().getContentTypes();
			for (ContentType type : contentTypes) {
				if (type != null) {
					contentTypeNames.add(type.getName());
				}
			}
		}
	}

	@Override
	protected String handleFileItems(HttpServletRequest request, List<FileItem> list) throws ServletException, IOException {
		FileItem file = null;

		for (FileItem item : list) {
			if ("xlsFile".equals(item.getFieldName())) {
				file = item;
				break;
			}
		}

		if (file != null) {
			try {
				Map<Integer, GwtBulkLoadRow> rows = new LinkedHashMap<Integer, GwtBulkLoadRow>();

				initContentTypeNames();
				initMediaService();
				
				POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
				HSSFWorkbook workbook = new HSSFWorkbook(fs);
				HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
				HSSFDataFormatter formatter = new HSSFDataFormatter();
				formatter.setDefaultNumberFormat(new DecimalFormat());
				HSSFSheet sheet = workbook.getSheetAt(0);

				GwtBulkLoadHeader header = new GwtBulkLoadHeader();
				Map<String, Set<Integer>> columnPositions = new HashMap<String, Set<Integer>>();

				int rowCount = sheet.getLastRowNum() + 1;

				Row firstRow = sheet.getRow(0);

				if (firstRow == null) {
					throw new CmsException("Header row not found (maybe empty)");
				}

				// PHASE #1 -- Process the first cell of the header
				processIdHeaderCell(header, firstRow, columnPositions);

				// PHASE #2 -- Process the first column that is the #id column
				Map<String, Set<Integer>> previousKeys = new HashMap<String, Set<Integer>>();
				for (int i = 1; i < rowCount; i++) {
					Row _row = sheet.getRow(i);

					if (isRowEmpty(_row, i))
						continue;

					GwtBulkLoadRow row = new GwtBulkLoadRow();

					row.getCells().add(processIdCell(header, row, _row.getCell(0, Row.CREATE_NULL_AS_BLANK), formatter, evaluator));

					rows.put(i, row);
					checkDuplicateContentNodes(rows, row, i, previousKeys);
				}

				// PHASE #3 -- Process attribute headers
				Map<String, ContentNodeI> usedNodes = new HashMap<String, ContentNodeI>();
				for (GwtBulkLoadRow row : rows.values()) {
					ContentNodeI node = (ContentNodeI) row.getRowStatus().getNode();
					if (node != null) {
						usedNodes.put(node.getKey().getEncoded(), node);
					}
				}
				processAttributeHeaderCells(header, firstRow, columnPositions, usedNodes);

				// PHASE #4 -- Process attribute columns
				for (int i = 1; i < rowCount; i++) {
					Row _row = sheet.getRow(i);

					if (isRowEmpty(_row, i))
						continue;

					GwtBulkLoadRow row = rows.get(i);

					int colCount = header.getCells().size();
					for (int j = 1; j < colCount; j++) {
						row.getCells().add(
								processCell(header, row, _row.getCell(j, Row.CREATE_NULL_AS_BLANK), formatter, evaluator));
					}
				}

				// PHASE #5 -- Check cross-references
				Set<String> newKeys = new HashSet<String>(previousKeys.size());
				for (String key : previousKeys.keySet()) {
					if (previousKeys.get(key).size() == 1)
						newKeys.add(key);
				}
				checkCrossReferences(header, rows.values(), newKeys);

				request.getSession().setAttribute("previewHeader", header);
				request.getSession().setAttribute("previewRows", new ArrayList<GwtBulkLoadRow>(rows.values()));
			} catch (CmsException e) {
				LOGGER.error("error during parsing XLS file: " + file.getName(), e);
				return e.getMessage();
			} catch (IOException e) {
				LOGGER.error("error reading XLS file: " + file.getName(), e);
				return e.getMessage();
			}

			return "OK";
		} else
			return "XML file was not found in upload content";
	}

	private boolean isRowEmpty(Row row, int index) {
		if (row == null) {
			LOGGER.info("row #" + index + " is totally empty and has been ignored");
			return true;
		}

		Iterator<Cell> rowIt = row.cellIterator();
		boolean hasNonEmpty = false;
		while (rowIt.hasNext()) {
			Cell cell = rowIt.next();
			if (cell == null)
				continue;
			int cellType = cell.getCellType();
			if (cellType == Cell.CELL_TYPE_BLANK)
				continue;
			if (cellType == Cell.CELL_TYPE_FORMULA)
				cellType = cell.getCachedFormulaResultType();
			if (cellType == Cell.CELL_TYPE_STRING && cell.getStringCellValue().length() == 0)
				continue;

			hasNonEmpty = true;
			break;
		}

		if (!hasNonEmpty) {
			LOGGER.info("row #" + index + " looks empty and has been ignored");
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	private void checkCrossReferences(GwtBulkLoadHeader header, Collection<GwtBulkLoadRow> rows, Set<String> foundKeys) {
		Set<String> notFoundKeys = new HashSet<String>();
		for (GwtBulkLoadRow row : rows) {
			if (row.getRowStatus().getState().isOperation()) {
				for (GwtBulkLoadCell cell : row.getCells()) {
					if (cell.getStatus().getState().isOperation()
							&& cell.getAttributeType().equals(EnumAttributeType.RELATIONSHIP.getName())) {
						Collection<String> parsedValue = (Collection<String>) cell.getParsedValue();
						if (parsedValue == null) {
							// deleted value
							continue;
						}
						Iterator<String> keyIt = parsedValue.iterator();
						int k = 0;
						while (keyIt.hasNext()) {
							String key = keyIt.next();

							if (notFoundKeys.contains(key)) {
								cell.getStatus().setStateWithMessage(ERROR_CELL, "#" + (k + 1) + ": no such content node");
								cell.getStatus().removeIndex();
								break;
							} else if (!foundKeys.contains(key)) {
								ContentNodeI node = CmsManager.getInstance().getContentNode(ContentKey.decode(key));
								if (node == null) {
									notFoundKeys.add(key);
									cell.getStatus().setStateWithMessage(ERROR_CELL, "#" + (k + 1) + ": no such content node");
									cell.getStatus().removeIndex();
									break;
								}
							}

							k++;
						}
					}
				}
			}
		}
	}

	private void checkDuplicateContentNodes(Map<Integer, GwtBulkLoadRow> rows, GwtBulkLoadRow row, int index,
			Map<String, Set<Integer>> previousKeys) {
		if (row.getRowStatus().getState().isOperation()) {
			final String key = ((ContentNodeI) row.getRowStatus().getNode()).getKey().getEncoded();
			Set<Integer> previousBlRows = previousKeys.get(key);
			if (previousBlRows == null) {
				previousBlRows = new LinkedHashSet<Integer>();
				previousKeys.put(key, previousBlRows);
			}
			if (previousBlRows.size() == 1) {
				int existing = previousBlRows.iterator().next();
				rows.get(existing).getRowStatus().setStateWithMessage(ERROR_ROW, "duplicate content node: " + key);
			}
			previousBlRows.add(index);
			if (previousBlRows.size() > 1) {
				row.getRowStatus().setStateWithMessage(ERROR_ROW, "duplicate content node: " + key);
			}
		}
	}

	private void processIdHeaderCell(GwtBulkLoadHeader header, Row firstRow, Map<String, Set<Integer>> columnPositions)
			throws CmsException {
		Cell cell = firstRow.getCell(0, Row.CREATE_NULL_AS_BLANK);
		GwtBulkLoadPreviewStatus status = new GwtBulkLoadPreviewStatus();
		String stringValue = getHeaderCellValue(cell, status);
		if (status.getState() == IGNORE_COLUMN) {
			status.setStateWithMessage(ERROR_SHEET, "invalid #id column");
		} else if (!"#id".equals(stringValue)) {
			status.setStateWithMessage(ERROR_SHEET, "missing #id column");
		}

		String attributeName = stringValue;
		if (!columnPositions.containsKey(attributeName))
			columnPositions.put(attributeName, new LinkedHashSet<Integer>());
		columnPositions.get(attributeName).add(0);

		header.getCells().put(0, new GwtBulkLoadHeaderCell(stringValue, attributeName, status));
		header.getSheetStatus().copyFrom(status);
	}

	private void processAttributeHeaderCells(GwtBulkLoadHeader header, Row firstRow, Map<String, Set<Integer>> columnPositions,
			Map<String, ContentNodeI> usedNodes) throws CmsException {
		int colCount = firstRow.getLastCellNum();
		for (int i = 1; i < colCount; i++) {
			Cell cell = firstRow.getCell(i, Row.CREATE_NULL_AS_BLANK);
			GwtBulkLoadPreviewStatus status = new GwtBulkLoadPreviewStatus();
			String stringValue = getHeaderCellValue(cell, status);
			if (status.getState() == IGNORE_COLUMN) {
				status.setMessage("invalid attribute name");
			}

			String attributeName = stringValue;
			if (!columnPositions.containsKey(stringValue))
				columnPositions.put(stringValue, new LinkedHashSet<Integer>());

			Set<Integer> columnIndexes = columnPositions.get(attributeName);
			if (status.getState() == UNKNOWN) {
				if ("#id".equals(stringValue)) {
					if (columnIndexes.size() > 0) {
						// we have a duplicate situation
						stringValue = stringValue + " (" + (columnIndexes.size() + 1) + ")";
						if (header.getSheetStatus().getState() == UNKNOWN) {
							status.setStateWithMessage(ERROR_SHEET, "duplicate #id column");
						} else {
							status.copyFrom(header.getSheetStatus());
						}
						if (columnIndexes.size() == 1) {
							header.getCells().get(columnIndexes.iterator().next()).getColumnStatus().copyFrom(status);
						}
					} else {
						status.setStateWithMessage(ERROR_SHEET, "#id column not first");
					}
					header.getSheetStatus().copyFrom(status);
				} else {
					if (columnIndexes.size() > 0) {
						stringValue = stringValue + " (" + (columnIndexes.size() + 1) + ")";
						status.setStateWithMessage(ERROR_COLUMN, "duplicate attribute: " + stringValue);

						if (columnIndexes.size() == 1) {
							header.getCells().get(columnIndexes.iterator().next()).getColumnStatus().copyFrom(status);
						}
					}
				}
			}
			if (status.getState() == UNKNOWN) {
				int colonIndex = attributeName.indexOf(':');
				int dotIndex = attributeName.indexOf('.');
				if (colonIndex > -1 && dotIndex > -1 && colonIndex < dotIndex) {
					String typePart = attributeName.substring(0, colonIndex);
					if (!contentTypeNames.contains(typePart)) {
						status.setStateWithMessage(ERROR_COLUMN, "unknown content type: " + typePart);
					} else {
						try {
							String idPart = attributeName.substring(colonIndex + 1, dotIndex);
							ContentType contentType = ContentType.get(typePart);
							ContentKey key = ContentKey.create(contentType, idPart);
							ContentNodeI node = CmsManager.getInstance().getContentNode(key);
							if (node == null) {
								node = usedNodes.get(key.getEncoded());
							}
							if (node == null) {
								status.setStateWithMessage(ERROR_COLUMN, "no such content node");
							} else {
								String attributePart = attributeName.substring(dotIndex + 1);
								AttributeDefI attrDef = node.getDefinition().getAttributeDef(attributePart);
								if (attrDef == null) {
									status.setStateWithMessage(ERROR_COLUMN, "no such attribute: " + attributePart);
								} else if (attrDef.getAttributeType() != EnumAttributeType.RELATIONSHIP) {
									status.setStateWithMessage(ERROR_COLUMN, "'" + attributePart + "' not a relationship");
								} else {
									RelationshipDef relDef = (RelationshipDef) attrDef;
									if (relDef.getCardinality() != EnumCardinality.MANY) {
										status.setStateWithMessage(ERROR_COLUMN, "'" + attributePart + "' not a MANY relationship");
									} else if (relDef.isReadOnly()) {
										status.setStateWithMessage(ERROR_COLUMN, "'" + attributePart + "' is readonly");
									} else {
										status.setNode(node);
										status.setAttribute(attributePart);
									}
								}
							}
						} catch (InvalidContentKeyException e) {
							status.setStateWithMessage(ERROR_COLUMN, "illegal characters in content id");
						}
					}
				} else if (colonIndex > -1 || dotIndex > -1) {
					status.setStateWithMessage(ERROR_COLUMN, "not in ContentType:Id.Attribute format");
				}
			}
			columnIndexes.add(i);

			header.getCells().put(i, new GwtBulkLoadHeaderCell(stringValue, attributeName, status));
		}
	}

	private String getHeaderCellValue(Cell cell, GwtBulkLoadPreviewStatus status) throws CmsException {
		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_FORMULA)
			type = cell.getCachedFormulaResultType();
		String stringValue = "";
		switch (type) {
		case Cell.CELL_TYPE_STRING:
			stringValue = cell.getStringCellValue();
			if (stringValue.length() == 0) {
				status.setState(IGNORE_COLUMN);
			}
			break;
		case Cell.CELL_TYPE_NUMERIC:
			stringValue = Double.toString(cell.getNumericCellValue());
			status.setState(IGNORE_COLUMN);
			break;
		case Cell.CELL_TYPE_ERROR:
			stringValue = cell.getStringCellValue();
			status.setState(IGNORE_COLUMN);
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			stringValue = Boolean.toString(cell.getBooleanCellValue());
			status.setState(IGNORE_COLUMN);
			break;
		case Cell.CELL_TYPE_BLANK:
			status.setState(IGNORE_COLUMN);
			break;
		default:
			throw new CmsException("Error in header at cell " + cell.getColumnIndex() + ": unknown data type: "
					+ cell.getCellType() + ", upgrade POI");
		}
		return stringValue;
	}

	private GwtBulkLoadCell processIdCell(GwtBulkLoadHeader header, GwtBulkLoadRow row, Cell cell, HSSFDataFormatter formatter,
			HSSFFormulaEvaluator evaluator) throws CmsException {
		String attributeName = header.getCells().get(cell.getColumnIndex()).getAttributeName();
		Object parsedValue = null;
		String valueType = "_U";
		GwtBulkLoadPreviewStatus status = new GwtBulkLoadPreviewStatus();

		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_FORMULA)
			type = cell.getCachedFormulaResultType();

		String stringValue = getCellStringValue(cell, type, status);
		stringValue = processClearMark(stringValue, status);

		if (header.getSheetStatus().getState().isError()) {
			status.copyFrom(header.getSheetStatus());
		} else if (type != Cell.CELL_TYPE_STRING) {
			status.setStateWithMessage(ERROR_ROW, "invalid #id value");
		} else if (status.getState() == DELETE) {
			status.setStateWithMessage(ERROR_ROW, "cannot clear #id");
		} else if (!status.getState().isError()) {
			valueType = "_K";
			status.setNode(parseContentNodeId(type, stringValue, status));
			if (status.getState().isError()) {
				status.setMessage("error parsing #id: " + status.getMessage());
			}
			ContentNodeI node = (ContentNodeI) status.getNode();
			parsedValue = node != null ? node.getKey().getEncoded() : null;
		}
		if (status.getState().isError()) {
			status.setState(ERROR_ROW);
		}
		row.setRowStatus(status);

		String displayValue = formatCellValueHack(cell, type, formatter, evaluator);
		GwtBulkLoadCell blCell = new GwtBulkLoadCell(header.getCells().get(cell.getColumnIndex()).getColumnName(), attributeName,
				valueType, displayValue, parsedValue, status);
		return blCell;
	}

	private String getCellStringValue(Cell cell, int type, GwtBulkLoadPreviewStatus status) throws CmsException {
		String stringValue = "";
		switch (type) {
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_STRING:
			stringValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			stringValue = Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			stringValue = Double.toString(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			status.setStateWithMessage(ERROR_CELL, "XLS error: " + Byte.toString(cell.getErrorCellValue()));
			break;
		default:
			throw new CmsException("Error in row " + cell.getRow().getRowNum() + " at cell " + cell.getColumnIndex()
					+ ": unknown data type: " + cell.getCellType() + ", upgrade POI");
		}
		return stringValue;
	}

	private GwtBulkLoadCell processCell(GwtBulkLoadHeader header, GwtBulkLoadRow row, Cell cell, HSSFDataFormatter formatter,
			HSSFFormulaEvaluator evaluator) throws CmsException {
		String attributeName = header.getCells().get(cell.getColumnIndex()).getAttributeName();
		Object parsedValue = null;
		String valueType = "_U";
		GwtBulkLoadPreviewStatus status = new GwtBulkLoadPreviewStatus();

		int type = cell.getCellType();
		if (type == Cell.CELL_TYPE_FORMULA)
			type = cell.getCachedFormulaResultType();

		String stringValue = getCellStringValue(cell, type, status);

		GwtBulkLoadPreviewStatus columnStatus = header.getCells().get(cell.getColumnIndex()).getColumnStatus();
		GwtBulkLoadPreviewStatus rowStatus = row.getRowStatus();

		if (header.getSheetStatus().getState().isError()) {
			status.copyFrom(header.getSheetStatus());
		} else if (columnStatus.getState() != UNKNOWN) {
			status.copyFrom(columnStatus);
		} else if (rowStatus.getState().isError()) {
			status.copyFrom(rowStatus);
		} else if (status.getState() == UNKNOWN) {
			ContentNodeI node = (ContentNodeI) rowStatus.getNode();
			if (columnStatus.getNode() != null) {
				stringValue = processClearMark(stringValue, status);
				if (status.getState() == DELETE) {
					status.setStateWithMessage(ERROR_CELL, "cannot clear relationship change");
				} else {
					status.copyFrom(rowStatus);
					valueType = "_RR";
					BulkLoadReverseRelationship change = null;
					if (stringValue.length() == 0) {
						change = BulkLoadReverseRelationship.NO_CHANGE;
					} else if ("add".equalsIgnoreCase(stringValue) || "+".equals(stringValue)) {
						change = BulkLoadReverseRelationship.ADD;
					} else if ("remove".equalsIgnoreCase(stringValue) || "-".equals(stringValue)) {
						change = BulkLoadReverseRelationship.REMOVE;
					}
					if (change == null) {
						status.setStateWithMessage(ERROR_CELL, "cannot parse relationship change");
					} else {
						if (change == BulkLoadReverseRelationship.NO_CHANGE) {
							status.setStateWithMessage(IGNORE_CELL, "no change");
						} else {
							ContentNodeI columnNode = (ContentNodeI) columnStatus.getNode();
							String columnAttribute = columnStatus.getAttribute();
							@SuppressWarnings("unchecked")
							Collection<ContentKey> attributeValue = (Collection<ContentKey>) columnNode.getAttributeValue(columnAttribute);
							if (attributeValue == null)
								attributeValue = Collections.emptyList();

							if (change == BulkLoadReverseRelationship.ADD) {
								if (attributeValue.contains(node.getKey())) {
									status.setStateWithMessage(NO_CHANGE, "already added");
								} else {
									if (attributeValue.isEmpty()) {
										status.setStateWithMessage(CREATE, "create");
									} else {
										status.setStateWithMessage(UPDATE, "add");
									}
								}
							} else /* REMOVE */{
								if (!attributeValue.contains(node.getKey())) {
									status.setStateWithMessage(NO_CHANGE, "not added");
								} else {
									status.setStateWithMessage(DELETE, "remove");
								}
							}
						}
					}
					parsedValue = change;
				}
			} else {
				ContentTypeDefI typeDef = CmsManager.getInstance().getTypeService().getContentTypeDefinition(
						node.getKey().getType());
				AttributeDefI attributeDef = typeDef.getAttributeDef(attributeName);
				if (attributeDef == null) {
					if (stringValue.length() == 0) {
						status.setStateWithMessage(IGNORE_CELL, "no such attribute");
					} else {
						status.setStateWithMessage(WARNING, "no such attribute");
					}
				} else if (!isSupportedAttribute(attributeDef)) {
					status.setStateWithMessage(ERROR_CELL, "unsupported attribute type");
				} else {
					valueType = attributeDef.getAttributeType().getName();
					stringValue = processClearMark(stringValue, status);
					if (status.getState() == DELETE) {
						if (attributeDef.isReadOnly()) {
							status.setStateWithMessage(ERROR_CELL, "read-only attribute");
						} else {
							handleClearValue(attributeName, attributeDef, status, rowStatus);
						}
					} else {
						if (stringValue.length() == 0) {
							status.setStateWithMessage(IGNORE_CELL, "ignored");
						} else if (attributeDef.isReadOnly()) {
							status.setStateWithMessage(ERROR_CELL, "read-only attribute");
						} else {
							if (rowStatus.getState() == CREATE) {
								status.setStateWithMessage(CREATE, "create attribute");
							} else if (rowStatus.getState() == UPDATE && node.getAttributeValue(attributeName) == null) {
								status.setStateWithMessage(CREATE, "was unset");
							} else /* UPDATE */{
								status.setStateWithMessage(UPDATE, "update attribute");
							}
							EnumAttributeType attributeType = attributeDef.getAttributeType();
							if (attributeType == EnumAttributeType.BOOLEAN) {
								Boolean booleanValue = parseBooleanValue(stringValue);
								if (booleanValue == null) {
									status.setStateWithMessage(ERROR_CELL, "cannot parse boolean");
								} else {
									parsedValue = handleSimpleValue(attributeName, booleanValue, status, rowStatus);
								}
							} else if (attributeType == EnumAttributeType.INTEGER) {
								Integer integerValue = parseIntegerValue(stringValue);
								if (integerValue == null) {
									status.setStateWithMessage(ERROR_CELL, "cannot parse integer");
								} else {
									parsedValue = handleSimpleValue(attributeName, integerValue, status, rowStatus);
								}
							} else if (attributeType == EnumAttributeType.DOUBLE) {
								Double doubleValue = parseDoubleValue(stringValue);
								if (doubleValue == null) {
									status.setStateWithMessage(ERROR_CELL, "cannot parse double");
								} else {
									parsedValue = handleSimpleValue(attributeName, doubleValue, status, rowStatus);
								}
							} else if (attributeType == EnumAttributeType.STRING) {
								parsedValue = handleSimpleValue(attributeName, stringValue, status, rowStatus);
							} else if (attributeType == EnumAttributeType.DATE) {
								Date dateValue = parseDateValue(cell, type, stringValue, status);
								if (dateValue != null) {
									parsedValue = handleSimpleValue(attributeName, dateValue, status, rowStatus);
								}
							} else if (attributeType == EnumAttributeType.ENUM) {
								EnumDefI enumDef = (EnumDefI) attributeDef;
								if (enumDef.getValueType() == EnumAttributeType.INTEGER) {
									Integer integerValue = parseIntegerValue(stringValue);
									if (integerValue == null) {
										status.setStateWithMessage(ERROR_CELL, "cannot parse enum value (integer)");
									} else {
										stringValue = Integer.toString(integerValue);
									}
								}
								if (!status.getState().isError()) {
									parsedValue = handleEnumValue(attributeName, stringValue, enumDef, status, rowStatus);
								}
							} else if (attributeType == EnumAttributeType.RELATIONSHIP) {
								GwtBulkLoadPreviewStatus[] nodeStatuses = parseRelationshipValue(type, stringValue);
								RelationshipDef relationshipDef = (RelationshipDef) attributeDef;
								postCheckRelationshipValue(nodeStatuses, relationshipDef, status);
								if (!status.getState().isError()) {
									parsedValue = handleRelationshipValue(attributeName, relationshipDef, nodeStatuses, status,
											rowStatus);
								}
							} else {
								status.setStateWithMessage(ERROR_CELL, "unhandled attribute type: " + attributeType.getLabel());
							}
						}
					}
				}
			}
		}

		String displayValue = formatCellValueHack(cell, type, formatter, evaluator);
		GwtBulkLoadCell blCell = new GwtBulkLoadCell(header.getCells().get(cell.getColumnIndex()).getColumnName(), attributeName,
				valueType, displayValue, parsedValue, status);
		return blCell;
	}

	private String processClearMark(String stringValue, GwtBulkLoadPreviewStatus status) {
		if ("@@CLEAR".equals(stringValue)) {
			status.setStateWithMessage(DELETE, "unset value");
		} else if (stringValue.endsWith("@@CLEAR")) {
			int prefixLength = stringValue.length() - "@@CLEAR".length();
			boolean prefixMatch = true;
			for (int i = 0; i < prefixLength; i++) {
				if (stringValue.charAt(i) != '@') {
					prefixMatch = false;
					break;
				}
			}
			if (prefixMatch) {
				stringValue = stringValue.substring(1);
			}
		}
		return stringValue;
	}

	/**
	 * it's an ugly HACK around to use the default number formatter for non-formatted numbers
	 * <p>
	 * the default number formatter is set for the date formatter (new DecimalFormat())
	 * 
	 * @param cell
	 * @param type
	 * @param formatter
	 * @param evaluator
	 * @return
	 */
	private String formatCellValueHack(Cell cell, int type, HSSFDataFormatter formatter, HSSFFormulaEvaluator evaluator) {
		String displayValue = formatter.formatCellValue(cell, evaluator);
		if (type == Cell.CELL_TYPE_NUMERIC) {
			String formatString = cell.getCellStyle().getDataFormatString();
			if (formatString == null || formatString.length() == 0 || "GENERAL".equals(formatString))
				displayValue = formatter.getDefaultFormat(cell).format(cell.getNumericCellValue());
		}
		return displayValue;
	}

	private boolean isSupportedAttribute(AttributeDefI attributeDef) {
		return (attributeDef.getAttributeType() == EnumAttributeType.STRING
				|| attributeDef.getAttributeType() == EnumAttributeType.RELATIONSHIP
				|| attributeDef.getAttributeType() == EnumAttributeType.ENUM
				|| attributeDef.getAttributeType() == EnumAttributeType.INTEGER
				|| attributeDef.getAttributeType() == EnumAttributeType.DOUBLE
				|| attributeDef.getAttributeType() == EnumAttributeType.DATE || attributeDef.getAttributeType() == EnumAttributeType.BOOLEAN);
	}

	private String prepareIntegerString(String value) {
		int dotIndex = value.indexOf('.');
		if (dotIndex >= 0)
			value = value.substring(0, dotIndex);
		return value;
	}

	private void handleClearValue(String attributeName, AttributeDefI attributeDef, GwtBulkLoadPreviewStatus status,
			GwtBulkLoadPreviewStatus rowStatus) {
		if (attributeDef.isRequired() && !attributeDef.isInheritable()) {
			status.setStateWithMessage(ERROR_CELL, "cannot clear required attribute");
		} else {
			// parsedValue remains null as it will be cleared
			if (rowStatus.getState() == CREATE) {
				status.setStateWithMessage(WARNING, "cannot clear unset value");
			} else if (((ContentNodeI) rowStatus.getNode()).getAttributeValue(attributeName) == null) {
				status.setStateWithMessage(NO_CHANGE, "no change");
			} else {
				status.setStateWithMessage(DELETE, "clear value");
			}
		}
	}

	private Object handleSimpleValue(String attributeName, Object parsedValue, GwtBulkLoadPreviewStatus status,
			GwtBulkLoadPreviewStatus rowStatus) {
		if (rowStatus.getState() == UPDATE) {
			Object currentValue = ((ContentNodeI) rowStatus.getNode()).getAttributeValue(attributeName);
			if (parsedValue.equals(currentValue)) {
				status.setStateWithMessage(NO_CHANGE, "no change");
			}
		}
		return parsedValue;
	}

	private Object handleEnumValue(String attributeName, String value, EnumDefI definition, GwtBulkLoadPreviewStatus status,
			GwtBulkLoadPreviewStatus rowStatus) {
		boolean found = false;
		for (Object key : definition.getValues().keySet()) {
			if (value.equals(key.toString())) {
				found = true;
				break;
			}
		}
		if (!found) {
			status.setStateWithMessage(ERROR_CELL, "unknown enum value");
			return null;
		} else if (rowStatus.getState() == UPDATE) {
			Object currentValue = ((ContentNodeI) rowStatus.getNode()).getAttributeValue(attributeName);
			if (currentValue != null && value.equals(currentValue.toString())) {
				status.setStateWithMessage(NO_CHANGE, "no change");
			}
		}
		EnumAttributeType valueType = definition.getValueType();
		Object parsedValue = null;
		if (valueType == EnumAttributeType.BOOLEAN)
			parsedValue = Boolean.parseBoolean(value);
		else if (valueType == EnumAttributeType.INTEGER)
			parsedValue = Integer.parseInt(value);
		else if (valueType == EnumAttributeType.DOUBLE)
			parsedValue = Double.parseDouble(value);
		else if (valueType == EnumAttributeType.STRING)
			parsedValue = value;
		else {
			status.setStateWithMessage(ERROR_CELL, "cannot handle the value type of enum (" + valueType.getLabel() + ")");
		}

		return parsedValue;
	}

	private Object handleRelationshipValue(String attributeName, RelationshipDef relationshipDef,
			GwtBulkLoadPreviewStatus[] nodeStatuses, GwtBulkLoadPreviewStatus status, GwtBulkLoadPreviewStatus rowStatus) {
		Object parsedValue;
		List<String> keyNames = new ArrayList<String>(nodeStatuses.length);
		for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
			keyNames.add(((ContentNodeI) nodeStatus.getNode()).getKey().getEncoded());
		}
		parsedValue = keyNames;
		if (rowStatus.getState() == UPDATE) {
			if (relationshipDef.getCardinality() == EnumCardinality.ONE) {
				ContentKey currentValue = (ContentKey) ((ContentNodeI) rowStatus.getNode()).getAttributeValue(attributeName);
				if (nodeStatuses.length > 0 && ((ContentNodeI) nodeStatuses[0].getNode()).getKey().equals(currentValue)) {
					status.setStateWithMessage(NO_CHANGE, "no change");
				}
			} else /* EnumCardinality.MANY */{
				boolean same = true;
				@SuppressWarnings("unchecked")
				Collection<ContentKey> currentKeys = (Collection<ContentKey>) ((ContentNodeI) rowStatus.getNode()).getAttributeValue(attributeName);
				if (currentKeys == null)
					currentKeys = Collections.emptyList();
				if (currentKeys.size() != nodeStatuses.length) {
					same = false;
				} else {
					for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
						if (!currentKeys.contains(((ContentNodeI) nodeStatus.getNode()).getKey())) {
							same = false;
							break;
						}
					}
				}
				if (same) {
					status.setStateWithMessage(NO_CHANGE, "no change");
				}
			}
		}
		return parsedValue;
	}

	private Boolean parseBooleanValue(String value) {
		if ("0".equals(value))
			return false;
		else if ("1".equals(value))
			return true;
		/* tricky, huh? ;) */
		if ("0.0".equals(value))
			return false;
		/* tricky, huh? ;) */
		else if ("1.0".equals(value))
			return true;
		else if ("no".equalsIgnoreCase(value))
			return false;
		else if ("yes".equalsIgnoreCase(value))
			return true;
		else if ("false".equalsIgnoreCase(value))
			return false;
		else if ("true".equalsIgnoreCase(value))
			return true;
		else
			return null;
	}

	private Integer parseIntegerValue(String value) {
		value = prepareIntegerString(value);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Double parseDoubleValue(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	private Date parseDateValue(Cell cell, int type, String value, GwtBulkLoadPreviewStatus status) {
		Date dateValue = null;
		if (type == Cell.CELL_TYPE_NUMERIC)
			dateValue = cell.getDateCellValue();
		else {
			try {
				dateValue = YYYY_MM_DD.parse(value);
			} catch (ParseException e) {
				status.setStateWithMessage(ERROR_CELL, "cannot parse date (yyyy-MM-dd)");
			}
		}
		return dateValue;
	}

	private ContentNodeI parseContentNodeId(int type, String stringValue, GwtBulkLoadPreviewStatus status) {
		ContentNodeI node = null;
		int colonIndex = stringValue.indexOf(':');
		if (type != Cell.CELL_TYPE_STRING) {
			status.setStateWithMessage(ERROR_CELL, "content node id must be a string");
		} else if (colonIndex < 0) {
			status.setStateWithMessage(ERROR_CELL, "content node id must have in <type>:<id> format");
		} else {
			String typePart = stringValue.substring(0, colonIndex);
			String idPart = stringValue.substring(colonIndex + 1);
			if (!contentTypeNames.contains(typePart)) {
				status.setStateWithMessage(ERROR_CELL, "unknown content type: " + typePart);
			} else {
				ContentKey contentKey;
				try {
					contentKey = ContentKey.create(ContentType.get(typePart), idPart);
					node = CmsManager.getInstance().getContentNode(contentKey);
					if (node == null) {
						node = CmsManager.getInstance().createPrototypeContentNode(contentKey);
						status.setStateWithMessage(CREATE, "new node");
					} else {
						status.setStateWithMessage(UPDATE, "update node");
					}
				} catch (InvalidContentKeyException e) {
					status.setStateWithMessage(ERROR_CELL, "illegal characters in content id");
				}
			}
		}
		return node;
	}

	private ContentNodeI parseContentNodeExtended(int type, String stringValue, GwtBulkLoadPreviewStatus status) {
		if (stringValue.charAt(0) == '/') {
			if (mediaService == null) {
				status.setStateWithMessage(ERROR_CELL, "media service unavailable");
				return null;
			}
			try {
				ContentNodeI node = mediaService.getContentNode(stringValue);
				if (node == null)
					status.setStateWithMessage(ERROR_CELL, "no such media");
				return node;
			} catch (CmsRuntimeException e) {
				status.setStateWithMessage(ERROR_CELL, "no such media");
				return null;
			}
		} else
			return parseContentNodeId(type, stringValue, status);
	}

	private GwtBulkLoadPreviewStatus[] parseRelationshipValue(int type, String stringValue) {
		String[] tokens = stringValue.split(",");
		GwtBulkLoadPreviewStatus[] nodeStatuses = new GwtBulkLoadPreviewStatus[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			GwtBulkLoadPreviewStatus nodeStatus = new GwtBulkLoadPreviewStatus(i);
			nodeStatus.setNode(parseContentNodeExtended(type, tokens[i].trim(), nodeStatus));
			nodeStatuses[i] = nodeStatus;
		}
		return nodeStatuses;
	}

	/**
	 * performs the following checks:
	 * <ul>
	 * <li>cardinality</li>
	 * <li>allowed content types</li>
	 * <li>propagate content node id status into the attribute status (if error)</li>
	 * </ul>
	 * 
	 * @param nodeStatuses
	 * @param relationshipDef
	 * @param status
	 */
	private void postCheckRelationshipValue(GwtBulkLoadPreviewStatus[] nodeStatuses, RelationshipDef relationshipDef,
			GwtBulkLoadPreviewStatus status) {
		EnumCardinality cardinality = relationshipDef.getCardinality();
		if (cardinality == EnumCardinality.ONE) {
			for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
				nodeStatus.removeIndex();
			}
			if (nodeStatuses.length != 1) {
				status.setStateWithMessage(ERROR_CELL, "single content node id expected");
			} else {
				for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
					if (nodeStatus.getState().isError()) {
						status.copyFrom(nodeStatus);
						break;
					}
				}
			}
		} else {
			Set<ContentType> contentTypes = relationshipDef.getContentTypes();
			Set<ContentKey> previousKeys = new HashSet<ContentKey>(nodeStatuses.length);
			for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
				if (nodeStatus.getState().isError()) {
					status.copyFrom(nodeStatus);
					break;
				} else {
					ContentNodeI node = (ContentNodeI) nodeStatus.getNode();
					if (!contentTypes.contains(node.getKey().getType())) {
						nodeStatus.setStateWithMessage(ERROR_CELL, "illegal content type");
						status.copyFrom(nodeStatus);
						break;
					} else if (previousKeys.contains(node.getKey())) {
						nodeStatus.setStateWithMessage(ERROR_CELL, "duplicate content node: " + node.getKey().getEncoded());
						status.copyFrom(nodeStatus);
					} else {
						previousKeys.add(node.getKey());
					}
				}
			}
		}
	}
}
