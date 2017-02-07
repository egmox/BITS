package mx.egm.bitsonepat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static String getTableName, getKeys[], getType[];
    private SQLiteDatabase getDB;

    public DatabaseHandler(Context context) {
        super(context, "NoticeManager", null, 1);
    }

    public DatabaseHandler(Context context, String tableName, String[] keys, String[] type) {
        super(context, "NoticeManager", null, 1);
        getTableName=tableName;
        getKeys=keys;
        getType=type;
    }

    // Creating Tables
    public void onCreate(SQLiteDatabase db) {
        StringBuilder CREATE_TABLE = new StringBuilder("CREATE TABLE " + getTableName + "(");
        for(int count=0; count<(getKeys.length); count++){
            CREATE_TABLE.append(getKeys[count]+getType[count]);
        }
        db.execSQL(CREATE_TABLE.toString());
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName);
        onCreate(db);
    }

	 /*
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

    String lastNotice(){return "1";}

    // Adding new Row
    void addRow(String tableName, String keys[],String data[]) {
        try{
            getDB = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            for(int count=0; count<keys.length; count++)
                values.put(keys[count], data[count]); // id
            // Inserting Row
            getDB.insert(tableName, null, values);
            getDB.close(); // Closing database connection
        }catch(Exception e){}
    }

    public ArrayList<ArrayList<String>> getRows(String tableName) {
        ArrayList<ArrayList<String>> rows = new ArrayList<>();
        ArrayList<String> columns;
        String selectQuery = "SELECT  * FROM " + tableName;

        getDB = this.getWritableDatabase();
        Cursor cursor = getDB.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do{
                columns = new ArrayList<>();
                for(int count=1; count<cursor.getColumnCount(); count++)
                        columns.add(count-1, cursor.getString(count));
                rows.add(columns);
            } while (cursor.moveToNext());
        }
        return rows;
    }

    // Updating single contact
	/*public int updateRow(Row row) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(DATE, row.getDate());
		values.put(TEACHER, row.getTime());

		// updating row
		return db.update(getTableName, values, ID + " = ?",
				new String[] { String.valueOf(row.getID()) });
	}

	// Deleting single contact
	public void deleterow(Row row) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(getTableName, ID + " = ?",
				new String[] { String.valueOf(row.getID()) });
		db.close();
	}*/


    // Getting contacts Count
    public int getRowsCount() {
        String countQuery = "SELECT  * FROM " + getTableName;
        getDB = this.getReadableDatabase();
        Cursor cursor = getDB.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}