package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CrmOperationCollection {
    
    protected List operations;
    
    public CrmOperationCollection() {
        super();
    }
    
    public List getOperations(CrmAgentRole role, CrmCaseSubject subject) {
        return this.getOperations(new CrmCaseOperation(role, subject, null, null, null));
    }
    
    public List getOperations(CrmAgentRole role, CrmCaseSubject subject, CrmCaseState startState) {
        return this.getOperations(new CrmCaseOperation(role, subject, startState, null, null));
    }
    
    public List getOperations(CrmAgentRole role, CrmCaseSubject subject, CrmCaseState startState, CrmCaseActionType type) {
        return this.getOperations(new CrmCaseOperation(role, subject, startState, null, type));
    }
    
    public List getOperations(CrmCaseOperation template) {
        return this.getMatches(template);
    }
    
    public void setOperations(List ops) {
        this.operations = ops;
    }
    
    public List getOperations() {
        return Collections.unmodifiableList(operations);
    }
    
    private List getMatches(CrmCaseOperation template) {
        List matches = new ArrayList();
        for (Iterator i=this.operations.iterator(); i.hasNext(); ) {
            CrmCaseOperation op = (CrmCaseOperation)i.next();
            if (op.isMatching(template)) {
                matches.add(op);
            }
        }
        return matches;
    }
    
    
}
