package mcce.sapp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
import java.util.Date;
import java.util.Set;
import org.hibernate.annotations.*;

import mcce.sapp.models.*;

/**
 * An entity User composed by three fields (id, email, name).
 * The Entity annotation indicates that this class is a JPA entity.
 * The Table annotation specifies the name for the table in the db.
 */
@Entity
@Table(name = "stage")
public class Stage {

  // ------------------------
  // PRIVATE FIELDS
  // ------------------------
  
  // An autogenerated id (unique for each article in the db)
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  
  @NotNull
  private String name;
  @NotNull
  private String description;


  @OneToMany(mappedBy="stage")
  private Set<Mode> modes;
  @OneToMany(mappedBy="stage")
  private Set<Comp> comps;


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
  
  public Stage() { }

  public Stage(long id) { 
    this.id = id;
  }
  
  public Stage( String name ,  String description  ) {
      this.name = name;
      this.description = description;
  }

  

  public long getId() {
      return this.id;
  }


  public String getName() {
      return this.name;
  }
  public String getDescription() {
      return this.description;
  }


    public Set<Mode> getModes() {
      return this.modes;
    } 
    public Set<Comp> getComps() {
      return this.comps;
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
  public void setDescription(String description) {
      this.description = description;
  }



  public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
  }

  public void setUdpatedAt(Date udpatedAt) {
      this.udpatedAt = udpatedAt;
  }

  
} // class