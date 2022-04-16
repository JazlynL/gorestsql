package com.careerdevs.gorestsql.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIErrorHandiling {

    //? - we using this sign as any type data value.
    public static ResponseEntity<?> genericApiError(Exception e){
        System.out.println(e.getMessage());
        System.out.println(e.getClass());
        return new ResponseEntity<>(e.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
    public static ResponseEntity<?> customApiError(String message, HttpStatus status){
        return new ResponseEntity<>( message, status);

    }
    public static boolean isStrNaN(String strNum){
        if(strNum == null ){
            return true;
        }

        try {

            // parsing the int so it can be output by a String.
            Integer.parseInt(strNum);
        } catch (NumberFormatException e) {
            return true;
        }

      return false;
    }}