package com.ktt.utils;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
// 数据格式化
public class DataFormat {



    // date -> date
    public Date formatDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp now = new Timestamp(date.getTime());
        String str = df.format(now);
        try {
            Date newDate = df.parse(str);
            return newDate;
        }catch (Exception e){
            e.printStackTrace();
        return null;
    }


    }

    // Date -> string
    public String formatSimpleDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp now = new Timestamp(date.getTime());
        String str = df.format(date);
        try {
            //String newDate = df.parse(str).toString();
            return str;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    // Date -> string
    public String formatOtherSimpleDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp now = new Timestamp(date.getTime());
        String str = df.format(date);
        try {
            //String newDate = df.parse(str).toString();
            return str;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    // Date -> string
    public Date formatDateToString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp now = new Timestamp(date.getTime());
        String str = df.format(date);
        try {
            Date newDate = df.parse(str);
            return newDate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    // date -> date
    public Date formatOtherDate(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp now = new Timestamp(date.getTime());
        String str = df.format(now);
        try {
            Date newDate = df.parse(str);
            return newDate;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

}
