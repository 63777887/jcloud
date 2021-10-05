package com.jwk.canal.rocketmq;


public enum FlatMessageType {
  UPDATE("UPDATE"),
  INSERT("INSERT"),
  DELETE("DELETE");

  FlatMessageType(String name) {
    this.name = name;
  }


  private String name;


  public String getName() {
    return name;
  }
}
