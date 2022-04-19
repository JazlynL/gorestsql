package com.careerdevs.gorestsql.utils;

import com.careerdevs.gorestsql.models.Users;

public class UserValidation {


    public static ValidationError validateNewUser(Users user){
        ValidationError errors = new ValidationError();
        if(user.getName()== null|| user.getName().trim().equals("")){
            //
            errors.addError("name","name can not be left blank");
        }
        if(user.getEmail()== null|| user.getEmail().trim().equals("")){
            //
            errors.addError("email","email can not be left blank");
        }
        if(user.getGender()== null|| user.getGender().trim().equals("")){
            //
            errors.addError("gender","gender can not be left blank");
        }
        if(user.getStatus()== null|| user.getStatus().trim().equals("")){
            //
            errors.addError("Status","status can not be left blank");
        }

      return  errors;
    }

}
