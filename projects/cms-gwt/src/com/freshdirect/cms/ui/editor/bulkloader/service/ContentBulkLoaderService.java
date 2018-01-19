package com.freshdirect.cms.ui.editor.bulkloader.service;

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
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import javax.annotation.PostConstruct;

import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.freshdirect.cms.core.domain.Attribute;
import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentNodeComparatorUtil;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.cms.core.domain.RelationshipCardinality;
import com.freshdirect.cms.core.domain.Scalar;
import com.freshdirect.cms.core.service.ContentTypeInfoService;
import com.freshdirect.cms.core.service.ContextualContentProvider;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.ui.editor.bulkloader.BulkUploadResult;
import com.freshdirect.cms.ui.editor.permission.ContentChangeSource;
import com.freshdirect.cms.ui.editor.service.ContentUpdateService;
import com.freshdirect.cms.ui.model.GwtSaveResponse;
import com.freshdirect.cms.ui.model.GwtUser;
import com.freshdirect.cms.ui.model.bulkload.BulkLoadPreviewState;
import com.freshdirect.cms.ui.model.bulkload.BulkLoadReverseRelationship;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeader;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadHeaderCell;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadPreviewStatus;
import com.freshdirect.cms.ui.model.bulkload.GwtBulkLoadRow;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

