package com.example.appphpserver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class LocalDBHelper extends SQLiteOpenHelper implements DBHelper  {

    private Context context;
    private static final String DATABASE_NAME = "etudians.db";
    private static final String TABLE_NAME = "info";
    private static final String COL_1 = "id";
    private static final String COL_2 = "name";
    private static final String COL_3 = "address";

    private static String success_string = "Success";
    private static String failed_string = "An Error Occured";

    public LocalDBHelper( Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
        context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2 + " TEXT,"
                + COL_3 + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    @Override
    public boolean Add(String name, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, address);
        db.beginTransaction();
        try {
            long result = db.insert(TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
            if (result != -1){
                Toast.makeText(context, success_string, Toast.LENGTH_LONG).show();
                MainActivity.txt_id.setText("");
                MainActivity.txt_name.setText("");
                MainActivity.txt_address.setText("");
            }else{
                Toast.makeText(context, failed_string, Toast.LENGTH_LONG).show();
            }
            return result != -1;
        }catch (Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean Show(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_2, COL_3};
        String selection = COL_1 + "=?";
        String[] selectionArgs = {id};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        if(cursor.moveToFirst()){
            int nameIndex = cursor.getColumnIndex(COL_2);
            if (nameIndex >= 0) {
                MainActivity.txt_name.setText(cursor.getString(nameIndex));
            }

            int addressIndex = cursor.getColumnIndex(COL_3);
            if (addressIndex >= 0) {
                MainActivity.txt_address.setText(cursor.getString(addressIndex));
            }
            Toast.makeText(context, "Record Found!", Toast.LENGTH_LONG).show();
            return true;
        }else{
            MainActivity.txt_id.setText("");
            MainActivity.txt_name.setText("");
            MainActivity.txt_address.setText("");
            Toast.makeText(context, "No Record Found!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    @Override
    public boolean Update(String id, String new_name, String new_address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, new_name);
        contentValues.put(COL_3, new_address);
        String selection = COL_1 + "=?";
        String[] selectionArgs = {id};
        db.beginTransaction();
        try {
            int result = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
            db.setTransactionSuccessful();
            if (result > 0) {
                Toast.makeText(context, success_string, Toast.LENGTH_LONG).show();
                MainActivity.txt_id.setText("");
                MainActivity.txt_name.setText("");
                MainActivity.txt_address.setText("");
                return true;
            } else {
                Toast.makeText(context, failed_string, Toast.LENGTH_LONG).show();
                return false;
            }
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean Delete(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            int result = db.delete(TABLE_NAME, "id = ?", new String[]{id});
            db.setTransactionSuccessful();
            if (result > 0) {
                Toast.makeText(context, success_string, Toast.LENGTH_LONG).show();
                MainActivity.txt_id.setText("");
                MainActivity.txt_name.setText("");
                MainActivity.txt_address.setText("");
            } else {
                Toast.makeText(context, failed_string, Toast.LENGTH_LONG).show();
            }
            return result > 0;
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public boolean List() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID | NAME | ADDRESS \n");
        stringBuilder.append("----------------------------------------------- \n");
        int count = 0;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COL_1));
            String name = cursor.getString(cursor.getColumnIndex(COL_2));
            String address = cursor.getString(cursor.getColumnIndex(COL_3));
            stringBuilder.append(String.format("%d | %s | %s \n", id, name, address));
            stringBuilder.append("-------------------------------------------- \n");
            count +=1;
        }
        cursor.close();
        String result = stringBuilder.toString().trim();
        MainActivity.txt_data.setText(result);
        if (result.isEmpty()) {
            Toast.makeText(context, "No records found", Toast.LENGTH_LONG).show();
            return false;
        } else {
            Toast.makeText(context, "Found "+count+" Records", Toast.LENGTH_LONG).show();
            return true;
        }
    }

}
