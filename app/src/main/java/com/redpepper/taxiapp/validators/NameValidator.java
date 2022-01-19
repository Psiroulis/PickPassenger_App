package com.redpepper.taxiapp.validators;

public class NameValidator {
    public boolean isNameValid(String name){

        if(name.length() <0 && name.length() > 50){
            return false;
        }

        if (name.matches("[0-9]+")) {
            return false;
        }

        return true;
    }
}
