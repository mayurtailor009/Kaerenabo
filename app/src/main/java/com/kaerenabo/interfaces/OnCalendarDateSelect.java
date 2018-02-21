package com.kaerenabo.interfaces;

import com.kaerenabo.models.CalendarDTO;

/**
 * The interface On calendar date select.
 */
public interface OnCalendarDateSelect{
    /**
     * Gets selected date.
     *
     * @param calendarDTO the calendar dto
     */
    public void getSelectedDate(CalendarDTO calendarDTO);
    }