package com.careerdevs.gorestsql.utils;

import java.util.HashMap;
import java.util.Map;

public class ValidationError {


    // this is a type of data , and it holds key value pairs.
    private HashMap<String, String > errors = new HashMap<>();


    public void addError(String key, String errorMsg){
    errors.put(key,errorMsg);}

    // this is grabbing the errors to see if the size of error is greater than 0.

    public boolean hasErrors(){
        return errors.size() != 0;
    }
   @Override
    public  String toString(){
        String errorMessage = " Validation error: \n";
       for(Map.Entry<String, String> err: errors.entrySet()){
           errorMessage+= err.getKey()+": " + err.getValue()+ "\n";

       }
       return errorMessage.toString() ;
   }

}
