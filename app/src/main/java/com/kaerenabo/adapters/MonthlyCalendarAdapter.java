package com.kaerenabo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaerenabo.R;
import com.kaerenabo.interfaces.OnCalendarDateSelect;
import com.kaerenabo.models.CalendarDTO;
import com.kaerenabo.utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * The type Monthly calendar adapter.
 */
public class MonthlyCalendarAdapter extends BaseAdapter  {

    private static final String tag = "MonthlyCalendarAdapter";
    private final Context _context;
    private Calendar calendar;
    private ArrayList<CalendarDTO> dateList;
    private static final int DAY_OFFSET = 1;
    private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
            "Wed", "Thu", "Fri", "Sat"};
    private final String[] months = {"January", "February", "March",
            "April", "May", "June", "July", "August", "September",
            "October", "November", "December"};
    private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31};
    private final int trails[] = {6, 0, 1, 2, 3, 4, 5};
    private int daysInMonth;
    private int currentDayOfMonth;
    private int currentWeekDay;
    //private TextView num_events_per_day;
    private final HashMap<String, Integer> eventsPerMonthMap;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
            "dd-MMM-yyyy");
    private OnCalendarDateSelect onCalendarDateSelect;

    /**
     * Instantiates a new Monthly calendar adapter.
     *
     * @param context the context
     * @param month   the month
     * @param year    the year
     */
    public MonthlyCalendarAdapter(Context context,
                                  int month, int year) {
        super();
        this._context = context;
        this.dateList = new ArrayList<CalendarDTO>();

        calendar = Calendar.getInstance();
        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));


        // Print Month
        //printMonth(month, year);
        dateList = DateUtils.getMonthlyDateList(month, year);

        // Find Number of Events
        eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
    }

    /**
     * Set month year array list.
     *
     * @param _context the context
     * @param month    the month
     * @param year     the year
     * @return the array list
     */
    public ArrayList<CalendarDTO> setMonthYear(Context _context,int month,int year){

           return dateList = DateUtils.getMonthlyDateList(month, year);
    }

    private String getMonthAsString(int i) {
        return months[i];
    }

    private String getWeekDayAsString(int i) {
        return weekdays[i];
    }

    private int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    public CalendarDTO getItem(int position) {
        return dateList.get(position);
    }

    @Override
    public int getCount() {
        return dateList.size();
    }


    /**
     * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
     * ALL entries from a SQLite database for that month. Iterate over the
     * List of All entries, and get the dateCreated, which is converted into
     * day.
     *
     * @param year
     * @param month
     * @return
     */
    private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                int month) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        return map;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CalendarDTO calendarDTO = dateList.get(position);

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_monthly_calendar, parent, false);
        }

        // Get a reference to the Day gridcell
        TextView tvDay = (TextView) row.findViewById(R.id.tv_date);

        LinearLayout llCalendar = (LinearLayout) row.findViewById(R.id.ll_calendar);
        View viewStrip = row.findViewById(R.id.view_strip);
        if (calendarDTO.isHasEvent())
            viewStrip.setVisibility(View.VISIBLE);
        else
            viewStrip.setVisibility(View.INVISIBLE);

        llCalendar.setTag(position);
        llCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                setDateSelected(pos);
            }
        });

        // Set the Day GridCell
        tvDay.setText(calendarDTO.getDate() + "");

        if (calendarDTO.isSelected()) { // CURRENT DATE
            llCalendar.setSelected(true);
        }
        else if (calendarDTO.getOperation().equals("INVISIBLE")) {
            llCalendar.setVisibility(View.INVISIBLE);
            viewStrip.setVisibility(View.INVISIBLE);
            llCalendar.setSelected(false);
        } else{
            llCalendar.setSelected(false);
        }
        /*if(calendarDTO.getDate() == calendar.get(Calendar.DAY_OF_MONTH)){
            llCalendar.setEnabled(false);
        }*/
        return row;
    }

    /**
     * Gets current day of month.
     *
     * @return the current day of month
     */
    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    /**
     * Sets current week day.
     *
     * @param currentWeekDay the current week day
     */
    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    /**
     * Gets current week day.
     *
     * @return the current week day
     */
    public int getCurrentWeekDay() {
        return currentWeekDay;
    }

    /**
     * Is month is current month boolean.
     *
     * @param month the month
     * @return the boolean
     */
    public boolean isMonthIsCurrentMonth(int month) {
        if (getCurrentMonth() == month) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets current month.
     *
     * @return the current month
     */
    public int getCurrentMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Sets date selected.
     *
     * @param position the position
     */
    public void setDateSelected(int position) {
        if (dateList != null && dateList.size() > 0) {
            int oldSelection = -1;
            for (int i = 0; i< dateList.size(); i++) {
                if (dateList.get(i).isSelected()) {
                    oldSelection = i;
                    break;
                }
            }
            if(oldSelection!=-1)
            dateList.get(oldSelection).setSelected(false);
            dateList.get(position).setSelected(true);
            notifyDataSetChanged();
            if(onCalendarDateSelect!=null){
                onCalendarDateSelect.getSelectedDate(dateList.get(position));
            }
        }
    }

    /**
     * Get date list array list.
     *
     * @return the array list
     */
    public ArrayList<CalendarDTO> getDateList(){
        return dateList;
    }

    /**
     * Set date list.
     *
     * @param dateList the date list
     */
    public void setDateList(ArrayList<CalendarDTO> dateList){
        this.dateList = dateList;
        notifyDataSetChanged();
    }

    /**
     * Set on calendar date select.
     *
     * @param onCalendarDateSelect the on calendar date select
     */
    public void setOnCalendarDateSelect(OnCalendarDateSelect onCalendarDateSelect){
        this.onCalendarDateSelect = onCalendarDateSelect;
    }

    /**
     * Get selected date calendar dto.
     *
     * @return the calendar dto
     */
    public CalendarDTO getSelectedDate(){
        if(dateList!=null && dateList.size()>0){
            for(CalendarDTO dto : dateList){
                if(dto.isSelected())
                    return dto;
            }
        }
        return null;
    }
}