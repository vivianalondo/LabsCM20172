package co.edu.udea.compumovil.gr06_20172.lab1;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

/**
 * Created by Viviana Londoño on 25/08/2017.
 */

public class EditFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private TextView txt;
    private Button btn;
    private Bitmap picture;
    private boolean control=false;
    private static final int REQUEST_CODE_GALLERY=1;
    private ImageView targetImage;

    private RadioGroup grupo;
    String option = "";
    String optionSelect = "";

    private ImageView targetImageR;
    DbHelper dbH;
    DbHelper dbHelper;
    SQLiteDatabase db;
    EditText[] txtEditR = new EditText[7];
    View view;
    ImageButton btnEditGallery;
    Button btnEditarRegistro;

    Fragment info = new InformationFragment();
    //FragmentTransaction manager = getSupportFragmentManager().beginTransaction();

    public EditFragment() {//fragmen creado para visualizar el acerca de de la aplicacion
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper =new DbHelper(getActivity().getBaseContext());

        view=inflater.inflate(R.layout.fragment_edit, container, false);

        //Se usa autocompletar para hacer uso en la selección de ciudades
        /**AutoCompleteTextView ciudad = (AutoCompleteTextView) view.findViewById(R.id.txtEditCity);
        String[] cities = getResources().getStringArray(R.array.cities_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        ciudad.setAdapter(adapter);
        Log.d("tag1","acá en el on create");*/

        picture = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_profile);

        txtEditR[2]=(EditText)view.findViewById(R.id.txtEditEmail);
        txtEditR[0]=(EditText)view.findViewById(R.id.txtEditName);
        txtEditR[1]=(EditText)view.findViewById(R.id.txtEditLasName);
        //txtEditR[3]=(EditText)view.findViewById(R.id.viewGender);
        txtEditR[3]=(EditText)view.findViewById(R.id.txtEditDate);
        txtEditR[4]=(EditText)view.findViewById(R.id.txtEditPhone);
        txtEditR[5]=(EditText)view.findViewById(R.id.txtEditAddress);
        txtEditR[6]=(EditText)view.findViewById(R.id.txtEditCity);
        targetImageR=(ImageView)view.findViewById(R.id.profilePicture);
        btnEditGallery = (ImageButton)view.findViewById(R.id.btnEditGallery);
        btnEditarRegistro = (Button) view.findViewById(R.id.btnEditarRegistro);

        grupo = (RadioGroup) view.findViewById(R.id.rbEditGender);

        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                optionSelect = itemChecked (checkedId);
                Log.d("tag1","acá adentro");
                Log.d("tag1", optionSelect);
            }
        });

        db= dbHelper.getWritableDatabase();
        Cursor search = db.rawQuery("select * from " + StatusContract.TABLE_LOGIN, null);

        search.moveToFirst();
        String aux = search.getString(1);
        Log.d("tag",aux);
        search = db.rawQuery("select * from "+StatusContract.TABLE_USER+ " where "+StatusContract.Column_User.MAIL+"='"+aux+"'", null);
        search.moveToFirst();
        Log.d("prueba",search.getString(1));
        txtEditR[0].setText(search.getString(2));
        txtEditR[1].setText(search.getString(3));
        txtEditR[2].setText(search.getString(1));
        //txtEditR[3].setText("Género:"+search.getString(4));
        txtEditR[3].setText(search.getString(5));
        txtEditR[4].setText(search.getString(6));
        txtEditR[5].setText(search.getString(7));
        txtEditR[6].setText(search.getString(9));
        byte[] auxx=search.getBlob(5);
        Bitmap pict= BitmapFactory.decodeByteArray(auxx, 0, (auxx).length);
        targetImageR.setImageBitmap(pict);
        db.close();

        btnEditGallery.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                ClickGallery1(view);
            }
        });

        txtEditR[3].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                onEditSet1(view);
            }
        });


        btnEditarRegistro.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Actualizar(view);
            }
        });

        //dbH = new DbHelper(this);
        btn = (Button)view.findViewById(R.id.btnEditarRegistro);
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
                if(verificarVaciosSinMessageE(txtEditR)){btn.setEnabled(true);}
                else{btn.setEnabled(false);}
            }
        };
        for (int n = 0; n < txtEditR.length; n++)
        {
            txtEditR[n].addTextChangedListener(btnActivation);
        }
        targetImage = (ImageView)view.findViewById(R.id.profilePicture);
        targetImage.setImageBitmap(picture);

        return view;
    }

    /**public void Actualizar(View v){

        int idUsuario = 0;
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null){
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("email", email);
            idUsuario = (int) db.insert(TableUsuarios, null, values);
        }
        db.close();
        return idUsuario;

    }*/

    public void Actualizar(View v){
        View focusView=null;
        if (!verificarVaciosE(txtEditR)){
        }else {
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                Cursor search = db.rawQuery("select count(*) from usuario", null);
                search.moveToFirst();
                int aux=Integer.parseInt(search.getString(0));
                //values.put(StatusContract.Column_User.ID,(aux+1));
                //values.put(StatusContract.Column_User.MAIL, txtEditR[0].getText().toString());
                //values.put(StatusContract.Column_User.PASS, txtEditR[1].getText().toString());
                values.put(StatusContract.Column_User.NAME, txtEditR[0].getText().toString());
                values.put(StatusContract.Column_User.LASTNAME, txtEditR[1].getText().toString());
                values.put(StatusContract.Column_User.DATE, txtEditR[3].getText().toString());
                values.put(StatusContract.Column_User.PHONE, txtEditR[4].getText().toString());
                values.put(StatusContract.Column_User.ADDRESS, txtEditR[5].getText().toString());
                values.put(StatusContract.Column_User.CITY, txtEditR[6].getText().toString());
                values.put(StatusContract.Column_User.GENDER, optionSelect);
                values.put(StatusContract.Column_User.PICTURE, getBitmapAsByteArray(picture));
                db.update(StatusContract.TABLE_USER, values, "_id="+aux, null);
                db.close();
                control=true;
                finish();

        }

    }

    public void finish() {//terminar la operación activity validando los dos campos usuario y password
        //public static Toast makeText (Context context, CharSequence text, int duration)
        //public static Toast toast = Toast.makeText(EditFragment.class, "Se ha modificado el registro", Toast.LENGTH_SHORT).show();
        Toast.makeText(EditFragment.this.getActivity(), "@string/mensaje_edicion", Toast.LENGTH_SHORT).show();
        //btn = (Button)view.findViewById(R.id.btnEditarRegistro);
        btn.setEnabled(false);
        txtEditR[0].setEnabled(false);
        txtEditR[1].setEnabled(false);
        txtEditR[2].setEnabled(false);
        txtEditR[3].setEnabled(false);
        txtEditR[4].setEnabled(false);
        txtEditR[5].setEnabled(false);
        txtEditR[6].setEnabled(false);
        btnEditGallery.setEnabled(false);
        RadioButton item1 = (RadioButton) view.findViewById (R.id.rbEditGenderF);
        RadioButton item2 = (RadioButton) view.findViewById (R.id.rbEditGenderM);
        grupo.setEnabled(false);
        item1.setEnabled(false);
        item2.setEnabled(false);
    }

    public boolean verificarVaciosE(EditText[] txtValidate)//verificacion de campo requerido
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
    public boolean verificarVaciosSinMessageE(EditText[] txtValidate)
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
    public void ClickGallery1(View v) {//llamada externa de la aplicacion a galeria
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    /**
     * Método para obtener el arreglo de birmap
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
    public void onEditSet1(View v){
        android.app.DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //se usa para implementar el metodo DatePicker
        EditText fecha  = (EditText)view.findViewById(R.id.txtEditDate);
        fecha.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
    }

    private String itemChecked (int id) {
        option = "vacio";
        RadioButton item1 = (RadioButton) view.findViewById (R.id.rbEditGenderF);
        RadioButton item2 = (RadioButton) view.findViewById (R.id.rbEditGenderM);

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
