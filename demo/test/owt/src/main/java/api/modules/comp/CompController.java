package mcce.sapp.controllers;

import mcce.sapp.models.Response;
import mcce.sapp.models.Comp;
import mcce.sapp.models.CompDao;
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
 * A class to test interactions with the MySQL database using the compDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class CompController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new comp and save it in the database.
   * 
   * @param email comp's email
   * @param name comp's name
   * @return A string describing if the comp is succesfully created or not.
   */
  @RequestMapping(value="/comps", method=RequestMethod.POST)
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

        Comp comp;
        try {
          comp = new Comp( requestBody.get("name").asText(), requestBody.get("description").asText() );
          compDao.save(comp);
          res.setStatus(200);
          res.setData("Comp succesfully created! (id = " + comp.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the comp: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * /delete  --> Delete the comp having the passed id.
   * 
   * @param id The id of the comp to delete
   * @return A string describing if the comp is succesfully deleted or not.
   */
  @RequestMapping(value="/comps/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteComp(@PathVariable long id) {
    try {
      Comp comp = new Comp(id);
      compDao.delete(comp);
    }
    catch (Exception ex) {
      return "Error deleting the comp: " + ex.toString();
    }
    return "comp succesfully deleted!";
  }

  /**
   * /get-by-email  --> Return the id for the comp having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The comp id or a message error if the comp is not found.
   */
  @RequestMapping("/comps/{id}")
  @ResponseBody
  public Response getComp(@PathVariable long id) {
    Comp comp;
    Response res = new Response();
    try {
      comp = compDao.findOne(id);
        res.setData(comp);
      if (comp == null) {
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
   * /update  --> Update the email and the name for the comp in the database 
   * having the passed id.
   * 
   * @param id The id for the comp to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the comp is succesfully updated or not.
   */
  @RequestMapping(value="/comps/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateComp(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      Comp comp = compDao.findOne(id);
        comp.setName(requestBody.get("name").asText());
        comp.setDescription(requestBody.get("description").asText());
      compDao.save(comp);
    }
    catch (Exception ex) {
      return "Error updating the comp: " + ex.toString();
    }
    return "Comp succesfully updated!";
  }

  @RequestMapping("/comps")
  @ResponseBody
  public Iterable<Comp> getComps() {
    Iterable<Comp> comps = compDao.findAll();
    return comps;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private CompDao compDao;

  @Autowired
  private Environment env;
  
} // class CompController