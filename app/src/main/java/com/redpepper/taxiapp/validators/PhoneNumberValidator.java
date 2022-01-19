package com.redpepper.taxiapp.validators;

public class PhoneNumberValidator {
    public boolean isPhoneNumberValid(String phoneNumber){

        if(!(phoneNumber.length() == 10) || (phoneNumber.length() == 0)){
            return false;
        }

        if( !String.valueOf(phoneNumber.charAt(0)).equalsIgnoreCase("6") ){
            return false;
        }

        if( phoneNumber.contains(" ") ){
            return false;
        }

        if (!phoneNumber.matches("[0-9]+")) {
            return false;
        }

        return true;
    }
}
