package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrmOperationCollection {
    
    protected List<CrmCaseOperation> operations;
    
    public CrmOperationCollection() {
        super();
    }
    
    public List<CrmCaseOperation> getOperations(CrmAgentRole role, CrmCaseSubject subject) {
        return this.getOperations(new CrmCaseOperation(role, subject, null, null, null));
    }
    
    public List<CrmCaseOperation> getOperations(CrmAgentRole role, CrmCaseSubject subject, CrmCaseState startState) {
        return this.getOperations(new CrmCaseOperation(role, subject, startState, null, null));
    }
    
    public List<CrmCaseOperation> getOperations(CrmAgentRole role, CrmCaseSubject subject, CrmCaseState startState, CrmCaseActionType type) {
        return this.getOperations(new CrmCaseOperation(role, subject, startState, null, type));
    }
    
    public List<CrmCaseOperation> getOperations(CrmCaseOperation template) {
        return this.getMatches(template);
    }
    
    public void setOperations(List<CrmCaseOperation> ops) {
        this.operations = ops;
    }
    
    public List<CrmCaseOperation> getOperations() {
        return Collections.unmodifiableList(operations);
    }
    
    private List<CrmCaseOperation> getMatches(CrmCaseOperation template) {
        List<CrmCaseOperation> matches = new ArrayList<CrmCaseOperation>();
        for ( CrmCaseOperation op : this.operations ) {
            if (op.isMatching(template)) {
                matches.add(op);
            }
        }
        return matches;
    }
    
    
}
