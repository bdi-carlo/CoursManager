package com.example.coursmanager.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.coursmanager.model.Lesson;
import com.example.coursmanager.tools.MySQLite;

public class LessonManager {

    protected static final String TABLE_NAME_LESSON = "lesson";
    public static final String KEY_ID_LESSON = "_id";
    public static final String KEY_NAME_LESSON = "name_lesson";
    public static final String KEY_NAME_TEACHER = "name_teacher";
    public static final String KEY_NOTE = "note";
    public static final String KEY_DATE = "date";
    public static final String KEY_IDSUBJECT_LESSON = "id_subject";
    public static final String KEY_FINISH_LESSON = "finish";

    public static final String CREATE_TABLE_LESSON = "CREATE TABLE "+TABLE_NAME_LESSON+
            "("+KEY_ID_LESSON+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " "+KEY_NAME_LESSON+" TEXT,"+
            " "+KEY_NAME_TEACHER+" TEXT,"+
            " "+KEY_NOTE+" TEXT,"+
            " "+KEY_DATE+" INTEGER,"+
            " "+KEY_IDSUBJECT_LESSON+" INTEGER,"+
            " "+KEY_FINISH_LESSON+" NUMERIC,"+
            " FOREIGN KEY("+KEY_IDSUBJECT_LESSON+") REFERENCES "+SubjectManager.TABLE_NAME_SUBJECT+"("+SubjectManager.KEY_ID_SUBJECT+")"+" ON DELETE CASCADE"+
            ");";
    private MySQLite mySQLiteBase;
    private SQLiteDatabase db;

    public LessonManager(Context context){
        mySQLiteBase = MySQLite.getInstance(context);
    }

    public void open(){
        db = mySQLiteBase.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public long addLesson(Lesson aLesson){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_LESSON, aLesson.getNameLesson());
        values.put(KEY_NAME_TEACHER, aLesson.getNameTeacher());
        values.put(KEY_NOTE, aLesson.getNote());
        values.put(KEY_DATE, aLesson.getDate());
        values.put(KEY_IDSUBJECT_LESSON, aLesson.getIdSubject());
        values.put(KEY_FINISH_LESSON, aLesson.getFinish());

        return db.insert(TABLE_NAME_LESSON, null, values);
    }

    public int updateLesson(Lesson aLesson){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_LESSON, aLesson.getNameLesson());
        values.put(KEY_NAME_TEACHER, aLesson.getNameTeacher());
        values.put(KEY_NOTE, aLesson.getNote());
        values.put(KEY_DATE, aLesson.getDate());
        values.put(KEY_IDSUBJECT_LESSON, aLesson.getIdSubject());
        values.put(KEY_FINISH_LESSON, aLesson.getFinish());

        String where = KEY_ID_LESSON+" = ?";
        String[] whereArgs = {aLesson.getIdLesson()+""};

        //Log.d("id ========>", aLesson.getNameLesson()+"/"+String.valueOf(aLesson.getIdLesson()));

        return db.update(TABLE_NAME_LESSON, values, where, whereArgs);
    }

    public void deleteLesson(Lesson aLesson){
        db.execSQL("DELETE FROM "+TABLE_NAME_LESSON+" WHERE "+KEY_ID_LESSON+"="+aLesson.getIdLesson());
    }

    public Lesson getLesson(long aId){
        Lesson l = new Lesson(0, "", "", 0, "", false, 0);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_LESSON+" WHERE "+KEY_ID_LESSON+"="+aId, null);
        if(c.moveToFirst()){
            l.setIdLesson(c.getInt(c.getColumnIndex(KEY_ID_LESSON)));
            l.setNameLesson(c.getString(c.getColumnIndex(KEY_NAME_LESSON)));
            l.setNameTeacher(c.getString(c.getColumnIndex(KEY_NAME_TEACHER)));
            l.setDate(c.getInt(c.getColumnIndex(KEY_DATE)));
            l.setNote(c.getString(c.getColumnIndex(KEY_NOTE)));
            l.setIdSubject(c.getInt(c.getColumnIndex(KEY_IDSUBJECT_LESSON)));
            l.setFinish(c.getInt(c.getColumnIndex(KEY_FINISH_LESSON)) > 0);
        }

        return l;
    }

    public Cursor getAllLessonSubject(long idSubject){
        return db.rawQuery("SELECT * FROM "+TABLE_NAME_LESSON+" WHERE "+KEY_IDSUBJECT_LESSON+" = "+idSubject, null);
    }

    public int getNumberLessons(){
        return mySQLiteBase.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME_LESSON, null).getCount();
    }

    public int getNumberLessonsFinished(){
        return mySQLiteBase.getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME_LESSON+" WHERE "+KEY_FINISH_LESSON+" = "+1, null).getCount();
    }

}