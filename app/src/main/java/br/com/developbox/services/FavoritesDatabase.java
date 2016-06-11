package br.com.developbox.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by fabio on 31/05/2016.
 */
public class FavoritesDatabase extends SQLiteOpenHelper{
    public static final String DBNAME = "fav.sqlite";
    public static final int VERSION = 1;

    public static final String TABLE_NAME = "favorites";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String URL = "url";

    public FavoritesDatabase(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private void createDatabase(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+TABLE_NAME +"("+
                ID+" integer primary key autoincrement not null, "+
                TITLE+" text,"+
                URL+" text);");
        db.close();
    }
    public void add(SQLiteDatabase db, Favorite favorite){
        ContentValues values = new ContentValues();

        values.put(TITLE, favorite.getTitle());
        values.put(URL, favorite.getUrl());

        Long id = (db.insert(TABLE_NAME, null, values));
        if(id != -1){
            Log.i("DATABASE", id.toString());
        }else{
            Log.e("DATABASE", id.toString());
        }
        db.close();
    }
    public Favorite[] getAll(SQLiteDatabase db){
        Cursor list = db.query(TABLE_NAME, new String[] {ID, TITLE, URL}, null, null, null, null, String.format("%s", TITLE));
        list.moveToFirst();

        if(!list.isAfterLast()){
            Favorite t[] = new Favorite[list.getCount()];
            int count = 0;
            do{
                String title = list.getString(1);
                String url = list.getString(2);

                t[count] = new Favorite(title, url,list.getInt(0));
                Log.i("DATABASE", t[count].getTitle()+" | "+t[count].getUrl());
                count++;
            }while(list.moveToNext());
            list.close();
            db.close();
            return t;
        }

        return null;
    }
    public Favorite getById(SQLiteDatabase db, String id){
        Favorite fav = new Favorite();

        String sql = "SELECT * FROM favorites WHERE id = ?;";
        Cursor result = db.rawQuery(sql, new String[] {id});

        if(result.moveToFirst()){
            fav.setId(result.getString(0));

            fav.setTitle(result.getString(1));

            fav.setUrl(result.getString(2));
        }
        db.close();
        return fav;

    }
    public void edit(SQLiteDatabase db, Favorite favorite){
        ContentValues values = new ContentValues();
        values.put(TITLE, favorite.getTitle());
        values.put(URL, favorite.getUrl());

        db.update(TABLE_NAME, values, ID+" = ?", new String[]{String.valueOf(favorite.getId())});
    }
    public void delete(SQLiteDatabase db, Favorite favorite){
        Log.i("DATABASE", "DELETE ITEM : "+favorite.getId());
        db.delete(TABLE_NAME, ID+" = ?", new String[]{String.valueOf(favorite.getId())});
    }
}
