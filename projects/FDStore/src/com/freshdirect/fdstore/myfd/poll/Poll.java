package com.freshdirect.fdstore.myfd.poll;
import java.io.Serializable;
import java.util.Date;

public class Poll implements Serializable, Comparable<Poll> {
	private static final long serialVersionUID = 8003685772892939462L;

	private String id;
	private String question;
	private Date created;
	private boolean closed;

	public Poll(String id, String question, Date created) {
		super();
		this.id = id;
		this.question = question;
		this.created = created;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Poll other = (Poll) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Poll [id=" + id + ", created=" + created + ", question=" + question + "]";
	}

	@Override
	public int compareTo(Poll o) {
		return created.compareTo(o.created);
	}
	
	public String getId() {
		return id;
	}

	public String getQuestion() {
		return question;
	}

	public Date getCreated() {
		return created;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public boolean isClosed() {
		return closed;
	}
}
