package com.example.isjahan.sinchcalltest.dbhelper;

/**
 * Created by shadman.rahman on 05-Jun-17.
 */

import android.database.sqlite.SQLiteDatabase;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


import com.example.isjahan.sinchcalltest.model.CallDetails;
import com.example.isjahan.sinchcalltest.model.UserCalls;
import com.example.isjahan.sinchcalltest.utility.Constant;

import java.io.File;
        import java.util.ArrayList;
        import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DB_NAME = "marsCall";
    private static final String TAG = "DatabaseHelper";

    public static String DB_PATH;
    private SQLiteDatabase database;
    private Context context;





    public DatabaseHelper(Context context){


        super(context, DB_NAME, null, 1);
        this.context = context;

        DB_PATH = context.getFilesDir().getPath() + "/databases/";
        this.database = openDatabase();

    }





    public SQLiteDatabase openDatabase(){
        if(database==null){
            createDatabase();
            Log.e(getClass().getName(), "Database created...");
        }

        return database;
    }

    private void createDatabase(){
        boolean dbExists = checkDB();
        if(!dbExists){
            this.getReadableDatabase();
            database = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);
            createTable();
            Log.e(getClass().getName(),"No Database");
        }

    }




    public SQLiteDatabase getDatabase(){
        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
        return database;
    }

    public void createTable() {
        String CREATE_USERCALL = "CREATE TABLE IF NOT EXISTS " + Constant.Database.TABLE_USER + "("
                + Constant.Database.User.UID + " VARCHAR PRIMARY KEY UNIQUE,"
                + Constant.Database.User.IS_ME + " INTEGER DEFAULT 0"
                + ")";

        String CREATE_USERCALLDETAILS = "CREATE TABLE IF NOT EXISTS " + Constant.Database.TABLE_CALLLOG + "("
                + Constant.Database.CallLog.CALLTO + " VARCHAR PRIMARY KEY UNIQUE,"
                + Constant.Database.CallLog.INITIATEDAT + " INTEGER ,"
                + Constant.Database.CallLog.CALLTYPE + " VARCHAR "
                + ")";


        try {
            database.execSQL( CREATE_USERCALL );
            database.execSQL( CREATE_USERCALLDETAILS );
            database.close();
        } catch (Exception e) {
            Log.e(getClass().getName(), "Error Creating Table");
        }
    }
    public void addUserLog(CallDetails callDetails){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.Database.CallLog.CALLTO, callDetails.getCallingTo());
        values.put(Constant.Database.CallLog.INITIATEDAT, callDetails.getCallInTime());
        values.put(Constant.Database.CallLog.CALLTYPE, callDetails.getCallType());



        db.insertWithOnConflict(Constant.Database.TABLE_CALLLOG, Constant.Database.CallLog.CALLTO , values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }







    public void addUserCall(UserCalls userCalls){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constant.Database.User.UID, userCalls.getUserName());

        values.put(Constant.Database.User.IS_ME, userCalls.getIs_me());


        db.insertWithOnConflict(Constant.Database.TABLE_USER, Constant.Database.User.UID , values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }




    public UserCalls getMe(){

        UserCalls userCalls=new UserCalls();



        String selectAll = "SELECT * FROM "+ Constant.Database.TABLE_USER + " WHERE "+ Constant.Database.User.IS_ME + " = 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);




        try {
            if (cursor.moveToFirst()) {
                do {

                    userCalls.setUserName(cursor.getString(0));
                    userCalls.setIs_me(1);

                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "nullpointer exception");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }



        return userCalls;

    }







    public UserCalls getUser( String uid){

        UserCalls user = new UserCalls();

        String selection = Constant.Database.User.UID+" = ? ";
        String[] selectionArgs = new String[] {uid};


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(Constant.Database.TABLE_USER, null,  selection, selectionArgs, null,  null, null, null);




        try {
            if (cursor.moveToFirst()) {
                do {

                    user.setUserName(cursor.getString(0));


                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "nullpointer exception");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }



        return user;

    }

    public ArrayList<CallDetails> getAllUserLog(){

        ArrayList<CallDetails> alluser = new ArrayList<>();

        String selectAll = "SELECT * FROM "+ Constant.Database.TABLE_CALLLOG +" ORDER BY initiatedat DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    CallDetails user = new CallDetails();
                    user.setCallingTo(cursor.getString(0));
                    user.setCallInTime(cursor.getLong(1));
                    user.setCallType(cursor.getString(2));
                    alluser.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "nullpointer exception");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return alluser;

    }

    public ArrayList<UserCalls> getAllUser(){
        ArrayList<UserCalls> alluser = new ArrayList<>();
        String selectAll = "SELECT DISTINCT uid FROM "+ Constant.Database.TABLE_USER + " WHERE "+ Constant.Database.User.IS_ME + " = 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    UserCalls user = new UserCalls();
                    user.setUserName(cursor.getString(0));
                    alluser.add(user);

                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d(TAG, "nullpointer exception");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return alluser;

    }

    private boolean checkDB(){
        String path = DB_PATH + DB_NAME;
        File file = new File(path);
        if(file.exists()){
            return true;
        }
        return false;
    }
    public synchronized void close(){
        if(this.database != null){
            this.database.close();
        }
    }








    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constant.Database.TABLE_CHAT_ROOMS);
        onCreate(db);
    }


}

