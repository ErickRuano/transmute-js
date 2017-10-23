package mcce.sapp.logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;
import org.hibernate.annotations.*;


/**
 *
 */
@Entity
@Table(name = "log")
public class Log {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NotNull
  private String message;

  @NotNull
  private String username;

  @NotNull
  private String ip;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createdAt")
  private Date createdAt;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updatedAt")
  private Date udpatedAt;

  public Log() { }
  
  public long getId() {
      return this.id;
  }

  public String getMessage() {
      return this.message;
  }

  public String getUsername() {
      return this.username;
  }

  public String getIp() {
      return this.ip;
  }

  public Date getCreatedAt() {
      return this.createdAt;
  }

  public Date getUdpatedAt() {
      return this.udpatedAt;
  }

  public void setId(long id) {
      this.id = id;
  }

  public void setMessage(String message) {
      this.message = message;
  }

  public void setUsername(String username) {
      this.username = username;
  }

  public void setIp(String ip) {
      this.ip = ip;
  }

  public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
  }

  public void setUdpatedAt(Date udpatedAt) {
      this.udpatedAt = udpatedAt;
  }

  
} // class Log