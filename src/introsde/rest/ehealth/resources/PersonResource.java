package introsde.rest.ehealth.resources;

import introsde.rest.ehealth.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Stateless
@LocalBean
public class PersonResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	EntityManager entityManager;
	
	int id;

	public PersonResource(UriInfo uriInfo, Request request,int id, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
	}
	
	public PersonResource(UriInfo uriInfo, Request request,int id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	
	// Application integration
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPerson() {
		Person person = this.getPersonById(id);
		if (person == null)
			return Response.status(404)
					.entity("Get: Person with id " + id + " not found").build();
		return Response.status(200).entity(person).build();
	}

	// for the browser
	@GET
	@Produces(MediaType.TEXT_XML)
	public Person getPersonHTML() {
		Person person = this.getPersonById(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		System.out.println("Returning person... " + person.getIdPerson());
		return person;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putPerson(Person person) {
		System.out.println("--> Updating Person... " +this.id);
		System.out.println("--> "+person.toString());
		Person.updatePerson(person);
		
		Response res;
		
		Person existing = getPersonById(this.id);
		
		if (existing == null) {
			res = Response.noContent().build();
		} else {
			//res = Response.created(uriInfo.getAbsolutePath()).build();
			person.setIdPerson(this.id);
			Person p = Person.updatePerson(person);
			res = Response.ok().entity(p).build();
		}

		return res;

		
	}

	@DELETE
	public Response deletePerson() {
		Person c = getPersonById(id);
		if (c == null) {
			return Response.status(404)
					.entity("Get: Person with id " + id + " not found").build();
		}
		Person.removePerson(c);
		return Response.status(200)
				.entity("Get: Person with id " + id + " is removed").build();
		
	}

	
	
	public Person getPersonById(int personId) {
		System.out.println("Reading person from DB with id: "+personId);
		//Person person = entityManager.find(Person.class, personId);
		
		Person person = Person.getPersonById(personId);
		if (person != null) {
			System.out.println("Person: " + person.toString());
			return person;
		}
		return null;
	}
}