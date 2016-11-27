/*
 * ErpNutritionHome.java
 *
 * Created on August 17, 2001, 5:16 PM
 */

package com.freshdirect.content.nutrition.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */

import javax.ejb.*;
import java.rmi.RemoteException;

public interface ErpNutritionHome extends EJBHome {

	public ErpNutritionSB create() throws CreateException, RemoteException;
}
