package com.smatech.smatrentalpro.backend.utils;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RandomString {

    public static String generateCode(int number){
        Random random = new Random();
        String value = String.valueOf(random.nextInt(9));
        for(int i = 0; i < number -1; i++){
            value = value + String.valueOf(random.nextInt(9));
        }
        return value;
    }
    private static final AtomicLong idCounter = new AtomicLong(System.currentTimeMillis() * 1000);
    public static String generateId(){

        return UUID.randomUUID().toString();
    }

    public static long generateUniqueId() {

        return idCounter.incrementAndGet() + (long) (Math.random() * 1000);
    }

}
