package com.jwk.rocketmq.constant;

public enum MessageEnum {
  COMMENT_LIKE_TOPIC("commentLikeItQueue", MessageTopic.COMMENT_LIKE_TOPIC, MessageTag.COMMENT_LIKE_TAG),
  ACTIVITY_TOPIC("activityQueue", MessageTopic.ACTIVITY_FREERIDE_TOPIC, MessageTag.ACTIVITY_TAG),
  BILL_PAY_TOPIC("billPayQueue", MessageTopic.BILL_PAY_TOPIC, MessageTag.BILL_PAY_TAG),
  HANDLE_TOPIC("handleQueue", MessageTopic.HANDLE_SERVICE_TOPIC, MessageTag.HANDLE_TAG),
  HOME_SERVICE_TOPIC("homeServiceQueue", MessageTopic.HOME_SERVICE_TOPIC, MessageTag.HOME_SERVICE_TAG),
  MORE_TOPIC("serviceMoreQueue", MessageTopic.MORE_TOPIC, MessageTag.MORE_TAG),
  WORK_ORDER_TOPIC("workOrderQueue", MessageTopic.WORK_ORDER_TOPIC, MessageTag.WORK_ORDER_TAG),
  RADIO_TOPIC("radioQueue", MessageTopic.LOTTERY_RADIO, MessageTag.RADIO_TAG),
  LOTTERY_TOPIC("lotteryQueue", MessageTopic.LOTTERY_RADIO, MessageTag.LOTTERY_TAG),
  COMMUNITY_CONSULTATION_TOPIC("communityConsultationQueue", MessageTopic.COMMUNITY_CONSULTATION_TOPIC, MessageTag.COMMUNITY_CONSULTATION_TAG),
  SMS_TOPIC("smsQueue", MessageTopic.SMS_TOPIC, MessageTag.SMS_TAG),
  FREE_RIDE_TOPIC("freeRideQueue", MessageTopic.ACTIVITY_FREERIDE_TOPIC, MessageTag.FREE_RIDE_TAG),
  VERSION_TOPIC("versionQueue", MessageTopic.PUBLIC_TOPIC, MessageTag.VERSION_TAG),
  SAVE_EX_CHANGE_RECORD_TOPIC("saveExChangeRecordQueue", MessageTopic.EXCHANGERECORD_TOPIC, MessageTag.save_exchange_record_tag),
  BEHAVIOR_TOPIC("behaviorQueue", MessageTopic.BEHAVIOR_TOPIC, MessageTag.BEHAVIOR_TAG),
  EX_CHANGE_RECORD_TOPIC("exChangeRecordQueue", MessageTopic.EXCHANGERECORD_TOPIC, MessageTag.exchange_record_tag),
  SMS_PLATFORM_SENDSMS_TOPIC("sms-platform-sendSmsQueue", MessageTopic.SMS_TOPIC, MessageTag.SMS_PLATFORM_SEND_SMS_TAG),
  SMS_PLATFORM_SYNC_SMS_TOPIC("sms-platform-syncSmsQueue", MessageTopic.SMS_TOPIC, MessageTag.SMS_PLATFORM_SYNC_SMS_TAG),
  SMS_PLATFORM_ACCOUNT_CONSUME_TOPIC("sms-platform-accountConsumeQueue", MessageTopic.SMS_TOPIC, MessageTag.SMS_PLATFORM_ACCOUNT_CONSUME_TAG),
  SMS_PLATFORM_ACCOUNT_CHARGE_TOPIC("sms-platform-accountChargeQueue", MessageTopic.SMS_TOPIC, MessageTag.SMS_PLATFORM_ACCOUNT_CHARGE_TAG),
  share_gift_topic("shareGiftQueue", MessageTopic.SHARE_GIFT_TOPIC, MessageTag.SHARE_GIFT_TAG),
  LEVEL_TOPIC("levelQueue", MessageTopic.BEHAVIOR_TOPIC, MessageTag.LEVEL_TAG),
  PUBLIC_TOPIC("publicQueue", MessageTopic.PUBLIC_TOPIC, MessageTag.PUBLIC_TAG),
  NOTICE_TOPIC("noticeQueue", MessageTopic.NOTICE_TOPIC, MessageTag.NOTICE_TAG),
  OTHER_TOPIC("otherQueue", MessageTopic.OTHER_TOPIC, MessageTag.OTHER_TAG),
  CALCULATION_TOPIC("calculationQueue", MessageTopic.BEHAVIOR_TOPIC, MessageTag.CALCULATION_TAG),
  CALCULATION_OVERRIDE_TOPIC("calculationQueue", MessageTopic.BEHAVIOR_TOPIC, MessageTag.CALCULATION_ORDINARY_TAG);

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
