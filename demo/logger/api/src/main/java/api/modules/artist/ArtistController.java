package mcce.sapp.controllers;

import mcce.sapp.models.*;
import mcce.sapp.logger.*;

import javax.servlet.http.HttpServletRequest;

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
import com.auth0.jwt.interfaces.Claim;

import org.springframework.core.env.*;
import java.io.UnsupportedEncodingException;
import com.fasterxml.jackson.databind.node.ObjectNode;


/**
 * A class to test interactions with the MySQL database using the artistDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class ArtistController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * [POST] /artists  --> Create a new artist and save it in the database.
   * 
   * @param name - artist's name
   * @return A string describing if the artist is succesfully created or not.
   */
  @RequestMapping(value="/artists", method=RequestMethod.POST)
  @ResponseBody
  public Response create(@RequestBody ObjectNode requestBody, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

    Response res = new Response();
    Message msg = new Message();

    String jwtSecret = env.getProperty("jwt.secret");
    JWTVerifier verifier;
    DecodedJWT jwt;

    try {
        try{
          verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          jwt = verifier.verify(token);
        }catch(UnsupportedEncodingException ex){
          //
        };

        Artist artist;
        try {
          artist = new Artist( requestBody.get("name").asText() );
          artistDao.save(artist);

          // Log
          // Get username
          verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Creó";
          String target = "artist";
          String subject = artist.getName ();
          String generatedMessage = msg.generateMessage(action, target, subject);
          //Get IP
          String ipAddress = request.getHeader("X-FORWARDED-FOR");
          if (ipAddress == null) {
               ipAddress = request.getRemoteAddr();
          }
          Log log = new Log();
          log.setUsername(tokenUsername.asString());
          log.setIp(ipAddress);
          log.setMessage(generatedMessage);
          logDao.save(log);

          res.setStatus(200);
          res.setData("Artist succesfully created! (id = " + artist.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the artist: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * [DELETE] /artists/{id}  --> Delete the artist having the passed id.
   * 
   * @param id - The id of the artist to delete
   * @return A string describing if the artist is succesfully deleted or not.
   */
  @RequestMapping(value="/artists/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public Response deleteArtist(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

    Response res = new Response();
    Message msg = new Message();

    String jwtSecret = env.getProperty("jwt.secret");

    try {
      try{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
      }catch(UnsupportedEncodingException ex){
        //
      };
      try {
        // Artist artist = new Artist(id);
        Artist artist = artistDao.findOne(id);
        artistDao.delete(artist);

        // Log
          // Get username
          JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          DecodedJWT jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Eliminó";
          String target = "artist";
          // Get subject
          String subject;

            if (artist == null) {
              subject = "[eliminado]";
            }else{
              subject = artist.getName();
            }

          // String subject = Long.toString(id);
          String generatedMessage = msg.generateMessage(action, target, subject);
          //Get IP
          String ipAddress = request.getHeader("X-FORWARDED-FOR");
          if (ipAddress == null) {
               ipAddress = request.getRemoteAddr();
          }
          Log log = new Log();
          log.setUsername(tokenUsername.asString());
          log.setIp(ipAddress);
          log.setMessage(generatedMessage);
          logDao.save(log);


        res.setStatus(200);
        res.setData("Artist succesfully deleted!");
      }
      catch (Exception ex) {
        res.setData("Error deleting the artist: " + ex.toString());
      }
      
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  /**
   * [GET] /artists/{id}  --> Return the artist
   * 
   * @return The artist id or a message error if the artist is not found.
   */
  @RequestMapping("/artists/{id}")
  @ResponseBody
  public Response getArtist(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

    Response res = new Response();
    Message msg = new Message();

    String jwtSecret = env.getProperty("jwt.secret");

    try {
      try{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
      }catch(UnsupportedEncodingException ex){
        //
      };
      Artist artist;
      try {
        artist = artistDao.findOne(id);
        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Consultó";
        String target = "artist";
        // Get subject
        String subject;
          if (artist == null) {
            subject = "[eliminado]";
          }else{
            subject = artist.getName();
          }
        // String subject = Long.toString(id);
        String generatedMessage = msg.generateMessage(action, target, subject);
        //Get IP
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
             ipAddress = request.getRemoteAddr();
        }
        Log log = new Log();
        log.setUsername(tokenUsername.asString());
        log.setIp(ipAddress);
        log.setMessage(generatedMessage);
        logDao.save(log);
        /// /log
          res.setData(artist);
        if (artist == null) {
          res.setStatus(404);
        }else{
          res.setStatus(200);
        }
      }
      catch (Exception ex) {
        res.setStatus(500);
      }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }


    return res;
  }
  
  /**
   * [PUT] /artists/{id}  --> Update the artist in the database 
   * having the passed id.
   * 
   * @param id The id for the artist to update.
   * @param name - artist's name to update.
   * @return A string describing if the artist is succesfully updated or not.
   */
  @RequestMapping(value="/artists/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody Response updateArtist(@PathVariable long id, @RequestBody ObjectNode requestBody, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    
    Response res = new Response();
    Message msg = new Message();

    String jwtSecret = env.getProperty("jwt.secret");

    try {
      try{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
      }catch(UnsupportedEncodingException ex){
        //
      };
      try {
        Artist artist = artistDao.findOne(id);
        String oldSubject = artist.getName();
          artist.setName(requestBody.get("name").asText());
        artistDao.save(artist);

        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Actualizó";
        String target = "artist";
        // Get subject
        String subject;
          if (artist == null) {
            subject = "[eliminado]";
          }else{
            subject = artist.getName();
            if(!oldSubject.equals(subject)){
              subject = oldSubject + " a " + artist.getName();
            }
          }
        // String subject = Long.toString(id);
        String generatedMessage = msg.generateMessage(action, target, subject);
        //Get IP
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
             ipAddress = request.getRemoteAddr();
        }
        Log log = new Log();
        log.setUsername(tokenUsername.asString());
        log.setIp(ipAddress);
        log.setMessage(generatedMessage);
        logDao.save(log);

        res.setStatus(200);
        res.setData("Artist succesfully updated!");
      }
      catch (Exception ex) {
        res.setData("Error updating the artist: " + ex.toString());
      }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  @RequestMapping("/artists")
  @ResponseBody
  public Iterable<Artist> getArtists(@RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    Message msg = new Message();
    Iterable<Artist> artists = artistDao.findAll();
    return artists;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private ArtistDao artistDao;

  @Autowired
  private LogDao logDao;

  @Autowired
  private Environment env;
  
} // class ArtistController