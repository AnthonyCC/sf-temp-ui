package com.freshdirect.framework.core;

/**
 * interface to implemeneted by objects that need to save themselves
 * to a persistent store
 * @version $Revision$
 * @author $Author$
 */ 
public interface PersistentBeanI extends ModelI, PersistentI, ModelConsumerI, ModelProducerI {
    
    /** ModelProducer, without RemoteException
     * @return a model object representing the internal state of
     * the persistent object
     */
    public ModelI getModel();
    
    /** ModelConsumer, without RemoteException
     * @param model a model whose properties should be copied to the
     * persistent object
     */
    public void setFromModel(ModelI model);
}

