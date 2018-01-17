package com.example.allahoakbar1.mymovie.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.allahoakbar1.mymovie.MovieClass;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MovieDATABASE2";
    private static final int DATABASE_VERSION = 3;
    //table name
    private static final String TABLE_NAME = "movieData";
    //columns name
    private static final String KEY_ID1 = "idOne";
    private static final String KEY_VOTE_COUNT = "voteCount";
    private static final String KEY_ID = "id";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_VOTE_AVERAGE = "voteAverage";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "posterPath";
    private static final String KEY_ORIGINAL_LANGUAGE = "originalLanguage";
    private static final String KEY_ORIGINAL_TITLE = "originalTitle";
    private static final String KEY_BACKDROP_PATH = "packDropPath";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASE_DATE = "releaseDate";
    private static final String KEY_IMAGE_DATA = "imageData";
    private static final String UNIQUE = "UNIQUE";

    //constractor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_movieDATA_TABLE = " CREATE TABLE " + TABLE_NAME + " ( "
                + KEY_ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ID + " TEXT, " + KEY_POSTER_PATH + " TEXT, "
                + KEY_TITLE + " TEXT, " + KEY_ORIGINAL_LANGUAGE + " TEXT, "
                + KEY_RELEASE_DATE + " TEXT, " + KEY_OVERVIEW + " TEXT, " + KEY_VOTE_AVERAGE + " TEXT ," + KEY_IMAGE_DATA + " TEXT "
                + " )";
        db.execSQL(CREATE_movieDATA_TABLE);
    }
    //upgrade DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    //inserting data in table
    public void Insert(MovieClass movieClass) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, movieClass.getId());
        values.put(KEY_POSTER_PATH, movieClass.getPoster_path());
        values.put(KEY_TITLE, movieClass.getTitle());
        values.put(KEY_ORIGINAL_LANGUAGE, movieClass.getOriginal_language());
        values.put(KEY_RELEASE_DATE, movieClass.getRelease_date());
        values.put(KEY_OVERVIEW, movieClass.getOverview());
        values.put(KEY_VOTE_AVERAGE, movieClass.getVote_average());
        values.put(KEY_IMAGE_DATA, movieClass.getIMAGE_DATA());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    //select data
    public ArrayList<MovieClass> Select() {
        ArrayList<MovieClass> movieClasses = new ArrayList<MovieClass>();
        String SELECT_QUERY = " SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                MovieClass movieClass = new MovieClass();
                movieClass.setId1(Integer.parseInt(cursor.getString(0)));
                movieClass.setId(cursor.getString(1));
                movieClass.setPoster_path(cursor.getString(2));
                movieClass.setTitle(cursor.getString(3));
                movieClass.setOriginal_language(cursor.getString(4));
                movieClass.setRelease_date(cursor.getString(5));
                movieClass.setOverview(cursor.getString(6));
                movieClass.setVote_average(cursor.getString(7));
                movieClass.setIMAGE_DATA(cursor.getString(8));
                movieClasses.add(movieClass);
            } while (cursor.moveToNext());

        }
        return movieClasses;
    }

    //delete all data
    public void clearTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
        db.close();
    }

}