package com.careerdevs.gorestsql.controllers;

import com.careerdevs.gorestsql.models.Users;
import com.careerdevs.gorestsql.repos.UserRepository;
import com.careerdevs.gorestsql.utils.APIErrorHandiling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    Make a GET request  that returns a user by id from the sql database.
    Make a get request that returns all users from the SQL database.
    Make a request that DELETE one user by ID from SQL database(returns the deleted sql user data)
    Delete route that deletes all users from SQL database(returns the amount of users deleted)
    POST route that queries one user by ID from GoRest and saves their data to your local database (returns the SQL user Data)
    POST route that uploads all users from theGORest API into the SQL database (returns how many users were uploaded)
    POST route that creates the user on Just the SQL database(returns the newly created SQL user data)
    PUT route that  updates the user on just the SQL database( returns the upload SQL data)
*/

    // getting user by ID;
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserData(@PathVariable("id") String id){
        try{

            if(APIErrorHandiling.isStrNaN(id)){
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, id + "is not a valid ID");

            }
            int uId = Integer.parseInt(id);

            Optional<Users> foundUser = userRepository.findById(uId);

            // a method to check if any users are actually empty.
            if(foundUser.isEmpty()){
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "No user with this ID is found"+ id);
            }


            return new ResponseEntity<>(foundUser,HttpStatus.OK);

        }  catch(HttpClientErrorException e){
            return APIErrorHandiling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return APIErrorHandiling.genericApiError(e);

        }


    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsers(@PathVariable ("id") String userId){
        try{
            if(APIErrorHandiling.isStrNaN(userId)){
                // print out message so user knows whats going on
                // also a simple way to send exception errors.
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId +" Not a valid number");
            }


            int uID = Integer.parseInt(userId);

            Optional<Users> foundUser = userRepository.findById(uID);

            if(foundUser.isEmpty()){
                throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, "user Data was Null "+ userId);
            }

            // we are using userRepository Crud methods.
            userRepository.deleteById(uID);



            return new ResponseEntity<>(foundUser,HttpStatus.OK);


        }  catch(HttpClientErrorException e){
            return APIErrorHandiling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return APIErrorHandiling.genericApiError(e);

        }
    }

    //getting all users
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


    @DeleteMapping("/deleteall")
    public ResponseEntity<?> deleteAllUsers(){
        try{

            //this wil display all the users in the gorest data
            // we are able to see how many users are un a table by the .count Method.
           long totalUsers = userRepository.count();
           userRepository.deleteAll();

           return  new ResponseEntity<>("Users deleted:"+ totalUsers,HttpStatus.OK);
        }catch(HttpClientErrorException e){
            return APIErrorHandiling.customApiError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
        catch(Exception exception){
            System.out.println(exception.getMessage());
            System.out.println(exception.getClass());


            return APIErrorHandiling.genericApiError(exception);

        }

    }


    @PostMapping("/upload/{id}")

    public ResponseEntity<?> uploadUserById(@PathVariable("id") String userId,
                                            // we only use rest template when we have to make an external API request.
                                            RestTemplate restTemplate){
        try{
            if(APIErrorHandiling.isStrNaN(userId)){
                // print out message so user knows whats going on
                // also a simple way to send exception errors.
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, userId +" Not a valid number");
            }


            int uID = Integer.parseInt(userId);


            // we use this url.
            String url = "https://gorest.co.in/public/v2/users/" + uID;

            Users foundUser = restTemplate.getForObject(url,Users.class);
            System.out.println(foundUser);


            if(foundUser == null){
                throw  new HttpClientErrorException(HttpStatus.NOT_FOUND, "user Data was Null");
            }

            // we are using userRepository Crud methods.
            // save will also be applicable to updating a user.
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

    //updating a user data
    @PutMapping
    public ResponseEntity<?> updateUsers (@RequestBody Users updateUser){
        {

            try{

                Users savedUsers = userRepository.save(updateUser);
                return new ResponseEntity<>(savedUsers,HttpStatus.CREATED);

            }
            catch(HttpClientErrorException e){
                return APIErrorHandiling.customApiError(e.getMessage(), e.getStatusCode());
            }catch(Exception e){
                return APIErrorHandiling.genericApiError(e);

            }


        }

    }

    //getting all users
    @PostMapping("/uploadall")
    public ResponseEntity<?> getAll(RestTemplate restTemplate) {
        try{


            String url = "https://gorest.co.in/public/v2/users";


             /*creating a response entity.
            Since we are requesting an Array of Users.
            We are going to be implementing the User Model Array,
            We will be using get request for entity because we want what's in the headers.
            We will implement in the parameters the url, and the data type we are using which
            is  the array class.
             */

            ResponseEntity<Users[]> response = restTemplate.getForEntity(url, Users[].class);


            // using getBody.
            Users [] firstPageOfUsers = response.getBody();





            //creating an Array List to get all the users.
            ArrayList<Users> allUsers = new ArrayList<>(Arrays.asList(firstPageOfUsers));

           /*The HTTP headers are used to pass additional information
             between the clients and the server through the request and response header.
             All the headers are case-insensitive, headers fields are separated by colon, key-value pairs in clear-text string format
            */

            HttpHeaders responseHeaders = response.getHeaders();


            /* we are using addAll to input a collection.
            We use addAll when working with Arraylists,Arrays is a static class that
            have all these different methods.
            Takes an array of any kind and makes it into a list.

            allUsers.addAll(Arrays.asList(Objects.requireNonNull(response.getBody())));
            */

            String totalPages = Objects.requireNonNull(responseHeaders.get("X-Pagination-Pages")).get(0);

            //accessing the Integer class and using parse so it can be read.
            int totalPagNum = Integer.parseInt(totalPages);

            //at this point we will begin iterating through the pages

            for(int i = 2; i <= totalPagNum ;i++){
                String pagUrl = url+"?page="+i;
                Users[] pageUsers = restTemplate.getForObject(pagUrl, Users[].class);

                if(pageUsers == null){
                    throw  new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, " Failed to get  page "+ i + "  of  users from GORest");

                }


                allUsers.addAll(Arrays.asList(firstPageOfUsers));
            }

            //upload allUsers
            userRepository.saveAll(allUsers);

            // just to see  the overall total of pages.
            System.out.println(allUsers.size());

            return new ResponseEntity("Users created: "+allUsers.size(),HttpStatus.OK);



        } catch(HttpClientErrorException e){
            return APIErrorHandiling.customApiError(e.getMessage(), e.getStatusCode());
        }catch(Exception e){
            return APIErrorHandiling.genericApiError(e);

        }
    }
}
