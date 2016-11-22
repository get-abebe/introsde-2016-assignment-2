package introsde.rest.ehealth.resources;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;

//import com.sun.jmx.snmp.Timestamp;

@Stateless
@LocalBean
public class MeasureHistoryResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
    EntityManager entityManager;
	
	int id;
	int measureDefId;
	String measureType;
	
	
	public MeasureHistoryResource(UriInfo uriInfo, Request request,int id,int measureDefId, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
		this.measureDefId = measureDefId;
		}
	public MeasureHistoryResource(UriInfo uriInfo, Request request,int id, int measureDefId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.measureDefId = measureDefId;
	}
	public MeasureHistoryResource(UriInfo uriInfo, Request request,int id, String measureDefType) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.measureType = measureDefType;
	}
	// Application integration
		@GET
		@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public List<HealthMeasureHistory> getPersonHealthHistory() {
			
			List<HealthMeasureHistory> healthHistory = this.getPersonHistoryById(this.id, this.measureType);

			if (healthHistory == null)
				throw new RuntimeException("Get: Person with " + id + "  have health history not found");
			return healthHistory;
		}
		
		// for the browser
		@GET
		@Produces(MediaType.TEXT_XML)
		public List<HealthMeasureHistory> getPersonHTML() {
			List<HealthMeasureHistory> healthHistory = this.getPersonHistoryById(this.id, this.measureType);
			if (healthHistory == null)
					throw new RuntimeException("Get: Person with " + id + " health history snot found");
			return healthHistory;
		}
		
		@GET
		 @Consumes({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		 @Produces({MediaType.TEXT_XML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
		public List<HealthMeasureHistory> getPersonMeasureBydate(
					 @QueryParam("before") String beforeDate,
					 @QueryParam("after") String afterDate) {
				 List<HealthMeasureHistory> measureHistory= null;
				 if(beforeDate != null)
			 {
			 	try {
				 measureHistory= this.getPersonHistoryByDate(beforeDate, afterDate, this.getPersonHistoryById(this.id, this.measureType));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 else
			 {
			 			measureHistory = getPersonHealthHistory();

			 }
				return measureHistory;
			}

		
		// let's create this service for responding a submission form
		@POST
		@Produces(MediaType.TEXT_HTML)
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void newMeasureValue(@FormParam("value") String value,			
				@Context HttpServletResponse servletResponse) throws IOException {
			Person p = Person.getPersonById(id);
			HealthMeasureHistory hmh = new HealthMeasureHistory();

			Date date = new Date();
			MeasureDefinition mdf = new MeasureDefinition();
			String oldValue = null;
			Person n = p;
			List<LifeStatus> ls =  n.getLifeStatus();
			for (int i = 0; i < ls.size(); i++)  {
				if(ls.get(i).getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType))
				{
					oldValue = ls.get(i).getValue();
					mdf = ls.get(i).getMeasureDefinition();
					ls.get(i).setValue(value);
					break;
				}
			}
			if(oldValue!= null)
			{
				hmh.setMeasureDefinition(mdf);
				hmh.setPerson(p);
				hmh.setTimestamp(date);
				hmh.setValue(oldValue);
			
			}
			p.setLifeStatus(ls);
			Person.savePerson(p);
			servletResponse.sendRedirect("../NewPerson.html");
		}
		
		
		// let's create this service for responding a submission form
		// 
		@POST
		@Produces(MediaType.APPLICATION_XML)
		@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
		public Person newPerson(String value) throws IOException {
			System.out.println("Creating new value...");
			Person p = Person.getPersonById(id);
			HealthMeasureHistory hmh = new HealthMeasureHistory();

			//System.out.println(value + " vvvvvvvvvaaaaaaaaaaaalueeee");
			//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			MeasureDefinition mdf = new MeasureDefinition();
			String oldValue = null;
			Person n = p;
			List<LifeStatus> ls =  n.getLifeStatus();
			for (int i = 0; i < ls.size(); i++)  {
				if(ls.get(i).getMeasureDefinition().getMeasureName().equalsIgnoreCase(measureType))
				{
					oldValue = ls.get(i).getValue();
					//System.out.println(oldValue + " ooooooooooooooooolld");
					mdf = ls.get(i).getMeasureDefinition();
					ls.get(i).setValue(value);
					break;
				}
			}
			if(oldValue!= null)
			{
				hmh.setMeasureDefinition(mdf);
				hmh.setPerson(p);
				hmh.setTimestamp(date);
				hmh.setValue(oldValue);
			
			}
			p.setLifeStatus(ls);
			//Person.savePerson(p);
			//EntityManager entityManager = entityManagerFactory.createEntityManager();
//			EntityManager entityManager = PersonDao.instance.createEntityManager();
//	        try {
//	    		entityManager.persist(person);
//	    		entityManager.refresh(person);
//	    		return person;
//	        } finally {
//	            entityManager.close();
//	        }
			
			return Person.updatePerson(p);
		}
		public Person getPersonById(int personId) {
			System.out.println("Reading person from DB with id: "+personId);
			//Person person = entityManager.find(Person.class, personId);
			
			Person person = Person.getPersonById(personId);
			//System.out.println("Person: "+person.toString());
			return person;
		}
		public List<HealthMeasureHistory> getPersonHistoryById(int personId, String MeasureDef)
		{
			//List<HealthMeasureHistory> healthMeasure =Person.getPersonById(personId).getHealthMeasureHistory();
			List<HealthMeasureHistory> healthMeasure = HealthMeasureHistory.getAll();
			List<HealthMeasureHistory> healthMeasureResult = new  ArrayList<HealthMeasureHistory>();
			for (int i = 0; i < healthMeasure.size(); i++) {
				//System.out.println(healthMeasure.get(i).getMeasureDefinition().getMeasureName() + MeasureDef);
				if((int)healthMeasure.get(i).getPerson().getIdPerson() == (int)Person.getPersonById(personId).getIdPerson() &&
						healthMeasure.get(i).getMeasureDefinition().getMeasureName().equalsIgnoreCase(MeasureDef))
				{	
					healthMeasureResult.add(healthMeasure.get(i));
				
				}
			}
			
			//System.out.println("chPerson: "+ Person.getPersonById(personId).toString());
		  //System.out.println("Measure History"+ personId);
			return healthMeasureResult;
		 // return Person.getPersonByMeasureId(personId, MeasureDefId).getLifeStatus();
		}
		public List<HealthMeasureHistory> getPersonHistoryByDate(String before, String after,
					List<HealthMeasureHistory> healthMeasure ) throws ParseException
		{

			List<HealthMeasureHistory> healthMeasureResult = new  ArrayList<HealthMeasureHistory>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for (int i = 0; i < healthMeasure.size(); i++) {
				if(healthMeasure.get(i).getTimestamp().before(df.parse(before)) &&
						healthMeasure.get(i).getTimestamp().after(df.parse(after)) )
				{
					healthMeasureResult.add(healthMeasure.get(i));
				}
			}
			return healthMeasureResult;
		}
		
		
		public List<HealthMeasureHistory> getPersonHistoryById(int personId, int MeasureDefId)
		{
			//List<HealthMeasureHistory> healthMeasure =Person.getPersonById(personId).getHealthMeasureHistory();
			List<HealthMeasureHistory> healthMeasure = HealthMeasureHistory.getAll();
			List<HealthMeasureHistory> healthMeasureResult = new  ArrayList<HealthMeasureHistory>();
			
			for (int i = 0; i < healthMeasure.size(); i++) {

				//System.out.println(healthMeasure.get(i).getMeasureDefinition() +"Or" +healthMeasure.get(i).getPerson()+ MeasureDefId );
				if((int)healthMeasure.get(i).getPerson().getIdPerson() == (int)Person.getPersonById(personId).getIdPerson()
						 && (int)healthMeasure.get(i).getMeasureDefinition().getIdMeasureDef() == MeasureDefId )
				{	
					healthMeasureResult.add(healthMeasure.get(i));
				
				}
			}
			/*for (HealthMeasureHistory healthMeasureHistory : healthMeasure) {
				System.out.println("Treee"+ healthMeasureHistory.getPerson().getIdPerson() + "b" + Person.getPersonById(personId).getIdPerson() );
				if((int)healthMeasureHistory.getPerson().getIdPerson() != (int)Person.getPersonById(personId).getIdPerson())
				{	
					System.out.println(healthMeasureResult.remove(index));
				
				}
				System.out.println("Measure History"+ personId);
			}*/
			
			//System.out.println("chPerson: "+ Person.getPersonById(personId).toString());
		  //System.out.println("Measure History"+ personId);
			return healthMeasureResult;
		 // return Person.getPersonByMeasureId(personId, MeasureDefId).getLifeStatus();
		}

}
