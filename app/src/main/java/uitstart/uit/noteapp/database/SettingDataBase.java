package uitstart.uit.noteapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import uitstart.uit.noteapp.model.MySetting;
import uitstart.uit.noteapp.model.Note;

/**
 * Created by Khang on 2/28/2017.
 */

public class SettingDataBase extends SQLiteOpenHelper {
    public static final String DB_NAME="setting.db";
    public static final String TABLE_NAME="setting";
    public static final String NOTE_COLUMN_ID="id";
    public static final String NOTE_COLUMN_IS_NOTIFITCATION="is_notification";
    public static final String NOTE_COLUMN_LANGUAGE="language";

    Context context;
    public SettingDataBase(Context context){
        super(context,DB_NAME,null,1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"( " + NOTE_COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NOTE_COLUMN_IS_NOTIFITCATION+" INTEGER,"+NOTE_COLUMN_LANGUAGE+" TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long insertSettingDefault(){
        ContentValues values=new ContentValues();
        values.put(NOTE_COLUMN_IS_NOTIFITCATION,1);
        values.put(NOTE_COLUMN_LANGUAGE,"en");
        return getWritableDatabase().insert(TABLE_NAME,null,values);
    }

    public boolean updateSetting(MySetting mySetting){
        ContentValues values=new ContentValues();
        values.put(NOTE_COLUMN_IS_NOTIFITCATION,mySetting.getIs_notification());
        values.put(NOTE_COLUMN_LANGUAGE,mySetting.getLanguage_code());
        getWritableDatabase().update(TABLE_NAME,values,"id = ? ",new String[]{Integer.toString(mySetting.getId())});
        return true;
    }

    public MySetting getMySetting(){
        String getSetting ="SELECT * FROM "+TABLE_NAME;

        Cursor cursor=getReadableDatabase().rawQuery(getSetting,null);
        while (cursor.moveToNext()) {
            int id=cursor.getInt(cursor.getColumnIndex(NOTE_COLUMN_ID));
            int is_notification = cursor.getInt(cursor.getColumnIndex(NOTE_COLUMN_IS_NOTIFITCATION));
            String language = cursor.getString(cursor.getColumnIndex(NOTE_COLUMN_LANGUAGE));
            return new MySetting(id,is_notification,language);
        }
        return null;
    }
}
