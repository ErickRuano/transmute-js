package mcce.sapp.models;

public class Response {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  //
  private long status;
  
  // 
  private Object data;
  
  // ------------------------
  // PUBLIC METHODS
  // ------------------------

  public Response() { 
  }

  public Response(long status) { 
    this.status = status;
  }
  
  public Response(long status, Object data) {
    this.status = status;
    this.data = data;
  }

  // Getter and setter methods

  public long getStatus() {
    return status;
  }

  public void setStatus(long value) {
    this.status = value;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object value) {
    this.data = value;
  }

  
} // class Response