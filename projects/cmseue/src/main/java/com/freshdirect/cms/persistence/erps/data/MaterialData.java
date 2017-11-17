package com.freshdirect.cms.persistence.erps.data;


public class MaterialData {

    private String sapId;
    private String name;
    private String description;
    private String upc;
    private MaterialAvailabilityStatus availabilityStatus;

    // see also: EnumATPRule
    private Integer atpRule;
    private Integer leadTime;
    private boolean alcoholicContent;
    private boolean taxable;
    private boolean kosher;
    private boolean platter;
    private String blockedDays;

    private Double promoPrice;
    private Integer latestMaterialVersion;

    public MaterialData(String sapId, String name, String description, String upc, String availability, String atpRule, String leadTime, String alcoholicContent,
            String taxable, String kosherProduction, String platter, String blockedDays, Double promoPrice, Integer latestMaterialVersion) {
        this.sapId = sapId;
        this.name = name;
        this.description = description;
        this.upc = upc;
        if (availability != null) {
            try {
                this.availabilityStatus = MaterialAvailabilityStatus.valueOfName(availability);
            } catch (IllegalArgumentException exc) {
            }
        }

        try {
            this.atpRule = Integer.valueOf(atpRule);
        } catch (NumberFormatException exc) {
        }

        this.leadTime = leadTime != null ? Integer.getInteger(leadTime) : null;
        this.alcoholicContent = alcoholicContent != null && "X".equals(alcoholicContent);
        this.taxable = taxable != null && "X".equals(taxable);
        this.kosher = kosherProduction != null && "X".equals(kosherProduction);
        this.platter = platter != null && "X".equals(platter);
        this.blockedDays = blockedDays;

        this.promoPrice = promoPrice;
        this.latestMaterialVersion = latestMaterialVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public MaterialAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(MaterialAvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Integer getAtpRule() {
        return atpRule;
    }

    public void setAtpRule(Integer atpRule) {
        this.atpRule = atpRule;
    }

    public Integer getLeadTime() {
        return leadTime;
    }

    public void setLeadTime(Integer leadTime) {
        this.leadTime = leadTime;
    }

    public boolean isAlcoholicContent() {
        return alcoholicContent;
    }

    public void setAlcoholicContent(boolean alcoholicContent) {
        this.alcoholicContent = alcoholicContent;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public boolean isKosher() {
        return kosher;
    }

    public void setKosher(boolean kosher) {
        this.kosher = kosher;
    }

    public boolean isPlatter() {
        return platter;
    }

    public void setPlatter(boolean platter) {
        this.platter = platter;
    }

    public String getBlockedDays() {
        return blockedDays;
    }

    public void setBlockedDays(String blockedDays) {
        this.blockedDays = blockedDays;
    }

    public Double getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(Double promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Integer getLatestMaterialVersion() {
        return latestMaterialVersion;
    }

    public void setLatestMaterialVersion(Integer latestMaterialVersion) {
        this.latestMaterialVersion = latestMaterialVersion;
    }

    @Override
    public String toString() {
        return "MaterialData [sapId=" + sapId
                + ", name=" + name
                + ", description=" + description
                + ", upc=" + upc
                + ", availability=" + (availabilityStatus != null ? availabilityStatus.name() : "<unknown>")
                + ", promo_price=" + promoPrice
                + ", version=" + latestMaterialVersion + "]";
    }
}
