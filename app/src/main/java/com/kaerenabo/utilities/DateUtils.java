package com.kaerenabo.utilities;


import com.kaerenabo.models.CalendarDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {


    private static final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
            31, 30, 31};

    private static final int trails[] = {6, 0, 1, 2, 3, 4, 5};

    /**
     * The constant existingUTCFormat.
     */
    public static SimpleDateFormat existingUTCFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * The constant sdfDate.
     */
// SQLite date format
    public static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * The constant FORMAT_DD_MM.
     */
    public static final String FORMAT_DD_MM = "dd MMM";
    /**
     * The constant FORMAT_DD_MM_YYYY.
     */
    public static final String FORMAT_DD_MM_YYYY = "dd MMM yyyy";

    /**
     * Convert to sqlite date format string.
     *
     * @param utcDate the utc date
     * @return the string
     */
    public static String convertToSqliteDateFormat(String utcDate) {

        Date date = null;
        String str = null;

        try {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            existingUTCFormat.setTimeZone(timeZone);
            date = existingUTCFormat.parse(utcDate);
            str = sdfDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Convert to sqlite time format string.
     *
     * @param utcDate the utc date
     * @return the string
     */
    public static String convertToSqliteTimeFormat(String utcDate) {

        Date date = null;
        String str = null;

        try {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            existingUTCFormat.setTimeZone(timeZone);
            date = existingUTCFormat.parse(utcDate);
            SimpleDateFormat requiredFormat = new SimpleDateFormat("h:mm a");
            return requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Convert to sqlite time format inhhmm ss string.
     *
     * @param utcDate the utc date
     * @return the string
     */
    public  static String convertToSqliteTimeFormatINHHMMSs(String utcDate)
    {
        Date date = null;
        String str = null;

        try {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            existingUTCFormat.setTimeZone(timeZone);
            date = existingUTCFormat.parse(utcDate);
            SimpleDateFormat requiredFormat = new SimpleDateFormat("HH:mm");
            return requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Gets day and date.
     *
     * @param utcTime the utc time
     * @return the day and date
     */
    public static String getDayAndDate(String utcTime) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("EEE dd");

        Date date = null;
        String str = null;

        try {
            date = sdfDate.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Parse date todd mm mddyyyy string.
     *
     * @param utcTime the utc time
     * @return the string
     */
    public static String parseDateToddMMMddyyyy(String utcTime) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("MMM dd, yyyy");

        Date date = null;
        String str = null;

        try {
            date = sdfDate.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Method to parse parse date to EEEE dd MMMMM yyyy format
     *
     * @param utcTime date as string in yyyy-MM-dd HH:mm:ss format
     * @return date as string in EEEE dd MMMM yyyy
     */
    public static String parseDateToEEEddMMMddyyyy(String utcTime) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("EEEE dd MMMM yyyy");

        Date date = null;
        String str = null;

        try {
            date = sdfDate.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Parse date to yyyym mdd string.
     *
     * @param utcTime the utc time
     * @return the string
     */
    public static String parseDateToYYYYMMdd(String utcTime) {
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat requiredFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        String str = null;

        try {
            date = readFormat.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Parse date to eee edd mmm mddyyyy string.
     *
     * @param isoDate the iso date
     * @return the string
     */
    public static String parseDateToEEEEddMMMMddyyyy(String isoDate) {
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat writeFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Date date = null;
        String str = null;

        try {
            readFormat.setTimeZone(TimeZone.getDefault());
            date = readFormat.parse(isoDate);
            str = writeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Parse in ios format string.
     *
     * @param EEEdate the ee edate
     * @return the string
     */
    public static String parseInIOSFormat(String EEEdate) {
        DateFormat writeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat readFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Date date = null;
        String str = null;
        try {
            date = readFormat.parse(EEEdate);
            str = writeFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Convert date format string.
     *
     * @param utcDate the utc date
     * @return the string
     */
    public static String convertDateFormat(String utcDate) {
        SimpleDateFormat readFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        Date date = null;
        String str = null;
        try {
            date = readFormat.parse(utcDate);
            str = sdfDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * Get HH:mm:ss from date yyyy-mm-dd HH:mm:ss
     *
     * @param utcTime the utc time
     * @return string
     */
    public static String parseDateToHHMMSS(String utcTime) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("HH:mm:ss");

        String str = "";

        try {
            Date date = sdfDate.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }


    /**
     * Get HH:mm from date yyyy-mm-dd HH:mm:ss
     *
     * @param utcTime the utc time
     * @return string
     */
    public static String parseDateToHHMM(String utcTime) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("HH:mm");

        String str = "";

        try {
            Date date = sdfDate.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Get HH:mm a from date yyyy-mm-dd HH:mm:ss
     *
     * @param utcTime the utc time
     * @return string
     */
    public static String parseDateToHHMMAmPm(String utcTime) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("h:mm a");

        String str = "";

        try {
            Date date = sdfDate.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Gets time in mill.
     *
     * @param dateStr the date str
     * @return the time in mill
     */
    public static long getTimeInMill(String dateStr) {

        long timeMills = 0;

        try {
            Date date = sdfDate.parse(dateStr);
            timeMills = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timeMills;
    }

    /**
     * Gets h hfrom hhmm.
     *
     * @param hhMin the hh min
     * @return the h hfrom hhmm
     */
    public static String getHHfromHHMM(String hhMin) {
        return hhMin.split(":")[0];
    }

    /**
     * Gets m mfrom hhmm.
     *
     * @param hhMin the hh min
     * @return the m mfrom hhmm
     */
    public static String getMMfromHHMM(String hhMin) {
        return hhMin.split(":")[1];
    }


    /**
     * Gets current date time.
     *
     * @return the current date time
     */
    public static String getCurrentDateTime() {

        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    /**
     * Gets current time.
     *
     * @return current time in HH:MM
     */
    public static String getCurrentTime() {

        Date now = new Date();

        String strDate = sdfDate.format(now);

        String time = strDate.split(" ")[1];
        strDate = time.split(":")[0] + ":" + time.split(":")[1];

        return strDate;
    }


    /**
     * Method to get current time round off 15 mins
     *
     * @return current time in round off
     */
    public static String getCurrentTimeInRoundOff()

    {
        Date date = new Date();
        Date roundoffDate = new Date(900000 * ((date.getTime() + 450000) / 900000));
        String strDate = sdfDate.format(roundoffDate);
        String time = strDate.split(" ")[1];
        strDate = time.split(":")[0] + ":" + time.split(":")[1];
        return strDate;
    }

    /**
     * Gets days before date time.
     *
     * @param date       the date
     * @param daysBefore the days before
     * @return return date before passed number of days from current date
     */
    public static String getDaysBeforeDateTime(String date, int daysBefore) {

        Calendar cal = Calendar.getInstance();

        if (date != null && date.length() > 0) {

            try {
                cal.setTime(sdfDate.parse(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cal.add(Calendar.DATE, -daysBefore);

        Date todate = cal.getTime();
        String fromdate = sdfDate.format(todate);

        return fromdate;
    }


    /**
     * Methoid to checks date
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return boolean
     */
    public static boolean CheckDates(String startDate,String endDate)    {
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if(dfDate.parse(startDate).before(dfDate.parse(endDate)))
            {
                b = true;//If start date is before end date
            }
            else if(dfDate.parse(startDate).equals(dfDate.parse(endDate)))
            {
                b = false;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }


    /**
     * Gets days after date time.
     *
     * @param daysAfter the days after
     * @return return date after passed number of days from current date
     */
    public static String getDaysAfterDateTime(int daysAfter) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +daysAfter);

        Date todate = cal.getTime();
        String fromdate = sdfDate.format(todate);

        return fromdate;
    }


    /**
     * Add days in date string.
     *
     * @param date      the date
     * @param daysAfter the days after
     * @return the string
     */
    public static String addDaysInDate(String date, int daysAfter) {
        String addedDate = "";
        try {

            Calendar c = Calendar.getInstance();
            c.setTime(sdfDate.parse(date));
            c.add(Calendar.DATE, daysAfter);  // number of days to add
            addedDate = sdfDate.format(c.getTime());  // addedDate is now the new date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addedDate;
    }


    /**
     * check if two dates are same or not
     *
     * @param date1 the date 1
     * @param date2 the date 2
     * @return the boolean
     */
    public static boolean compareDates(Date date1, Date date2) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);

        return sameDay;
    }

    /**
     * return true if date1 < date2 else false
     *
     * @param date1 the date 1
     * @param date2 the date 2
     * @return the bigger date
     */
    public static boolean getBiggerDate(String date1, String date2) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        boolean date2IsGreater = false;

        try {

            cal1.setTime(sdfDate.parse(date1));
            cal2.setTime(sdfDate.parse(date2));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal1.getTimeInMillis() < cal2.getTimeInMillis()) {
            date2IsGreater = true;
        }

        return date2IsGreater;
    }

    /**
     * If last searched today boolean.
     *
     * @param lastSearchedDate the last searched date
     * @return the boolean
     */
    public static boolean ifLastSearchedToday(String lastSearchedDate) {

        Date date1 = new Date();
        Date date2 = DateUtils.getDate(lastSearchedDate);

        return DateUtils.compareDates(date1, date2);
    }

    /**
     * Gets date.
     *
     * @param startDate the start date
     * @return the date
     */
    public static Date getDate(String startDate) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat("EEEE dd MMMM yyyy");

        Date date = null;

        try {
            date = sdfDate.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Parse date todd mm myyyy string.
     *
     * @param utcTime the utc time
     * @return the string
     */
    public static String parseDateToddMMMyyyy(String utcTime) {

        if (utcTime == null)
            return "";
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd MMM yyyy");

        Date date = null;
        String str = null;

        try {
            date = existingUTCFormat.parse(utcTime);
            str = requiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Gets utc date.
     *
     * @param sdate the sdate
     * @return the utc date
     */
    public static String getUtcDate(String sdate) {

        if (sdate == null)
            return "";

        Date date = null;
        String str = null;

        try {
            date = sdfDate.parse(sdate);
            str = existingUTCFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param sdate the sdate
     * @return String with format "yyyy-MM-dd'T'HH:mm:ss'Z'"
     */
    public static String getDateForISO8601String(String sdate) {

        Date date = null;
        String str = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

            date = dateFormat.parse(sdate);

            str = sdfDate.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Sets selected date list.
     *
     * @param dateList       the date list
     * @param selectCalendar the select calendar
     * @return the selected date list
     */
    public static ArrayList<CalendarDTO> setSelectedDateList(ArrayList<CalendarDTO> dateList,
                                                             Calendar selectCalendar) {
        for (int i = 0; i < dateList.size(); i++) {
            CalendarDTO calendarDTO = dateList.get(i);
            if (calendarDTO.getDate() == selectCalendar.get(Calendar.DAY_OF_MONTH)
                    && calendarDTO.getMonthInt() == selectCalendar.get(Calendar.MONTH)
                    && calendarDTO.getYear() == selectCalendar.get(Calendar.YEAR)) {
                dateList.get(i).setSelected(true);
            } else {
                dateList.get(i).setSelected(false);
            }
        }

        return dateList;
    }

    /**
     * Sets selected date position.
     *
     * @param dateList       the date list
     * @param selectCalendar the select calendar
     * @return the selected date position
     */
    public static int setSelectedDatePosition(ArrayList<CalendarDTO> dateList,
                                              Calendar selectCalendar) {
        int position = -1;
        for (int i = 0; i < dateList.size(); i++) {
            CalendarDTO calendarDTO = dateList.get(i);
            if (calendarDTO.getDate() == selectCalendar.get(Calendar.DAY_OF_MONTH)
                    && calendarDTO.getMonthInt() == selectCalendar.get(Calendar.MONTH)
                    && calendarDTO.getYear() == selectCalendar.get(Calendar.YEAR)) {
                position = i;
            }
        }

        return position;
    }


    /**
     * Gets monthly date list.
     *
     * @param mm the mm
     * @param yy the yy
     * @return the monthly date list
     */
    public static ArrayList<CalendarDTO> getMonthlyDateList(int mm, int yy) {
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;
        int daysInMonth;
        int currentDayOfMonth;
        int currentMonth = mm - 1;
        String tag = "DateUtil";
        int DAY_OFFSET = 1;
        ArrayList<CalendarDTO> dateList = new ArrayList<CalendarDTO>();

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        currentDayOfMonth = now.get(Calendar.DAY_OF_MONTH);

        daysInMonth = getNumberOfDaysOfMonth(currentMonth);

        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
        Utils.printLog(tag, "Gregorian Calendar:= " + cal.getTime().toString());

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
            Utils.printLog(tag, "*->PrevYear: " + prevYear + " PrevMonth:"
                    + prevMonth + " NextMonth: " + nextMonth
                    + " NextYear: " + nextYear);
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            nextMonth = 1;
            Utils.printLog(tag, "**--> PrevYear: " + prevYear + " PrevMonth:"
                    + prevMonth + " NextMonth: " + nextMonth
                    + " NextYear: " + nextYear);
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            Utils.printLog(tag, "***---> PrevYear: " + prevYear + " PrevMonth:"
                    + prevMonth + " NextMonth: " + nextMonth
                    + " NextYear: " + nextYear);
        }

        int currentWeekDay = trails[cal.get(Calendar.DAY_OF_WEEK) - 1];
        trailingSpaces = currentWeekDay;

        Utils.printLog(tag, "No. Trailing space to Add: " + trailingSpaces);
        Utils.printLog(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

        if (cal.isLeapYear(cal.get(Calendar.YEAR)))
            if (mm == 2)
                ++daysInMonth;
            else if (mm == 3)
                ++daysInPrevMonth;

        CalendarDTO calendarDTO = null;
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {

            calendarDTO = new CalendarDTO();
            calendarDTO.setHasEvent(false);
            calendarDTO.setMonthInt(prevMonth);
            calendarDTO.setDate((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i);
            calendarDTO.setYear(prevYear);
            calendar.set(Calendar.DAY_OF_MONTH, calendarDTO.getDate());
            calendar.set(Calendar.MONTH, calendarDTO.getMonthInt());
            calendar.set(Calendar.YEAR, calendarDTO.getYear());
            calendarDTO.setOperation("INVISIBLE");

            calendarDTO.setMonth(getMonthLong(calendar));
            calendarDTO.setMonthAbr(getMonth(calendar));
            calendarDTO.setDay(getWeek(calendar));
            calendarDTO.setDayLong(getWeekLong(calendar));

            calendarDTO.setTime(getTime(calendar));
            dateList.add(calendarDTO);

        }
        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {

            calendarDTO = new CalendarDTO();
            calendarDTO.setDate(i);
            calendarDTO.setMonthInt(currentMonth);
            calendarDTO.setYear(yy);

            calendar.set(Calendar.DAY_OF_MONTH, calendarDTO.getDate());
            calendar.set(Calendar.MONTH, calendarDTO.getMonthInt());
            calendar.set(Calendar.YEAR, calendarDTO.getYear());

            calendarDTO.setMonth(getMonthLong(calendar));
            calendarDTO.setMonthAbr(getMonth(calendar));
            calendarDTO.setDay(getWeek(calendar));
            calendarDTO.setDayLong(getWeekLong(calendar));

            if (now.getTimeInMillis() == calendar.getTimeInMillis()) { //DATE IS CURRENT DATE.
                calendarDTO.setOperation("BLUE");
                calendarDTO.setSelected(true);

            } else if (calendar.getTime().before(now.getTime())) {// DATE IS PREVIOUS TO CURRENT
                calendarDTO.setOperation("GRAY");

            } else {    // DATE IS FOR FUTURE.
                calendarDTO.setOperation("WHITE");

            }

            calendarDTO.setTime(getTime(calendar));
            dateList.add(calendarDTO);

        }

        // Leading Month days
        for (int i = 0; i < dateList.size() % 7; i++) {

            calendarDTO = new CalendarDTO();
            calendarDTO.setHasEvent(false);
            calendarDTO.setDate(i + 1);
            calendarDTO.setMonthInt(nextMonth);
            calendarDTO.setYear(nextYear);

            calendar.set(Calendar.DAY_OF_MONTH, calendarDTO.getDate());
            calendar.set(Calendar.MONTH, calendarDTO.getMonthInt());
            calendar.set(Calendar.YEAR, calendarDTO.getYear());

            calendarDTO.setMonth(getMonthLong(calendar));
            calendarDTO.setMonthAbr(getMonth(calendar));
            calendarDTO.setDay(getWeek(calendar));
            calendarDTO.setDayLong(getWeekLong(calendar));

            calendarDTO.setOperation("INVISIBLE");

            dateList.add(calendarDTO);
            calendarDTO.setTime(getTime(calendar));

        }
        return dateList;
    }

    /**
     * Gets current weeks.
     *
     * @param selectCalendar the select calendar
     * @return the current weeks
     */
    public static ArrayList<CalendarDTO> getCurrentWeeks(Calendar selectCalendar) {

        Calendar now = Calendar.getInstance();
        now.set(Calendar.DATE, selectCalendar.get(Calendar.DATE));
        now.set(Calendar.MONTH, selectCalendar.get(Calendar.MONTH));
        now.set(Calendar.YEAR, selectCalendar.get(Calendar.YEAR));
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.DATE, selectCalendar.get(Calendar.DATE));
        today.set(Calendar.MONTH, selectCalendar.get(Calendar.MONTH));
        today.set(Calendar.YEAR, selectCalendar.get(Calendar.YEAR));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        int delta = -now.get(Calendar.DAY_OF_WEEK) + 2; //add 2 if your week start on monday
        if (delta == 1)
            delta = -6;
        now.add(Calendar.DAY_OF_MONTH, delta);
        CalendarDTO calendarDTO = null;
        ArrayList<CalendarDTO> dateList = new ArrayList<CalendarDTO>();
        for (int i = 0; i < 7; i++) {

            calendarDTO = new CalendarDTO();
            calendarDTO.setDate(now.get(Calendar.DAY_OF_MONTH));
            calendarDTO.setMonthInt(now.get(Calendar.MONTH));
            calendarDTO.setYear(now.get(Calendar.YEAR));

            calendarDTO.setMonth(getMonthLong(now));
            calendarDTO.setMonthAbr(getMonth(now));
            calendarDTO.setDay(getWeek(now));
            calendarDTO.setDayLong(getWeekLong(now));

            if (now.getTimeInMillis() == today.getTimeInMillis()) { //DATE IS CURRENT DATE.
                calendarDTO.setOperation("BLUE");
                calendarDTO.setSelected(true);

            } else if (now.getTime().before(today.getTime())) {// DATE IS PREVIOUS TO CURRENT
                calendarDTO.setOperation("GRAY");
            } else {    // DATE IS FOR FUTURE.
                calendarDTO.setOperation("WHITE");
            }
            calendarDTO.setTime(getTime(now));


            now.add(Calendar.DAY_OF_MONTH, 1);
            dateList.add(calendarDTO);
        }

        return dateList;
    }

    /**
     * Gets number of days of month.
     *
     * @param i the
     * @return the number of days of month
     */
    public static int getNumberOfDaysOfMonth(int i) {
        return daysOfMonth[i];
    }

    /**
     * Is month is current month boolean.
     *
     * @param month the month
     * @param now   the now
     * @return the boolean
     */
    public static boolean isMonthIsCurrentMonth(int month, Calendar now) {
        if (getCurrentMonth(now) == month) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets current month.
     *
     * @param calendar the calendar
     * @return the current month
     */
    public static int getCurrentMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Gets time.
     *
     * @param calendar the calendar
     * @return the time
     */
    public static long getTime(Calendar calendar) {
        return calendar.getTime().getTime();
    }


    /**
     * Gets current month.
     *
     * @return the current month
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Gets current day of month.
     *
     * @return the current day of month
     */
    public static int getCurrentDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Gets tomorrow day of month.
     *
     * @return the tomorrow day of month
     */
    public static int getTomorrowDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) + 1;
    }

    /**
     * Gets yester day of month.
     *
     * @return the yester day of month
     */
    public static int getYesterDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) - 1;
    }

    /**
     * Gets current week of year.
     *
     * @return the current week of year
     */
    public static int getCurrentWeekOfYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Gets current year.
     *
     * @return the current year
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Gets hh mm from timestamp.
     *
     * @param timestamp the timestamp
     * @return the hh mm from timestamp
     */
    public static String getHhMmFromTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        SimpleDateFormat requiredFormat = new SimpleDateFormat("h:mm a");

        String str = "";

        try {
            Date date = calendar.getTime();
            str = requiredFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Gets required format from calander.
     *
     * @param calendar           the calendar
     * @param requiredDateFormat the required date format
     * @return the required format from calander
     */
    public static String getRequiredFormatFromCalander(Calendar calendar, String
            requiredDateFormat) {

        SimpleDateFormat requiredFormat = new SimpleDateFormat(requiredDateFormat);

        String str = "";

        try {
            Date date = calendar.getTime();
            str = requiredFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Gets week.
     *
     * @param calendar the calendar
     * @return the week
     */
    public static String getWeek(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Gets week long.
     *
     * @param calendar the calendar
     * @return the week long
     */
    public static String getWeekLong(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Gets month.
     *
     * @param calendar the calendar
     * @return the month
     */
    public static String getMonth(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM", Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Gets month long.
     *
     * @param calendar the calendar
     * @return the month long
     */
    public static String getMonthLong(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM", Locale.US);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Convert 5:9 to 05:09
     *
     * @param fromTime the from time
     * @return accurate time
     */
    public static String getAccurateTime(String fromTime) {

        String accurateTime = "";

        String hr = fromTime.split(":")[0];
        if (hr.length() == 1) {
            hr = "0" + hr;
        }

        String sec = fromTime.split(":")[1];
        if (sec.length() == 1) {
            sec = "0" + sec;
        }

        accurateTime = hr + ":" + sec;
        return accurateTime;
    }


    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param sdate the sdate
     * @return String with format "yyyy-MM-dd'T'HH:mm:ssZ"
     */
    public static String getISO8601StringForDate(String sdate) {

        Date date = null;
        String str = null;

        try {
            date = sdfDate.parse(sdate);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);

            str = dateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Gets date from iso format.
     *
     * @param isoDate the iso date
     * @return the date from iso format
     */
    public static String getDateFromISOFormat(String isoDate) {

        Date date = null;
        String str = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            date = dateFormat.parse(isoDate);
            str = sdfDate.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Gets date for expire.
     *
     * @param strDate the str date
     * @return the date for expire
     */
    public static Date getDateForExpire(String strDate) {

        Date date = null;
        String str = null;

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);
            date = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * Gets subscription isoutc.
     *
     * @return the subscription isoutc
     */
    public static String getSubscriptionISOUTC() {

        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 14);
            cal.set(Calendar.DAY_OF_MONTH, 29);
            Date date = new Date(cal.getTimeInMillis());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            dateFormat.setTimeZone(timeZone);

            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            ;
        }
        return null;
    }


    /**
     * Gets mili seconds.
     *
     * @param fromdate the fromdate
     * @return the mili seconds
     */
    public static long getMiliSeconds(String fromdate) {
        try {
            Date date = sdfDate.parse(fromdate);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Gets day and month.
     *
     * @param utcDate the utc date
     * @return the day and month
     */
    public static String getDayAndMonth(String utcDate) {
        String dayNumberSuffix = DateUtils.getDayNumberSuffix(Integer.parseInt(utcDate.split("-")[2]));
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("d'" + dayNumberSuffix + "' MMMM");
        try {

            Date date = readFormat.parse(utcDate);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets day and month in mmm format.
     *
     * @param utcDate the utc date
     * @return the day and month in mmm format
     */
    public static String getDayAndMonthInMMMFormat(String utcDate)
    {
        String dayNumberSuffix = DateUtils.getDayNumberSuffix(Integer.parseInt(utcDate.split("-")[2]));
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("d'" + dayNumberSuffix + "' MMM");
        try {

            Date date = readFormat.parse(utcDate);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets day month and year.
     *
     * @param utcDate the utc date
     * @return the day month and year
     */
    public  static  String getDayMonthAndYear(String utcDate)
    {
        String dayNumberSuffix = DateUtils.getDayNumberSuffix(Integer.parseInt(utcDate.split("-")[2]));
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("d'" + dayNumberSuffix + "' MMM yyyy");
        try {

            Date date = readFormat.parse(utcDate);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets day.
     *
     * @param utcDate the utc date
     * @return the day
     */
    public static String getDay(String utcDate) {
        String dayNumberSuffix = DateUtils.getDayNumberSuffix(Integer.parseInt(utcDate.split("-")[2]));
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat(" d'" + dayNumberSuffix + "'");
        try {

            Date date = readFormat.parse(utcDate);
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets day number suffix.
     *
     * @param day the day
     * @return the day number suffix
     */
    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }


    /**
     * Method to get current seconds
     *
     * @return default second
     */
    public static long getDefaultSecond() {
        Date date = new Date();
        Calendar lCDateTime = Calendar.getInstance();
        lCDateTime.setTime(date);
        long milliSecond = lCDateTime.getTimeInMillis();
        long second = milliSecond / 1000;
        return second;
    }

}
