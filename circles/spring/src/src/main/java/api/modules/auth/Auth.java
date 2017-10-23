package mcce.sapp.models;

import java.util.*;

/**
 *
 */
public class Auth {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  private String token;
  
  public Auth() { }

  public Auth(String token) { 
    this.token = token;
  }

  public String getToken() {
      return this.token;
  }

  public void setToken(String token) {
      this.token = token;
  }
  
} // class Auth