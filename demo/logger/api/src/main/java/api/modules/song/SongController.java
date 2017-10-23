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
 * A class to test interactions with the MySQL database using the songDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class SongController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * [POST] /songs  --> Create a new song and save it in the database.
   * 
   * @param name - song's name
   * @return A string describing if the song is succesfully created or not.
   */
  @RequestMapping(value="/songs", method=RequestMethod.POST)
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

        Song song;
        try {
          song = new Song( requestBody.get("name").asText() );
          // Create Album
          Album album = new Album();
          album.setId(requestBody.get("albumId").asLong());
          song.setAlbum(album);
          songDao.save(song);

          // Log
          // Get username
          verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Creó";
          String target = "song";
          String subject = song.getName ();
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
          res.setData("Song succesfully created! (id = " + song.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the song: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * [DELETE] /songs/{id}  --> Delete the song having the passed id.
   * 
   * @param id - The id of the song to delete
   * @return A string describing if the song is succesfully deleted or not.
   */
  @RequestMapping(value="/songs/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public Response deleteSong(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

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
        // Song song = new Song(id);
        Song song = songDao.findOne(id);
        songDao.delete(song);

        // Log
          // Get username
          JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          DecodedJWT jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Eliminó";
          String target = "song";
          // Get subject
          String subject;

            if (song == null) {
              subject = "[eliminado]";
            }else{
              subject = song.getName();
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
        res.setData("Song succesfully deleted!");
      }
      catch (Exception ex) {
        res.setData("Error deleting the song: " + ex.toString());
      }
      
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  /**
   * [GET] /songs/{id}  --> Return the song
   * 
   * @return The song id or a message error if the song is not found.
   */
  @RequestMapping("/songs/{id}")
  @ResponseBody
  public Response getSong(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

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
      Song song;
      try {
        song = songDao.findOne(id);
        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Consultó";
        String target = "song";
        // Get subject
        String subject;
          if (song == null) {
            subject = "[eliminado]";
          }else{
            subject = song.getName();
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
          res.setData(song);
        if (song == null) {
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
   * [PUT] /songs/{id}  --> Update the song in the database 
   * having the passed id.
   * 
   * @param id The id for the song to update.
   * @param name - song's name to update.
   * @return A string describing if the song is succesfully updated or not.
   */
  @RequestMapping(value="/songs/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody Response updateSong(@PathVariable long id, @RequestBody ObjectNode requestBody, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    
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
        Song song = songDao.findOne(id);
        String oldSubject = song.getName();
          song.setName(requestBody.get("name").asText());
        songDao.save(song);

        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Actualizó";
        String target = "song";
        // Get subject
        String subject;
          if (song == null) {
            subject = "[eliminado]";
          }else{
            subject = song.getName();
            if(!oldSubject.equals(subject)){
              subject = oldSubject + " a " + song.getName();
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
        res.setData("Song succesfully updated!");
      }
      catch (Exception ex) {
        res.setData("Error updating the song: " + ex.toString());
      }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  @RequestMapping("/songs")
  @ResponseBody
  public Iterable<Song> getSongs(@RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    Message msg = new Message();
    Iterable<Song> songs = songDao.findAll();
    return songs;
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private SongDao songDao;

  @Autowired
  private LogDao logDao;

  @Autowired
  private Environment env;
  
} // class SongController