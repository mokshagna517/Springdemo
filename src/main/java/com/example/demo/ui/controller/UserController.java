package com.example.demo.ui.controller;

import com.example.demo.ui.model.request.UpdateUserDetailsRequestModel;
import com.example.demo.ui.model.request.UserDetailsRequest;
import com.example.demo.ui.model.response.UserRest;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    Map<String, UserRest> users;

    @GetMapping
    public String getUsers(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "limit", defaultValue="50") int limit,
                           @RequestParam(value="sort", defaultValue = "desc", required = false) String sort){
        return "Get users was called on page: " + page +" and with limit: "+ limit + "& sorting: "+sort;
    }

    @GetMapping(path="/{userid}", produces ={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserRest> getUser(@PathVariable String userid){
        if(users.containsKey(userid)){
            return new ResponseEntity<UserRest>(users.get(userid), HttpStatus.OK);
        }
        else
        return new ResponseEntity<UserRest>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},consumes = {
            MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<UserRest> createUser(@RequestBody UserDetailsRequest userDetails){
        UserRest user=new UserRest();
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmailId());

        if(users==null)
            users=new HashMap<>();
        String userId= UUID.randomUUID().toString();
        user.setUserId(userId);
        users.put(userId, user);
        return new ResponseEntity<UserRest>(user, HttpStatus.OK);
    }

    @PutMapping(path="/{userid}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},consumes = {
            MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity<UserRest> updateUser(@PathVariable String userid, @RequestBody UpdateUserDetailsRequestModel userDetails) {
       if(!users.containsKey(userid))
           return new ResponseEntity<UserRest>(HttpStatus.NO_CONTENT);
       UserRest storedUser=users.get(userid);
       storedUser.setFirstName(userDetails.getFirstName());
       storedUser.setLastName(userDetails.getLastName());
       users.put(userid,storedUser);
       return new ResponseEntity<UserRest>(storedUser, HttpStatus.OK);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        users.remove(id);
      return ResponseEntity.noContent().build();
    }
}
