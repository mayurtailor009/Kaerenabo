package com.kaerenabo.customviews;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaerenabo.R;
import com.kaerenabo.adapters.MonthlyCalendarAdapter;
import com.kaerenabo.interfaces.OnCalendarDateSelect;
import com.kaerenabo.interfaces.OnMonthChangeListener;
import com.kaerenabo.models.CalendarDTO;

import java.util.Calendar;
import java.util.Locale;


public class CustomCalendarView extends LinearLayout implements View.OnClickListener {

    private View view;
    private TextView currentMonth;
    private ImageView prevMonth;
    private ImageView nextMonth;
    private ExpandableHeightGridView calendarView;
    private MonthlyCalendarAdapter adapter;
    private Calendar _calendar;
    private int month, year;
    private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM";
    private Context context;
    private OnMonthChangeListener onMonthChangeListener;
    /*public static boolean nextMonthFlag=false;
    public static boolean prevMonthFlag=false;*/

    /**
     * Instantiates a new Custom calendar view.
     *
     * @param context the context
     */
    public CustomCalendarView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Instantiates a new Custom calendar view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context, attrs);
    }

    /**
     * Instantiates a new Custom calendar view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomCalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context, attrs);
    }

    private void initControl(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.calendar_view, this);

        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);

        prevMonth = (ImageView) view.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(this);

        currentMonth = (TextView) view.findViewById(R.id.currentMonth);
        String monthStr = DateFormat.format(dateTemplate,
                _calendar.getTime()).toString();
        currentMonth.setText(monthStr.toUpperCase());

        nextMonth = (ImageView) view.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(this);

        calendarView = (ExpandableHeightGridView) view.findViewById(R.id.calendar);
        calendarView.setExpanded(true);
        // Initialised
        adapter = new MonthlyCalendarAdapter(context,
                month, year);
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);
    }


    /**
     * @param month
     * @param year
     */
    private void setGridCellAdapterToDate(int month, int year) {
          /*JobsDataSource jobsDataSource = new JobsDataSource(context);
        jobsDataSource.open();*/
        adapter.setMonthYear(context, month, year);
                /*new MonthlyCalendarAdapter(context,
                month, year);*/

//        MyCalendarFragment fragment= MyCalendarFragment.newInstance();
//        fragment.startJobLoader(month,year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        String monthStr = DateFormat.format(dateTemplate,
                _calendar.getTime()).toString();
        currentMonth.setText(monthStr.toUpperCase());

        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

        // Set change month and year value in listener
        if (onMonthChangeListener != null) {
            onMonthChangeListener.getMonthChangeDetail(month, year);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextMonth:
                if (month > 11) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                //nextMonth.setEnabled(nextMonthFlag);
                setGridCellAdapterToDate(month, year);
                break;

            case R.id.prevMonth:
                if (month <= 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                //prevMonth.setEnabled(prevMonthFlag);
                setGridCellAdapterToDate(month, year);

                break;

        }
       /* if (v == prevMonth) {
            if (month <= 1) {
                month = 12;
                year--;
            } else {
                month--;
            }

            BaseFragment.showProgDialog((Activity) context,null);
            setGridCellAdapterToDate(month, year);
        }
        if (v == nextMonth) {
            if (month > 11) {
                month = 1;
                year++;
            } else {
                month++;
            }
            BaseFragment.showProgDialog((Activity) context,null);
            setGridCellAdapterToDate(month, year);
        }*/

    }


    /**
     * Gets calendar adapter.
     *
     * @return the calendar adapter
     */
    public MonthlyCalendarAdapter getCalendarAdapter() {
        return adapter;
    }


    /**
     * Sets on calendar date select.
     *
     * @param onCalendarDateSelect the on calendar date select
     */
    public void setOnCalendarDateSelect(OnCalendarDateSelect onCalendarDateSelect) {
        adapter.setOnCalendarDateSelect(onCalendarDateSelect);
    }

    /**
     * Sets month change listener.
     *
     * @param onMonthChangeListener the on month change listener
     */
    public void setMonthChangeListener(OnMonthChangeListener onMonthChangeListener) {
        this.onMonthChangeListener = onMonthChangeListener;
    }


    /**
     * Gets selected date.
     *
     * @return the selected date
     */
    public CalendarDTO getSelectedDate() {
        return adapter.getSelectedDate();
    }

    /**
     * On arrow visibility.
     */
    public void onArrowVisibility() {

        prevMonth.setVisibility(VISIBLE);
        nextMonth.setVisibility(VISIBLE);
    }
}
