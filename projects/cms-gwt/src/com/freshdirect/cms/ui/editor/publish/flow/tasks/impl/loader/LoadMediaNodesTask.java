package com.freshdirect.cms.ui.editor.publish.flow.tasks.impl.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.cms.media.domain.Media;
import com.freshdirect.cms.media.service.MediaService;
import com.freshdirect.cms.ui.editor.publish.domain.StorePublishMessageSeverity;
import com.freshdirect.cms.ui.editor.publish.entity.StorePublishMessage;
import com.freshdirect.cms.ui.editor.publish.flow.service.PublishMessageLoggerService;
import com.freshdirect.cms.ui.editor.publish.flow.tasks.PublishTask;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public final class LoadMediaNodesTask extends PublishTask implements Callable<List<Media>> {

    @Autowired
    private PublishMessageLoggerService publishMessageLogger;

    @Autowired
    private MediaService mediaService;

    @Override
    public List<Media> call() throws Exception {
        publishMessageLogger.log(publishId, new StorePublishMessage(StorePublishMessageSeverity.INFO, "Loading Media nodes", LoadMediaNodesTask.class.getSimpleName()));
        List<Media> allMediaNodes = mediaService.loadAll();
        Map<String, Media> folderMap = new HashMap<String, Media>();
        Map<ContentKey, Media> nodeMap = new HashMap<ContentKey, Media>();
        for (Media node : allMediaNodes) {
            if (ContentType.MediaFolder == node.getContentKey().type) {
                folderMap.put(node.getUri(), node);
            }
            nodeMap.put(node.getContentKey(), node);
        }

        buildFolderStructure(nodeMap, folderMap);

        return new ArrayList<Media>(nodeMap.values());
    }

    @Override
    public String getName() {
        return "Load Media Nodes";
    }

    private void buildFolderStructure(Map<ContentKey, Media> nodeMap, Map<String, Media> folderMap) {
        for (final Media node : nodeMap.values()) {
            final String path = node.getUri();

            // root path is skipped
            if (path.length() <= 1) {
                continue;
            }

            final int parentSepIndex = path.lastIndexOf("/");
            final String parentPath = parentSepIndex == 0 ? "/" : path.substring(0, parentSepIndex);
            final Media parentFolder = folderMap.get(parentPath);

            if (parentFolder == null) {
                continue;
            }

            if (ContentType.MediaFolder == node.getContentKey().type) {
                if (null == parentFolder.getSubFolders()) {
                    parentFolder.setSubFolders(new ArrayList<ContentKey>());
                }
                parentFolder.getSubFolders().add(node.getContentKey());
            } else {
                if (null == parentFolder.getFiles()) {
                    parentFolder.setFiles(new ArrayList<ContentKey>());
                }
                parentFolder.getFiles().add(node.getContentKey());
            }
        }
    }

}
