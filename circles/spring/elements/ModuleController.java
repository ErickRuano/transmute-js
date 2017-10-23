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
 * A class to test interactions with the MySQL database using the {{id}}Dao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class {{capitalizedId}}Controller {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  /**
   * [POST] /{{pluralId}}  --> Create a new {{id}} and save it in the database.
   * 
   {{#fields}}
   * @param {{name}} - {{../id}}'s {{name}}
   {{/fields}}
   * @return A string describing if the {{id}} is succesfully created or not.
   */
  @RequestMapping(value="/{{pluralId}}", method=RequestMethod.POST)
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

        {{capitalizedId}} {{id}};
        try {
          {{id}} = new {{capitalizedId}}( {{#fields}}requestBody.get("{{name}}"){{#if isString}}.asText(){{/if}}{{#if isInt}}.asInt(){{/if}}{{#unless @last}}, {{/unless}}{{/fields}} );
          {{#parents}}
          // Create {{capitalizedId}}
          {{capitalizedId}} {{lowerId}} = new {{capitalizedId}}();
          {{lowerId}}.setId(requestBody.get("{{lowerId}}Id").asLong());
          {{../lowerId}}.set{{capitalizedId}}({{lowerId}});
          {{/parents}}
          {{id}}Dao.save({{id}});

          // Log
          // Get username
          verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Creó";
          String target = "{{id}}";
          String subject = {{id}}.get{{#fields}}{{#if @first}}{{capitalizedName}} {{/if}}{{/fields}}();
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
          res.setData("{{capitalizedId}} succesfully created! (id = " + {{id}}.getId() + ")");
        }
        catch (Exception ex) {
          res.setStatus(401);
          res.setData("Error creating the {{id}}: " + ex.toString());
        }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }
    return res;

  }
  
  /**
   * [DELETE] /{{pluralId}}/{id}  --> Delete the {{id}} having the passed id.
   * 
   * @param id - The id of the {{id}} to delete
   * @return A string describing if the {{id}} is succesfully deleted or not.
   */
  @RequestMapping(value="/{{pluralId}}/{id}", method=RequestMethod.DELETE)
  @ResponseBody
  public Response delete{{capitalizedId}}(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

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
        // {{capitalizedId}} {{id}} = new {{capitalizedId}}(id);
        {{capitalizedId}} {{id}} = {{id}}Dao.findOne(id);
        {{id}}Dao.delete({{id}});

        // Log
          // Get username
          JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
          DecodedJWT jwt = verifier.verify(token);
          Claim tokenUsername = jwt.getHeaderClaim("username");
          // Set action and target
          String action = "Eliminó";
          String target = "{{id}}";
          // Get subject
          String subject;

            if ({{id}} == null) {
              subject = "[eliminado]";
            }else{
              subject = {{id}}.get{{#fields}}{{#if @first}}{{capitalizedName}}{{/if}}{{/fields}}();
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
        res.setData("{{capitalizedId}} succesfully deleted!");
      }
      catch (Exception ex) {
        res.setData("Error deleting the {{id}}: " + ex.toString());
      }
      
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  /**
   * [GET] /{{pluralId}}/{id}  --> Return the {{id}}
   * 
   * @return The {{id}} id or a message error if the {{id}} is not found.
   */
  @RequestMapping("/{{pluralId}}/{id}")
  @ResponseBody
  public Response get{{capitalizedId}}(@PathVariable long id, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {

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
      {{capitalizedId}} {{id}};
      try {
        {{id}} = {{id}}Dao.findOne(id);
        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Consultó";
        String target = "{{id}}";
        // Get subject
        String subject;
          if ({{id}} == null) {
            subject = "[eliminado]";
          }else{
            subject = {{id}}.get{{#fields}}{{#if @first}}{{capitalizedName}}{{/if}}{{/fields}}();
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
          res.setData({{id}});
        if ({{id}} == null) {
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
   * [PUT] /{{pluralId}}/{id}  --> Update the {{id}} in the database 
   * having the passed id.
   * 
   * @param id The id for the {{id}} to update.
   {{#fields}}
   * @param {{name}} - {{../id}}'s {{name}} to update.
   {{/fields}}
   * @return A string describing if the {{id}} is succesfully updated or not.
   */
  @RequestMapping(value="/{{pluralId}}/{id}", method=RequestMethod.PUT)
  
  public @ResponseBody Response update{{capitalizedId}}(@PathVariable long id, @RequestBody ObjectNode requestBody, @RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    
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
        {{capitalizedId}} {{id}} = {{id}}Dao.findOne(id);
        String oldSubject = {{id}}.get{{#fields}}{{#if @first}}{{capitalizedName}}{{/if}}{{/fields}}();
        {{#fields}}
          {{../id}}.set{{capitalizedName}}(requestBody.get("{{name}}"){{#if isString}}.asText(){{/if}}{{#if isInt}}.asInt(){{/if}});
        {{/fields}}
        {{id}}Dao.save({{id}});

        // Log
        // Get username
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtSecret)).withIssuer("Pentcloud").build();
        DecodedJWT jwt = verifier.verify(token);
        Claim tokenUsername = jwt.getHeaderClaim("username");
        // Set action and target
        String action = "Actualizó";
        String target = "{{id}}";
        // Get subject
        String subject;
          if ({{id}} == null) {
            subject = "[eliminado]";
          }else{
            subject = {{id}}.get{{#fields}}{{#if @first}}{{capitalizedName}}{{/if}}{{/fields}}();
            if(!oldSubject.equals(subject)){
              subject = oldSubject + " a " + {{id}}.get{{#fields}}{{#if @first}}{{capitalizedName}}{{/if}}{{/fields}}();
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
        res.setData("{{capitalizedId}} succesfully updated!");
      }
      catch (Exception ex) {
        res.setData("Error updating the {{id}}: " + ex.toString());
      }
    } catch (JWTVerificationException exception){
        //Invalid signature/claims
        res.setStatus(401);
        res.setData("Unauthorized");
      
    }

    return res;

  }

  @RequestMapping("/{{pluralId}}")
  @ResponseBody
  public Iterable<{{capitalizedId}}> get{{capitalizedId}}s(@RequestHeader("X-Access-Token") String token, HttpServletRequest request) {
    Message msg = new Message();
    Iterable<{{capitalizedId}}> {{pluralId}} = {{id}}Dao.findAll();
    return {{pluralId}};
  }

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private {{capitalizedId}}Dao {{id}}Dao;

  @Autowired
  private LogDao logDao;

  @Autowired
  private Environment env;
  
} // class {{capitalizedId}}Controller