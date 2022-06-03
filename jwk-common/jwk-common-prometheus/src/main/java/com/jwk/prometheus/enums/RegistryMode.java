package com.jwk.prometheus.enums;


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
