package mcce.sapp.logger;

import java.util.*;

/**
 *
 */
public class Message {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------

  private static final String template = "{{action}} {{target}} {{subject}}.";
  
  private String message;
  
  public Message() { }

  public Message(String message) {
    this.message = message;
  }

  public String getMessage() {
      return this.message;
  }

  public void setMessage(String token) {
      this.message = message;
  }

  public String generateMessage(String action, String target, String subject) {
      String generated = "";
      generated = this.template.replace("{{action}}", action);
      generated = generated.replace("{{target}}", target);
      generated = generated.replace("{{subject}}", subject);
      this.message = generated;
      return this.message;
  }

  
} // class Message