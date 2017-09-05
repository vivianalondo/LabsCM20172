package co.edu.udea.compumovil.gr06_20172.lab1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Viviana Londoño on 24/08/2017.
 */

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class InformationFragment extends Fragment {

    private ImageView targetImageR;
    DbHelper dbHelper;
    SQLiteDatabase db;
    TextView[] txtValidateR = new TextView[8];
    View view;

    public InformationFragment() {//activity que enseña información

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dbHelper =new DbHelper(getActivity().getBaseContext());

        view=inflater.inflate(R.layout.fragment_information, container, false);
        txtValidateR[0]=(TextView)view.findViewById(R.id.viewName);
        txtValidateR[1]=(TextView)view.findViewById(R.id.viewLastName);
        txtValidateR[2]=(TextView)view.findViewById(R.id.viewEmail);
        txtValidateR[3]=(TextView)view.findViewById(R.id.viewGender);
        txtValidateR[4]=(TextView)view.findViewById(R.id.viewDate);
        txtValidateR[5]=(TextView)view.findViewById(R.id.viewPhone);
        txtValidateR[6]=(TextView)view.findViewById(R.id.viewAddress);
        txtValidateR[7]=(TextView)view.findViewById(R.id.viewCity);
        targetImageR=(ImageView)view.findViewById(R.id.viewProfile);
        db= dbHelper.getWritableDatabase();
        Cursor search = db.rawQuery("select * from " + StatusContract.TABLE_LOGIN, null);

        search.moveToFirst();
        String aux = search.getString(1);
        Log.d("tag",aux);
        search = db.rawQuery("select * from "+StatusContract.TABLE_USER+ " where "+StatusContract.Column_User.MAIL+"='"+aux+"'", null);
        search.moveToFirst();
        Log.d("prueba",search.getString(1));
        txtValidateR[0].setText("Nombre: "+search.getString(2));
        txtValidateR[1].setText("Apellido: "+search.getString(3));
        txtValidateR[2].setText("E-mail:"+search.getString(1));
        txtValidateR[3].setText("Género:"+search.getString(4));
        txtValidateR[4].setText("Fecha de nacimiento:"+search.getString(5));
        txtValidateR[5].setText("Teléfono:"+search.getString(6));
        txtValidateR[6].setText("Dirección:"+search.getString(7));
        txtValidateR[7].setText("Ciudad:"+search.getString(9));
        byte[] auxx=search.getBlob(5);
        Bitmap pict= BitmapFactory.decodeByteArray(auxx, 0, (auxx).length);
        targetImageR.setImageBitmap(pict);
        db.close();
        return view;
    }
}
