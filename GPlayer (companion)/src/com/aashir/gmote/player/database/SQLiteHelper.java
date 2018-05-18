package com.aashir.gmote.player.database;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.aashir.gmote.player.model.Constants;
import com.aashir.gmote.player.model.VideoItem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SQLiteHelper extends SQLiteOpenHelper {
 
    public SQLiteHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, 1);  
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.DATABASE_TABLE_NAME + " ( " +
                Constants.DATABASE_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.DATABASE_KEY_TITLE + " TEXT, " +
                Constants.DATABASE_KEY_DURATION + " TEXT," +
                Constants.DATABASE_KEY_PATH + " TEXT, " +
                Constants.DATABASE_KEY_RESOLUTION + " TEXT )";
        
        db.execSQL(CREATE_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DATABASE_TABLE_NAME);
        this.onCreate(db);
    }
 
    public void addVideoItem(VideoItem video){
    	deleteVideoItem(video);
        SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
        values.put(Constants.DATABASE_KEY_TITLE, video.getTitle());
        values.put(Constants.DATABASE_KEY_DURATION, video.getDuration());
        values.put(Constants.DATABASE_KEY_PATH, video.getPath());
        values.put(Constants.DATABASE_KEY_RESOLUTION , video.getResolution());
 
        db.insert(Constants.DATABASE_TABLE_NAME, null, values);
        db.close(); 
    }
    
    public ArrayList<VideoItem> getAllVideoItems() {
    	ArrayList<VideoItem> videos = new ArrayList<VideoItem>();
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constants.DATABASE_TABLE_NAME, null, null, null, null, null,
        		Constants.DATABASE_KEY_ID + " DESC");
        VideoItem video = null;
        if (cursor.moveToFirst()) {
            do {
                video = new VideoItem();
                video.setID(Integer.parseInt(cursor.getString(0)));
                video.setTitle(cursor.getString(1));
                video.setDuration(cursor.getString(2));
                video.setPath(cursor.getString(3));
                video.setResolution(cursor.getString(4));
                
                videos.add(video);
            } while (cursor.moveToNext());
        }
        
        return videos;
    }
    
    public void deleteVideoItem(VideoItem video) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.DATABASE_TABLE_NAME,  Constants.DATABASE_KEY_TITLE +" = ?", new String[] { String.valueOf(video.getTitle()) });
        db.close();
    }
    
}