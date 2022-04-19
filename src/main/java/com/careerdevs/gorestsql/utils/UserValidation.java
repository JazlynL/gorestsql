package com.careerdevs.gorestsql.utils;

import com.careerdevs.gorestsql.models.Users;
import com.careerdevs.gorestsql.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserValidation {

    @Autowired
    private  UserRepository userRepository;
    private UserValidation userValidation;


    public static ValidationError validateNewUser(Users user, UserRepository userRepo,  boolean isUpdate){

        ValidationError errors = new ValidationError();
     /* this attaches the correct error messages to the KVP that its assigned too.
     */

        if(isUpdate){

        if(user.getId() == null){
            errors.addError("id" , "Name cannot blank ");

        }else{
            Optional<Users> foundUser = new UserValidation().userRepository.findById(user.getId());
            if(foundUser.isEmpty()){
                errors.addError("id","No User found with the ID." + user.getId());
            }else{
                System.out.println(foundUser.get());
            }
        }
        }



        String userName = user.getName();
        String userGender = user.getGender();
        String userStatus = user.getStatus();
        String userEmail = user.getEmail();
        if(userName== null|| userName.trim().equals("")){
            //
            errors.addError("name","name can not be left blank");
        }
        if(userEmail== null|| userEmail.trim().equals("")){
            //
            errors.addError("email","email can not be left blank");
        }
        if(userGender== null|| userGender.trim().equals("")){
            //
            errors.addError("gender","gender can not be left blank");
        }else if(!userGender.equals("male")|| userGender.equals("female")||userGender.equals("other")) {
            errors.addError("gender","Gender must be male,female, or other");

        }
        if(userStatus== null|| userStatus.trim().equals("")){
            //
            errors.addError("status","status can not be left blank");
        }else if(!userStatus.equals("active")|| userGender.equals("inactive")) {
            errors.addError("status","Status must be active or inactive");

        }

      return  errors;
    }

}