@Service
public class ContentBulkLoaderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentBulkLoaderService.class);

    private static final DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    private static final DateFormat HH_MM = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    @Autowired
    private ContextualContentProvider contentProviderService;

    @Autowired
    private ContentTypeInfoService contentTypeInfoService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private ContentUpdateService contentUpdateService;

    private Set<String> contentTypeNames;

    @PostConstruct
    private void initContentTypeNames() {
        contentTypeNames = new HashSet<String>();

        Set<ContentType> contentTypes = contentTypeInfoService.getContentTypes();
        for (ContentType type : contentTypes) {
            if (type != null) {
                contentTypeNames.add(type.name());
            }
        }
    }

    public BulkUploadResult doUpload(InputStream inputStream) {
        try {
            Map<Integer, GwtBulkLoadRow> rows = new LinkedHashMap<Integer, GwtBulkLoadRow>();

            initContentTypeNames();

            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
            HSSFDataFormatter formatter = new HSSFDataFormatter();
            formatter.setDefaultNumberFormat(new DecimalFormat());
            HSSFSheet sheet = workbook.getSheetAt(0);

            GwtBulkLoadHeader header = new GwtBulkLoadHeader();
            Map<String, Set<Integer>> columnPositions = new HashMap<String, Set<Integer>>();

            final Row firstRow = sheet.getRow(0);

            Assert.notNull(firstRow, "Header row not found (maybe empty)");

            // PHASE #1 -- Process the first cell of the header
            processIdHeaderCell(header, firstRow, columnPositions);

            // PHASE #2 -- Process the first column that is the #id column
            Map<String, Set<Integer>> previousKeys = processIdColumns(sheet, header, rows, formatter, evaluator);

            // PHASE #3 -- Process attribute headers
            processAttributeHeaderCells(header, firstRow, columnPositions, rows);

            // PHASE #4 -- Process attribute columns
            procesAttributeColumns(sheet, header, rows, formatter, evaluator);

            // PHASE #5 -- Check cross-references
            checkCrossReferences(header, rows.values(), previousKeys);

            return new BulkUploadResult(header, new ArrayList<GwtBulkLoadRow>(rows.values()));
        } catch (RuntimeException e) {
            LOGGER.error("error during parsing XLS file", e);
            return new BulkUploadResult(e);
        } catch (IOException e) {
            LOGGER.error("error reading XLS file", e);
            return new BulkUploadResult(e);
        }
    }

    private boolean isRowEmpty(Row row, int index) {
        if (row == null) {
            //LOGGER.info("row #" + index + " is totally empty and has been ignored");
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
            //LOGGER.info("row #" + index + " looks empty and has been ignored");
            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void checkCrossReferences(GwtBulkLoadHeader header, Collection<GwtBulkLoadRow> rows, Map<String, Set<Integer>> previousKeys) {
        Set<String> foundKeys = new HashSet<String>(previousKeys.size());
        for (String key : previousKeys.keySet()) {
            if (previousKeys.get(key).size() == 1)
                foundKeys.add(key);
        }

        Set<String> notFoundKeys = new HashSet<String>();
        for (GwtBulkLoadRow row : rows) {
            if (row.getRowStatus().getState().isOperation()) {
                for (GwtBulkLoadCell cell : row.getCells()) {
                    if (cell.getStatus().getState().isOperation() && cell.getAttributeType().equals("R")) { // R means Relationship
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
                                if (!contentProviderService.containsContentKey(ContentKeyFactory.get(key))) {
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

    private void checkDuplicateContentNodes(Map<Integer, GwtBulkLoadRow> rows, GwtBulkLoadRow row, int index, Map<String, Set<Integer>> previousKeys) {
        if (row.getRowStatus().getState().isOperation()) {
            final String key = ((Map<ContentKey, Map<Attribute, Object>>) row.getRowStatus().getNode()).keySet().iterator().next().toString();
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

    private void processIdHeaderCell(GwtBulkLoadHeader header, Row firstRow, Map<String, Set<Integer>> columnPositions) {
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

    public Map<String, Set<Integer>> processIdColumns(HSSFSheet sheet, GwtBulkLoadHeader header, Map<Integer, GwtBulkLoadRow> rows, HSSFDataFormatter formatter, HSSFFormulaEvaluator evaluator) {
        Map<String, Set<Integer>> previousKeys = new HashMap<String, Set<Integer>>();

        final int rowCount = sheet.getLastRowNum() + 1;

        for (int i = 1; i < rowCount; i++) {
            Row _row = sheet.getRow(i);

            if (isRowEmpty(_row, i))
                continue;

            GwtBulkLoadRow row = new GwtBulkLoadRow();

            row.getCells().add(processIdCell(header, row, _row.getCell(0, Row.CREATE_NULL_AS_BLANK), formatter, evaluator));

            rows.put(i, row);
            checkDuplicateContentNodes(rows, row, i, previousKeys);
        }
        return previousKeys;
    }


    private Map<ContentKey, Map<Attribute, Object>> collectUsedNodes(Map<Integer, GwtBulkLoadRow> rows) {
        Map<ContentKey, Map<Attribute, Object>> usedNodes = new HashMap<ContentKey, Map<Attribute, Object>>();
        for (GwtBulkLoadRow row : rows.values()) {
            Map<ContentKey, Map<Attribute, Object>> node = (Map<ContentKey, Map<Attribute, Object>>) row.getRowStatus().getNode();
            if (node != null && !node.isEmpty()) {
                ContentKey nodeKey = node.keySet().iterator().next();
                if (node != null) {
                    usedNodes.put(nodeKey, node.get(nodeKey));
                }
            }
        }
        return usedNodes;
    }

    private void processAttributeHeaderCells(GwtBulkLoadHeader header, Row firstRow, Map<String, Set<Integer>> columnPositions, Map<Integer, GwtBulkLoadRow> rows) {
        Map<ContentKey, Map<Attribute, Object>> usedNodes = collectUsedNodes(rows);

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
                            ContentType contentType = ContentType.valueOf(typePart);
                            ContentKey key = ContentKeyFactory.get(contentType, idPart);
                            Map<Attribute, Object> node = contentProviderService.getAllAttributesForContentKey(key);
                            if (node == null || node.isEmpty()) {
                                node = usedNodes.get(key);
                            }
                            if (node == null || node.isEmpty()) {
                                status.setStateWithMessage(ERROR_COLUMN, "no such content node");
                            } else {
                                String attributePart = attributeName.substring(dotIndex + 1);
                                Set<Attribute> attributesOfType = contentTypeInfoService.selectAttributes(contentType);
                                Attribute attrDef = null;
                                for (Attribute attribute : attributesOfType) {
                                    if (attribute.getName().equals(attributePart)) {
                                        attrDef = attribute;
                                        break;
                                    }
                                }
                                if (attrDef == null) {
                                    status.setStateWithMessage(ERROR_COLUMN, "no such attribute: " + attributePart);
                                } else if (!(attrDef instanceof Relationship)) {
                                    status.setStateWithMessage(ERROR_COLUMN, "'" + attributePart + "' not a relationship");
                                } else {
                                    Relationship relDef = (Relationship) attrDef;
                                    if (relDef.getCardinality() != RelationshipCardinality.MANY) {
                                        status.setStateWithMessage(ERROR_COLUMN, "'" + attributePart + "' not a MANY relationship");
                                        // } else if (relDef.isReadOnly()) {
                                        // status.setStateWithMessage(ERROR_COLUMN, "'" + attributePart + "' is readonly");
                                    } else {
                                        Map<ContentKey, Map<Attribute, Object>> nodeWithKey = new HashMap<ContentKey, Map<Attribute, Object>>();
                                        nodeWithKey.put(key, node);
                                        status.setContentKey(key);
                                        status.setNode(nodeWithKey);
                                        status.setAttribute(attributePart);
                                    }
                                }
                            }
                        } catch (RuntimeException e) { // TODO: this was InvalidKeyException. Check what the hell is that
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

    private void procesAttributeColumns(HSSFSheet sheet, GwtBulkLoadHeader header, Map<Integer, GwtBulkLoadRow> rows, HSSFDataFormatter formatter, HSSFFormulaEvaluator evaluator) {
        final int rowCount = sheet.getLastRowNum() + 1;

        for (int i = 1; i < rowCount; i++) {
            Row _row = sheet.getRow(i);

            if (isRowEmpty(_row, i))
                continue;

            GwtBulkLoadRow row = rows.get(i);

            int colCount = header.getCells().size();
            for (int j = 1; j < colCount; j++) {
                row.getCells().add(processCell(header, row, _row.getCell(j, Row.CREATE_NULL_AS_BLANK), formatter, evaluator));
            }
        }

    }

    // CELL LEVEL
    private String getHeaderCellValue(Cell cell, GwtBulkLoadPreviewStatus status) {
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
                throw new RuntimeException("Error in header at cell " + cell.getColumnIndex() + ": unknown data type: " + cell.getCellType() + ", upgrade POI");
        }
        return stringValue;
    }

    private GwtBulkLoadCell processIdCell(GwtBulkLoadHeader header, GwtBulkLoadRow row, Cell cell, HSSFDataFormatter formatter, HSSFFormulaEvaluator evaluator) {
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
            Map<ContentKey, Map<Attribute, Object>> node = (Map<ContentKey, Map<Attribute, Object>>) status.getNode();
            parsedValue = node != null ? node.keySet().iterator().next().toString() : null;
        }
        if (status.getState().isError()) {
            status.setState(ERROR_ROW);
        }
        row.setRowStatus(status);

        String displayValue = formatCellValueHack(cell, type, formatter, evaluator);
        GwtBulkLoadCell blCell = new GwtBulkLoadCell(header.getCells().get(cell.getColumnIndex()).getColumnName(), attributeName, valueType, displayValue, parsedValue, status);
        return blCell;
    }

    private String getCellStringValue(Cell cell, int type, GwtBulkLoadPreviewStatus status) {
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
                throw new RuntimeException(
                        "Error in row " + cell.getRow().getRowNum() + " at cell " + cell.getColumnIndex() + ": unknown data type: " + cell.getCellType() + ", upgrade POI");
        }
        return stringValue;
    }

    private GwtBulkLoadCell processCell(GwtBulkLoadHeader header, GwtBulkLoadRow row, Cell cell, HSSFDataFormatter formatter, HSSFFormulaEvaluator evaluator) {
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
            final Map<ContentKey, Map<Attribute, Object>> node = (Map<ContentKey, Map<Attribute, Object>>) rowStatus.getNode();
            final Map<ContentKey, Map<Attribute, Object>> reverseNode = (Map<ContentKey, Map<Attribute, Object>>) columnStatus.getNode();

            if (reverseNode != null) {
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
                            Map<ContentKey, Map<Attribute, Object>> columnNode = reverseNode;
                            Map<Attribute, Object> columnNodeAttributes = columnNode.get(columnNode.keySet().iterator().next());
                            String columnAttribute = columnStatus.getAttribute();
                            Collection<ContentKey> attributeValue = null;
                            for (Attribute attribute : columnNodeAttributes.keySet()) {
                                if (attribute.getName().equals(columnAttribute)) {
                                    attributeValue = (Collection<ContentKey>) columnNodeAttributes.get(attribute);
                                    break;
                                }
                            }
                            if (attributeValue == null)
                                attributeValue = Collections.emptyList();

                            if (change == BulkLoadReverseRelationship.ADD) {
                                if (attributeValue.contains(node.keySet().iterator().next())) {
                                    status.setStateWithMessage(NO_CHANGE, "already added");
                                } else {
                                    if (attributeValue.isEmpty()) {
                                        status.setStateWithMessage(CREATE, "create");
                                    } else {
                                        status.setStateWithMessage(UPDATE, "add");
                                    }
                                }
                            } else /* REMOVE */ {
                                if (!attributeValue.contains(node.keySet().iterator().next())) {
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
                ContentType typeDef = node.keySet().iterator().next().type;
                Attribute attributeDef = contentTypeInfoService.findAttributeByName(typeDef, attributeName).orNull();
                if (attributeDef == null) {
                    if (stringValue.length() == 0) {
                        status.setStateWithMessage(IGNORE_CELL, "no such attribute");
                    } else {
                        status.setStateWithMessage(WARNING, "no such attribute");
                    }
                } else {

                    Optional<String> optionalValueType = determineCellValueType(attributeDef, status);
                    if (optionalValueType.isPresent()) {
                        valueType = optionalValueType.get();

                        stringValue = processClearMark(stringValue, status);
                        if (status.getState() == DELETE) {
                            if (attributeDef.isReadOnly()) {
                                status.setStateWithMessage(ERROR_CELL, "read-only attribute");
                            } else {
                                handleClearValue(attributeName, attributeDef, status, rowStatus);
                            }
                        } else {
                            if (Strings.isNullOrEmpty(stringValue)) {
                                status.setStateWithMessage(IGNORE_CELL, "ignored");
                            } else if (attributeDef.isReadOnly()) {
                                status.setStateWithMessage(ERROR_CELL, "read-only attribute");
                            } else {
                                Object attributeValue = null;
                                Map<Attribute, Object> nodeAttributes = node.get(node.keySet().iterator().next());
                                for (Attribute attribute : nodeAttributes.keySet()) {
                                    if (attribute.getName().equals(attributeName)) {
                                        attributeValue = nodeAttributes.get(attribute);
                                        break;
                                    }
                                }
                                if (rowStatus.getState() == CREATE) {
                                    status.setStateWithMessage(CREATE, "create attribute");
                                } else if (rowStatus.getState() == UPDATE && attributeValue == null) {
                                    status.setStateWithMessage(CREATE, "was unset");
                                } else /* UPDATE */ {
                                    status.setStateWithMessage(UPDATE, "update attribute");
                                }

                                if (attributeDef instanceof Scalar && ((Scalar) attributeDef).isEnumerated()) {
                                    parsedValue = handleEnumValue(attributeName, stringValue, (Scalar) attributeDef, status, rowStatus);
                                } else if ("S".equals(valueType)) {
                                    parsedValue = handleSimpleValue(attributeName, stringValue, status, rowStatus);
                                } else if ("I".equals(valueType)) {
                                    Integer integerValue = parseIntegerValue(stringValue);
                                    if (integerValue == null) {
                                        status.setStateWithMessage(ERROR_CELL, "cannot parse integer");
                                    } else {
                                        parsedValue = handleSimpleValue(attributeName, integerValue, status, rowStatus);
                                    }

                                } else if ("B".equals(valueType)) {
                                    Boolean booleanValue = parseBooleanValue(stringValue);
                                    if (booleanValue == null) {
                                        status.setStateWithMessage(ERROR_CELL, "cannot parse boolean");
                                    } else {
                                        parsedValue = handleSimpleValue(attributeName, booleanValue, status, rowStatus);
                                    }

                                } else if ("DT".equals(valueType)) {
                                    Date dateValue = parseDateValue(cell, type, stringValue, status);
                                    if (dateValue != null) {
                                        parsedValue = handleSimpleValue(attributeName, dateValue, status, rowStatus);
                                    }

                                } else if ("D".equals(valueType)) {
                                    Double doubleValue = parseDoubleValue(stringValue);
                                    if (doubleValue == null) {
                                        status.setStateWithMessage(ERROR_CELL, "cannot parse double");
                                    } else {
                                        parsedValue = handleSimpleValue(attributeName, doubleValue, status, rowStatus);
                                    }

                                } else if ("R".equals(valueType)) {
                                    GwtBulkLoadPreviewStatus[] nodeStatuses = parseRelationshipValue(type, stringValue);
                                    Relationship relationshipDef = (Relationship) attributeDef;
                                    postCheckRelationshipValue(nodeStatuses, relationshipDef, status);
                                    if (!status.getState().isError()) {
                                        parsedValue = handleRelationshipValue(attributeName, relationshipDef, nodeStatuses, status, rowStatus);
                                    }
                                } else {
                                    status.setStateWithMessage(ERROR_CELL, "unhandled attribute type: " + attributeDef.getClass().getSimpleName());
                                }
                            }
                        }

                    }

                }
            }
        }

        String displayValue = formatCellValueHack(cell, type, formatter, evaluator);
        GwtBulkLoadCell blCell = new GwtBulkLoadCell(header.getCells().get(cell.getColumnIndex()).getColumnName(), attributeName, valueType, displayValue, parsedValue, status);
        return blCell;
    }

    // -- save uploaded content --

    public GwtSaveResponse save(GwtUser author, List<GwtBulkLoadRow> rows) {

        LinkedHashMap<ContentKey, Map<Attribute, Object>> changedNodes = prepareContentNodes(rows);

        // inverse relationship to owner node mapping (ie. 'Category:lp_test.products' -> category object )
        Map<String, ContentKey> rrNodes = new HashMap<String, ContentKey>();
        // inverse relationship to attribute mapping (ie 'Category:lp_test.products' -> 'products' )
        Map<String, Relationship> rrAttributes = new HashMap<String, Relationship>();
        for (GwtBulkLoadRow row : rows) {
            final ContentKey contentKey = (ContentKey) row.getRowStatus().getContentKey();
            final Map<Attribute, Object> node = (Map<Attribute, Object>) row.getRowStatus().getNode();
            if (contentKey == null || node == null) {
                continue;
            }

            attributeLoop: for (int i = 1; i < row.getCells().size(); i++) {
                GwtBulkLoadCell cell = row.getCells().get(i);

                if (!cell.getStatus().getState().isOperation()) {
                    continue attributeLoop;
                }

                final String attributeName = cell.getAttributeName();
                final String attributeType = cell.getAttributeType();
                final Object parserValue = cell.getParsedValue();

                if ("R".equals(attributeType)) {

                    Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(contentKey.type, attributeName);
                    if (optionalAttribute.isPresent() && optionalAttribute.get() instanceof Relationship) {
                        List<ContentKey> keys = new ArrayList<ContentKey>();
                        if (parserValue instanceof List<?>) {
                            List<String> parsedKeys = (List<String>) parserValue;
                            for (String parsedKey : parsedKeys) {
                                keys.add(ContentKeyFactory.get(parsedKey));
                            }
                        }

                        Relationship relationship = (Relationship) optionalAttribute.get();

                        if (RelationshipCardinality.ONE == relationship.getCardinality()) {
                            if (keys.size() == 1) {
                                modifyRelationshipValue(contentKey, attributeName, node, keys.get(0));
                            }
                        } else {
                            modifyRelationshipValue(contentKey, attributeName, node, keys);
                        }
                    }

                } else if ("_RR".equals(attributeType)) {
                    // Example: Category:lp_test.products
                    // inverseRelationshipParts[0] := content key of the far end
                    // inverseRelationshipParts[1] := relationship name
                    final String[] inverseRelationshipParts = attributeName.split("\\.");

                    // retrieve key of owner object (inverse-relationship)
                    ContentKey rrNodeKey = rrNodes.get(attributeName);
                    if (rrNodeKey == null) {
                        String keyPart = inverseRelationshipParts[0];

                        rrNodeKey = ContentKeyFactory.get(keyPart);
                        rrNodes.put(attributeName, rrNodeKey);
                    }

                    // process inverse-relationship name
                    Relationship rrAttribute = rrAttributes.get(attributeName);
                    if (rrAttribute == null) {
                        Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(rrNodeKey.type, inverseRelationshipParts[1]);
                        if (optionalAttribute.isPresent()) {
                            rrAttribute = (Relationship) optionalAttribute.get();
                            rrAttributes.put(attributeName, rrAttribute);
                        }
                    }

                    // prepare owner node
                    Map<Attribute, Object> rrNode = changedNodes.get(rrNodeKey);
                    if (rrNode == null) {
                        rrNode = fetchContentNode(rrNodeKey);

                        changedNodes.put(rrNodeKey, rrNode);
                    }

                    // get the original value

                    @SuppressWarnings("unchecked")
                    List<ContentKey> originalKeysList = (List<ContentKey>) rrNode.get(rrAttribute);
                    List<ContentKey> updatedKeysList = new ArrayList<ContentKey>();
                    if (originalKeysList != null) {
                        updatedKeysList.addAll(originalKeysList);
                    }

                    // determine change, apply it to the original value

                    BulkLoadReverseRelationship change = (BulkLoadReverseRelationship) cell.getParsedValue();
                    if (change == BulkLoadReverseRelationship.ADD) {
                        if (!updatedKeysList.contains(contentKey)) {
                            updatedKeysList.add(contentKey);
                        }
                    } else /* BulkLoadReverseRelationship.REMOVE */ {
                        updatedKeysList.remove(contentKey);
                    }

                    if (ContentNodeComparatorUtil.isValueChanged(originalKeysList, updatedKeysList)) {
                        modifyRelationshipValue(rrNodeKey, rrAttribute, rrNode, updatedKeysList);
                    }

                } else {
                    modifyScalarValue(contentKey, attributeName, node, parserValue);
                }
            }
        }

        String authorNote = MessageFormat.format("Performed bulk upload by {0} on draft {1}", author.getName(), author.getDraftName());

        return contentUpdateService.updateContent(author, changedNodes, authorNote, ContentChangeSource.BULKLOADER);
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

    private String prepareIntegerString(String value) {
        int dotIndex = value.indexOf('.');
        if (dotIndex >= 0)
            value = value.substring(0, dotIndex);
        return value;
    }

    private void handleClearValue(String attributeName, Attribute attributeDef, GwtBulkLoadPreviewStatus status, GwtBulkLoadPreviewStatus rowStatus) {
        if (attributeDef.getFlags().isRequired() && !attributeDef.getFlags().isInheritable()) {
            status.setStateWithMessage(ERROR_CELL, "cannot clear required attribute");
        } else {
            // parsedValue remains null as it will be cleared
            Object nodeValue = getAttributeValue(rowStatus, attributeName);
            if (rowStatus.getState() == CREATE) {
                status.setStateWithMessage(WARNING, "cannot clear unset value");
            } else if (nodeValue == null) {
                status.setStateWithMessage(NO_CHANGE, "no change");
            } else {
                status.setStateWithMessage(DELETE, "clear value");
            }
        }
    }

    private Object handleSimpleValue(String attributeName, Object parsedValue, GwtBulkLoadPreviewStatus status, GwtBulkLoadPreviewStatus rowStatus) {
        if (rowStatus.getState() == UPDATE) {
            Object currentValue = getAttributeValue(rowStatus, attributeName);
            if (parsedValue.equals(currentValue)) {
                status.setStateWithMessage(NO_CHANGE, "no change");
            }
        }
        return parsedValue;
    }

    private Object getAttributeValue(GwtBulkLoadPreviewStatus rowStatus, String attributeName) {
        Map<ContentKey, Map<Attribute, Object>> node = (Map<ContentKey, Map<Attribute, Object>>) rowStatus.getNode();
        Map<Attribute, Object> nodeAttributes = node.get(node.keySet().iterator().next());
        Object nodeValue = null;
        for (Attribute attribute : nodeAttributes.keySet()) {
            if (attributeName.equals(attribute.getName())) {
                nodeValue = nodeAttributes.get(attribute);
            }
        }
        return nodeValue;
    }

    private Object handleEnumValue(String attributeName, String value, Scalar definition, GwtBulkLoadPreviewStatus status, GwtBulkLoadPreviewStatus rowStatus) {
        boolean found = false;

        if (Integer.class.equals(definition.getType())) {
            value = prepareIntegerString(value);
        }

        for (Object key : definition.getEnumeratedValues()) {
            if (value.equals(key.toString())) {
                found = true;
                break;
            }
        }
        if (!found) {
            status.setStateWithMessage(ERROR_CELL, "unknown enum value");
            return null;
        } else if (rowStatus.getState() == UPDATE) {
            Object currentValue = getAttributeValue(rowStatus, attributeName);
            if (currentValue != null && value.equals(currentValue.toString())) {
                status.setStateWithMessage(NO_CHANGE, "no change");
            }
        }
        Class<?> valueType = definition.getType();
        Object parsedValue = null;
        if (valueType.equals(Integer.class)) {
            parsedValue = Integer.parseInt(value);
        } else if (valueType.equals(String.class)) {
            parsedValue = value;
        } else {
            status.setStateWithMessage(ERROR_CELL, "cannot handle the value type of enum (" + valueType.getSimpleName() + ")");
        }

        return parsedValue;
    }

    private Object handleRelationshipValue(String attributeName, Relationship relationshipDef, GwtBulkLoadPreviewStatus[] nodeStatuses, GwtBulkLoadPreviewStatus status,
            GwtBulkLoadPreviewStatus rowStatus) {
        Object parsedValue;
        List<String> keyNames = new ArrayList<String>(nodeStatuses.length);
        for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
            keyNames.add(((Map<ContentKey, Map<Attribute, Object>>) nodeStatus.getNode()).keySet().iterator().next().toString());
        }
        parsedValue = keyNames;
        if (rowStatus.getState() == UPDATE) {
            if (relationshipDef.getCardinality() == RelationshipCardinality.ONE) {
                ContentKey currentValue = (ContentKey) getAttributeValue(rowStatus, attributeName);
                if (nodeStatuses.length > 0 && ((Map<ContentKey, Map<Attribute, Object>>) nodeStatuses[0].getNode()).keySet().iterator().next().equals(currentValue)) {
                    status.setStateWithMessage(NO_CHANGE, "no change");
                }
            } else /* RelationshipCardinality.MANY */ {
                boolean same = true;
                @SuppressWarnings("unchecked")
                Collection<ContentKey> currentKeys = (Collection<ContentKey>) getAttributeValue(rowStatus, attributeName);
                if (currentKeys == null)
                    currentKeys = Collections.emptyList();
                if (currentKeys.size() != nodeStatuses.length) {
                    same = false;
                } else {
                    for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
                        if (!currentKeys.contains(((Map<ContentKey, Map<Attribute, Object>>) nodeStatus.getNode()).keySet().iterator().next())) {
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

    private Map<ContentKey, Map<Attribute, Object>> parseContentNodeId(int type, String stringValue, GwtBulkLoadPreviewStatus status) {
        Map<ContentKey, Map<Attribute, Object>> nodeWithKey = new HashMap<ContentKey, Map<Attribute, Object>>();
        Map<Attribute, Object> node = null;
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

                contentKey = ContentKeyFactory.get(ContentType.valueOf(typePart), idPart);

                if (!contentProviderService.containsContentKey(contentKey)) {
                    node = new HashMap<Attribute, Object>();
                    status.setStateWithMessage(CREATE, "new node");
                } else {
                    node = contentProviderService.getAllAttributesForContentKey(contentKey);
                    status.setStateWithMessage(UPDATE, "update node");
                }
                nodeWithKey.put(contentKey, node);
            }
        }

        return nodeWithKey;
    }

    private Map<ContentKey, Map<Attribute, Object>> parseContentNodeExtended(int type, String stringValue, GwtBulkLoadPreviewStatus status) {
        if (stringValue.charAt(0) == '/') {
            try {
                Optional<Media> node = mediaService.getMediaByUri(stringValue);
                if (!node.isPresent()) {
                    status.setStateWithMessage(ERROR_CELL, "no such media");
                }
                // return node.get();
                return null;
            } catch (RuntimeException e) {
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
    private void postCheckRelationshipValue(GwtBulkLoadPreviewStatus[] nodeStatuses, Relationship relationshipDef, GwtBulkLoadPreviewStatus status) {
        RelationshipCardinality cardinality = relationshipDef.getCardinality();
        if (cardinality == RelationshipCardinality.ONE) {
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
            Set<ContentType> contentTypes = new HashSet<ContentType>(Arrays.asList(relationshipDef.getDestinationTypes()));
            Set<ContentKey> previousKeys = new HashSet<ContentKey>(nodeStatuses.length);
            for (GwtBulkLoadPreviewStatus nodeStatus : nodeStatuses) {
                if (nodeStatus.getState().isError()) {
                    status.copyFrom(nodeStatus);
                    break;
                } else {
                    Map<ContentKey, Map<Attribute, Object>> node = (Map<ContentKey, Map<Attribute, Object>>) nodeStatus.getNode();
                    ContentKey nodeKey = node.keySet().iterator().next();
                    if (!contentTypes.contains(nodeKey.type)) {
                        nodeStatus.setStateWithMessage(ERROR_CELL, "illegal content type");
                        status.copyFrom(nodeStatus);
                        break;
                    } else if (previousKeys.contains(nodeKey)) {
                        nodeStatus.setStateWithMessage(ERROR_CELL, "duplicate content node: " + nodeKey.toString());
                        status.copyFrom(nodeStatus);
                    } else {
                        previousKeys.add(nodeKey);
                    }
                }
            }
        }
    }

    private Optional<String> determineCellValueType(Attribute attributeDef, GwtBulkLoadPreviewStatus status) {
        String valueType = null;

        if (attributeDef instanceof Scalar) {
            Scalar scalarAttribute = (Scalar) attributeDef;

            final Class<?> scalarValueType = scalarAttribute.getType();
            if (scalarAttribute.isEnumerated()) {
                if (scalarValueType.equals(String.class)) {
                    valueType = "S";
                } else if (scalarValueType.equals(Integer.class)) {
                    valueType = "I";
                } else {
                    status.setStateWithMessage(ERROR_CELL, "unsupported enumerated scalar type");
                }
            } else {
                if (scalarValueType.equals(String.class)) {
                    valueType = "S";
                } else if (scalarValueType.equals(Integer.class)) {
                    valueType = "I";
                } else if (scalarValueType.equals(Boolean.class)) {
                    valueType = "B";
                } else if (scalarValueType.equals(Date.class)) {
                    valueType = "DT";
                } else if (scalarValueType.equals(Double.class)) {
                    valueType = "D";
                } else {
                    status.setStateWithMessage(ERROR_CELL, "unsupported scalar type");
                }
            }

        } else if (attributeDef instanceof Relationship) {
            valueType = "R";
        } else {
            status.setStateWithMessage(ERROR_CELL, "unsupported attribute type");
        }

        return Optional.fromNullable(valueType);
    }

    // --- content save methods ---


    private LinkedHashMap<ContentKey, Map<Attribute, Object>> prepareContentNodes(List<GwtBulkLoadRow> rows) {
        LinkedHashMap<ContentKey, Map<Attribute, Object>> nodesToChange = new LinkedHashMap<ContentKey, Map<Attribute, Object>>();

        for (GwtBulkLoadRow row : rows) {
            GwtBulkLoadCell firstCellInTheRow = row.getCells().get(0);

            final String attributeType = firstCellInTheRow.getAttributeType();
            final BulkLoadPreviewState rowState = firstCellInTheRow.getStatus().getState();

            if ("_K".equals(attributeType) && (BulkLoadPreviewState.CREATE == rowState || BulkLoadPreviewState.UPDATE == rowState)) {
                final Object parsedValue = firstCellInTheRow.getParsedValue();

                ContentKey key = ContentKeyFactory.get(parsedValue.toString());

                Map<Attribute, Object> node = prepareContentNode(key, BulkLoadPreviewState.CREATE == rowState, parsedValue);

                nodesToChange.put(key, node);

                assignContentNodeToRow(key, node, row);
            }
        }

        return nodesToChange;
    }

    private Map<Attribute, Object> prepareContentNode(ContentKey key, boolean createOrUpdate, Object parsedValue) {

        Map<Attribute, Object> node = null;
        if (createOrUpdate) {
            node = createPrototypeContentNode(key);
        } else {
            node = fetchContentNode(key);
        }

        return node;
    }

    private void assignContentNodeToRow(ContentKey key, Map<Attribute, Object> node, GwtBulkLoadRow row) {
        row.getRowStatus().setContentKey(key);
        row.getRowStatus().setNode(node);
    }

    private Map<Attribute, Object> createPrototypeContentNode(ContentKey key) {
        return new HashMap<Attribute, Object>();
    }

    private Map<Attribute, Object> fetchContentNode(ContentKey key) {
        return new HashMap<Attribute, Object>(contentProviderService.getAllAttributesForContentKey(key));
    }

    private void modifyScalarValue(ContentKey contentKey, String attributeName, Map<Attribute, Object> targetNode, Object changedValue) {
        Assert.notNull(contentKey, "Content Key parameter is required");
        Assert.notNull(attributeName, "Attribute name parameter is required");
        Assert.notNull(targetNode, "targetNode parameter is required");

        Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(contentKey.type, attributeName);
        if (!optionalAttribute.isPresent()) {
            LOGGER.error("Unable to lookup definition for attribute name " + attributeName + " of type " + contentKey.type);
            return;
        }
        Attribute attribute = optionalAttribute.get();
        if (!(attribute instanceof Scalar)) {
            LOGGER.error(attribute + " is not scalar!");
            return;
        }

        modifyValueInternal(targetNode, attribute, changedValue);
    }

    private void modifyRelationshipValue(ContentKey contentKey, String attributeName, Map<Attribute, Object> targetNode, Object changedValue) {
        Assert.notNull(contentKey, "Content Key parameter is required");
        Assert.notNull(attributeName, "Attribute name parameter is required");
        Assert.notNull(targetNode, "targetNode parameter is required");

        Optional<Attribute> optionalAttribute = contentTypeInfoService.findAttributeByName(contentKey.type, attributeName);
        if (!optionalAttribute.isPresent()) {
            LOGGER.error("Unable to lookup definition for attribute name " + attributeName + " of type " + contentKey.type);
            return;
        }
        Attribute attribute = optionalAttribute.get();
        if (!(attribute instanceof Relationship)) {
            LOGGER.error(attribute + " is not relationship!");
            return;
        }

        modifyRelationshipValue(contentKey, (Relationship) attribute, targetNode, changedValue);
    }

    private void modifyRelationshipValue(ContentKey contentKey, Relationship relationship, Map<Attribute, Object> targetNode, Object changedValue) {
        Assert.notNull(contentKey, "Content Key parameter is required");
        Assert.notNull(relationship, "Relationship parameter is required");
        Assert.notNull(targetNode, "targetNode parameter is required");

        if (RelationshipCardinality.ONE == relationship.getCardinality()) {
            @SuppressWarnings("unchecked")
            ContentKey singleKey = changedValue != null ? (ContentKey) changedValue : null;
            modifyValueInternal(targetNode, relationship, singleKey);
        } else /* RelationshipCardinality.MANY */ {
            @SuppressWarnings("unchecked")
            Collection<ContentKey> keys = (Collection<ContentKey>) changedValue;
            if (keys == null) {
                modifyValueInternal(targetNode, relationship, null);
            } else {
                modifyValueInternal(targetNode, relationship, keys);
            }
        }
    }

    private void modifyValueInternal(Map<Attribute, Object> node, Attribute attribute, Object value) {
        node.put(attribute, value);
    }
}
