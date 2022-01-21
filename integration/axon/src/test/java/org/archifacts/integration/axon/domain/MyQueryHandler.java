package org.archifacts.integration.axon.domain;

import org.axonframework.messaging.Message;
import org.axonframework.queryhandling.QueryHandler;

public class MyQueryHandler {

	@QueryHandler
	public Integer handle(final MyQuery1 query) {
		return 0;
	}

	@QueryHandler
	public Integer handle(final Message<MyQuery2> event) {
		return 0;
	}

}
