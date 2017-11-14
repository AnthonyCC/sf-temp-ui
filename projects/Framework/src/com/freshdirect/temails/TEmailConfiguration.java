package com.freshdirect.temails;

import java.io.IOException;

import com.freshdirect.properties.FDStorePropertyResolver;
import com.freshdirect.temails.cheetah.CheetahLoopParser;

/**
 * This class is to provide configuration for TEmail instead of hivemind
 *
 */
public class TEmailConfiguration {

    private final TEmailsConfig config = new TEmailsConfig();

    public TEmailConfiguration() {
        config.setSubsystem("CHEETAH");
        config.setServiceName("com.freshdirect.fdstore.temails.cheetah.Engine");

        TemplateDescriptor baseParserDescriptor = new TemplateDescriptor();
        baseParserDescriptor.setLabel("Base Parser");
        baseParserDescriptor.setXmlTag("BaseParser");
        baseParserDescriptor.setOrder("1");
        baseParserDescriptor.setTargetClass(BaseParser.class);

        TemplateDescriptor cheetahParserDescriptor = new TemplateDescriptor();
        cheetahParserDescriptor.setLabel("Loop Parser");
        cheetahParserDescriptor.setXmlTag("LoopParser");
        cheetahParserDescriptor.setOrder("2");
        cheetahParserDescriptor.setTargetClass(CheetahLoopParser.class);

        config.addParserType(baseParserDescriptor);
        config.addParserType(cheetahParserDescriptor);
    }

    public TEmailsConfig getConfig() {
        return config;
    }

    public String xmlFile() {
        try {
            String xmlFile = FDStorePropertyResolver.getPropertyValue("com.freshdirect.fdstore.templates.cheetah.location");
            return xmlFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
