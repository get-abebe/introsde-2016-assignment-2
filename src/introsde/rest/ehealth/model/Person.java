package introsde.rest.ehealth.model;

import introsde.rest.ehealth.dao.LifeCoachDao;
import introsde.rest.ehealth.model.LifeStatus;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import introsde.rest.ehealth.xml.HealthProfile_XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the "Person" database table.
 * 
 */
@Entity
@Table(name="Person")
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
@XmlType(propOrder = { "idPerson", "name", "lastname",  "birthdate", "username", "email", "healthprofile", "lifeStatus" })

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	// For sqlite in particular, you need to use the following @GeneratedValue annotation
	// This holds also for the other tables
	// SQLITE implements auto increment ids through named sequences that are stored in a 
	// special table named "sqlite_sequence"
	@GeneratedValue(generator="sqlite_person")
	@TableGenerator(name="sqlite_person", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="Person")
	
	@Column(name="idPerson")
	private int idPerson;

	@Column(name="name")
	private String name;
	
	@Column(name="lastname")
	private String lastname;
	
	//@Temporal(TemporalType.DATE)
	@Column(name="birthdate")
	private String birthdate;
		
	@Column(name="username")
	private String username;
		
	@Column(name="email")
	private String email;

	// mappedBy must be equal to the name of the attribute in LifeStatus that maps this relation
	@OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<LifeStatus> lifeStatus;
	
	@OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private List<HealthMeasureHistory> healthHistory;
	//uncomment below
	@Transient
	private HealthProfile_XML healthprofile = new HealthProfile_XML();
	
	public Person() {
	}
	public HealthProfile_XML gethealthprofile() {
		return this.healthprofile;
	}

	public void sethealthprofile(HealthProfile_XML healthprofile) {
		this.healthprofile = healthprofile;
	}
	public int getIdPerson() {
		return this.idPerson;
	}

	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy  HH:mm");
	public String getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	// the XmlElementWrapper defines the name of node in which the list of LifeStatus elements
	// will be inserted
	@XmlElementWrapper(name = "Measurements")//changed from Measurements to HealthProfile
//	@XmlElement(name = "Measure")
//	@XmlTransient
	public List<LifeStatus> getLifeStatus() {
	    return lifeStatus;
	}

	public void setLifeStatus(List<LifeStatus> param) {
	    this.lifeStatus = param;
	}
	
	//@XmlElementWrapper(name = "Measurements")
	//@XmlElement(name = "Measure") //comment the block of code below
	public List<HealthMeasureHistory> healthHistory() {
	    return healthHistory;
	}

	public void healthHistory(List<HealthMeasureHistory> param) {
	    this.healthHistory = param;
	}
	
	// Database operations
	
	public static Person getPersonById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		if (p == null)
		return null;		
		else 
		return p;
	}
	
	public static List<Person> getAll() {

		System.out.println("--> Initializing Entity manager...");
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		System.out.println("--> Querying the database for all the people...");
	    List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		System.out.println("--> Closing connections of entity manager...");
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static Person savePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static Person updatePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}
