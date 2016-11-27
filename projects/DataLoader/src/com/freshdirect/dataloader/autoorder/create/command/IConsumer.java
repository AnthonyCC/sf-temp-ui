package com.freshdirect.dataloader.autoorder.create.command;

import java.sql.Connection;
import java.util.Date;

public interface IConsumer {
	
	void consume(Object object, Connection conn);
	void start(String filePath, Date baseDate);
	void end();
}
