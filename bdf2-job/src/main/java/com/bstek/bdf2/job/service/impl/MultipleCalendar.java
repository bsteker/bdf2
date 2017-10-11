package com.bstek.bdf2.job.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.impl.calendar.DailyCalendar;

/**
 * @author Jacky.gao
 * @since 2015年1月12日
 */
public class MultipleCalendar extends BaseCalendar{
	private static final long serialVersionUID = -4142756339211533719L;
	private List<DailyCalendar> dailyCalendars=new ArrayList<DailyCalendar>();
	private List<BaseCalendar> calendars=new ArrayList<BaseCalendar>();
	public void addCalendar(BaseCalendar calendar){
		if(calendar instanceof DailyCalendar){
			dailyCalendars.add((DailyCalendar)calendar);
		}else{
			calendars.add(calendar);
		}
	}
	@Override
	public boolean isTimeIncluded(long timeStamp) {
		for(BaseCalendar calendar:calendars){
			if(calendar.isTimeIncluded(timeStamp)==false){
				return false;
			}
		}
		for(DailyCalendar calendar:dailyCalendars){
			if(calendar.isTimeIncluded(timeStamp)==false){
				return false;
			}
		}
		return true;
	}
	@Override
	public long getNextIncludedTime(long timeStamp) {
        java.util.Calendar day=null;
        for(BaseCalendar calendar:calendars){
        	timeStamp = calendar.getNextIncludedTime(timeStamp);
        	day = getStartOfDayJavaCalendar(timeStamp);
        	while (isTimeIncluded(day.getTime().getTime()) == false) {
        		day.add(java.util.Calendar.DATE, 1);
        	}
        }
        for(DailyCalendar calendar:dailyCalendars){
        	timeStamp = calendar.getNextIncludedTime(timeStamp);
        	day = getStartOfDayJavaCalendar(timeStamp);
        	while (isTimeIncluded(day.getTime().getTime()) == false) {
        		day.add(java.util.Calendar.DATE, 1);
        	}
        }
        return day.getTime().getTime();
	}
}
