package com.freshdirect.framework.util;

public class DurationFormat {

	public final static int MASK_DAY = 8;
	public final static int MASK_HOUR = 4;
	public final static int MASK_MINUTE = 2;
	public final static int MASK_SECOND = 1;

	private final boolean full;
	private final int componentMask;

	private final StringBuffer sb = new StringBuffer();
	private int processed;
	private long ms;

	public DurationFormat() {
		this(true, MASK_DAY | MASK_HOUR | MASK_MINUTE | MASK_SECOND);
	}

	public DurationFormat(boolean full, int componentMask) {
		this.componentMask = componentMask;
		this.full = full;
	}

	public synchronized String format(long milliseconds) {
		this.sb.setLength(0);
		this.processed = 0;
		this.ms = milliseconds;

		if ((componentMask & MASK_DAY) > 0) {
			this.process(full ? "day" : "d", DateUtil.DAY);
		}
		if ((componentMask & MASK_HOUR) > 0) {
			this.process(full ? "hour" : "h", DateUtil.HOUR);
		}
		if ((componentMask & MASK_MINUTE) > 0) {
			this.process(full ? "minute" : "m", DateUtil.MINUTE);
		}
		if ((componentMask & MASK_SECOND) > 0) {
			this.process(full ? "second" : "s", DateUtil.SECOND);
		}

		return sb.toString();
	}

	private void process(String name, long period) {
		if (this.processed >= 2) {
			// show two most signifcant components
			return;
		}
		long t = this.ms / period;
		if (this.processed > 0 || t > 0) {
			this.processed++;
		}
		if (t > 0) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			this.ms -= t * period;
			this.sb.append(t).append(' ').append(name);
			if (full && t > 1) {
				sb.append('s');
			}
		}
	}

}
