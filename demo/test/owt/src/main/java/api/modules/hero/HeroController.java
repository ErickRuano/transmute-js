package mcce.sapp.controllers;

import mcce.sapp.models.Response;
import mcce.sapp.models.Hero;
import mcce.sapp.models.HeroDao;
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
 * A class to test interactions with the MySQL database using the heroDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class HeroController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new hero and save it in the database.
   * 
   * @param email hero's email
   * @param name hero's name
   * @return A string describing if the hero is succesfully created or not.
   */
  @RequestMapping(value="/heroes", method=RequestMethod.POST)
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

        Hero hero;
        try {
          hero = new Hero( requestBody.get("name").asText() );
          heroDao.save(hero);
          res.setStatus(200);
          res.setData("Hero succesfully created! (id = " + hero.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the hero: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * /delete  --> Delete the hero having the passed id.
   * 
   * @param id The id of the hero to delete
   * @return A string describing if the hero is succesfully deleted or not.
   */
  @RequestMapping(value="/heroes/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteHero(@PathVariable long id) {
    try {
      Hero hero = new Hero(id);
      heroDao.delete(hero);
    }
    catch (Exception ex) {
      return "Error deleting the hero: " + ex.toString();
    }
    return "hero succesfully deleted!";
  }

  /**
   * /get-by-email  --> Return the id for the hero having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The hero id or a message error if the hero is not found.
   */
  @RequestMapping("/heroes/{id}")
  @ResponseBody
  public Response getHero(@PathVariable long id) {
    Hero hero;
    Response res = new Response();
    try {
      hero = heroDao.findOne(id);
        res.setData(hero);
      if (hero == null) {
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
   * /update  --> Update the email and the name for the hero in the database 
   * having the passed id.
   * 
   * @param id The id for the hero to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the hero is succesfully updated or not.
   */
  @RequestMapping(value="/heroes/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateHero(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      Hero hero = heroDao.findOne(id);
        hero.setName(requestBody.get("name").asText());
      heroDao.save(hero);
    }
    catch (Exception ex) {
      return "Error updating the hero: " + ex.toString();
    }
    return "Hero succesfully updated!";
  }

  @RequestMapping("/heroes")
  @ResponseBody
  public Iterable<Hero> getHeros() {
    Iterable<Hero> heroes = heroDao.findAll();
    return heroes;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private HeroDao heroDao;

  @Autowired
  private Environment env;
  
} // class HeroController