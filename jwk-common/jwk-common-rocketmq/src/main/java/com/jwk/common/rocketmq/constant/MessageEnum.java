package com.jwk.common.rocketmq.constant;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 枚举类
 */
public enum MessageEnum {
  /**
   *
   */
  COMMENT_LIKE_TOPIC("commentLikeItQueue", MessageTopic.COMMENT_LIKE_TOPIC, MessageTag.COMMENT_LIKE_TAG);

  private String oldQueue;
  private String newQueue;
  private String tags;

  private MessageEnum(String oldQueue, String newQueue, String tags) {
    this.oldQueue = oldQueue;
    this.newQueue = newQueue;
    this.tags = tags;
  }

  public String getOldQueue() {
    return this.oldQueue;
  }

  public void setOldQueue(String oldQueue) {
    this.oldQueue = oldQueue;
  }

  public String getNewQueue() {
    return this.newQueue;
  }

  public void setNewQueue(String newQueue) {
    this.newQueue = newQueue;
  }

  public String getTags() {
    return this.tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public static MessageEnum get(String oldQueue) {
    MessageEnum[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      MessageEnum messageEnum = var1[var3];
      if (messageEnum.oldQueue.equals(oldQueue)) {
        return messageEnum;
      }
    }

    return null;
  }
}
