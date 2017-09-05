package co.edu.udea.compumovil.gr06_20172.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Viviana Londoño on 22/08/2017.
 */

public class DbHelper extends SQLiteOpenHelper{

    private static final String TAG = DbHelper.class.getSimpleName();

    public DbHelper(Context context){
        super(context, StatusContract.DB_NAME, null, StatusContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sqlLogin = String.format(
                "create table %s(%s int primary key, %s text unique)",
                StatusContract.TABLE_LOGIN,
                StatusContract.Column_Login.ID,
                StatusContract.Column_Login.EMAIL);
        db.execSQL(sqlLogin);
        String sqlUser = String.format(
                "create table %s(%s int primary key, %s text unique, %s text, %s text, %s text, %s text, %s text, %s text, %s text, %s text, %s blob)",
                StatusContract.TABLE_USER,
                StatusContract.Column_User.ID,
                StatusContract.Column_User.MAIL,
                StatusContract.Column_User.NAME,
                StatusContract.Column_User.LASTNAME,
                StatusContract.Column_User.GENDER,
                StatusContract.Column_User.DATE,
                StatusContract.Column_User.PHONE,
                StatusContract.Column_User.ADDRESS,
                StatusContract.Column_User.PASS,
                StatusContract.Column_User.CITY,
                StatusContract.Column_User.PICTURE);
        db.execSQL(sqlUser);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists" + StatusContract.TABLE_USER);
        db.execSQL("drop table if exists" + StatusContract.TABLE_LOGIN);
    }
}
