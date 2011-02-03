package com.freshdirect.fdstore.myfd.poll;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PollDaddyService {
	private static long REFRESH_PERIOD = 1000 * 60 * 5; // 5 minutes

	private static BalkingExpiringReference<List<Poll>> POLLS;

	private static class PollListReference extends BalkingExpiringReference<List<Poll>> {
		private static final Logger LOGGER = LoggerFactory.getInstance(PollListReference.class);

		public PollListReference() {
			super(REFRESH_PERIOD);
			set(load());
		}

		@Override
		protected List<Poll> load() {
			LOGGER.info("load started");
			try {
				List<Poll> polls = new PollDaddyClient().getPolls();
				LOGGER.info("load completed (" + polls.size() + ")");
				return polls;
			} catch (IOException e) {
				LOGGER.error("IO exception while retrieving poll list", e);
			} catch (PollDaddyProtocolException e) {
				LOGGER.error("protocol exception while retrieving poll list", e);
			} catch (ParseException e) {
				LOGGER.error("parse exception while retrieving poll list", e);
			} catch (NoSuchElementException e) {
				LOGGER.error("no such element exception while retrieving poll list", e);				
			}
			LOGGER.info("load completed abruptly");
			return Collections.emptyList();
		}
	}

	public synchronized static List<Poll> getPolls() {
		if (POLLS == null) {
			POLLS = new PollListReference();
		}
		return POLLS.get();
	}
	
	public static Poll getFirstOpenPoll() {
		List<Poll> polls = getPolls();
		for (Poll poll : polls)
			if (!poll.isClosed())
				return poll;

		return null;
	}
}
