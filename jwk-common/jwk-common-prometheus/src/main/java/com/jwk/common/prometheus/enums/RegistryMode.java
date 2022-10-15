package com.jwk.common.prometheus.enums;


/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 支持的注册模式
 */
public enum RegistryMode {

  /**
   * ZOOKEEPER
   */
  ZOOKEEPER("zookeeper"),
  DUBBO("dubbo");


  private final String name;

  RegistryMode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
