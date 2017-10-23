package mcce.sapp.controllers;

import mcce.sapp.models.Response;
import mcce.sapp.models.Team;
import mcce.sapp.models.TeamDao;
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
 * A class to test interactions with the MySQL database using the teamDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class TeamController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * /create  --> Create a new team and save it in the database.
   * 
   * @param email team's email
   * @param name team's name
   * @return A string describing if the team is succesfully created or not.
   */
  @RequestMapping(value="/teams", method=RequestMethod.POST)
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

        Team team;
        try {
          team = new Team( requestBody.get("name").asText(), requestBody.get("country").asText(), requestBody.get("description").asText() );
          teamDao.save(team);
          res.setStatus(200);
          res.setData("Team succesfully created! (id = " + team.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the team: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * /delete  --> Delete the team having the passed id.
   * 
   * @param id The id of the team to delete
   * @return A string describing if the team is succesfully deleted or not.
   */
  @RequestMapping(value="/teams/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public String deleteTeam(@PathVariable long id) {
    try {
      Team team = new Team(id);
      teamDao.delete(team);
    }
    catch (Exception ex) {
      return "Error deleting the team: " + ex.toString();
    }
    return "team succesfully deleted!";
  }

  /**
   * /get-by-email  --> Return the id for the team having the passed email.
   * 
   * @param email The email to search in the database.
   * @return The team id or a message error if the team is not found.
   */
  @RequestMapping("/teams/{id}")
  @ResponseBody
  public Response getTeam(@PathVariable long id) {
    Team team;
    Response res = new Response();
    try {
      team = teamDao.findOne(id);
        res.setData(team);
      if (team == null) {
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
   * /update  --> Update the email and the name for the team in the database 
   * having the passed id.
   * 
   * @param id The id for the team to update.
   * @param email The new email.
   * @param name The new name.
   * @return A string describing if the team is succesfully updated or not.
   */
  @RequestMapping(value="/teams/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody String updateTeam(@PathVariable long id, @RequestBody ObjectNode requestBody) {
    try {
      Team team = teamDao.findOne(id);
        team.setName(requestBody.get("name").asText());
        team.setCountry(requestBody.get("country").asText());
        team.setDescription(requestBody.get("description").asText());
      teamDao.save(team);
    }
    catch (Exception ex) {
      return "Error updating the team: " + ex.toString();
    }
    return "Team succesfully updated!";
  }

  @RequestMapping("/teams")
  @ResponseBody
  public Iterable<Team> getTeams() {
    Iterable<Team> teams = teamDao.findAll();
    return teams;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private TeamDao teamDao;

  @Autowired
  private Environment env;
  
} // class TeamController