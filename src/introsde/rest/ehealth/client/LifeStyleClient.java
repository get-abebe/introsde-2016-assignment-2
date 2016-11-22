package introsde.rest.ehealth.client;

//import introsde.rest.ehealth.model.HealthLifeStatusHistory;
import introsde.rest.ehealth.model.HealthMeasureHistory;
import introsde.rest.ehealth.model.LifeStatus;
import introsde.rest.ehealth.model.Person;
import introsde.rest.ehealth.xml.MeasureTypeXML;
import introsde.rest.ehealth.xml.MeasureHistory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.glassfish.jersey.client.ClientConfig;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LifeStyleClient {

	static int first_person_id = 0;
	static int last_person_id = 0;
	static int newID = 0;
	static String name;
	static String[] measure_types;
	static int measure_id;
	static String measureType;
	static MeasureTypeXML mlist;
	static int PersonId = 0;
	static LifeStatus LifeStatus;
	static HealthMeasureHistory healthhistory;

	public static void main(String[] args) throws Exception {

		ClientConfig config = new ClientConfig();

		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target(getBaseURI());

		System.out.println("*********Step 3.1*********");
		getRequestOne(target, MediaType.APPLICATION_XML);
		getRequestOne(target, MediaType.APPLICATION_JSON);

		System.out.println("*********Step 3.2*********");
		Person firstPerson = getRequestTwo(target, first_person_id,MediaType.APPLICATION_XML);
		Person firstperson2 = getRequestTwo(target, first_person_id,MediaType.APPLICATION_JSON);
		if (firstperson2 != null) {
			firstPerson = firstperson2;
		}
    	
		name = firstPerson.getName();
		// set the first name to "changed"
		firstPerson.setName("Changed");
		
		System.out.println("*********Step 3.3*********");
		getRequestThree(target, first_person_id, firstPerson, MediaType.APPLICATION_XML);
		getRequestThree(target, first_person_id, firstPerson, MediaType.APPLICATION_JSON);

		// create a new person firstPerson
		firstPerson = new Person();
		firstPerson.setBirthdate("1980-01-01");
		firstPerson.setName("david");
		firstPerson.setLastname("lukas");
		firstPerson.setEmail("lucy@gmail.com");
		firstPerson.setUsername("hilucy");

		System.out.println("*********Step 3.4*********");
		newID = getRequestFour(target, firstPerson, MediaType.APPLICATION_XML);
		int id = getRequestFour(target, firstPerson, MediaType.APPLICATION_JSON);
		if (id != 0 || newID == 0) {newID = id;}
		
		System.out.println("*********Step 3.5*********");
		getRequestFive(target, newID, MediaType.APPLICATION_XML);
		getRequestFive(target, newID, MediaType.APPLICATION_JSON);

		System.out.println("*********Step 3.6*********");
		getRequestSix(target, MediaType.APPLICATION_XML);
		getRequestSix(target, MediaType.APPLICATION_JSON);

		System.out.println("*********Step 3.7*********");
		getRequestSeven(target, MediaType.APPLICATION_XML);
		getRequestSeven(target, MediaType.APPLICATION_JSON);

		System.out.println("*********Step 3.8*********");
		getRequestEight(target, first_person_id, MediaType.APPLICATION_XML);
		getRequestEight(target, first_person_id, MediaType.APPLICATION_JSON);
		

		/*
		 * System.out.println("*********Step 3.9*********");
		 * //getRequestNine(target, MediaType.APPLICATION_XML);
		 * getRequestNine(target, MediaType.APPLICATION_JSON);
		 * 
		 * System.out.println("*********Step 3.10*********");
		 * getRequestTen(target, MediaType.APPLICATION_XML);
		 * getRequestTen(target, MediaType.APPLICATION_JSON);
		 */

		System.out.println("*********Step 3.11*********");
		getRequestEleven(target, 51, MediaType.APPLICATION_XML);
		getRequestEleven(target, 51, MediaType.APPLICATION_JSON);

		System.out.println("*********Step 3.12*********");
		getRequestTwelve(target, MediaType.APPLICATION_XML);
		getRequestTwelve(target, MediaType.APPLICATION_JSON);
	}

	private static URI getBaseURI() {
		// to run locally
		return UriBuilder.fromUri("http://localhost:5500/sdelab/").build();
		// to run from heroku
	    // return UriBuilder.fromUri("https://morning-atoll-68102.herokuapp.com/sdelab").build();
	}

	// Step 3.1. Send R#1 (GET BASE_URL/person). Calculate how many people
	// are in the response. If more than 2, result is OK, else is ERROR
	// (less than 3 persons). Save into a variable id of the first person
	// (first_person_id) and of the last person (last_person_id)
	
	public static void getRequestOne(WebTarget target, String applicationXml) {

		WebTarget personPath = target.path("person");

		Response res = personPath.request().accept(applicationXml)
				.get(Response.class);

		List<Person> list = (List<Person>) res
				.readEntity(new GenericType<List<Person>>() {
				});

		if (res.getStatus() == 200 || res.getStatus() == 201
				|| res.getStatus() == 202) {
			if (list.size() > 2) {
				getResponseTemplateCustom(1, res, "GET", personPath.getUri(),
						"OK");
			} else {
				getResponseTemplateCustom(1, res, "GET", personPath.getUri(),
						"ERROR");
			}
			
			if (res.getMediaType().toString().equalsIgnoreCase("application/json")) {
				// objToJSON(list);
				System.out.println(list.toString() + "\n");
			} else if (res.getMediaType().toString().equalsIgnoreCase("application/xml")) {
			   personToXML(list);
			   System.out.println("\n");
			}

			first_person_id = list.get(0).getIdPerson();
			last_person_id = list.get(list.size() - 1).getIdPerson();
		} else {
			System.out.println(applicationXml + " is not supported");
		}


	}

	// Step 3.2. Send R#2 for first_person_id. If the responses for this is
	// 200 or 202, the result is OK.
	
	public static Person getRequestTwo(WebTarget target, int id,
			String applicationXml) {

		WebTarget personPath = target.path("person").path(Integer.toString(id));

		Builder req = personPath.request().accept(applicationXml);

		Response res = req.get(Response.class);
		Person p = res.readEntity(Person.class);
		if (res.getStatus() == 200 || res.getStatus() == 202) {

			getResponseTemplate(2, res, "GET", personPath.getUri());
			
			
			if (res.getMediaType().toString().equalsIgnoreCase("application/json")) {
			//objToJSON(p);
				System.out.println(p.toString() + "\n");
			} else if (res.getMediaType().toString()
					.equalsIgnoreCase("application/xml")) {
				singlePersonToXML(p);
				  System.out.println("\n");
			}
			
			return p;
		} else {
			System.out.println(applicationXml + " is not supported");
		}

		return null;
	}

	// Step 3.3. Send R#3 for first_person_id changing the firstname. If the
	// responses has the name changed, the result is OK.

	public static void getRequestThree(WebTarget target, int id, Person p,
			String applicationXml) {

		WebTarget personPath = target.path("person").path(Integer.toString(id));

		Builder req = personPath.request().accept(applicationXml);

		Entity<Person> per = Entity.xml(p);

		Response res = req.put(per);
		// Response res = req.get(Response.class);

		Person person = res.readEntity(Person.class);
		
		if ((res.getStatus() == 200 || res.getStatus() == 201 || res
				.getStatus() == 202) & name != person.getName()) {
			getResponseTemplate(3, res, "PUT", personPath.getUri());
			if (res.getMediaType().toString().equalsIgnoreCase("application/json")) {
				//objToJSON(person);
				System.out.println(person.toString() + "\n");
			} else if (res.getMediaType().toString()
					.equalsIgnoreCase("application/xml")) {
				singlePersonToXML(person);
				  System.out.println("\n");
			}
			
		} else {
			System.out.println(applicationXml + " is not supported");
		}

	}

	// Step 3.4. Send R#4 to create the following person (first using JSON
	// and then using XML). Store the id of the new person. If the answer is
	// 201 (200 or 202 are also applicable) with a person in the body who
	// has an ID, the result is OK.

	public static int getRequestFour(WebTarget target, Person p,
			String applicationXml) {

		WebTarget path = target.path("person");
		Builder req = path.request().accept(applicationXml);
		// Person pNew = req.post(Entity.xml(p), Person.class);

		Response res = req.post(Entity.xml(p));

		Person person = res.readEntity(Person.class);

		int d = person.getIdPerson();

		if ((res.getStatus() == 200 || res.getStatus() == 201 || res
				.getStatus() == 202)) {
			if (d != 0) {
				getResponseTemplate(4, res, "POST", path.getUri());
				if (res.getMediaType().toString().equalsIgnoreCase("application/json")) {
					//objToJSON(person);
					System.out.println(person.toString() + "\n");
				} else if (res.getMediaType().toString()
						.equalsIgnoreCase("application/xml")) {
					singlePersonToXML(person);
					  System.out.println("\n");
				}
				
			}
			return d;
		} else {
			System.out.println(applicationXml + " is not supported");
		}

		return 0;

	}

	// Step 3.5. Send R#5 for the person you have just created. Then send
	// R#1 with the id of that person. If the answer is 404, your result
	// must be OK.

	public static void getRequestFive(WebTarget target, int id,
			String applicationXml) {

		WebTarget personPath = target.path("person").path(Integer.toString(id));

		Builder req = personPath.request().accept(applicationXml);

		Response res = req.delete(Response.class);

		if (res.getStatus() == 200 || res.getStatus() == 202
				|| res.getStatus() == 404) {

			getResponseTemplateCustom(5, res, "DELETE", personPath.getUri(),
					"OK");

			res = req.get(Response.class);

			if (res.getStatus() == 404) {
				getResponseTemplateCustom(1, res, "GET", personPath.getUri(),
						"OK");

				String resultString = res.readEntity(String.class);
				System.out.println(resultString + "\n");
			}
		} else {
			System.out.println(applicationXml + " is not supported");
		}
	}

	// Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes). If
	// response contains more than 2 measureTypes - result is OK, else is
	// ERROR (less than 3 measureTypes). Save all measureTypes into array
	// (measure_types)
	public static void getRequestSix(WebTarget target, String applicationXml) {

		WebTarget path = target.path("measureTypes");

		Builder req = path.request().accept(applicationXml);

		Response res = req.get(Response.class);
		// TODO
		mlist = res.readEntity(MeasureTypeXML.class);
		if (res.getStatus() == 200 || res.getStatus() == 201
				|| res.getStatus() == 202) {

			if (mlist.get().size() > 2) {
				getResponseTemplateCustom(9, res, "GET", path.getUri(), "OK");
			} else {
				getResponseTemplateCustom(9, res, "GET", path.getUri(), "ERROR");
			}
			measure_types = new String[mlist.get().size()];
			for (int i = 0; i < mlist.get().size(); i++) {
				measure_types[i] = mlist.get().get(i);
				
			}
			if (res.getMediaType().toString().equalsIgnoreCase("application/json")) {
				//measureObjToJSON(mlist);
				System.out.println(mlist.toString() + "\n");
			} else if (res.getMediaType().toString()
					.equalsIgnoreCase("application/xml")) {
				measureObjToXML(mlist);
				  System.out.println("\n");
							}
					} else {
			System.out.println(applicationXml + " is not supported");
		}

	}

	// Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType}) for the
	// first person you obtained at the beginning and the last person, and
	// for each LifeStatus types from measure_types. If no response has at
	// least one LifeStatus - result is ERROR (no data at all) else result is
	// OK. Store one measure_id and one measureType.
	
	public static void getRequestSeven(WebTarget target, String applicationXml) {

		boolean hasLifeStatus = true;
		//Response res = null;

		//WebTarget personPath = target.path("person").path(
			//	Integer.toString(first_person_id));
		for (int i = 0; i < measure_types.length; i++) {
			WebTarget personPath = target.path("person").path(
					Integer.toString(first_person_id)).path(measure_types[i]);
			Builder req = personPath.request().accept(applicationXml);

			Response res = req.get(Response.class);

			List<HealthMeasureHistory> list = (List<HealthMeasureHistory>) res
					.readEntity(new GenericType<List<HealthMeasureHistory>>() {
					});
			
			if (list.size() > 0) {
				hasLifeStatus = true;
			}
			if (list.size() == 0) {
				hasLifeStatus = false;
			}
			if (i == 0) {
				measure_id = list.get(0).getIdMeasureHistory();
				measureType = measure_types[0];
			}
			if (res != null) {
				if (hasLifeStatus) {
					getResponseTemplateCustom(6, res, "GET", personPath.getUri(),
							"OK");
					System.out.println(list.toString() + "\n");
				} else {
					getResponseTemplateCustom(6, res, "GET", personPath.getUri(),
							"ERROR");
				}
				
			}

		}
		
		
		//Response res1 = null;
		
		for (int i = 0; i < measure_types.length; i++) {
			WebTarget personPath = target.path("person").path(
					Integer.toString(last_person_id)).path(measure_types[i]);
			Builder req1 = personPath.request().accept(applicationXml);

			Response res1 = req1.get(Response.class);

			List<HealthMeasureHistory> list1 = res1.readEntity(new GenericType<List<HealthMeasureHistory>>() {});
			if (list1.size() > 0) {
				hasLifeStatus = true;

			}
			if (list1.size() == 0) {
				hasLifeStatus = false;
			}
			if (res1 != null) {
				if (hasLifeStatus) {
					getResponseTemplateCustom(6, res1, "GET", personPath.getUri(),
							"OK");
					System.out.println(list1.toString() + "\n");
				} else {
					getResponseTemplateCustom(6, res1, "GET", personPath.getUri(),
							"ERROR");
				}
			}
		}

		System.out.println("\n");

	}

	// Step 3.8. Send R#7 (R#6 written in assignment web page) (GET
	// BASE_URL/person/{id}/{measureType}/{mid}) for
	// the stored measure_id and measureType. If the response is 200, result
	// is OK, else is ERROR.

	public static void getRequestEight(WebTarget target, int id,
			String applicationXml) {

		WebTarget personPath = target.path("person").path(Integer.toString(id))
				.path(measureType).path(Integer.toString(measure_id));

		Builder req = personPath.request().accept(applicationXml);

		Response res = req.get(Response.class);

		if (res.getStatus() == 200 || res.getStatus() == 201
				|| res.getStatus() == 202) {
			getResponseTemplate(7, res, "GET", personPath.getUri());

			String resultString = res.readEntity(String.class);
			System.out.println(resultString + "\n");
		} else {
			System.out.println(applicationXml + " is not supported");
		}
	}

	// Step 3.9. Choose a measureType from measure_types and send the
	// request R#6 (GET BASE_URL/person/{first_person_id}/{measureType}) and
	// save count value (e.g. 5 LifeStatusments). Then send R#8 (POST
	// BASE_URL/person/{first_person_id}/{measureTypes}) with the
	// LifeStatusment specified below. Follow up with another R#6 as the first
	// to check the new count value. If it is 1 time more - print OK, else
	// print ERROR. Remember, first with JSON and then with XML as
	// content-types

	public static void getRequestNine(WebTarget target, String applicationXml)
			throws JAXBException {

		// measureType[0] ==> weight
		WebTarget personPath = target.path("person")
				.path(Integer.toString(first_person_id)).path(measure_types[0]);
		Builder req = personPath.request().accept(MediaType.APPLICATION_XML);

		Response res = req.get(Response.class);
		List<HealthMeasureHistory> list = res
				.readEntity(new GenericType<List<HealthMeasureHistory>>() {
				});
		getResponseTemplate(6, res, "GET", personPath.getUri());
		/*
		 * String resultString = res.readEntity(String.class);
		 * System.out.println(resultString + "\n");
		 */
		System.out.println(list.toString() + "\n");

		int count = list.size();
		System.out.println(count);
		// for (int i = 0; i < measureTypes.length; i++) {
		personPath = target.path("person")
				.path(Integer.toString(first_person_id)).path(measure_types[0]);
		req = personPath.request().accept(MediaType.APPLICATION_XML);

		LifeStatus ls = new LifeStatus();
		ls.setValue("42");
		HealthMeasureHistory m = new HealthMeasureHistory();
		// m.getTimestamp("12/01/10");

		m.setTimestamp(java.sql.Date.valueOf("2011-12-09"));
		m.setValue("72");

		Response res1 = req.post(Entity.xml(m));
		healthhistory = res.readEntity(HealthMeasureHistory.class);
		getResponseTemplate(8, res, "POST", personPath.getUri());
		System.out.println(res.toString() + "\n");

		JAXBContext jaxbContext = JAXBContext.newInstance(LifeStatus.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				Boolean.TRUE);
		jaxbMarshaller.marshal(res, System.out);

		// }

		personPath = target.path("person")
				.path(Integer.toString(first_person_id)).path(measure_types[0]);
		req = personPath.request().accept(MediaType.APPLICATION_XML);

		Response res2 = req.get(Response.class);
		list = res2.readEntity(new GenericType<List<HealthMeasureHistory>>() {
		});
		getResponseTemplate(6, res2, "GET", personPath.getUri());

		// LifeStatusToXML(list);

		System.out.println(list.toString() + "\n");

		if (list.size() == count + 1) {
			System.out.println("******************OK******************");
		} else {
			System.out.println("*****************ERROR*****************");
		}

		System.out.println("\n");
	}

	// Step 3.10. Send R#10 using the {mid} or the LifeStatus created in the
	// previous step and updating the value at will. Follow up with at R#6
	// to check that the value was updated. If it was, result is OK, else is
	// ERROR.

	public static void getRequestTen(WebTarget target, String applicationXml)
			throws JAXBException {

		// WebTarget personPath = target.path("person")
		// .path(Integer.toString(LifeStatus.getPerson().getPersonId()))
		// .path(LifeStatus.getmeasureType().getLifeStatusName())
		// .path(Integer.toString(LifeStatus.getLifeStatusId()));

		WebTarget personPath = target.path("person")
				.path(Integer.toString(first_person_id)).path(measure_types[0])
				.path(Integer.toString(LifeStatus.getIdMeasure()));

		Builder req = personPath.request().accept(MediaType.APPLICATION_XML);

		LifeStatus m = new LifeStatus();
		m.setValue("71");
		// m.setCreateDate("2011-12-09");

		Response res = req.put(Entity.xml(m));
		// LifeStatus mNew = res.readEntity(LifeStatus.class);
		getResponseTemplate(10, res, "PUT", personPath.getUri());

		personPath = target.path("person")
				.path(Integer.toString(first_person_id)).path(measure_types[0])
				.path(Integer.toString(LifeStatus.getIdMeasure()));

		req = personPath.request().accept(MediaType.APPLICATION_XML);

		Response res1 = req.get(Response.class);
		LifeStatus mDB = res1.readEntity(LifeStatus.class);
		getResponseTemplate(6, res1, "GET", personPath.getUri());
		System.out.println(mDB.toString() + "\n");
		// JAXBContext jaxbContext = JAXBContext.newInstance(LifeStatus.class);
		// Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
		// Boolean.TRUE);
		// jaxbMarshaller.marshal(LifeStatus, System.out);

		System.out.println("");
		if (LifeStatus.getValue() != mDB.getValue()) {
			System.out.println("***************OK******************");
		} else {
			System.out.println("******************ERROR****************");
		}
		System.out.println("\n");
	}

	// Step 3.11. Send R#11 for a measureType, before and after dates given
	// by your fellow student (who implemented the server). If status is 200
	// and there is at least one LifeStatus in the body, result is OK, else is
	// ERROR
	// http://localhost:5678/assignment02/person/1/weight?before=2011-12-09&after=2007-12-09
	
	public static void getRequestEleven(WebTarget target, int id,
			String applicationXml) {

		String beforeDate = "2014-12-09";
		String afterDate = "2007-12-09";

		WebTarget path = target.path("person").path(Integer.toString(id))
				.path(measureType).queryParam("before", beforeDate)
				.queryParam("after", afterDate);

		Builder req = path.request().accept(applicationXml);

		Response res = req.get(Response.class);

		List<HealthMeasureHistory> list = null;
		if (res.getStatus() == 200 || res.getStatus() == 202) {
			list = res.readEntity(new GenericType<List<HealthMeasureHistory>>() {});
			if (list.size() > 0) {
				getResponseTemplateCustom(11, res, "GET", path.getUri(), "OK");
				// LifeStatusToXML(list);
				
				if (res.getMediaType().toString().equalsIgnoreCase("application/json")) {
     			//objToJSON(list);
				} else if (res.getMediaType().toString()
						.equalsIgnoreCase("application/xml")) {
					MeasureHistoryToXML(list);
					
				}
				
				System.out.println(list.toString() + "\n");
				

			} else {
				getResponseTemplateCustom(11, res, "GET", path.getUri(),
						"ERROR");
			}
		} else {
			System.out.println(applicationXml + " is not supported.");
		}
	}

	// Step 3.12. Send R#12 using the same parameters as the previous
	// steps. If status is 200 and there is at least one person in the body,
	// result is OK, else is ERROR
	// http://localhost:5678/assignment02/person?measureType=weight&max=86&min=71
	public static void getRequestTwelve(WebTarget target, String applicationXml) {

		String max = "101";
		String min = "80";

		WebTarget path = target.path("person")
				.queryParam("measureType", measureType).queryParam("max", max)
				.queryParam("min", min);

		Builder req = path.request().accept(applicationXml);

		Response res = req.get(Response.class);

		if (res.getStatus() == 200 || res.getStatus() == 202) {
			List<Person> list = res.readEntity(new GenericType<List<Person>>() {
			});
			if (list.size() > 0) {
				getResponseTemplateCustom(12, res, "GET", path.getUri(), "OK");
				
				// personToXML(list);
				if (res.getMediaType().toString()
						.equalsIgnoreCase("application/json")) {
					objToJSON(list);
				} else if (res.getMediaType().toString()
						.equalsIgnoreCase("application/xml")) {
					personToXML(list);
					System.out.println("\n");
				}

			} else {
				getResponseTemplateCustom(12, res, "GET", path.getUri(),
						"ERROR");
			}
		} else {
			System.out.println(applicationXml + " is not supported.");
		}
	}

	public static void getResponseTemplate(int id, Response res, String method,
			URI path) {


		String content_type = null;
		for (Entry<String, List<String>> header : res.getStringHeaders()
				.entrySet()) {
			if (header.getKey().equalsIgnoreCase("Content-type")) {
				content_type = header.getValue().toString();
			}
		}

		String request = "Request #" + id + ": " + method + " " + path
				+ " Accept: " + res.getMediaType() + " Content-Type: "
				+ content_type;
		String result = "=> Result: " + res.getStatusInfo();
		String status = "=> HTTP Status: " + res.getStatus();

		System.out.println(request);
		System.out.println(result);
		System.out.println(status);
	}

	public static void getResponseTemplateCustom(int id, Response res,
			String method, URI path, String result) {

		String content_type = null;
		for (Entry<String, List<String>> header : res.getStringHeaders()
				.entrySet()) {
			if (header.getKey().equalsIgnoreCase("Content-type")) {
				content_type = header.getValue().toString();
			}
		}

		String request = "Request #" + id + ": " + method + " " + path
				+ " Accept: " + res.getMediaType() + " Content-Type: "
				+ content_type;
		String resultString = "=> Result: " + result;
		String status = "=> HTTP Status: " + res.getStatus();

		System.out.println(request);
		System.out.println(resultString);
		System.out.println(status);
	}

	public static void personToXML(List<Person> people) {
		try {

			PersonList list = new PersonList();
			list.set(people);

			JAXBContext jaxbContext = JAXBContext.newInstance(PersonList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			jaxbMarshaller.marshal(list, System.out);

		} catch (JAXBException e) {
			// some exception occured
			System.out.println(e);
		}
	}
	
	public static void singlePersonToXML(Person people) {
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			jaxbMarshaller.marshal(people, System.out);

		} catch (JAXBException e) {
			// some exception occured
			System.out.println(e);
		}
	}

	public static void LifeStatusToXML(List<LifeStatus> people) {
		try {

			LifeStatusList list = new LifeStatusList();
			list.set(people);

			JAXBContext jaxbContext = JAXBContext
					.newInstance(LifeStatusList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			jaxbMarshaller.marshal(list, System.out);

		} catch (JAXBException e) {
			// some exception occured
			System.out.println(e);
		}
	}

	public static void objToJSON(List<Person> people) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(System.out, people);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void MeasureHistoryToXML(List<HealthMeasureHistory> history) {
		try {

			MeasureHistoryList list = new MeasureHistoryList();
			list.set(history);

			JAXBContext jaxbContext = JAXBContext
					.newInstance(MeasureHistoryList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			jaxbMarshaller.marshal(history, System.out);

		} catch (JAXBException e) {
			// some exception occured
			System.out.println(e);
		}
	}
	
	public static void measureObjToXML(MeasureTypeXML measures) {
		try {
			/*MeasureTypeList list = new MeasureTypeList();
			list.set(measures);*/

		
			JAXBContext jaxbContext = JAXBContext
					.newInstance(MeasureTypeList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			jaxbMarshaller.marshal(measures, System.out);

		} catch (JAXBException e) {
			// some exception occured
			System.out.println(e);
		}
	}
	
	
	public static void measureObjToJSON(MeasureTypeXML measures) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(System.out, measures);

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*public static void objToJSON(Person people) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			mapper.writeValue(System.out, people);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	@XmlRootElement(name = "people")
	static class PersonList {

		@XmlElement(name = "person")
		private List<Person> list;

		public PersonList() {
			list = new ArrayList<Person>();
		}

		public void add(Person p) {
			list.add(p);
		}

		public void set(List<Person> list) {
			this.list = list;
		}
	}
	@XmlRootElement(name = "Measuretype")
	static class MeasureTypeList {

		@XmlElement(name = "Measure")
		private List<MeasureTypeXML> list;

		public MeasureTypeList() {
			list = new ArrayList<MeasureTypeXML>();
		}

		public void add(MeasureTypeXML m) {
			list.add(m);
		}

		public void set(List<MeasureTypeXML> list) {
			this.list = list;
		}
	}
	
	@XmlRootElement(name = "Measuretype")
	static class MeasureHistoryList {

		@XmlElement(name = "Measure")
		private List<HealthMeasureHistory> list;

		public MeasureHistoryList() {
			list = new ArrayList<HealthMeasureHistory>();
		}

		public void add(HealthMeasureHistory m) {
			list.add(m);
		}

		public void set(List<HealthMeasureHistory> list) {
			this.list = list;
		}
	}

	@XmlRootElement(name = "LifeStatusHistory")
	static class LifeStatusList {

		@XmlElement(name = "LifeStatus")
		private List<LifeStatus> list;

		public LifeStatusList() {
			list = new ArrayList<LifeStatus>();
		}

		public void add(LifeStatus p) {
			list.add(p);
		}

		public void set(List<LifeStatus> list) {
			this.list = list;
		}
	}

}