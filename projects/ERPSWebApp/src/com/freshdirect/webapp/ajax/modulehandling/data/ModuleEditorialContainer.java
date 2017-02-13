package com.freshdirect.webapp.ajax.modulehandling.data;

import java.io.Serializable;

public class ModuleEditorialContainer implements Serializable {

    private static final long serialVersionUID = -4621492346013763150L;

    private String heroTitle;
    private String heroSubtitle;
    private String headerTitle;
    private String headerSubtitle;

    private String heroGraphic;
    private String headerGraphic;

    private String editorialContent;

    public String getHeroTitle() {
        return heroTitle;
    }

    public void setHeroTitle(String heroTitle) {
        this.heroTitle = heroTitle;
    }

    public String getHeroSubtitle() {
        return heroSubtitle;
    }

    public void setHeroSubtitle(String heroSubtitle) {
        this.heroSubtitle = heroSubtitle;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderSubtitle() {
        return headerSubtitle;
    }

    public void setHeaderSubtitle(String headerSubtitle) {
        this.headerSubtitle = headerSubtitle;
    }

    public String getHeroGraphic() {
        return heroGraphic;
    }

    public void setHeroGraphic(String heroGraphic) {
        this.heroGraphic = heroGraphic;
    }

    public String getHeaderGraphic() {
        return headerGraphic;
    }

    public void setHeaderGraphic(String headerGraphic) {
        this.headerGraphic = headerGraphic;
    }

    public String getEditorialContent() {
        return editorialContent;
    }

    public void setEditorialContent(String editorialContent) {
        this.editorialContent = editorialContent;
    }

}
