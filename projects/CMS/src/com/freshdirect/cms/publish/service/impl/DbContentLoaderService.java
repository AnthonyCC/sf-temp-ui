package com.freshdirect.cms.publish.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.application.service.SimpleContentService;
import com.freshdirect.cms.application.service.db.DbTypeService;
import com.freshdirect.cms.application.service.xml.XmlTypeService;
import com.freshdirect.cms.publish.config.DatabaseConfig;
import com.freshdirect.cms.publish.repository.DbContentNodeRepository;
import com.freshdirect.cms.publish.repository.ERPSDataRepository;
import com.freshdirect.cms.publish.repository.MediaContentNodeRepository;
import com.freshdirect.cms.publish.service.ContentLoaderService;
import com.freshdirect.cms.publish.service.ContentNodeGeneratorService;

public final class DbContentLoaderService implements ContentLoaderService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(DbContentLoaderService.class);

    private static final DbContentLoaderService INSTANCE = new DbContentLoaderService();

    /**
     * Repository for CMS content node access
     */
    private DbContentNodeRepository contentNodeRepository = null;

    private MediaContentNodeRepository mediaNodeRepository = null;

    private ERPSDataRepository erpsRepository = null;

    private DbContentLoaderService() {

        final DataSource dataSource = DatabaseConfig.getDataSource();

        // construct type service
        final DbTypeService typeService = new DbTypeService();
        typeService.setDataSource(dataSource);
        typeService.initialize();

        // setup content node generator
        final ContentNodeGeneratorService generatorService = new GeneratedContentNodeGeneratorService(typeService);
        generatorService.createContentNode(ContentKey.getContentKey("Sku:sku00"));

        this.contentNodeRepository = new DbContentNodeRepository(typeService, generatorService);

        final ContentTypeServiceI mediaTypeService = new XmlTypeService("classpath:/com/freshdirect/cms/resource/MediaDef.xml");
        final ContentServiceI dummyContentService = new SimpleContentService(mediaTypeService);
        final ContentNodeGeneratorService mediaGeneratorService = new PlainContentNodeGeneratorService(dummyContentService);

        this.mediaNodeRepository = new MediaContentNodeRepository(mediaGeneratorService);

        this.erpsRepository = new ERPSDataRepository();
    }

    public static DbContentLoaderService defaultService() {
        return INSTANCE;
    }

    @Override
    public Collection<ContentNodeI> fetchAllContentNodes() {
        return contentNodeRepository.findAll();
    }

    @Override
    public Collection<ContentNodeI> fetchAllMediaNodes() {
        return mediaNodeRepository.findAll();
    }

    @Override
    public Map<String, String> fetchSkuMaterialAssociations() {
        return erpsRepository.fetchSkuMaterialAssociations();
    }

    @Override
    public Map<String,Map<String,Set<String>>> fetchMaterialConfigurationMap() {
        return erpsRepository.fetchMaterialConfigurationMap();
    }

    @Override
    public Map<String, Set<String>> fetchMaterialSalesUnits() {
        return erpsRepository.fetchSalesUnits();
    }

    @Override
    public List<String> fetchMediaURIs() {
        return mediaNodeRepository.fetchMediaURIs();
    }
}
