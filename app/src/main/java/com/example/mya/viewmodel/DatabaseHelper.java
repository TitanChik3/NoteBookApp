package com.example.mya.viewmodel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class DatabaseHelper  extends SQLiteOpenHelper {
    private Context context;
    private static final String DatabaseName = "MyNotes";
    private static final int DatabaseVersion = 1;
    private static final String tableName = "myNotes";
    private static final String columnId = "id";
    private static final String columnTitle = "title";
    private static final String columnDescription = "description";

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + tableName + " (" + columnId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + columnTitle + " TEXT, " + columnDescription + " Text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(db);
    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }
    public void addNotes(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(columnTitle,title);
        cv.put(columnDescription,description);
        long resultValue = db.insert(tableName,null, cv);
        if (resultValue == -1){
            Toast.makeText(context, "Данные в БД не добавлены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Данные в БД успешно добавлены", Toast.LENGTH_SHORT).show();
        }
    }
    public Cursor readNotes(){
        String query = "SELECT * FROM " +  tableName;
        SQLiteDatabase database= this.getReadableDatabase();
        Cursor cursor = null;
        if (database != null){
            cursor = database.rawQuery(query,null);
        }
        return  cursor;
    }
    public void deleteAllNotes() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "DELETE FROM " + tableName;
        database.execSQL(query);

    }
    public void updateNotes(String title, String description , String id){
        SQLiteDatabase database =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(columnTitle,title);
        cv.put(columnDescription, description);
        long result  = database.update(tableName, cv,"id=?", new String[]{id});

        if (result == -1) {
            Toast.makeText(context, "Обновление не получилось", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Обновление прошло успешно", Toast.LENGTH_SHORT).show();
        }
    }
    public  void  deleteSingleItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tableName,"id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Запись не удалена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Запись успешно удалена", Toast.LENGTH_SHORT).show();
        }
    }
}

