package mcce.sapp.controllers;

import mcce.sapp.models.Response;
import mcce.sapp.models.Stage;
import mcce.sapp.models.StageDao;
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
 * A class to test interactions with the MySQL database using the stageDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class StageController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new stage and save it in the database.
   * 
   * @param email stage's email
   * @param name stage's name
   * @return A string describing if the stage is succesfully created or not.
   */
  @RequestMapping(value="/stages", method=RequestMethod.POST)
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

        Stage stage;
        try {
          stage = new Stage( requestBody.get("name").asText(), requestBody.get("description").asText() );
          stageDao.save(stage);
          res.setStatus(200);
          res.setData("Stage succesfully created! (id = " + stage.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the stage: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * /delete  --> Delete the stage having the passed id.
   * 
   * @param id The id of the stage to delete
   * @return A string describing if the stage is succesfully deleted or not.
   */
  @RequestMapping(value="/stages/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteStage(@PathVariable long id) {
    try {
      Stage stage = new Stage(id);
      stageDao.delete(stage);
    }
    catch (Exception ex) {
      return "Error deleting the stage: " + ex.toString();
    }
    return "stage succesfully deleted!";
  }

  /**
   * /get-by-email  --> Return the id for the stage having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The stage id or a message error if the stage is not found.
   */
  @RequestMapping("/stages/{id}")
  @ResponseBody
  public Response getStage(@PathVariable long id) {
    Stage stage;
    Response res = new Response();
    try {
      stage = stageDao.findOne(id);
        res.setData(stage);
      if (stage == null) {
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
   * /update  --> Update the email and the name for the stage in the database 
   * having the passed id.
   * 
   * @param id The id for the stage to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the stage is succesfully updated or not.
   */
  @RequestMapping(value="/stages/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateStage(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      Stage stage = stageDao.findOne(id);
        stage.setName(requestBody.get("name").asText());
        stage.setDescription(requestBody.get("description").asText());
      stageDao.save(stage);
    }
    catch (Exception ex) {
      return "Error updating the stage: " + ex.toString();
    }
    return "Stage succesfully updated!";
  }

  @RequestMapping("/stages")
  @ResponseBody
  public Iterable<Stage> getStages() {
    Iterable<Stage> stages = stageDao.findAll();
    return stages;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private StageDao stageDao;

  @Autowired
  private Environment env;
  
} // class StageController