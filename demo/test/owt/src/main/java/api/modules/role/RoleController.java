package mcce.sapp.controllers;

import mcce.sapp.models.Response;
import mcce.sapp.models.Role;
import mcce.sapp.models.RoleDao;
import mcce.sapp.models.Auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


import org.springframework.core.env.*;
import java.io.UnsupportedEncodingException;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * A class to test interactions with the MySQL database using the roleDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class RoleController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new role and save it in the database.
   * 
   * @param email role's email
   * @param name role's name
   * @return A string describing if the role is succesfully created or not.
   */
  @RequestMapping(value="/roles", method=RequestMethod.POST)
  @ResponseBody
  public Response create(@RequestBody ObjectNode requestBody, @RequestHeader("X-Access-Token") String token) {

    Response res = new Response();
    String jwtSecret = env.getProperty("jwt.secret");

    // try {
    //     Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(accessToken);
    // } catch (SignatureException e) {
    //     //don't trust the JWT!
    //     res.setStatus(401);
    //     res.setData("Unauthorized "+e.getCause()+" "+e.getMessage()+" "+jwtSecret+" "+accessToken);
    //     return res;
    // }

    try {
        try{
          JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          DecodedJWT jwt = verifier.verify(token);
        }catch(UnsupportedEncodingException ex){
          //
        };

        Role role;
        try {
          role = new Role( requestBody.get("name").asText(), requestBody.get("description").asText() );
          roleDao.save(role);
          res.setStatus(200);
          res.setData("Role succesfully created! (id = " + role.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the role: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * /delete  --> Delete the role having the passed id.
   * 
   * @param id The id of the role to delete
   * @return A string describing if the role is succesfully deleted or not.
   */
  @RequestMapping(value="/roles/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteRole(@PathVariable long id) {
    try {
      Role role = new Role(id);
      roleDao.delete(role);
    }
    catch (Exception ex) {
      return "Error deleting the role: " + ex.toString();
    }
    return "role succesfully deleted!";
  }

  /**
   * /get-by-email  --> Return the id for the role having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The role id or a message error if the role is not found.
   */
  @RequestMapping("/roles/{id}")
  @ResponseBody
  public Response getRole(@PathVariable long id) {
    Role role;
    Response res = new Response();
    try {
      role = roleDao.findOne(id);
        res.setData(role);
      if (role == null) {
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
   * /update  --> Update the email and the name for the role in the database 
   * having the passed id.
   * 
   * @param id The id for the role to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the role is succesfully updated or not.
   */
  @RequestMapping(value="/roles/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateRole(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      Role role = roleDao.findOne(id);
        role.setName(requestBody.get("name").asText());
        role.setDescription(requestBody.get("description").asText());
      roleDao.save(role);
    }
    catch (Exception ex) {
      return "Error updating the role: " + ex.toString();
    }
    return "Role succesfully updated!";
  }

  @RequestMapping("/roles")
  @ResponseBody
  public Iterable<Role> getRoles() {
    Iterable<Role> roles = roleDao.findAll();
    return roles;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private RoleDao roleDao;

  @Autowired
  private Environment env;
  
} // class RoleController