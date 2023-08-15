package com.example.whatsapp;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LastSeen {
    public LastSeen() {
    }

    String Date;
    String Time;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public LastSeen(String date, String time) {
        Date = date;
        Time = time;
    }
}
