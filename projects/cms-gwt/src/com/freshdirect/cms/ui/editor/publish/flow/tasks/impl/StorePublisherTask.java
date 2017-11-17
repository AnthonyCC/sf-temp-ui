package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StorePublisherTask extends PublisherTask {

    @PostConstruct
    private void initialize() {
        setTargetXmlFileName("Store.xml.gz");
    }

    @Override
    public String getName() {
        return "Store.xml publish to " + targetPath;
    }

}
