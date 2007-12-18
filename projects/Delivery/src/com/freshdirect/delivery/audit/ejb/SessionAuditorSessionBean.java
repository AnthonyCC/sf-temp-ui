package com.freshdirect.delivery.audit.ejb;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.freshdirect.delivery.HibernateUtil;
import com.freshdirect.delivery.audit.SessionAuditEntry;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateRange;

public class SessionAuditorSessionBean extends SessionBeanSupport {
	
	private SessionFactory sf = HibernateUtil.getSessionFactory();

	private static final Category LOGGER = Category
			.getInstance(SessionAuditorSessionBean.class);

	public List getSessionLogEntries(DateRange range) {
		Session session = sf.getCurrentSession();
		try {
			Query query = session
					.createQuery(" from "
							+ SessionAuditEntry.class.getName()
							+ " where :startDate < startDate and startDate < :endDate and (endDate is null or :endDate > endDate) order by startDate desc");
			query.setTimestamp("startDate", range.getStartDate());
			query.setTimestamp("endDate", range.getEndDate());
			return query.list();
		} finally {
			session.close();
		}
	}

	public void userLoggedIn(String sessionId, String username, String role) {
		Session session = sf.getCurrentSession();
		try {
			SessionAuditEntry auditEntry = new SessionAuditEntry();
			Date now = new Date();
			auditEntry.setStartDate(now);
			auditEntry.setLastInteraction(now);
			auditEntry.setSessionId(sessionId);
			auditEntry.setUserId(username);
			auditEntry.setRole(role);
			session.save(auditEntry);
			session.flush();
		} finally {
			session.close();
		}
	}

	public void userLogggedOut(String sessionId) {
		Session session = sf.getCurrentSession();
		try {
			SessionAuditEntry auditEntry = getSessionAuditEntryBySessionId(session, sessionId);
			if(auditEntry == null) {
				return;
			}
			auditEntry.setEndDate(new Date());
			session.save(auditEntry);
			session.flush();
		} finally {
			session.close();
		}
	}

	private SessionAuditEntry getSessionAuditEntryBySessionId(Session session, String sessionId) {
		Query query = session.createQuery(" from "
				+ SessionAuditEntry.class.getName()
				+ " where sessionId = :sessionId");
		query.setString("sessionId", sessionId);
		SessionAuditEntry auditEntry = (SessionAuditEntry) query
				.uniqueResult();
		if (auditEntry == null) {
			LOGGER.warn("Session was never logged in: " + sessionId);
			return null;
		}
		if (auditEntry.getEndDate() != null) {
			LOGGER.warn("Session was already closed: " + sessionId);
			return null;
		}
		return auditEntry;
	}
	
	public void userInteraction(String sessionId) {
		Session session = sf.getCurrentSession();
		try {
			SessionAuditEntry auditEntry = getSessionAuditEntryBySessionId(session, sessionId);
			if(auditEntry == null) {
				return;
			}
			auditEntry.setLastInteraction(new Date());
			session.save(auditEntry);
			session.flush();
		} finally {
			session.close();
		}
	}
}
