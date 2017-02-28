package uitstart.uit.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import uitstart.uit.noteapp.model.Note;

/**
 * Created by Khang on 2/19/2017.
 */

public class NoteDataBase extends SQLiteOpenHelper{
    public static final String DB_NAME="note.db";
    public static final String TABLE_NAME="note";
    public static final String NOTE_COLUMN_ID="id";
    public static final String NOTE_COLUMN_DATE="date";
    public static final String NOTE_COLUMN_TIME="time";
    public static final String NOTE_COLUMN_NAME="name";
    public static final String NOTE_COLUMN_DETAIL="detail";

    public NoteDataBase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"( " + NOTE_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NOTE_COLUMN_DATE+" TEXT,"+NOTE_COLUMN_TIME+" TEXT,"+NOTE_COLUMN_NAME+" TEXT,"+NOTE_COLUMN_DETAIL+" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public long insertNote(Note n){
        ContentValues values=new ContentValues();
        values.put(NOTE_COLUMN_DATE,n.getDate());
        values.put(NOTE_COLUMN_TIME,n.getTime());
        values.put(NOTE_COLUMN_NAME,n.getName());
        values.put(NOTE_COLUMN_DETAIL,n.getDetai());
        return getWritableDatabase().insert(TABLE_NAME,null,values);
    }

    public Note getNote(int id){
        String getNote="SELECT * FROM "+TABLE_NAME+" WHERE ID="+id;

        Cursor cursor=getReadableDatabase().rawQuery(getNote,null);
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_TIME));
            String name = cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_NAME));
            String detail = cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_DETAIL));
            return new Note(id, date, time, name, detail);
        }
        return null;
    }

    public ArrayList<Note> getNote(String dateSelected){
        ArrayList<Note> arrayListAllNote=new ArrayList<Note>();
        String getNote="SELECT * FROM "+TABLE_NAME+" WHERE "+NOTE_COLUMN_DATE+"='"+dateSelected+"'";
        Cursor cursor=getReadableDatabase().rawQuery(getNote,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(NOTE_COLUMN_ID));
            String date=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_DATE));
            String time=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_TIME));
            String name=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_NAME));
            String detail=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_DETAIL));
            arrayListAllNote.add(new Note(id,date,time,name,detail));
        }
        return arrayListAllNote;
    }

    public boolean updateNote(Note n){
        ContentValues values=new ContentValues();
        values.put(NOTE_COLUMN_DATE,n.getDate());
        values.put(NOTE_COLUMN_TIME,n.getTime());
        values.put(NOTE_COLUMN_NAME,n.getName());
        values.put(NOTE_COLUMN_DETAIL,n.getDetai());
        getWritableDatabase().update(TABLE_NAME,values,"id = ? ",new String[]{Integer.toString(n.getId())});
        return true;
    }
    public boolean deleteNote(Note n){
        getWritableDatabase().delete(TABLE_NAME,"id = ?",new String[]{Integer.toString(n.getId())});
        return true;
    }

    public ArrayList<Note> getAllNote(){
        ArrayList<Note> arrayListAllNote=new ArrayList<Note>();
        Cursor cursor=getReadableDatabase().rawQuery("SELECT * FROM "+TABLE_NAME,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex(NOTE_COLUMN_ID));
            String date=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_DATE));
            String time=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_TIME));
            String name=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_NAME));
            String detail=cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_DETAIL));
            arrayListAllNote.add(new Note(id,date,time,name,detail));
        }
        return arrayListAllNote;
    }

}
