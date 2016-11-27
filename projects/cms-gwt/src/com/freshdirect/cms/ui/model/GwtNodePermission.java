package com.freshdirect.cms.ui.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class GwtNodePermission implements Serializable {
    private static final long serialVersionUID = 6137551856755944584L;

    public static final GwtNodePermission NO_PERMISSION = new GwtNodePermission();
    public static final GwtNodePermission FULL_PERMISSION = new GwtNodePermission();
    static {
        NO_PERMISSION.setEditable( false );
        NO_PERMISSION.setAttributeEditable( false );

        FULL_PERMISSION.setEditable( true );
        FULL_PERMISSION.setAttributeEditable( true );
    }


    // Returns true if node is editable
    /**
     * This field marks denotes the whole node is editable
     */
    private boolean isEditable;
    private Set<String> disallowedForCreateTypes = new HashSet<String>();
    private Set<String> disallowedForDeleteTypes = new HashSet<String>();

    // for multi-homed nodes like products
    private Set<String> allowedStores = new HashSet<String>();
    
    /**
     * Particular attribute of node editable
     */
    private boolean isAttributeEditable;

    public GwtNodePermission() {}

    /**
     * Copy constructor, does shallow copy
     * @param other
     */
    public GwtNodePermission(GwtNodePermission other) {
        this.isEditable = other.isEditable;
        this.isAttributeEditable = other.isAttributeEditable;
        this.disallowedForCreateTypes = other.getDisallowedForCreateTypes();
        this.disallowedForDeleteTypes = other.getDisallowedForDeleteTypes();
        this.allowedStores = other.getAllowedStores();
    }
    
    public boolean isEditable() {
        return isEditable;
    }
    
    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }
    
    
    public boolean isAttributeEditable() {
        return isAttributeEditable;
    }
    

    public void setAttributeEditable(boolean isAttributeEditable) {
        this.isAttributeEditable = isAttributeEditable;
    }
    
    
    public boolean isReadonly() {
        return !(this.isEditable && this.isAttributeEditable);
    }

    /**
     * Return true if general-mode is enabled (no restrictions)
     * @return
     */
    public boolean isGeneralPermissionAllowed() {
        return disallowedForCreateTypes.isEmpty() && disallowedForDeleteTypes.isEmpty();
    }
    
    public void setDisallowedForCreateTypes(Set<String> disallowedForCreateTypes) {
        this.disallowedForCreateTypes = disallowedForCreateTypes;
    }

    
    protected Set<String> getDisallowedForCreateTypes() {
        return disallowedForCreateTypes;
    }
    
    public void setDisallowedForDeleteTypes(Set<String> disallowedForDeleteTypes) {
        this.disallowedForDeleteTypes = disallowedForDeleteTypes;
    }

    
    protected Set<String> getDisallowedForDeleteTypes() {
        return disallowedForDeleteTypes;
    }
    
    /**
     * @param baseTypes
     * @return
     */
    public Set<String> getTypesAllowedForCreate(Set<String> baseTypes) {
        if (this.isGeneralPermissionAllowed()) {
            return baseTypes;
        }

        Set<String> filteredSet = new HashSet<String>();
        for (String type : baseTypes) {
            if ( !disallowedForCreateTypes.contains(type)) {
                filteredSet.add(type);
            }
        }

        return filteredSet;
    }

    /**
     * @param baseTypes
     * @return
     */
    public Set<String> getTypesAllowedForDelete(Set<String> baseTypes) {
        if (this.isGeneralPermissionAllowed()) {
            return baseTypes;
        }

        Set<String> filteredSet = new HashSet<String>();
        for (String type : baseTypes) {
            if ( !disallowedForDeleteTypes.contains(type)) {
                filteredSet.add(type);
            }
        }

        return filteredSet;
    }


    
    public Set<String> getAllowedStores() {
        return allowedStores;
    }
    
    public void setAllowedStores(Set<String> allowedStores) {
        this.allowedStores = allowedStores;
    }



    public enum EvalResult {
        READONLY, // == read_only
        GENERAL,  // == read_write (as is)
        STRICT;   // == requires additional permission check
    }

    public EvalResult evaluate( final boolean readOnly ) {
        if (readOnly || isReadonly()) {
            return EvalResult.READONLY;
        }
        
        // !readonly from this point
        if ( this.isGeneralPermissionAllowed() ) {
            return EvalResult.GENERAL;
        }
        
        return EvalResult.STRICT;
    }
}
