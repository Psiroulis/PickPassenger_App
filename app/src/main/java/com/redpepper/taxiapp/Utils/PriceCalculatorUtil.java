package com.redpepper.taxiapp.Utils;

import java.util.Calendar;
import java.util.Date;

public class PriceCalculatorUtil {

    private static final int SINGLE_TARIFFS_START = 500;
    private static final int SINGLE_TARIFFS_END = 0;

    private static final double MINIMUM_CHARGE = 3.16;

    private static final double SINGLE_TARIFFS_CHARGE_PER_METER = 0.00068;
    private static final double DOUBLE_TARIFFS_CHARGE_PER_METER = 0.00119;

    public PriceCalculatorUtil() { }

    private boolean isBetween(){

        Date date = new Date();

        Calendar c = Calendar.getInstance();

        c.setTime(date);

        int t = c.get(Calendar.HOUR_OF_DAY) * 100 + c.get(Calendar.MINUTE);

        return t >= SINGLE_TARIFFS_START || t <= SINGLE_TARIFFS_END;

    }

    public String calculatePrice(int distanceToMeters){

        double priceAmount;

        if(isBetween()){
            //Single Tarifa;

            priceAmount = 1.19 + (distanceToMeters * SINGLE_TARIFFS_CHARGE_PER_METER);

        }else{
           //Double Tarifa;

            priceAmount = 1.19 + (distanceToMeters * DOUBLE_TARIFFS_CHARGE_PER_METER);

        }

        if(priceAmount <= MINIMUM_CHARGE){

            return MINIMUM_CHARGE +  " €";

        }

        int amountFrom = (int) priceAmount;

        int amountTo = amountFrom + 2;

        return amountFrom + " ~ " + amountTo + " €";

    }

}
