package com.kaerenabo.models;

import java.io.Serializable;

/**
 * Created by mayur.tailor on 30-05-2016.
 */
public class CalendarDTO implements Serializable{

    private boolean hasEvent;
    private String day;
    private String dayLong;
    private String month;
    private String monthAbr;
    private int year;
    private String operation;
    private boolean isSelected;
    private int date;
    private int monthInt;
    private long time;
    private String monthShort;

    /**
     * Is has event boolean.
     *
     * @return the boolean
     */
    public boolean isHasEvent() {
        return hasEvent;
    }

    /**
     * Sets has event.
     *
     * @param hasEvent the has event
     */
    public void setHasEvent(boolean hasEvent) {
        this.hasEvent = hasEvent;
    }


    /**
     * Gets month.
     *
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets month.
     *
     * @param month the month
     */
    public void setMonth(String month) {
        this.month = month;
    }


    /**
     * Gets month abr.
     *
     * @return the month abr
     */
    public String getMonthAbr() {
        return monthAbr;
    }

    /**
     * Sets month abr.
     *
     * @param monthAbr the month abr
     */
    public void setMonthAbr(String monthAbr) {
        this.monthAbr = monthAbr;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year the year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets operation.
     *
     * @param operation the operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Is selected boolean.
     *
     * @return the boolean
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets selected.
     *
     * @param selected the selected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Gets day.
     *
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets day.
     *
     * @param day the day
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public int getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(int date) {
        this.date = date;
    }

    /**
     * Gets month int.
     *
     * @return the month int
     */
    public int getMonthInt() {
        return monthInt;
    }

    /**
     * Sets month int.
     *
     * @param monthInt the month int
     */
    public void setMonthInt(int monthInt) {
        this.monthInt = monthInt;
    }

    /**
     * Gets time.
     *
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Gets month short.
     *
     * @return the month short
     */
    public String getMonthShort() {
        return monthShort;
    }

    /**
     * Sets month short.
     *
     * @param monthShort the month short
     */
    public void setMonthShort(String monthShort) {
        this.monthShort = monthShort;
    }

    /**
     * Gets day long.
     *
     * @return the day long
     */
    public String getDayLong() {
        return dayLong;
    }

    /**
     * Sets day long.
     *
     * @param dayLong the day long
     */
    public void setDayLong(String dayLong) {
        this.dayLong = dayLong;
    }
}
