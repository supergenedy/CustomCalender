package com.supergenedy.customcalender;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by Ahmed El Genedy on 09-Sep-19.
 * Copyright (C) 2019 SuperGenedy. supergenedy@gmail.com All Rights Reserved.
 */
public class CalenderView extends LinearLayout {
    // default date format
    private static final String DATE_FORMAT = "MMMM yyyy";
    SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_FORMAT);


    // internal components
    private ImageView btnPrev;
    private ImageView btnNext;
    private TextView txtDate;
    private RecyclerView recyclerView;


    //Calender all Months
    ArrayList<String> calenderDateList = new ArrayList<>();
    //Position of Selected Month
    int selectedDatePosition;

    //Days of Selected Month
    ArrayList<Date> monthDays = new ArrayList<>();


    //event handling
    private EventHandler eventHandler = null;

    HashSet<Date> allEvents = new HashSet<>();
    HashSet<Date> disableDays = new HashSet<>();
    HashSet<Date> unavailableDates = new HashSet<>();
    HashSet<Date> fullyBookedDays = new HashSet<>();

    public CalenderView(Context context) {
        super(context);
    }

    public CalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    public CalenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    /**
     * Load component XML layout
     */
    private void initControl(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_calender, this);

        assignUiElements();
        assignClickHandlers();

    }


    private void assignUiElements() {
        // layout is inflated, assign local variables to components
        btnPrev = findViewById(R.id.calendar_prev_button);
        btnNext = findViewById(R.id.calendar_next_button);
        txtDate = findViewById(R.id.calendar_date_display);
        recyclerView = findViewById(R.id.calendar_grid);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
    }

    private void assignClickHandlers() {
        // add one month and refresh UI
        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle long-press
                if (eventHandler == null) return;

                selectedDatePosition++;

                if (selectedDatePosition == calenderDateList.size() - 1) {
                    btnNext.setImageResource(R.drawable.forward_disabled_icon);
                    btnNext.setEnabled(false);
                }
                txtDate.setText(calenderDateList.get(selectedDatePosition));
                btnPrev.setImageResource(R.drawable.previous_icon);
                btnPrev.setEnabled(true);

                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateFormater.parse(calenderDateList.get(selectedDatePosition)));

                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    int myMonth = cal.get(Calendar.MONTH);


                    monthDays.clear();
                    while (myMonth == cal.get(Calendar.MONTH)) {
                        monthDays.add(cal.getTime());
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        if (cal.getTime().before(startDate.getTime())) {
                            disableDays.add(cal.getTime());
                        } else if (cal.getTime().after(endDate.getTime())) {
                            disableDays.add(cal.getTime());
                        }
                    }

                    updateAdapter();


                    SimpleDateFormat mFormatter = new SimpleDateFormat("MM");
                    SimpleDateFormat yFormatter = new SimpleDateFormat("yyyy");

                    eventHandler.OnMonthChange(Integer.valueOf(mFormatter.format(dateFormater.parse(calenderDateList.get(selectedDatePosition))))
                            , Integer.valueOf(yFormatter.format(dateFormater.parse(calenderDateList.get(selectedDatePosition)))));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        // subtract one month and refresh UI
        btnPrev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventHandler == null) return;

                selectedDatePosition--;

                if (selectedDatePosition == 0) {
                    btnPrev.setImageResource(R.drawable.previous_disabled_icon);
                    btnPrev.setEnabled(false);
                }
                txtDate.setText(calenderDateList.get(selectedDatePosition));
                btnNext.setImageResource(R.drawable.forward_icon);
                btnNext.setEnabled(true);

                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateFormater.parse(calenderDateList.get(selectedDatePosition)));

                    cal.set(Calendar.DAY_OF_MONTH, 1);
                    int myMonth = cal.get(Calendar.MONTH);


                    monthDays.clear();
                    while (myMonth == cal.get(Calendar.MONTH)) {
                        monthDays.add(cal.getTime());
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        if (cal.getTime().before(startDate.getTime())) {
                            disableDays.add(cal.getTime());
                        } else if (cal.getTime().after(endDate.getTime())) {
                            disableDays.add(cal.getTime());
                        }
                    }

                    updateAdapter();

                    SimpleDateFormat mFormatter = new SimpleDateFormat("MM");
                    SimpleDateFormat yFormatter = new SimpleDateFormat("yyyy");

                    eventHandler.OnMonthChange(Integer.valueOf(mFormatter.format(dateFormater.parse(calenderDateList.get(selectedDatePosition))))
                            , Integer.valueOf(yFormatter.format(dateFormater.parse(calenderDateList.get(selectedDatePosition)))));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void setEventsDays(HashSet<Date> events) {
        this.allEvents.addAll(events);
        if (recyclerView != null && monthDays.size() > 0) updateAdapter();
    }

    public void setDisableDays(HashSet<Date> disabled) {
        this.disableDays.addAll(disabled);
        if (recyclerView != null && monthDays.size() > 0) updateAdapter();
    }

    public void setUnavailableDays(HashSet<Date> unavailable) {
        this.unavailableDates.addAll(unavailable);
        if (recyclerView != null && monthDays.size() > 0) updateAdapter();
    }

    public void setFullyBookedDays(HashSet<Date> fullyBooked) {
        this.fullyBookedDays.addAll(fullyBooked);
        if (recyclerView != null && monthDays.size() > 0) updateAdapter();
    }

    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();

    /**
     * Display dates correctly in grid
     */
    public void updateCalendar(Date minDate, Date maxDate) {
        //Calender Start and Finish Date
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        if (minDate != null) {
            beginCalendar.setTime(minDate);
            startDate.setTime(minDate);
        }
        if (maxDate != null) {
            finishCalendar.setTime(maxDate);
            endDate.setTime(maxDate);
        }

        do {
            // add one month to date per loop
            calenderDateList.add(dateFormater.format(beginCalendar.getTime()));
            beginCalendar.add(Calendar.MONTH, 1);
        } while (beginCalendar.before(finishCalendar));

        selectedDatePosition = calenderDateList.indexOf(dateFormater.format(Calendar.getInstance().getTime()));


        if (calenderDateList.size() <= selectedDatePosition + 1) {
            btnNext.setImageResource(R.drawable.forward_disabled_icon);
            btnNext.setEnabled(false);
        }

        if (selectedDatePosition == 0) {
            btnPrev.setImageResource(R.drawable.previous_disabled_icon);
            btnPrev.setEnabled(false);
        }


        txtDate.setText(calenderDateList.get(selectedDatePosition));


        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormater.parse(calenderDateList.get(selectedDatePosition)));

            cal.set(Calendar.DAY_OF_MONTH, 1);
            int myMonth = cal.get(Calendar.MONTH);


            monthDays.clear();
            while (myMonth == cal.get(Calendar.MONTH)) {
                monthDays.add(cal.getTime());
                cal.add(Calendar.DAY_OF_MONTH, 1);
                if (cal.getTime().before(startDate.getTime())) {
                    disableDays.add(cal.getTime());
                } else if (cal.getTime().after(endDate.getTime())) {
                    disableDays.add(cal.getTime());
                }
            }

            updateAdapter();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateAdapter() {
        recyclerView.setAdapter(new CalenderAdapter(getContext(), monthDays, allEvents, disableDays, unavailableDates, fullyBookedDays));
    }


    private class CalenderAdapter extends RecyclerView.Adapter {

        Context context;
        ArrayList<Date> calenderDates;

        HashSet<Date> eventDays;
        HashSet<Date> disableDays;
        HashSet<Date> unavailableDays;
        HashSet<Date> fullyBooked;


        TextView mLastTV;
        boolean mlastIsBooked;
        int mlastDay, mlastMonth;

        public CalenderAdapter(Context context, ArrayList<Date> calenderCells, HashSet<Date> events, HashSet<Date> disableDays, HashSet<Date> unavailableDays, HashSet<Date> fullyBooked) {
            this.context = context;
            this.eventDays = events;
            this.disableDays = disableDays;
            this.unavailableDays = unavailableDays;
            this.fullyBooked = fullyBooked;

            calenderDates = new ArrayList<>();

            Calendar date = Calendar.getInstance();
            date.setTime(calenderCells.get(0));

            switch (date.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    break;
                case Calendar.MONDAY:
                    calenderDates.add(new Date(1800, 10, 10));
                    break;
                case Calendar.TUESDAY:
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    break;
                case Calendar.WEDNESDAY:
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    break;
                case Calendar.THURSDAY:
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    break;
                case Calendar.FRIDAY:
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    break;
                case Calendar.SATURDAY:
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    calenderDates.add(new Date(1800, 10, 10));
                    break;
            }

            this.calenderDates.addAll(calenderCells);
        }

        class CalenderViewHolder extends RecyclerView.ViewHolder {

            CalenderViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_calender, parent, false);
            return new CalenderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Date dateItem = calenderDates.get(position);
            final Calendar date = Calendar.getInstance();
            date.setTime(dateItem);

            final int day = date.get(Calendar.DAY_OF_MONTH);
            final int month = date.get(Calendar.MONTH);
            int year = date.get(Calendar.YEAR);

            boolean isFullyBooked;

            isFullyBooked = false;

            final TextView textView = (TextView) holder.itemView;

            // set text
            textView.setText(String.valueOf(day));


            // this day is outside current month, hide it
            if ((position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5) && day == 10) {
                textView.setVisibility(INVISIBLE);
            }

            // if this day has an event, set event dot
            if (eventDays != null) {
                for (Date eventDate : eventDays) {
                    Calendar eventD = Calendar.getInstance();
                    eventD.setTime(eventDate);
                    if (eventD.get(Calendar.DAY_OF_MONTH) == day
                            && eventD.get(Calendar.MONTH) == month
                            && eventD.get(Calendar.YEAR) == year) {
                        // mark this day for event
                        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_dot_brown);
                        break;
                    }
                }
            }

            // if this day is disabled, specify disabled shape
            if (disableDays != null) {
                for (Date disabledDate : disableDays) {
                    Calendar disabledCal = Calendar.getInstance();
                    disabledCal.setTime(disabledDate);
                    if (disabledCal.get(Calendar.DAY_OF_MONTH) == day
                            && disabledCal.get(Calendar.MONTH) == month
                            && disabledCal.get(Calendar.YEAR) == year) {
                        // mark this day for event
                        textView.setAlpha((float) 0.5);
                        textView.setEnabled(false);
                        break;
                    }
                }
            }

            // if this day has unavailable, specify closed shape
            if (unavailableDays != null) {
                for (Date unavailableDate : unavailableDays) {
                    Calendar unavailableCal = Calendar.getInstance();
                    unavailableCal.setTime(unavailableDate);
                    if (unavailableCal.get(Calendar.DAY_OF_MONTH) == day
                            && unavailableCal.get(Calendar.MONTH) == month
                            && unavailableCal.get(Calendar.YEAR) == year) {
                        // mark this day for event
                        textView.setAlpha((float) 0.5);
                        textView.setTextColor(context.getResources().getColor(R.color.brownColor));
                        break;
                    }
                }
            }

            // if this day has Fully Booked, specify closed shape
            if (fullyBooked != null) {
                for (Date fullyBookedDate : fullyBooked) {
                    Calendar fullyBookedC = Calendar.getInstance();
                    fullyBookedC.setTime(fullyBookedDate);
                    if (fullyBookedC.get(Calendar.DAY_OF_MONTH) == day
                            && fullyBookedC.get(Calendar.MONTH) == month
                            && fullyBookedC.get(Calendar.YEAR) == year) {
                        // mark this day for event
                        textView.setAlpha((float) 0.5);
                        textView.setBackgroundResource(R.drawable.shape_corner_closed);
                        isFullyBooked = true;
                        break;
                    }
                }
            }

            // if it is today, set its shape
            if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    && month == Calendar.getInstance().get(Calendar.MONTH)) {
                textView.setBackgroundResource(R.drawable.shape_corner_today);
            }


            final boolean finalIsBooked = isFullyBooked;
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLastTV != null)
                        deSelect(mLastTV, mlastDay, mlastMonth, mlastIsBooked);

                    mlastDay = day;
                    mlastMonth = month;
                    mlastIsBooked = finalIsBooked;
                    mLastTV = textView;
                    select(textView);
                    eventHandler.OnDateSelected(dateItem);
                }
            });
        }

        private void select(TextView textView) {
            textView.setTypeface(ResourcesCompat.getFont(context, R.font.tstar_pro_bold));
            textView.setBackgroundResource(R.drawable.shape_corner_selected);
        }

        private void deSelect(TextView textView, int day, int month, boolean isBooked) {
            textView.setTypeface(ResourcesCompat.getFont(context, R.font.tstar_pro_regular));
            if (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    && month == Calendar.getInstance().get(Calendar.MONTH)) {
                textView.setBackgroundResource(R.drawable.shape_corner_today);
            } else if (isBooked) {
                textView.setBackgroundResource(R.drawable.shape_corner_closed);
            } else {
                textView.setBackgroundResource(0);
            }
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return calenderDates.size();
        }
    }

    /**
     * Assign event handler to pass needed events
     */
    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * This interface defines what events to be reported
     */
    public interface EventHandler {
        void OnDateSelected(Date date);

        void OnMonthChange(int month, int year);
    }
}
