package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video{

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean flagged=false;
  private String flagReason="Not supplied";

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /**
   * @return true if video is flagged
   * false otherwise
   */
  public boolean isFlagged(){
    return flagged;
  }
  /**
   * @return the reason for being flagged
   */
  public String getFlagReason(){
    return flagReason;
  }

  public void setFlagReason(String flagReason){
    this.flagReason=flagReason;
  }

  public void setFlagStatus(boolean command){
    this.flagged=command;
  }

}
