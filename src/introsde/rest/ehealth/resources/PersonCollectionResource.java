package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.junit.runners.Parameterized.Parameters;

/*
 * TODO 
 * - There is a problem with the EntityManager injection through @PersistenceUnit or @PersistenceContext
 * - will look into it later
 */

@Stateless
@LocalBean//Will map the resource to the URL /ehealth/v2
@Path("/person")
public class PersonCollectionResource {

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	// THIS IS NOT WORKING
	@PersistenceUnit(unitName="introsde-jpa")
	EntityManager entityManager;
	
	// THIS IS NOT WORKING
    @PersistenceContext(unitName = "introsde-jpa",type=PersistenceContextType.TRANSACTION)
    private EntityManagerFactory entityManagerFactory;
	  
	// Return the list of people to the user in the browser based on the params given
		@GET
		@Consumes(MediaType.APPLICATION_XML)
		@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
		public List<Person> getPersonsByMeasureType(@QueryParam("measureType")String measureType, 
				@QueryParam("max") String max, @QueryParam("min") String min) {

			System.out.println("Getting list of people by measure types ...");
		    List<Person> people = Person.getAll();
		    List<Person> peoplebymeasureType = new ArrayList<Person>();
			if( measureType != null)
			{
		    for (Person person : people) {
		    	List<LifeStatus> lifeStatus = person.getLifeStatus();
				System.out.println("Here ???  ...");
		    	if(lifeStatus.size()>0)
		    	for (LifeStatus liStatus : lifeStatus)
		    	{
		    		System.out.println(liStatus.getMeasureDefinition().getMeasureName()	);
		    		if(liStatus.getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType)
		    				&&(Double.parseDouble(liStatus.getValue())<Double.parseDouble(max) && 
		    						Double.parseDouble(liStatus.getValue())>Double.parseDouble(min)))
		    		{

			    		System.out.println(liStatus.getMeasureDefinition().getMeasureName()	+"loop");
		    			peoplebymeasureType.add(person);
						System.out.println(person.getLifeStatus());
		    			break;
		    		}

		    	}
		    	if(peoplebymeasureType != null)
				System.out.println(peoplebymeasureType.size());
			}
			}
			else
				peoplebymeasureType = people;
			return peoplebymeasureType;
		}

	// retuns the number of people
	// to get the total number of records
	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		System.out.println("Getting count...");
	    //List<Person> list = entityManager.createNamedQuery("Person.findAll", Person.class).getResultList();
	    List<Person> people = Person.getAll();
		int count = people.size();
		return String.valueOf(count);
	}

//	// let's create this service for responding a submission form
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newPerson(@FormParam("id") int id,
			@FormParam("firstname") String firstname,
			@FormParam("lastname") String lastname,
			@FormParam("healthProfile/weight") String weight,
			@FormParam("healthProfile/height") String height,
			@Context HttpServletResponse servletResponse) throws IOException {
		Person p = new Person();

		System.out.println("xxxxxxxxxxxxxx" + weight);
		List<LifeStatus> ls = new ArrayList<LifeStatus>();
		LifeStatus lis = new LifeStatus();
		MeasureDefinition mesDef = new MeasureDefinition();
		lis.setMeasureDefinition(mesDef.getMeasure("weight"));
		lis.setValue(weight);
		ls.add(lis);
		lis.setMeasureDefinition(mesDef.getMeasure("height"));
		lis.setValue(height);
		ls.add(lis);
		p.setIdPerson(id);
		p.setName(firstname);
		p.setLastname(lastname);
		System.out.println("xxxxxxxxxxxxxx" + weight);
		p.setLifeStatus(ls); 
		Person.savePerson(p);
		servletResponse.sendRedirect("../NewPerson.html");
	}
	
	
	// let's create this service for responding a submission form
	// 
	@POST
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person newPerson(Person person) throws IOException {
		System.out.println("Creating new person...");
		//EntityManager entityManager = entityManagerFactory.createEntityManager();
//		EntityManager entityManager = PersonDao.instance.createEntityManager();
//        try {
//    		entityManager.persist(person);
//    		entityManager.refresh(person);
//    		return person;
//        } finally {
//            entityManager.close();
//        }
		
		
		System.out.println("xxxxxxxxxxxxxx");
		return Person.savePerson(person);
	}
	

	// Defines that the next path parameter after the base url is
	// treated as a parameter and passed to the PersonResources
	// Allows to type http://localhost:599/base_url/1
	// 1 will be treaded as parameter todo and passed to PersonResource
	@Path("{personId}")
	public PersonResource getPerson(@PathParam("personId") int id) {
//		//EntityManager entityManager = entityManagerFactory.createEntityManager();
//		EntityManager em = PersonDao.instance.createEntityManager();
//        try {
//    		System.out.println("Person by id..."+id);
//    		return new PersonResource(uriInfo, request, id, em);
//        } finally {
//            em.close();
//        }
		
		return new PersonResource(uriInfo, request, id);
	}
	@Path("{personId}/{measureType}")
	public MeasureHistoryResource getPerson(@PathParam("personId") int id, @PathParam("measureType") String measure) {
//		//EntityManager entityManager = entityManagerFactory.createEntityManager();
//		EntityManager em = PersonDao.instance.createEntityManager();
//        try {
   		System.out.println("Person Health History by id..."+id);
//    		return new PersonResource(uriInfo, request, id, em);
//        } finally {
//            em.close();
//        }
		
		return new MeasureHistoryResource(uriInfo, request, id, measure);
	}
	
	@Path("{personId}/{measureType}/{mId}")
	public MeasureHistoryValueResource getPerson(@PathParam("personId") int id, @PathParam("measureType") String measure, @PathParam("mId") int measureId) {
//		//EntityManager entityManager = entityManagerFactory.createEntityManager();
//		EntityManager em = PersonDao.instance.createEntityManager();
//        try {
   		System.out.println("Person Health History by id..."+id);
//    		return new PersonResource(uriInfo, request, id, em);
//        } finally {
//            em.close();
//        }
		
		return new MeasureHistoryValueResource(uriInfo, request, id, measure, measureId);
	}
   /* @Path("{personId}/{measureType}/{measureHistoryId}")
    public int getMeasureValue(@PathParam("personId") int id, @PathParam("measureType") String measure, 
    							@PathParam("measureHistoryId") int measureHistoryId) {
    	
    }*/
}
