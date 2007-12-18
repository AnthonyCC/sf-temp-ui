package com.freshdirect.delivery;

import java.rmi.RemoteException;

/** Indicates that no valid delivery zone exists for the Address that was searched upon.
  *	@author Erik Klein, Versatile Consulting Inc.
  */
public class UndeliverableAddressException extends RemoteException
{
    public UndeliverableAddressException(String _message)
    {
        super(_message);
    }
}