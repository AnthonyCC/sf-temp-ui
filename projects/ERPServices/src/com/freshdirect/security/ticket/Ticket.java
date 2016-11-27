package com.freshdirect.security.ticket;

import java.io.Serializable;
import java.util.Date;

public class Ticket implements Serializable {
	private static final long serialVersionUID = 7459239611299314257L;

	private String key;
	private String owner;
	private String purpose;
	private Date expiration;
	private boolean used;

	/**
	 * @param key
	 *            the unique key of the ticket
	 * @param owner
	 *            the owner of the ticket
	 * @param purpose
	 *            the purpose of the ticket use, usually the userId (email) of
	 *            the customer
	 * @param expiration
	 *            expiration offset expressed in minutes
	 * @param used
	 *            tells whether the ticket is used
	 */
	protected Ticket(String key, String owner, String purpose, int expiration, boolean used) {
		this.key = key;
		this.owner = owner;
		this.purpose = purpose;
		this.expiration = new Date(new Date().getTime() + expiration * 60000l);
		this.used = used;
	}

	Ticket(String key, String owner, String purpose, Date expiration, boolean used) {
		this.key = key;
		this.owner = owner;
		this.purpose = purpose;
		this.expiration = expiration;
		this.used = used;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expiration == null) ? 0 : expiration.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((purpose == null) ? 0 : purpose.hashCode());
		result = prime * result + (used ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (expiration == null) {
			if (other.expiration != null)
				return false;
		} else if (!expiration.equals(other.expiration))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (purpose == null) {
			if (other.purpose != null)
				return false;
		} else if (!purpose.equals(other.purpose))
			return false;
		if (used != other.used)
			return false;
		return true;
	}

	/**
	 * @return the unique property which identifies the ticket
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the owner (issuer) of the ticket
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @return the purpose of the ticket (usually the user id (email) of
	 *         customer)
	 */
	public String getPurpose() {
		return purpose;
	}

	/**
	 * @return when the ticket is expired
	 */
	public Date getExpiration() {
		return expiration;
	}

	/**
	 * @return whether the ticket was expired
	 */
	public boolean isExpired() {
		return expiration.before(new Date());
	}

	/**
	 * @return whether the ticket is used
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * make the ticket used
	 * 
	 * @user the user of the ticket (must be the same as the owner)
	 * @purpose the purpose must match with the original purpose of the ticket
	 * 
	 * @throws IllegalTicketUseException
	 *             thrown when owner != user
	 * @throws TicketExpiredException
	 *             thrown when ticket is expired
	 */
	protected void use(String user, String purpose) throws IllegalTicketUseException, TicketExpiredException {
		if (user.equals(owner))
			if (purpose.equals(this.purpose))
				if (isUsed())
					throw new IllegalTicketUseException("already used");
				else if (isExpired())
					throw new TicketExpiredException();
				else
					used = true;
			else
				throw new IllegalTicketUseException("purpose does not match");
		else
			throw new IllegalTicketUseException("user does not match with owner");
	}
}
