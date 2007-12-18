package com.freshdirect.delivery.audit;

import java.util.List;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.framework.util.DateRange;

/**
 * Service interface for session auditing.
 */
public interface SessionAuditorI {

	/**
	 * Get session log entries for the specified date range.
	 * The search ensures that the
	 * <ul>
	 *  <li>login date is on or after the specified start date,</li>
	 *  <li>logout date is blank or it is less than or equal to the specifed end date,</li>
	 *  <li>login date is before the specified end date.</li>
	 * </ul>
	 * 
	 * @param range
	 *            date range to get entries for (never null)
	 * @return List of {@link SessionAuditEntry}
	 * @throws DlvResourceException
	 */
	List getSessionLogEntries(DateRange range) throws DlvResourceException;

	/**
	 * Record a login audit event.
	 * 
	 * @param sessionId
	 * @param username
	 * @param role
	 * @throws DlvResourceException
	 */
	void userLoggedIn(String sessionId, String username, String role)
			throws DlvResourceException;

	/**
	 * Record a logout audit event.
	 * 
	 * @param sessionId
	 * @throws DlvResourceException
	 */
	void userLogggedOut(String sessionId) throws DlvResourceException;
	
	void userInteraction(String sessionId) throws DlvResourceException;

}
