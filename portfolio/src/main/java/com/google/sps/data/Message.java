package com.google.sps.data;

/** A message to the comment section. */
public final class  Message{

  private final long id;
  private final String content;
  private final long timestamp;

   /**
   * @param id         datastore-generated unique id for this comment.
   * @param content   main text of this message.
   * @param timestamp  comment creation date.
   */
  public Message(long id, String content, long timestamp) {
    this.id = id;
    this.content = content;
    this.timestamp = timestamp;
  }
}
