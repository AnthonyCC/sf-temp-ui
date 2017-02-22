package com.freshdirect.cms.search;


public enum SynonymDictionaryListKey {

    SYNONYM("FDFolder:synonymList"),
    SPELLING_SYNONYM("FDFolder:spellingSynonymList");
    
    private final String contentKey;
    
    private SynonymDictionaryListKey(String key){
        this.contentKey = key;
    }
    
    public String getContentKey() {
        return contentKey;
    }
}
