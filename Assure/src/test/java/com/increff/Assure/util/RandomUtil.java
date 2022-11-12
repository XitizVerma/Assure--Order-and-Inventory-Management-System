package com.increff.Assure.util;

public class RandomUtil
{

    public static String getRandomString()
    {
        return getRandomString(8);
    }


    public static String getRandomString(int len){
        String src = "abcdefghijklmnopqrstuvwxyz0123456789";
        String str = "TST";

        for(int i=0;i<len;i++){
            int index = (int) (Math.random()*100%(src.length()));
            str += src.charAt(index);
        }

        return str;
    }

    public static int getRandomNumber()
    {
        return (int) (Math.random()*9999999);
    }

    public static Long getRandomNumberBetween1to100()
    {
        int max = 100;
        int min = 1;
        int range = max - min + 1;
        return (long)(Math.random() * range) +min;
    }

}