package com.careerdevs.gorestsql.controllers;

import com.careerdevs.gorestsql.models.Users;
import com.careerdevs.gorestsql.repos.UserRepository;
import com.careerdevs.gorestsql.utils.APIErrorHandiling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {


    // using this annotation to access the methods within the  userRepository.
    @Autowired
    private UserRepository userRepository;
    /*
    Make a GET request that queries one user by ID and saved their data to your local database,
    While also returning thw user data in the response.
    Make a POST request that creates a user in your local database.
    Make a PUT request that creates a user in your local database.

     */

    //getting all users
    @PostMapping("/uploadall")
    public ResponseEntity getAll(RestTemplate restTemplate) {
        try{
            //creating an Array List to get all the users.
            ArrayList<Users> allUsers = new ArrayList<>();
            String url = "https://gorest.co.in/public/v2/users";

            /*creating a response entity.
            Since we are requesting an Array of Users.
            We are going to be implementing the User Model Array,
            We will be using get request for entity because we want what's in the headers.
            We will implement in the parameters the url, and the data type we are using which
            is  the array class.
             */

            ResponseEntity<Users[]> response = restTemplate.getForEntity(url, Users[].class);


            /* we are using addAll to input a collection.
            We use addAll when working with Arraylists,Arrays is a static class that
            have all these different methods.
            Takes an array of any kind and makes it into a list.
            */

            allUsers.addAll(Arrays.asList(Objects.requireNonNull(response.getBody())));

            //accessing the Integer class and using parse so it can be read.
            int totalPages = Integer.parseInt(response.getHeaders().get("X-Pagination-Pages").get(0));

            //at this point we will begin iterating through the pages

            for(int i = 2; i <= totalPages ;i++){
                String tempUrl = url+"?page="+i;
                Users[] pageData = restTemplate.getForObject(tempUrl, Users[].class);
                allUsers.addAll(Arrays.asList(Objects.requireNonNull(pageData)));
            }
            userRepository.saveAll(allUsers);

            // just to see  the overall total of pages.
            System.out.println(allUsers.size());

            return new ResponseEntity(allUsers.size(),HttpStatus.OK);



        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getClass());

            // will return a message, status, or body

            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @GetMapping("/all")

    public ResponseEntity<?> getAllUsers(){
        try{

            Iterable <Users> allUsers = userRepository.findAll();
            return  new ResponseEntity<>(allUsers,HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getClass());
            return new ResponseEntity<>(e.getClass(),HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserData(@PathVariable("id") String id){
        try{
            int uId = Integer.parseInt(id);

            Optional<Users> foundUser = userRepository.findById(uId);


          return new ResponseEntity<>(foundUser,HttpStatus.CREATED);

        }  catch(HttpClientErrorException e){
            return APIErrorHandiling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return APIErrorHandiling.genericApiError(e);

        }


    }

    @PostMapping("/{upload}/{id}")

    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId,
                                            RestTemplate restTemplate){
        try{
            if(APIErrorHandiling.isStrNaN(userId)){
                // print out message so user knows whats going on
                // also a simple way to send exception errors.
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId +" Not a valid number");


            }


            int uID = Integer.parseInt(userId);

         String url = "https://gorest.co.in/public/v2/users/" + uID;

         Users foundUser = restTemplate.getForObject(url,Users.class);
         System.out.println(foundUser);
         if(foundUser == null){
             throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, "user Data was Null");
         }

         // we are using userRepository Crud methods.
            assert foundUser != null;
            Users savedUsers = userRepository.save(foundUser);




            return new ResponseEntity(savedUsers,HttpStatus.OK);





        }catch(NumberFormatException e){
            return new ResponseEntity<>("ID must be a number",HttpStatus.NOT_FOUND);

        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
            System.out.println(exception.getClass());


            return APIErrorHandiling.genericApiError(exception);

        }


    }

@PostMapping("/")
public ResponseEntity<?> createUser(@RequestBody Users newUser){

        try{

            Users savedUsers = userRepository.save(newUser);
            return new ResponseEntity<>(savedUsers,HttpStatus.CREATED);

        }
        catch(HttpClientErrorException e){
            return APIErrorHandiling.customApiError(e.getMessage(), e.getStatusCode());
    }catch(Exception e){
            return APIErrorHandiling.genericApiError(e);

        }

}

    @PutMapping



    @DeleteMapping("/deleteAll")

    public ResponseEntity<?> deleteUsers(){
        try{

            long totalUsers = userRepository.count();
            userRepository.deleteAll();
            return  new ResponseEntity<>("Users deleted , " + totalUsers, HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e.getClass());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
