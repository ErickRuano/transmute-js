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
 * A class to test interactions with the MySQL database using the albumDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class AlbumController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * [POST] /albums  --> Create a new album and save it in the database.
   * 
   * @param name - album's name
   * @return A string describing if the album is succesfully created or not.
   */
  @RequestMapping(value="/albums", method=RequestMethod.POST)
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

        Album album;
        try {
          album = new Album( requestBody.get("name").asText() );
          // Create Artist
          Artist artist = new Artist();
          artist.setId(requestBody.get("artistId").asLong());
          album.setArtist(artist);
          albumDao.save(album);

          // Log
          // Get username
          verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Creó";
          String target = "album";
          String subject = album.getName ();
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
          res.setData("Album succesfully created! (id = " + album.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the album: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * [DELETE] /albums/{id}  --> Delete the album having the passed id.
   * 
   * @param id - The id of the album to delete
   * @return A string describing if the album is succesfully deleted or not.
   */
  @RequestMapping(value="/albums/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public Response deleteAlbum(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

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
        // Album album = new Album(id);
        Album album = albumDao.findOne(id);
        albumDao.delete(album);

        // Log
          // Get username
          JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          DecodedJWT jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Eliminó";
          String target = "album";
          // Get subject
          String subject;

            if (album == null) {
              subject = "[eliminado]";
            }else{
              subject = album.getName();
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
        res.setData("Album succesfully deleted!");
      }
      catch (Exception ex) {
        res.setData("Error deleting the album: " + ex.toString());
      }
      
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  /**
   * [GET] /albums/{id}  --> Return the album
   * 
   * @return The album id or a message error if the album is not found.
   */
  @RequestMapping("/albums/{id}")
  @ResponseBody
  public Response getAlbum(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

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
      Album album;
      try {
        album = albumDao.findOne(id);
        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Consultó";
        String target = "album";
        // Get subject
        String subject;
          if (album == null) {
            subject = "[eliminado]";
          }else{
            subject = album.getName();
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
          res.setData(album);
        if (album == null) {
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
   * [PUT] /albums/{id}  --> Update the album in the database 
   * having the passed id.
   * 
   * @param id The id for the album to update.
   * @param name - album's name to update.
   * @return A string describing if the album is succesfully updated or not.
   */
  @RequestMapping(value="/albums/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody Response updateAlbum(@PathVariable long id, @RequestBody ObjectNode requestBody, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    
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
        Album album = albumDao.findOne(id);
        String oldSubject = album.getName();
          album.setName(requestBody.get("name").asText());
        albumDao.save(album);

        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Actualizó";
        String target = "album";
        // Get subject
        String subject;
          if (album == null) {
            subject = "[eliminado]";
          }else{
            subject = album.getName();
            if(!oldSubject.equals(subject)){
              subject = oldSubject + " a " + album.getName();
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
        res.setData("Album succesfully updated!");
      }
      catch (Exception ex) {
        res.setData("Error updating the album: " + ex.toString());
      }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  @RequestMapping("/albums")
  @ResponseBody
  public Iterable<Album> getAlbums(@RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    Message msg = new Message();
    Iterable<Album> albums = albumDao.findAll();
    return albums;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private AlbumDao albumDao;

  @Autowired
  private LogDao logDao;

  @Autowired
  private Environment env;
  
} // class AlbumController