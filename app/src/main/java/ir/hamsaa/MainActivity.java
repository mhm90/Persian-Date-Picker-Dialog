package ir.hamsaa;


import android.app.PersianDatePicker;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.app.PersianDatePickerDialog;

import java.util.Calendar;
import java.util.PersianCalendar;


public class MainActivity extends AppCompatActivity {

    private PersianDatePickerDialog picker;

    private TextView mTimeText, mTimePrevText;

    private long mTimePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimeText = findViewById(R.id.text_time);
        mTimePrevText = findViewById(R.id.text_time_prev);
    }


    public void showCalendar(View v) {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Shabnam-Light-FD.ttf");

//        PersianCalendar initDate = new PersianCalendar();
//        initDate.setPersianDate(1370, 3 - 1, 13);

        PersianCalendar initDate = new PersianCalendar(mTimePicked > 0 ? mTimePicked : System.currentTimeMillis());

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
                        PersianCalendar pCal = new PersianCalendar(mTimePicked > 0 ? mTimePicked : System.currentTimeMillis());
                        pCal.setPersianDate(persianCalendar.getPersianYear(), persianCalendar.getPersianMonth(), persianCalendar.getPersianDay());
                        mTimePicked = pCal.getTimeInMillis();

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(mTimePicked);

                        refreshTexts(
                                "Picked time: \n" + persianCalendar.getPersianLongDateAndTime() + "\n" + persianCalendar.getTime() + "\n\n" +
                                "Updated Time: \n" + pCal.getPersianLongDateAndTime() + "\n" + pCal.getTime() + getUtcManualTime(pCal) + "\n\n" +
                                "Gregorian Time: \n" + cal.getTime() + getUtcManualTime(cal)
                        );

                        Toast.makeText(MainActivity.this, persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonthName() + "/" + persianCalendar.getPersianDay(), Toast.LENGTH_SHORT).show();
                    }



                    @Override
                    public void onDismissed() {
                    }
                });

        picker.show();
    }

    private String getUtcManualTime(Calendar cal) {
        int millis = (int) (cal.getTimeInMillis() % (24*3600000));
        return " (utc=" + (millis / 3600000) + ":" + ((millis % 3600000) / 60000) + " )";
    }

    public void showTimePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        if (mTimePicked > 0)
            calendar.setTimeInMillis(mTimePicked);
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                if (mTimePicked > 0)
                    cal.setTimeInMillis(mTimePicked);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                mTimePicked = cal.getTimeInMillis();

                PersianCalendar pCal = new PersianCalendar(mTimePicked);

                refreshTexts(
                        "Updated Time: \n" + pCal.getPersianLongDateAndTime() + "\n" + pCal.getTime() + getUtcManualTime(pCal) + "\n\n" +
                                "Gregorian Time: \n" + cal.getTime() + getUtcManualTime(cal)
                );
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    private void refreshTexts(String s) {
        mTimePrevText.setText(mTimeText.getText());
        mTimeText.setText(s);
    }

}
