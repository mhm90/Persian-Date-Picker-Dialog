package ir.hamsaa;


import android.app.PersianDatePicker;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import android.app.PersianDatePickerDialog;

import java.util.PersianCalendar;


public class MainActivity extends AppCompatActivity {

    private PersianDatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void showCalendar(View v) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Shabnam-Light-FD.ttf");

        PersianCalendar initDate = new PersianCalendar();
        initDate.setPersianDate(1370, 3 - 1, 13);

        picker = new PersianDatePickerDialog(this)
                .setPositiveButtonString("باشه")
                .setNegativeButton("بیخیال")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setMinYear(1300)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setInitDate(initDate)
                .setActionTextColor(Color.GRAY)
                .setTypeFace(typeface)
                .setOnDateSetListener(new PersianDatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(PersianDatePicker datePickerView, PersianCalendar persianCalendar) {
                        Toast.makeText(MainActivity.this, persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonthName() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        picker.show();
    }

}
