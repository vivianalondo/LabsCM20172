package co.edu.udea.compumovil.gr06_20172.lab1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Viviana Londoño on 21/08/2017.
 */

public class Login extends AppCompatActivity {

    private static final int REQUEST_CODE=1;
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onResume(){
        super.onResume();
        emailView.setError(null);
        passwordView.setError(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailView = (AutoCompleteTextView) findViewById(R.id.txtLoginEmail);
        passwordView = (EditText) findViewById(R.id.txtLoginPassword);
        dbHelper = new DbHelper(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        String u="",p="";
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            if(data.hasExtra("email") && data.hasExtra("pass")) {
                u = data.getExtras().getString("email");
                p = data.getExtras().getString("pass");
            }
            if(!u.equals(".")) {
                emailView.setText(u);
                passwordView.setText(p);
                Toast.makeText(this, getString(R.string.ok_register), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,getString(R.string.erro_register),Toast.LENGTH_LONG).show();
            }
        }
    }


    public void register(View v){
        Intent newActivity = new Intent(this, Register.class);
        startActivityForResult(newActivity, REQUEST_CODE);
    }

    public void sigInLogin(View v) throws InterruptedException{
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)){
            emailView.setError(getString(R.string.field_required));
            focusView = emailView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)){
            passwordView.setError(getString(R.string.field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPassword(email,password)){
            passwordView.setError(getString(R.string.field_incorrect));
            focusView = passwordView;
            cancel = true;
        }
        if (cancel){
            focusView.requestFocus();
        }else {
            register();
        }
    }

    private boolean isPassword(String sEmail, String pass){

        db = dbHelper.getWritableDatabase();
        Cursor search=db.rawQuery("select * from "+StatusContract.TABLE_USER+" where email='"+sEmail+"'",null);
        String validation="";
        if (search.moveToFirst()){
            do {
                validation = search.getString(8);
            }while (search.moveToNext());
        }
        db.close();
        if (pass.equals(validation)){return true;}
        return false;
    }

    private void register() throws InterruptedException{
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StatusContract.Column_Login.ID,(1));
        values.put(StatusContract.Column_Login.EMAIL, emailView.getText().toString());
        Log.d("tag",emailView.getText().toString());
        db.insertWithOnConflict(StatusContract.TABLE_LOGIN, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
        Intent newActivity = new Intent(this, MainActivity.class);
        startActivity(newActivity);
        finish();
    }

}
