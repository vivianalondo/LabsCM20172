package co.edu.udea.compumovil.gr06_20172.lab1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by dfrancisco.hernandez on 16/08/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    //se crea la clase DatePicker para hacer uso de la fecha con una de las clases que android utiliza.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Set the current date in the DatePickerFragment
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());
        //((DatePickerDialog.OnDateSetListener) getActivity()).onDateSet(view,year,monthOfYear,dayOfMonth);
    }
}