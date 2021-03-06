package com.coursmanager.app.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.coursmanager.app.model.UE;

public class UEManager extends Manager {

    protected static final String TABLE_NAME_UE = "ue";
    public static final String KEY_ID_UE = "_id";
    public static final String KEY_NAME_UE = "name_ue";
    public static final String KEY_PERCENTAGE_UE = "percentage_ue";
    public static final String KEY_IDFOLDER_UE = "id_folder";
    public static final String CREATE_TABLE_UE = "CREATE TABLE "+TABLE_NAME_UE+
            "("+KEY_ID_UE+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            " "+KEY_NAME_UE+" TEXT,"+
            " "+KEY_PERCENTAGE_UE+" REAL,"+
            " "+KEY_IDFOLDER_UE+" INTEGER,"+
            " FOREIGN KEY("+KEY_IDFOLDER_UE+") REFERENCES "+FolderManager.TABLE_NAME_FOLDER+"("+FolderManager.KEY_ID_FOLDER+")"+" ON DELETE CASCADE"+
            ");";
    //CREATE TABLE t1(a INT, b TEXT, c REAL);
    public UEManager(Context context){
        super(context);
    }

    // Return the id of new insert
    public long addUE(UE aUE){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_UE, aUE.getNameUE());
        values.put(KEY_PERCENTAGE_UE, aUE.getPercentageUE());
        values.put(KEY_IDFOLDER_UE, aUE.getIdFolder());

        return db.insert(TABLE_NAME_UE, null, values);
    }

    public int updateUE(UE aUE){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_UE, aUE.getNameUE());
        values.put(KEY_PERCENTAGE_UE, aUE.getPercentageUE());
        values.put(KEY_IDFOLDER_UE, aUE.getIdFolder());

        String where = KEY_ID_UE+" = ?";
        String[] whereArgs = {aUE.getIdUE()+""};

        return db.update(TABLE_NAME_UE, values, where, whereArgs);
    }

    public void deleteUE(UE aUE){
        db.execSQL("DELETE FROM "+TABLE_NAME_UE+" WHERE "+KEY_ID_UE+"="+aUE.getIdUE());
    }

    public UE getUE(long aId){
        UE ue = new UE(0, "", 0, 0);

        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME_UE+" WHERE "+KEY_ID_UE+"="+aId, null);
        if(c.moveToFirst()){
            ue.setIdUE(c.getInt(c.getColumnIndex(KEY_ID_UE)));
            ue.setNameUE(c.getString(c.getColumnIndex(KEY_NAME_UE)));
            ue.setPercentageUE(c.getFloat(c.getColumnIndex(KEY_PERCENTAGE_UE)));
            ue.setIdFolder(c.getLong(c.getColumnIndex(KEY_IDFOLDER_UE)));
        }

        return ue;
    }

    public int getProgressOfanUE(long aId){
        String requete = "SELECT * FROM "+SubjectManager.TABLE_NAME_SUBJECT+" INNER JOIN "+LessonManager.TABLE_NAME_LESSON+" ON "+SubjectManager.TABLE_NAME_SUBJECT+"."+SubjectManager.KEY_ID_SUBJECT+" = "+LessonManager.TABLE_NAME_LESSON+"."+LessonManager.KEY_IDSUBJECT_LESSON+" WHERE "+SubjectManager.KEY_IDUE_SUBJECT+" = "+aId;
        int nbTot = db.rawQuery(requete, null).getCount();
        int nbFinished = db.rawQuery(requete+" AND "+LessonManager.TABLE_NAME_LESSON+"."+LessonManager.KEY_FINISH_LESSON+" = "+1 , null).getCount();

        return (int) Math.round(((float)nbFinished/(float)nbTot)*100);
    }

    public Cursor getAllUEFolder(long idFolder, int order){
        String request = "SELECT * FROM "+TABLE_NAME_UE+" WHERE "+KEY_IDFOLDER_UE+" = "+idFolder;
        //Order 1 is by name ascendant, 2 by name descendant, 3 by creation date (default)
        switch (order){
            case 1:
                return db.rawQuery(request, null);
            case 2:
                return db.rawQuery(request+" ORDER BY "+KEY_NAME_UE, null);
            case 3:
                return db.rawQuery(request+" ORDER BY "+KEY_NAME_UE+" DESC ", null);
            default:
                return db.rawQuery(request, null);
        }
    }

    public void deleteAllUEFolder(long idFolder){
        db.execSQL("DELETE FROM "+TABLE_NAME_UE+" WHERE "+KEY_IDFOLDER_UE+" = "+idFolder);
    }

    @Override
    public void rename(String newName, long aId){
        ContentValues value = new ContentValues();
        value.put(KEY_NAME_UE, newName);
        db.update(TABLE_NAME_UE, value, KEY_ID_UE + "= ?", new String[] {aId+""});
    }

}
