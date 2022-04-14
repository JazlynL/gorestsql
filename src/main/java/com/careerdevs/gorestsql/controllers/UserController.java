package com.careerdevs.gorestsql.controllers;

import com.careerdevs.gorestsql.models.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class UserController {
    /*
    Make a GET request that queries one user by ID and saved their data to your local database,
    While also returning thw user data in the response.
    Make a POST request that creates a user in your local database.
    Make a PUT request that creates a user in your local database.

     */

    @GetMapping("/{upload}/{id}")

    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId,
                                            RestTemplate restTemplate){
        try{


            int uID = Integer.parseInt(userId);

         String url = "https://gorest.co.in/public/v2/users" + uID;

         Users foundUser = restTemplate.getForObject(url,Users.class);
            System.out.println(foundUser);


         return new ResponseEntity<>("Temp",HttpStatus.OK);





        }catch(NumberFormatException e){
            return new ResponseEntity<>("ID must be a number",HttpStatus.NOT_FOUND);

        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
            System.out.println(exception.getClass());


            return  new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }

}
