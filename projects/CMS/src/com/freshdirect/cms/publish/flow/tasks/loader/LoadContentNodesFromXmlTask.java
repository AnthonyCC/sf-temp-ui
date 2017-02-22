package com.freshdirect.cms.publish.flow.tasks.loader;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.publish.flow.Phase;
import com.freshdirect.cms.publish.service.impl.XmlContentLoaderService;

public final class LoadContentNodesFromXmlTask extends ContentLoaderTask<Collection<ContentNodeI>> {

    private final XmlContentLoaderService xmlContentLoaderService = XmlContentLoaderService.getInstance();

    private final String storeDefinitionFilePath;
    private final String storeXmlFilePath;

    public LoadContentNodesFromXmlTask(String publishId, Phase phase, String storeDefinitionFilePath, String storeXmlFilePath) {
        super(publishId, phase);
        this.storeDefinitionFilePath = storeDefinitionFilePath;
        this.storeXmlFilePath = storeXmlFilePath;
    }

    @Override
    public Collection<ContentNodeI> call() throws Exception {
        return xmlContentLoaderService.fetchAllContentNodes(storeDefinitionFilePath, storeXmlFilePath);
    }

    @Override
    public String getName() {
        return "Load ContentNodes from StoreXml";
    }

}
