package com.jwk.common.canal;

import java.util.List;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * @date 2022/6/11
 */
public class MessageInfo {

	private String topic;

	private List<String> messages;

	public MessageInfo() {
	}

	public String getTopic() {
		return this.topic;
	}

	public MessageInfo setTopic(final String topic) {
		this.topic = topic;
		return this;
	}

	public List<String> getMessages() {
		return this.messages;
	}

	public MessageInfo setMessages(final List<String> messages) {
		this.messages = messages;
		return this;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof MessageInfo;
	}

	@Override
	public String toString() {
		return "MessageInfo(topic=" + this.getTopic() + ", messages=" + this.getMessages() + ")";
	}

}
