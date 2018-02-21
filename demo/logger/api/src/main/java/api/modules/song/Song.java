package mcce.sapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Lob;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany  ;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.*;
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.*;

import mcce.sapp.models.*;

/**
 * An entity Song composed by   .
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 */
@Entity
@Table(name = "song")
public class Song {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  // An autogenerated id (unique for each song in the db)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  
  @NotNull
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="albumId")
  @JsonIgnore
  private Album album;



  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "createdAt")
  private Date createdAt;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updatedAt")
  private Date udpatedAt;

  // ------------------------
  // PUBLIC METHODS
  // ------------------------
  
  public Song() { }

  public Song(long id) { 
    this.id = id;
  }
  
  public Song( String name  ) {
      this.name = name;
  }

  

  public long getId() {
      return this.id;
  }


  public String getName() {
      return this.name;
  }

    public Album getAlbum() {
      return this.album;
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


  public void setName(String name) {
      this.name = name;
  }

  public void setAlbum(Album album) {
      this.album = album;
  }


  public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
  }

  public void setUdpatedAt(Date udpatedAt) {
      this.udpatedAt = udpatedAt;
  }

  
} // class