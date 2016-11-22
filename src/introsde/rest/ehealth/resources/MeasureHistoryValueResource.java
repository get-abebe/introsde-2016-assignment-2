package introsde.rest.ehealth.resources;

import java.util.ArrayList;
import java.util.List;

import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.Person;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless
@LocalBean
public class MeasureHistoryValueResource {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
    EntityManager entityManager;
	
	int id;
	String measureType;
	int measureHistoryId;
	
	public MeasureHistoryValueResource(UriInfo uriInfo, Request request,int id,String measureType, int measureHisId, EntityManager em) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		this.entityManager = em;
		this.measureHistoryId = measureHisId;
		this.measureType = measureType;
		}
	public MeasureHistoryValueResource(UriInfo uriInfo, Request request,int id,String measureType, int measureHisId) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;

		System.out.println("measure id history xxxxxxxx" + measureHisId);
		this.measureHistoryId = measureHisId;
		this.measureType = measureType;
		
	}
	// Application integration
			@GET
			@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
			public String  getPersonHistoryMeasureTypeValue() {
				//System.out.println("I am in get produces health ");
				List<HealthMeasureHistory> healthHistory = this.getPersonHistoryById(this.id, this.measureType);
                String returnValue = GetValue(healthHistory, measureHistoryId);
				System.out.println("I am in get person history" + returnValue);
					return returnValue;
			}

			// for the browser
			@GET
			@Produces(MediaType.TEXT_XML)
			public String getPersonHTML() {
				List<HealthMeasureHistory> healthHistory = this.getPersonHistoryById(this.id, this.measureType);
				if (healthHistory == null)
						throw new RuntimeException("Get: Person with " + id + " health history snot found");
				System.out.println("Returning person health history measure value... " + healthHistory.size() + this.measureHistoryId);
				return GetValue(healthHistory, this.measureHistoryId);
			}
			
			@PUT
			@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
			public Response putPerson(@PathParam("value") String Value) {
				System.out.println("--> Updating History Value... " +this.id);
				//System.out.println("--> "+person.toString());
				List<HealthMeasureHistory> healthHistory = this.getPersonHistoryById(this.id, this.measureType);
				if (healthHistory == null)
						throw new RuntimeException("Get: Person with " + id + " health history snot found");
				System.out.println("Returning person health history measure value... " + healthHistory.size() + this.measureHistoryId);
				
				HealthMeasureHistory healhis = this.GetHealthMeasure(healthHistory, this.measureHistoryId);
				healhis.setValue(Value);
				HealthMeasureHistory.updateHealthMeasureHistory(healhis);
				//Person.updatePerson(person);
				
				Response res;
				
				HealthMeasureHistory existing =  GetHealthMeasureById(this.measureHistoryId);
				
				if (existing == null) {
					res = Response.noContent().build();
				} else {
					res = Response.created(uriInfo.getAbsolutePath()).build();
					healhis.setIdMeasureHistory(this.measureHistoryId);
					HealthMeasureHistory.updateHealthMeasureHistory(healhis);
				}

				return res;

				
			}
			
			public HealthMeasureHistory GetHealthMeasureById(int id)
			{
				//Person person = entityManager.find(Person.class, personId);
				
				HealthMeasureHistory healHis = HealthMeasureHistory.getHealthMeasureHistoryById(id);
				return healHis;
			}
			public HealthMeasureHistory GetHealthMeasure(List<HealthMeasureHistory> healthHistory, int mid)
			{
				HealthMeasureHistory returnValue = new HealthMeasureHistory();
			  for (HealthMeasureHistory healthMeasureHistory : healthHistory) {

					System.out.println(healthMeasureHistory.getIdMeasureHistory() + "Returning person health history measure value... " + mid);
				if(healthMeasureHistory.getIdMeasureHistory() == mid)
				{
					System.out.println("Returning person health history measure value... " + mid);
					returnValue = healthMeasureHistory;
					break;
				}
			}
			  System.out.println("Returning person health history measure value... " + returnValue);
				
				return returnValue;
			}
			public String GetValue(List<HealthMeasureHistory> healthHistory, int mid)
			{
				String returnValue = "No Value Found";
			  for (HealthMeasureHistory healthMeasureHistory : healthHistory) {

					System.out.println(healthMeasureHistory.getIdMeasureHistory() + "Returning person health history measure value... " + mid);
				if(healthMeasureHistory.getIdMeasureHistory() == mid)
				{
					System.out.println("Returning person health history measure value... " + mid);
					returnValue = healthMeasureHistory.getValue();
					break;
				}
			}
			  System.out.println("Returning person health history measure value... " + returnValue);
				
				return returnValue;
			}

			public List<HealthMeasureHistory> getPersonHistoryById(int personId, String MeasureDef)
			{
				//List<HealthMeasureHistory> healthMeasure =Person.getPersonById(personId).getHealthMeasureHistory();
				List<HealthMeasureHistory> healthMeasure = HealthMeasureHistory.getAll();
				List<HealthMeasureHistory> healthMeasureResult = new  ArrayList<HealthMeasureHistory>();
				for (int i = 0; i < healthMeasure.size(); i++) {
					System.out.println(healthMeasure.get(i).getMeasureDefinition().getMeasureName() + MeasureDef);
					if((int)healthMeasure.get(i).getPerson().getIdPerson() == (int)Person.getPersonById(personId).getIdPerson() &&
							healthMeasure.get(i).getMeasureDefinition().getMeasureName().equalsIgnoreCase(MeasureDef))
					{	
						System.out.println(healthMeasureResult.add(healthMeasure.get(i)));
					
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
			  System.out.println("Measure History"+ personId);
				return healthMeasureResult;
			 // return Person.getPersonByMeasureId(personId, MeasureDefId).getLifeStatus();
			}
			public String GetPersonMeasValue(int personId, String MeasureDef, int mid)
			{
				//List<HealthMeasureHistory> healthMeasure =Person.getPersonById(personId).getHealthMeasureHistory();
				List<HealthMeasureHistory> healthMeasure = HealthMeasureHistory.getAll();
				List<HealthMeasureHistory> healthMeasureResult = new  ArrayList<HealthMeasureHistory>();
				for (int i = 0; i < healthMeasure.size(); i++) {
					System.out.println(healthMeasure.get(i).getMeasureDefinition().getMeasureName() + MeasureDef);
					if((int)healthMeasure.get(i).getPerson().getIdPerson() == (int)Person.getPersonById(personId).getIdPerson() &&
							healthMeasure.get(i).getMeasureDefinition().getMeasureName().equalsIgnoreCase(MeasureDef))
					{	
						System.out.println(healthMeasureResult.add(healthMeasure.get(i)));
					
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
			  System.out.println("Measure History"+ personId);
				return "healthMeasureResult";
			 // return Person.getPersonByMeasureId(personId, MeasureDefId).getLifeStatus();
			}

}
