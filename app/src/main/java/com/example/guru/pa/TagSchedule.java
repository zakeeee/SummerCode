package com.example.guru.pa;

/**
 * Created by Haoyu on 2016/7/20.
 */
public class TagSchedule {

    private int day;
    private int month;
    private int year;
    private int remindId;
    private int scheduleId;
    private int tagId;

    public TagSchedule(int year, int month, int day, int remindId, int tagId, int scheduleId){
        this.year = year;
        this.month = month;
        this.day = day;

        this.remindId = remindId;
        this.tagId = tagId;
        this.scheduleId = scheduleId;
    }

    public TagSchedule() {}

    public int getTagId() {
        return tagId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getRemindId() {
        return remindId;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public void setScheduleId(int scheduleId){
        this.scheduleId =scheduleId;
    }

    public void setRemindId(int remindId){
        this.remindId = remindId;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
