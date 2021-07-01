package com.google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private boolean anyVideoPlaying = false, anyVideoPaused = false;
  private String videoPlayingTitle = "";
  private Video videoPlaying;
  private ArrayList<String> playlistNames = new ArrayList<String>();
  private HashSet<String> uniquePlaylistNames = new HashSet<String>();
  private HashMap<String, HashSet<Video>> playlists = new HashMap<String, HashSet<Video>>();
  private List<Video> unflaggedVideos;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    unflaggedVideos = new ArrayList<Video>(videoLibrary.getVideos());
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void printVideo(Video video) {
    System.out.print(video.getTitle() + " (" + video.getVideoId() + ") [");
    boolean firstWord = true;
    for (String tag : video.getTags()) {
      if (firstWord == true) {
        System.out.print(tag);
        firstWord = false;
      } else
        System.out.print(" " + tag);
    }
    System.out.print("]");
    if (video.isFlagged() == true) {
      System.out.print(" - FLAGGED (reason: " + video.getFlagReason() + ")");
    }

  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());
    Collections.sort(videos, new Comparator<Video>() {
      public int compare(Video video1, Video video2) {
        return video1.getTitle().compareTo(video2.getTitle());
      }
    });
    for (Video video : videos) {
      printVideo(video);
      System.out.println();
    }
  }

  public void playVideo(String videoId) {
    boolean videoIdFound = false;
    List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());
    for (Video video : videos)
      if (video.getVideoId().equals(videoId))
        if (video.isFlagged() == false) {
          if (anyVideoPlaying == true) {
            System.out.println("Stopping video: " + videoPlayingTitle);
          }
          System.out.println("Playing video: " + video.getTitle());
          anyVideoPlaying = true;
          videoPlayingTitle = video.getTitle();
          videoPlaying = video;
          videoIdFound = true;
          anyVideoPaused = false;
          break;
        } else {
          System.out.println("Cannot play video: Video is currently flagged (reason: " + video.getFlagReason() + ")");
          videoIdFound = true;
        }
    if (videoIdFound == false)
      System.out.println("Cannot play video: Video does not exist");

  }

  public void stopVideo() {
    if (anyVideoPlaying == false)
      System.out.println("Cannot stop video: No video is currently playing");
    else {
      System.out.println("Stopping video: " + videoPlayingTitle);
      anyVideoPlaying = false;
    }
  }

  public void playRandomVideo() {
    if (unflaggedVideos.size() == 0)
      System.out.println("No videos available");
    else {
      Random random = new Random();
      Video randomVideo = unflaggedVideos.get(random.nextInt(unflaggedVideos.size()));
      if (anyVideoPlaying == true) {
        System.out.println("Stopping video: " + videoPlayingTitle);
        videoPlayingTitle = randomVideo.getTitle();
        videoPlaying = randomVideo;
      }
      System.out.println("Playing video: " + randomVideo.getTitle());
    }
  }

  public void pauseVideo() {
    if (anyVideoPlaying == false)
      System.out.println("Cannot pause video: No video is currently playing");
    else {
      if (anyVideoPaused == true) {
        System.out.println("Video already paused: " + videoPlayingTitle);
      } else {
        System.out.println("Pausing video: " + videoPlayingTitle);
        anyVideoPaused = true;
      }
    }
  }

  public void continueVideo() {
    if (anyVideoPlaying == false)
      System.out.println("Cannot continue video: No video is currently playing");
    else {
      if (anyVideoPaused == false)
        System.out.println("Cannot continue video: Video is not paused");
      else {
        System.out.println("Continuing video: " + videoPlayingTitle);
        anyVideoPaused = false;
      }
    }
  }

  public void showPlaying() {
    if (anyVideoPlaying == false)
      System.out.println("No video is currently playing");
    else {
      System.out.print("Currently playing: ");
      printVideo(videoPlaying);
      if (anyVideoPaused == true)
        System.out.print(" - PAUSED");
    }
  }

  public void createPlaylist(String playlistName) {
    if (uniquePlaylistNames.contains(playlistName.toLowerCase()))
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    else {
      System.out.println("Successfully created new playlist: " + playlistName);
      playlistNames.add(playlistName);
      uniquePlaylistNames.add(playlistName.toLowerCase());
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    if (uniquePlaylistNames.contains(playlistName.toLowerCase()) == false) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    } else {
      boolean videoIdFound = false;
      List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());
      for (Video video : videos) {
        String videoTitle = video.getTitle();
        if (video.getVideoId().equals(videoId))
          if (video.isFlagged() == false) {
            if (playlists.containsKey(playlistName.toLowerCase()) == true
                && playlists.get(playlistName.toLowerCase()).contains(video))
              System.out.println("Cannot add video to " + playlistName + ": Video already added");
            else {
              System.out.println("Added video to " + playlistName + ": " + videoTitle);
              if (playlists.containsKey(playlistName.toLowerCase()) == false) {
                HashSet<Video> videoNames = new HashSet<Video>();
                videoNames.add(video);
                playlists.put(playlistName.toLowerCase(), videoNames);
              } else {
                HashSet<Video> videoNames = playlists.get(playlistName.toLowerCase());
                videoNames.add(video);
                playlists.put(playlistName.toLowerCase(), videoNames);

              }
            }
            videoIdFound = true;
            break;
          } else {
            System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: "
                + video.getFlagReason() + ")");
            videoIdFound = true;
            break;
          }
      }
      if (videoIdFound == false)
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
    }

  }

  public void showAllPlaylists() {
    if (playlistNames.size() == 0)
      System.out.println("No playlists exist yet");
    else {
      System.out.println("Showing all playlists:");
      Collections.sort(playlistNames);
      for (String playlistName : playlistNames)
        System.out.println(playlistName);
    }
  }

  public void showPlaylist(String playlistName) {
    if (uniquePlaylistNames.contains(playlistName.toLowerCase()) == false) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    } else {
      System.out.println("Showing playlist: " + playlistName);
      if (playlists.containsKey(playlistName.toLowerCase()) == false
          || playlists.get(playlistName.toLowerCase()).size() == 0) {
        System.out.println("No videos here yet");
      } else {
        for (Video video : playlists.get(playlistName.toLowerCase())) {
          printVideo(video);
          System.out.println();
        }
      }
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if (uniquePlaylistNames.contains(playlistName.toLowerCase()) == false) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    } else {
      boolean videoIdFound = false;
      List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());
      for (Video video : videos) {
        String videoTitle = video.getTitle();
        if (video.getVideoId().equals(videoId)) {
          if (playlists.containsKey(playlistName.toLowerCase()) == false
              || playlists.get(playlistName.toLowerCase()).contains(video) == false)
            System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
          else {
            System.out.println("Removed video from " + playlistName + ": " + videoTitle);
            HashSet<Video> newPlaylist = playlists.get(playlistName.toLowerCase());
            newPlaylist.remove(video);
            playlists.put(playlistName.toLowerCase(), newPlaylist);
          }
          videoIdFound = true;
          break;
        }
      }
      if (videoIdFound == false)
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");

    }
  }

  public void clearPlaylist(String playlistName) {
    if (uniquePlaylistNames.contains(playlistName.toLowerCase()) == false) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    } else {
      System.out.println("Successfully removed all videos from " + playlistName);
      playlists.remove(playlistName.toLowerCase());
    }
  }

  public void deletePlaylist(String playlistName) {
    if (uniquePlaylistNames.contains(playlistName.toLowerCase()) == false) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    } else {
      System.out.println("Deleted playlist: " + playlistName);
      uniquePlaylistNames.remove(playlistName.toLowerCase());
      playlists.remove(playlistName);
    }
  }

  public void searchVideos(String searchTerm) {
    ArrayList<String> resultNames = new ArrayList<String>();
    Collections.sort(unflaggedVideos, new Comparator<Video>() {
      public int compare(Video video1, Video video2) {
        return video1.getTitle().compareTo(video2.getTitle());
      }
    });
    int count = 1;
    for (Video video : unflaggedVideos) {
      if (video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
        if (count == 1)
          System.out.println("Here are the results for " + searchTerm + ":");
        System.out.print(count + ") ");
        printVideo(video);
        System.out.println();
        resultNames.add(video.getTitle());
        count++;
      }
    }
    if (count == 1)
      System.out.println("No search results for " + searchTerm);
    else {
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      Scanner reader = new Scanner(System.in);
      String nextLine = reader.next();
      try {
        if (Integer.parseInt(nextLine) >= 1 && Integer.parseInt(nextLine) < count)
          System.out.println("Playing video: " + resultNames.get(Integer.parseInt(nextLine) - 1));
      } catch (Exception e) {
      }
    }
  }

  public void searchVideosWithTag(String videoTag) {
    if (videoTag.contains("#") == false)
      System.out.println("No search results for " + videoTag);
    ArrayList<String> resultNames = new ArrayList<String>();
    Collections.sort(unflaggedVideos, new Comparator<Video>() {
      public int compare(Video video1, Video video2) {
        return video1.getTitle().compareTo(video2.getTitle());
      }
    });
    int count = 1;
    for (Video video : unflaggedVideos) {
      for (String tag : video.getTags())
        if (tag.toLowerCase().equals(videoTag.toLowerCase())) {
          if (count == 1)
            System.out.println("Here are the results for " + videoTag + ":");
          System.out.print(count + ") ");
          printVideo(video);
          System.out.println();
          resultNames.add(video.getTitle());
          count++;
          break;
        }
    }
    if (count == 1)
      System.out.println("No search results for " + videoTag);
    else {
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      Scanner reader = new Scanner(System.in);
      String nextLine = reader.next();
      try {
        if (Integer.parseInt(nextLine) >= 1 && Integer.parseInt(nextLine) < count)
          System.out.println("Playing video: " + resultNames.get(Integer.parseInt(nextLine) - 1));
      } catch (Exception e) {
      }
    }
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    boolean videoIdFound = false;
    List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());
    for (Video video : videos) {
      String videoTitle = video.getTitle();
      if (video.getVideoId().equals(videoId)) {
        if (video.isFlagged() == true)
          System.out.println("Cannot flag video: Video is already flagged");
        else {
          if (anyVideoPlaying == true && videoPlayingTitle.equals(videoTitle)) {
            System.out.println("Stopping video: " + videoTitle);
            anyVideoPlaying = false;
          }
          System.out.println("Successfully flagged video: " + videoTitle + " (reason: " + reason + ")");
          video.setFlagStatus(true);
          video.setFlagReason(reason);
          unflaggedVideos.remove(video);
        }
        videoIdFound = true;
        break;
      }
    }
    if (videoIdFound == false)
      System.out.println("Cannot flag video: Video does not exist");
  }

  public void allowVideo(String videoId) {
    boolean videoIdFound = false;
    List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());
    for (Video video : videos) {
      String videoTitle = video.getTitle();
      if (video.getVideoId().equals(videoId)) {
        if (video.isFlagged() == false)
          System.out.println("Cannot remove flag from video: Video is not flagged");
        else {
          System.out.println("Successfully removed flag from video: " + videoTitle);
          video.setFlagStatus(false);
          unflaggedVideos.add(video);
        }
        videoIdFound = true;
        break;
      }
    }
    if (videoIdFound == false)
      System.out.println("Cannot remove flag from video: Video does not exist");
  }
}