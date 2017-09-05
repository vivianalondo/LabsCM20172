package co.edu.udea.compumovil.gr06_20172.lab1;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


/**
 * Created by Viviana Londoño on 22/08/2017.
 */

public class Register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private TextView txt;
    private Button btn;
    private Bitmap picture;
    private boolean control=false;
    private static final int REQUEST_CODE_GALLERY=1;
    private ImageView targetImage;
    EditText[] txtValidate = new EditText[9];
    DbHelper dbH;
    SQLiteDatabase db;
    private RadioGroup grupo;
    String option = "";
    String optionSelect = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {//graba los datos de los usuarios
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Se usa autocompletar para hacer uso en la selección de ciudades
        AutoCompleteTextView ciudad = (AutoCompleteTextView) findViewById(R.id.txtRegisterCity);
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        ciudad.setAdapter(adapter);
        Log.d("tag1","acá en el on create");

        picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_profile);
        txtValidate[0]=(EditText)findViewById(R.id.txtRegisterEmail);
        txtValidate[1]=(EditText)findViewById(R.id.textRegisterPass);
        txtValidate[2]=(EditText)findViewById(R.id.txtRegisterPassConf);
        txtValidate[3]=(EditText)findViewById(R.id.txtRegisterName);
        txtValidate[4]=(EditText)findViewById(R.id.txtRegisterLastName);
        txtValidate[5]=(EditText)findViewById(R.id.txtRegisterDate);
        txtValidate[6]=(EditText)findViewById(R.id.txtRegisterPhone);
        txtValidate[7]=(EditText)findViewById(R.id.txtRegisterAddress);
        txtValidate[8]=(EditText)findViewById(R.id.txtRegisterCity);
        grupo = (RadioGroup) findViewById(R.id.rbRegisterGender);

        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                optionSelect = itemChecked (checkedId);
                Log.d("tag1","acá adentro");
                Log.d("tag1", optionSelect);
            }
        });

        dbH = new DbHelper(this);
        btn = (Button)findViewById(R.id.btnEnviarRegistro);
        btn.setEnabled(false);
        TextWatcher btnActivation = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(verificarVaciosSinMessage(txtValidate)){btn.setEnabled(true);}
                else{btn.setEnabled(false);}
            }
        };
        for (int n = 0; n < txtValidate.length; n++)
        {
            txtValidate[n].addTextChangedListener(btnActivation);
        }
        targetImage = (ImageView)findViewById(R.id.profilePicture);
        targetImage.setImageBitmap(picture);


    }



    @Override
    public void finish() {//terminar la operación activity validando los dos campos usuario y password
        Intent data = new Intent();
        if(control) {
            data.putExtra("email", txtValidate[0].getText().toString());
            data.putExtra("pass", txtValidate[2].getText().toString());
        } else{
            data.putExtra("email", ".");
            data.putExtra("pass", ".");
        }
        setResult(RESULT_OK,data);
        super.finish();
    }

    public void Validar(View v){
        View focusView=null;
        if (!verificarVacios(txtValidate)){
        }else if(!txtValidate[0].getText().toString().contains("@")){
            txtValidate[0].setError(getString(R.string.invalid_mail));
            focusView = txtValidate[0];
        }else{
            if (!txtValidate[2].getText().toString().equals(txtValidate[1].getText().toString())){
                txtValidate[2].setError(getString(R.string.pass_no_equals));
                focusView = txtValidate[2];
            }else if(!existEmail(txtValidate[0].getText().toString())){
                db = dbH.getWritableDatabase();
                ContentValues values = new ContentValues();
                Cursor search = db.rawQuery("select count(*) from usuario", null);
                search.moveToFirst();
                int aux=Integer.parseInt(search.getString(0));
                values.put(StatusContract.Column_User.ID,(aux+1));
                values.put(StatusContract.Column_User.MAIL, txtValidate[0].getText().toString());
                values.put(StatusContract.Column_User.PASS, txtValidate[1].getText().toString());
                values.put(StatusContract.Column_User.NAME, txtValidate[3].getText().toString());
                values.put(StatusContract.Column_User.LASTNAME, txtValidate[4].getText().toString());
                values.put(StatusContract.Column_User.DATE, txtValidate[5].getText().toString());
                values.put(StatusContract.Column_User.PHONE, txtValidate[6].getText().toString());
                values.put(StatusContract.Column_User.ADDRESS, txtValidate[7].getText().toString());
                values.put(StatusContract.Column_User.CITY, txtValidate[8].getText().toString());
                values.put(StatusContract.Column_User.GENDER, optionSelect);
                values.put(StatusContract.Column_User.PICTURE, getBitmapAsByteArray(picture));
                db.insertWithOnConflict(StatusContract.TABLE_USER, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                db.close();
                control=true;
                finish();
            }
            else
            {
                txtValidate[0].setError(getString(R.string.user_exists));
                focusView = txtValidate[0];
            }
        }

    }


    /**
     * Método para verificar si el email existe
     * @param sEmail
     * @return
     */
    public boolean existEmail(String sEmail)//verifiacion de nombre de usuario
    {
        db = dbH.getWritableDatabase();
        Cursor nick=db.rawQuery("select * from "+StatusContract.TABLE_USER+" where email='"+sEmail+"'", null);
        if (nick.moveToFirst()) {
            db.close();
            return true;
        }
        return false;
    }

    /**
     * Método para verificar vacíos
     * @param txtValidate
     * @return
     */
    public boolean verificarVacios(EditText[] txtValidate)//verificacion de campo requerido
    {
        View focus=null;
        for(int i=0; i<txtValidate.length;i++)
        {
            if((txtValidate[i].getText().toString()).isEmpty())
            {
                txtValidate[i].setError(getString(R.string.field_required));
                focus = txtValidate[i];
                return false;
            }
        }
        return true;
    }

    /**
     * Método para verificar vacíos sin mensaje
     * @param txtValidate
     * @return
     */
    public boolean verificarVaciosSinMessage(EditText[] txtValidate)
    {
        View focus=null;
        for(int i=0; i<txtValidate.length;i++)
        {
            if((txtValidate[i].getText().toString()).isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Método para hacer la llamada externa de la aplicación a la galería
     * @param v
     */
    public void ClickGallery(View v) {//llamada externa de la aplicacion a galeria
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode==REQUEST_CODE_GALLERY )){
            try {
                Uri targetUri = data.getData();
                picture = redimensionarImagenMaximo(BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri)),350,350);
                targetImage.setImageBitmap(picture);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Método para obtener el arreglo de bitmap
     * @param bitmap
     * @return
     */
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Método para redimensionar el tamaño de la imagen
     * @param mBitmap
     * @param newWidth
     * @param newHeigth
     * @return
     */
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){//tamaño de la imagen
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    //Este metodo es para el uso del datePicker la vista
    public void onEditSet(View v){
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //se usa para implementar el metodo DatePicker
        EditText fecha  = (EditText)findViewById(R.id.txtRegisterDate);
        fecha.setText(new StringBuilder().append(year).append("/").append(monthOfYear).append("/").append(dayOfMonth));
    }

    private String itemChecked (int id) {
        option = "vacio";
        RadioButton item1 = (RadioButton) findViewById (R.id.rbRegisterGenderF);
        RadioButton item2 = (RadioButton) findViewById (R.id.rbRegisterGenderM);

        // Compruebo si el id coincide con alguno de los RadioButton
        if (item1.getId() == id){
            option = "Femenino";
        }
        if (item2.getId() == id){
            option = "Masculino";
        }
        return(option);
    }

}
