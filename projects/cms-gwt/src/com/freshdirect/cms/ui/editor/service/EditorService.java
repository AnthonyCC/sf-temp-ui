package com.freshdirect.cms.ui.editor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.ui.editor.ReportAttributes;
import com.freshdirect.cms.ui.editor.formdef.EditorFormDefinitionParser;
import com.freshdirect.cms.ui.editor.formdef.data.Editor;
import com.freshdirect.cms.ui.editor.formdef.data.Field;
import com.freshdirect.cms.ui.editor.formdef.data.FieldType;
import com.freshdirect.cms.ui.editor.formdef.data.Page;
import com.freshdirect.cms.ui.editor.formdef.data.Section;
import com.freshdirect.cms.ui.model.CustomFieldDefinition;
import com.freshdirect.cms.ui.model.TabDefinition;

@Service
public class EditorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditorService.class);

    private final Map<String, Editor> editors = new HashMap<String, Editor>();

    private EditorService() {
        List<Editor> editorList = new EditorFormDefinitionParser().parse();

        for (Editor editor : editorList) {
            if (editor.contentType != null) {
                editors.put(editor.contentType, editor);
            }
        }
    }

    public TabDefinition gwtTabDefinition(ContentKey nodeKey) {
        TabDefinition tabDef = new TabDefinition();

        if (editors.containsKey(nodeKey.getType().toString())) {

            processEditorStructure(editors.get(nodeKey.getType().toString()), tabDef);

        } else if (Media.isMediaType(nodeKey)) {

            Editor editor = createMediaEditorDefinition(nodeKey);

            processEditorStructure(editor, tabDef);


        } else if (ContentType.CmsQuery == nodeKey.getType() ) {
            Editor editor = createCmsQueryEditorDefinition(nodeKey);

            processEditorStructure(editor, tabDef);

        } else if (ContentType.CmsReport == nodeKey.getType()) {
            Editor editor = createCmsReportEditorDefinition(nodeKey);

            processEditorStructure(editor, tabDef);

        } else {
            LOGGER.error("No form registered for content type " + nodeKey.getType());
        }

        return tabDef;
    }

    private Editor createMediaEditorDefinition(ContentKey nodeKey) {
        List<Field> formFields = new ArrayList<Field>();

        formFields.add( new Field("path", FieldType.CmsField, ContentTypes.Image.path.getName(), null, null) );
        formFields.add( new Field("lastmodified", FieldType.CmsField, ContentTypes.Image.lastmodified.getName(), null, null) );
        if (ContentType.Image == nodeKey.type) {
            formFields.add( new Field("width", FieldType.CmsField, ContentTypes.Image.width.getName(), null, null) );
            formFields.add( new Field("height", FieldType.CmsField, ContentTypes.Image.height.getName(), null, null) );
        }
        else if (ContentType.Html == nodeKey.type) {
            formFields.add( new Field("popupSize", FieldType.CmsField, ContentTypes.Html.popupSize.getName(), null, null) );
            formFields.add( new Field("title", FieldType.CmsField, ContentTypes.Html.title.getName(), null, null) );
        }
        else if (ContentType.MediaFolder == nodeKey.type) {
            formFields.add( new Field("files", FieldType.CmsField, ContentTypes.MediaFolder.files.getName(), null, null) );
            formFields.add( new Field("subFolders", FieldType.CmsField, ContentTypes.MediaFolder.subFolders.getName(), null, null) );
        }
        List<Section> sections = new ArrayList<Section>( Arrays.asList( new Section[] {new Section("mediaSection", "", formFields)}) ) ;

        List<Page> pages = new ArrayList<Page>( Arrays.asList( new Page[] { new Page("mediaPage", "Media", sections) }) );

        Editor editor = new Editor("mediaEditor", nodeKey.getType().toString(), pages);

        return editor;
    }

    private Editor createCmsQueryEditorDefinition(ContentKey nodeKey) {
        List<Field> formFields = new ArrayList<Field>();

        formFields.add( new Field("cmsquery_name", FieldType.CmsField, ReportAttributes.CmsQuery.name.getName(), null, null) );
        formFields.add( new Field("cmsquery_desc", FieldType.CmsField, ReportAttributes.CmsQuery.description.getName(), null, null) );
        formFields.add(new Field("cmsquery_results", FieldType.CmsField, ReportAttributes.CmsQuery.results.getName(), null, null));

        List<Section> sections = new ArrayList<Section>( Arrays.asList( new Section[] {new Section("cmsquery_section", "", formFields)}) ) ;

        List<Page> pages = new ArrayList<Page>( Arrays.asList( new Page[] { new Page("cmsquery_page", "Report", sections) }) );

        Editor editor = new Editor("cmsquery_editor", nodeKey.getType().toString(), pages);

        return editor;
    }

    private Editor createCmsReportEditorDefinition(ContentKey nodeKey) {
        List<Field> formFields = new ArrayList<Field>();

        formFields.add( new Field("cmsreport_name", FieldType.CmsField, ReportAttributes.CmsReport.name.getName(), null, null) );
        formFields.add( new Field("cmsreport_desc", FieldType.CmsField, ReportAttributes.CmsReport.description.getName(), null, null) );
        formFields.add( new Field("cmsreport_results", FieldType.CmsField, ReportAttributes.CmsReport.results.getName(), null, null) );

        List<Section> sections = new ArrayList<Section>( Arrays.asList( new Section[] {new Section("cmsreport_section", "", formFields)}) ) ;

        List<Page> pages = new ArrayList<Page>( Arrays.asList( new Page[] { new Page("cmsreport_page", "Report", sections) }) );

        Editor editor = new Editor("cmsreport_editor", nodeKey.getType().toString(), pages);

        return editor;
    }

    private void processEditorStructure(Editor editor, TabDefinition tabDef) {
        for (Page page : editor.pages) {
            tabDef.addTab(page.id, page.title);

            for (Section section : page.sections) {
                tabDef.addSection(page.id, section.id, section.title);

                for (Field field : section.fields) {
                    tabDef.addAttributeKey(section.id, field.attribute);

                    registerCustomFieldType(tabDef, field);
                }
            }
        }
    }

    private void registerCustomFieldType(TabDefinition tabDef, Field field) {
        switch (field.fieldType) {
            case CmsGridField:
                {
                    CustomFieldDefinition cfd = new CustomFieldDefinition(CustomFieldDefinition.Type.Grid);
                    for (String columnAttribute : field.columns) {
                        cfd.addColumn(columnAttribute);
                    }
                    tabDef.addCustomFieldDefinition(field.attribute, cfd);

                }
                break;
            case CmsMultiColumnField:
                {
                    CustomFieldDefinition cfd = new CustomFieldDefinition(CustomFieldDefinition.Type.CmsMultiColumnField);
                    for (String columnAttribute : field.columns) {
                        cfd.addColumn(columnAttribute);
                    }
                    tabDef.addCustomFieldDefinition(field.attribute, cfd);
                }
                break;
            case CmsCustomField:
                tabDef.addCustomFieldDefinition(field.attribute, new CustomFieldDefinition(field.componentName));
                break;
            default:
                break;
        }
    }
}
