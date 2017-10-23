package mcce.sapp.controllers;

import java.util.*;

import mcce.sapp.models.*;
import mcce.sapp.logger.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.core.env.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;

/**
 * A class to test interactions with the MySQL database using the UserDao class.
 *
 * @author pentcloud
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class LogController {

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  @RequestMapping("/logs")
  @ResponseBody
  public Iterable<Log> getLogs() {
    Iterable<Log> logs = logDao.findAll();

    return logs;
  }


  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  @Autowired
  private LogDao logDao;

  @Autowired
  private Environment env;
    
} // class LoController