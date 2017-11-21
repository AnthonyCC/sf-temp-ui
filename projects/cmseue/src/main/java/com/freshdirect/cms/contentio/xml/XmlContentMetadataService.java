package com.freshdirect.cms.contentio.xml;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("xml")
public class XmlContentMetadataService {

    private String type;
    private String description;
    private String date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String calculatePublishId() {
        String publishId = null;
        if (description != null && description.length() > 0) {
            String[] descriptionParts = description.split(":");
            if (descriptionParts.length == 2  && descriptionParts[0].trim().toLowerCase().startsWith("publishid")) {
                publishId = descriptionParts[1].trim();
            }
        }
        return publishId;
    }


}
