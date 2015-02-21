package com.swagappsincorporated.blubz;

import java.util.Calendar;

/**
 * Created by Swag Apps on 4/30/14.
 */
public class TimeHelper {

    public static boolean isSameDay(long timestamp1, long timestamp2){

        if(timestamp1 == 0 || timestamp2 == 0){
            return false;
        }


        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.setTimeInMillis(timestamp1);
        calendar2.setTimeInMillis(timestamp2);

        return(calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR));

    }

}
