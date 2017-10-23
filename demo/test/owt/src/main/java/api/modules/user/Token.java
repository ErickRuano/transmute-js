package mcce.sapp.models;

import java.util.*;

/**
 *
 */
public class Token {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  private User user;

  private String token;
  
  public Token() { }

  public Token(User user, String token) { 
    this.user = user;
    this.token = token;
  }

  public User getUser() {
      return this.user;
  }

  public String getToken() {
      return this.token;
  }

  public void setUser(User user) {
      this.user = user;
  }

  public void setToken(String token) {
      this.token = token;
  }

  
} // class Token