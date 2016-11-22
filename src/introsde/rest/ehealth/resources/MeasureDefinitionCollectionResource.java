package introsde.rest.ehealth.resources;
import introsde.rest.ehealth.model.MeasureDefinition;
import introsde.rest.ehealth.xml.MeasureTypeXML;
import java.util.List;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/*
 * TODO 
 * - There is a problem with the EntityManager injection through @PersistenceUnit or @PersistenceContext
 * - will look into it later
 */

/**
 * @author getch
 *
 */
@Stateless
@LocalBean//Will map the resource to the URL /ehealth/v2
@Path("/measureTypes")
public class MeasureDefinitionCollectionResource {
	
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

		// Return the list of people to the user in the browser
		/*@GET
		@Produces({MediaType.TEXT_XML,  MediaType.APPLICATION_JSON ,  MediaType.APPLICATION_XML })
		public List<MeasureDefinition> getPersonsBrowser() {
			System.out.println("Getting list of measure Definitions...");
		    List<MeasureDefinition> measureDef = MeasureDefinition.getAll();
			return measureDef;
		}*/
		
		@GET
		@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_XML })
		public MeasureTypeXML MeasureTypes() {

			List<MeasureDefinition> outlist = MeasureDefinition.getAll();
			MeasureTypeXML measuretype = new MeasureTypeXML();

			for (MeasureDefinition measureDefinition : outlist) {
				measuretype.add(measureDefinition.getMeasureName());
			}

			return measuretype;
		}
		
		
		/*
		@GET
		@Path("detail")
		@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_XML })
		public List<MeasureDefinition> getMeasures() {
			System.out.println("Getting list of measure definition...");
			List<MeasureDefinition> list = MeasureDefinition.getAll();
			System.out.println("Measures: " + list.toString());
			return list;
		}

		// Request #9: GET /measureTypes should return the list of measures your
		// model supports in the following formats:
		@GET
		@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_JSON,
				MediaType.APPLICATION_XML })
		public MeasureTypeXML getPersonsNames() {
			System.out.println("Getting list of measures type...");
			List<String> list = MeasureDefinition.getAllNames();
			MeasureTypeXML measureTypes = new MeasureTypeXML();

			for (String string : list) {
				measureTypes.add(string);
			}
			System.out.println("Measures: " + measureTypes.toString());
			return measureTypes;
		}*/
	    

}
