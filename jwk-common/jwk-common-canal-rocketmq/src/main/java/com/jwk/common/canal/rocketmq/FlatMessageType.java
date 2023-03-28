package com.jwk.common.canal.rocketmq;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * @date 2022/6/11
 */
public enum FlatMessageType {

	/**
	 * UPDATE
	 */
	UPDATE("UPDATE"),
	/**
	 * INSERT
	 */
	INSERT("INSERT"),
	/**
	 * DELETE
	 */
	DELETE("DELETE");

	private final String name;

	FlatMessageType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
