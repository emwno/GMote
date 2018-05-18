package com.aashir.gmote.model;

public class Constants {
	
	final public static int MAX_VOLUME = 15;
	final public static String SHARED_PREF = "GMote.Preferences";
	final public static String SHARED_PREF_KEY_SKIP = "GMote.Preferences.Skip";
	
	final public static String ACTION_ADJUST_VOLUME = "com.aashir.gmote.ADJUST_VOLUME";
	
    final public static String ACTION = "com.aashir.gmote.ACTION";
    final public static String ACTION_CALLBACK = "com.example.customactivity.ACTION_CALLBACK";
	
    final public static String ACTION_VIDEO_PLAYBACK = "com.aashir.gmote.ACTION_VIDEO_PLAYBACK";
    final public static String ACTION_VIDEO_STOP = "com.aashir.gmote.ACTION_VIDEO_STOP";
    
    final public static String ACTION_VIDEO_PATH = "com.aashir.gmote.ACTION_VIDEO_PATH";
    final public static String ACTION_VIDEO_LIST = "com.aashir.gmote.ACTION_VIDEO_LIST";
    
    final public static String ACTION_VIDEO_PLAYBACK_SPEED = "com.aashir.gmote.ACTION_VIDEO_PLAYBACK_SPEED";
	
    final public static String ACTION_START_ACTVITY = "com.aashir.gmote.ACTION_START_ACTIVITY";
    
    final public static String ACTION_SEEK = "com.aashir.gmote.ACTION_SEEK";
    final public static String ACTION_SEEK_SKIP = "com.aashir.gmote.ACTION_SEEK_SKIP";
    
    final public static String ACTION_ROTATE = "com.aashir.gmote.ACTION_ROTATE";
    
    final public static String ACTION_VIDEO_POSITION = "com.aashir.gmote.ACTION_VIDEO_POSITION";
    final public static String ACTION_VIDEO_DURATION = "com.aashir.gmote.ACTION_VIDEO_DURATION";
    final public static String ACTION_VIDEO_TITLE = "com.aashir.gmote.ACTION_VIDEO_TITLE";
    final public static String ACTION_VIDEO_SUBTITLE = "com.aashir.gmote.ACTION_VIDEO_SUBTITLE";
    
    final public static String PROPERTY_VIDEO_DURATION = "/peer/com.aashir.gmote.player/video_duration";
    final public static String PROPERTY_QPAIR_CONNECTED =  "/local/qpair/is_connected";
    final public static String PROPERTY_VIDEO_TITLE = "/peer/com.aashir.gmote.player/video_title";
    final public static String PROPERTY_VIDEO_PLAYBACK = "/peer/com.aashir.gmote.player/video_playback";
    final public static String PROPERTY_VIDEO_SUBTITLE = "/local/com.aashir.gmote/video_subtitle";
    
    final public static String BROADCAST_RECIEVER_MEDIA = "com.aashir.gmote.reciever.MediaReciever";
    final public static String BROADCAST_RECIEVER_PEER_DATA = "com.aashir.gmote.reciever.VideoDataReciever";
    final public static String BROADCAST_RECIEVER_ACTION = "com.aashir.gmote.player.reciever.ActionBroadcastReciever";

}
