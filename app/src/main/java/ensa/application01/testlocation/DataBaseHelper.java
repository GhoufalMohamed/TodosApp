package ensa.application01.testlocation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
private Context context;
public static final String DATABASE8_NAME="TodosApp.db";
    private static final int DATABASE_VERSION=1;
    private static final String TABLE_NAME="my_data";
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="object_name";
    private static final String COLUMN_TIME="time";
    private static final String COLUMN_DESIGNATION="designation";
    private static final String COLUMN_LONG="longitude";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_IMAGE="Image";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE8_NAME, null, DATABASE_VERSION);
        this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+ TABLE_NAME+
                "( "+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_NAME +" TEXT," +
                COLUMN_TIME +" TEXT," +
                COLUMN_DESIGNATION +" TEXT,"+
                COLUMN_LAT +" REAL,"+
                COLUMN_LONG +" REAL," +
                COLUMN_IMAGE +" TEXT);";
        db.execSQL(query); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME );
onCreate(db);
    }

    void addObject(String name,String time,  String designation, double lat, double longitude, String image){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(COLUMN_NAME, name);
        value.put(COLUMN_TIME,time);
        value.put(COLUMN_DESIGNATION, designation);
        value.put(String.valueOf(COLUMN_LAT), lat);
        value.put(String.valueOf(COLUMN_LONG), longitude);
        value.put(COLUMN_IMAGE,image);
        long result = db.insert(TABLE_NAME,null, value);
        if(result == -1){
           Toast.makeText(context,"Failed", Toast.LENGTH_SHORT).show();
       }
        else{
            Toast.makeText(context,"Added successfully", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db !=null){
          cursor=  db.rawQuery(query, null);

        }
        return cursor;
    }
}
