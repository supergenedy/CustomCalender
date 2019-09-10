package com.supergenedy.customcalender;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    CalenderView cv;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    HashSet<Date> eventsDays = new HashSet<>();
    HashSet<Date> disableDays = new HashSet<>();
    HashSet<Date> unavailableDates = new HashSet<>();
    HashSet<Date> fullyBookedDates = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv = findViewById(R.id.calendar_view);

        try {
            eventsDays.add(dateFormat.parse("2019-9-5"));
            cv.setEventsDays(eventsDays);


            disableDays.add(dateFormat.parse("2019-9-7"));
            disableDays.add(dateFormat.parse("2019-9-20"));
            disableDays.add(dateFormat.parse("2019-9-15"));
            disableDays.add(dateFormat.parse("2019-9-23"));
            cv.setDisableDays(disableDays);


            unavailableDates.add(dateFormat.parse("2019-9-3"));
            unavailableDates.add(dateFormat.parse("2019-9-25"));
            unavailableDates.add(dateFormat.parse("2019-9-17"));
            unavailableDates.add(dateFormat.parse("2019-9-28"));
            cv.setUnavailableDays(unavailableDates);

            fullyBookedDates.add(dateFormat.parse("2019-9-1"));
            fullyBookedDates.add(dateFormat.parse("2019-9-5"));
            fullyBookedDates.add(dateFormat.parse("2019-9-20"));
            fullyBookedDates.add(dateFormat.parse("2019-9-30"));
            cv.setFullyBookedDays(fullyBookedDates);


            cv.updateCalendar(dateFormat.parse("2019-08-10"), dateFormat.parse("2019-10-20"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // assign event handler
        cv.setEventHandler(new CalenderView.EventHandler() {
            @Override
            public void OnDateSelected(Date date) {
                // show returned day
                DateFormat df = SimpleDateFormat.getDateInstance();
                Toast.makeText(MainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnMonthChange(int month, int year) {
                Toast.makeText(MainActivity.this, month + "-" + year, Toast.LENGTH_SHORT).show();


                try {

                    eventsDays.add(dateFormat.parse("2019-10-5"));
                    cv.setEventsDays(eventsDays);

                    disableDays.add(dateFormat.parse("2019-10-7"));
                    disableDays.add(dateFormat.parse("2019-10-20"));
                    disableDays.add(dateFormat.parse("2019-10-15"));
                    disableDays.add(dateFormat.parse("2019-10-23"));
                    cv.setDisableDays(disableDays);


                    unavailableDates.add(dateFormat.parse("2019-10-3"));
                    unavailableDates.add(dateFormat.parse("2019-10-25"));
                    unavailableDates.add(dateFormat.parse("2019-10-17"));
                    unavailableDates.add(dateFormat.parse("2019-10-28"));
                    cv.setUnavailableDays(unavailableDates);

                    fullyBookedDates.add(dateFormat.parse("2019-10-1"));
                    fullyBookedDates.add(dateFormat.parse("2019-10-5"));
                    fullyBookedDates.add(dateFormat.parse("2019-10-20"));
                    fullyBookedDates.add(dateFormat.parse("2019-10-30"));
                    cv.setFullyBookedDays(fullyBookedDates);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        });


    }
}
