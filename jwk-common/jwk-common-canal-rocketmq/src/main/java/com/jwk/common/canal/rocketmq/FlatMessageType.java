package com.jwk.common.canal.rocketmq;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
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

	FlatMessageType(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

}
