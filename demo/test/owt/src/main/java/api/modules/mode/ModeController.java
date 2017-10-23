package mcce.sapp.controllers;

import mcce.sapp.models.Response;
import mcce.sapp.models.Mode;
import mcce.sapp.models.ModeDao;
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
 * A class to test interactions with the MySQL database using the modeDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class ModeController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new mode and save it in the database.
   * 
   * @param email mode's email
   * @param name mode's name
   * @return A string describing if the mode is succesfully created or not.
   */
  @RequestMapping(value="/modes", method=RequestMethod.POST)
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

        Mode mode;
        try {
          mode = new Mode( requestBody.get("name").asText(), requestBody.get("description").asText(), requestBody.get("size").asInt() );
          modeDao.save(mode);
          res.setStatus(200);
          res.setData("Mode succesfully created! (id = " + mode.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the mode: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * /delete  --> Delete the mode having the passed id.
   * 
   * @param id The id of the mode to delete
   * @return A string describing if the mode is succesfully deleted or not.
   */
  @RequestMapping(value="/modes/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteMode(@PathVariable long id) {
    try {
      Mode mode = new Mode(id);
      modeDao.delete(mode);
    }
    catch (Exception ex) {
      return "Error deleting the mode: " + ex.toString();
    }
    return "mode succesfully deleted!";
  }

  /**
   * /get-by-email  --> Return the id for the mode having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The mode id or a message error if the mode is not found.
   */
  @RequestMapping("/modes/{id}")
  @ResponseBody
  public Response getMode(@PathVariable long id) {
    Mode mode;
    Response res = new Response();
    try {
      mode = modeDao.findOne(id);
        res.setData(mode);
      if (mode == null) {
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
   * /update  --> Update the email and the name for the mode in the database 
   * having the passed id.
   * 
   * @param id The id for the mode to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the mode is succesfully updated or not.
   */
  @RequestMapping(value="/modes/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateMode(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      Mode mode = modeDao.findOne(id);
        mode.setName(requestBody.get("name").asText());
        mode.setDescription(requestBody.get("description").asText());
        mode.setSize(requestBody.get("size").asInt());
      modeDao.save(mode);
    }
    catch (Exception ex) {
      return "Error updating the mode: " + ex.toString();
    }
    return "Mode succesfully updated!";
  }

  @RequestMapping("/modes")
  @ResponseBody
  public Iterable<Mode> getModes() {
    Iterable<Mode> modes = modeDao.findAll();
    return modes;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private ModeDao modeDao;

  @Autowired
  private Environment env;
  
} // class ModeController