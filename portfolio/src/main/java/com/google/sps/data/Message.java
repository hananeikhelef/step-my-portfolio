package com.google.sps.data;

public final class  Message{

  private final long id;
  private final String content;
  private final long timestamp;

  public Message(long id, String content, long timestamp) {
    this.id = id;
    this.content = content;
    this.timestamp = timestamp;
  }
}
