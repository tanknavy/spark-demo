package com.tanknavy.blog.test;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Author: Alex Cheng 7/12/2020 8:24 PM
 */
public class TempTest {

    public static void main(String[] args) {
        long ts = System.currentTimeMillis();
        System.out.println(ts);//1594610733156

        Date date = new Date(ts);
        Instant instant = Instant.ofEpochMilli(1594611551977L);
        Instant instant2 = Instant.ofEpochMilli(1594237370373L);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("America/Los_Angeles"));
        System.out.println(localDateTime);

        LocalDateTime localDateTime2 = LocalDateTime.ofInstant(instant2, ZoneId.of("America/Los_Angeles"));
        System.out.println(localDateTime2);

    }
}
