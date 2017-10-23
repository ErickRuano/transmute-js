package mcce.sapp.controllers;

import java.util.*;

import mcce.sapp.models.Response;
import mcce.sapp.models.Token;
import mcce.sapp.models.User;
import mcce.sapp.models.UserDao;
import mcce.sapp.services.PasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.core.env.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class UserController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new user and save it in the database.
   * 
   * @param email User's email
   * @param name User's name
   * @return A string describing if the user is succesfully created or not.
   */
  @RequestMapping(value="/users", method=RequestMethod.POST)
  @ResponseBody
    // User user = null;
  public String create(@RequestBody ObjectNode requestBody) {
    User user;
     PasswordService ps=new PasswordService();
    try {
      user = new User(requestBody.get("username").asText(), ps.encrypt(requestBody.get("password").asText()));
      userDao.save(user);
    }
    catch (Exception ex) {
      return "Error creating the user: " + ex.toString();
    }
    return "User succesfully created! (id = " + user.getId() + ")";
  }
  
  /**
   * /delete  --> Delete the user having the passed id.
   * 
   * @param id The id of the user to delete
   * @return A string describing if the user is succesfully deleted or not.
   */
  @RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteUser(@PathVariable long id) {
    try {
      User user = new User(id);
      userDao.delete(user);
    }
    catch (Exception ex) {
      return "Error deleting the user: " + ex.toString();
    }
    return "User succesfully deleted!";
  }

  /**
   * /get  --> Return the id for the user having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The user id or a message error if the user is not found.
   */
  @RequestMapping("/users/{id}")
  @ResponseBody
  public Response getUser(@PathVariable long id) {
    User user;
    Response res = new Response();
    try {
      user = userDao.findOne(id);
        res.setData(user);
      if (user == null) {
        res.setStatus(404);
      }else{
        res.setStatus(200);
      }
    }
    catch (Exception ex) {
      res.setStatus(500);
    }

    return res;
  }
  
  /**
   * /update  --> Update the email and the name for the user in the database 
   * having the passed id.
   * 
   * @param id The id for the user to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the user is succesfully updated or not.
   */
  @RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateUser(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      User user = userDao.findOne(id);
      user.setUsername(requestBody.get("username").asText());
      user.setPassword(requestBody.get("password").asText());
      userDao.save(user);
    }
    catch (Exception ex) {
      return "Error updating the user: " + ex.toString();
    }
    return "User succesfully updated!";
  }

  /**
   * /login  --> desc
   * 
   * @param email The email to search in the database.
   * @param password The password
   * @return The user id or a message error if the user is not found.
   */
  @RequestMapping(value="/login", method=RequestMethod.POST)
  @ResponseBody
  public Response authUser(@RequestBody ObjectNode requestBody) {
    List<User> user;
    Response res = new Response();
    Token token = new Token();
    PasswordService ps = new PasswordService();
    try {
      user = userDao.findByUsernameAndPassword( requestBody.get("username").asText(), ps.encrypt(requestBody.get("password").asText()) );
      user.get(0).clean();


      String jwtSecret = env.getProperty("jwt.secret");
      
      try {
        String jwt = JWT.create()
        .withIssuer("Pentcloud")
        .sign(Algorithm.HMAC256(jwtSecret));

        token.setUser(user.get(0));

        token.setToken(jwt);
        res.setData(token);

      } catch (JWTCreationException exception){
          //Invalid Signing configuration / Couldn't convert Claims.
        res.setData("JWTCreationException");
      }

      if (user == null) {
        res.setStatus(404);
      }else{
        res.setStatus(200);
      }
    }
    catch (Exception ex) {
      res.setStatus(500);
    }

    return res;
  }


  @RequestMapping("/users")
  @ResponseBody
  public Iterable<User> getUsers() {
    Iterable<User> users = userDao.findAll();

    // Collection<Object> list = new ArrayList<Object>();
    // for (Object item : iter) {
        // list.add(item);
    // }

    return users;
  }


  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private UserDao userDao;

  @Autowired
  private Environment env;
    
} // class UserController