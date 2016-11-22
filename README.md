introsde-2016-assignment-2

Service Design Engineering Course: Readme for Assignment 2 RESTFUL Services Name: Getachew Demissie Abebe ---- I have done it by myself. URL of server: https://morning-atoll-68102.herokuapp.com/sdelab but heroku did not work, because of some database error. Therefore, please check it locally. It works fine in my local machine: http://localhost:5500/sdelab/

Complete Assignment. Server: R#1 to R#12 Client: Step 3.1 to Step 3.12 ==================================

                        *********** PART 1 ***********
**** Structure of the project

** Here are the packages which contains the application:

i. introsde.rest.ehelth.dao In this package there is a class called lifecoachDao, the class that will connect our model to the database. The LifeCoachDao is a singleton java instance that contains an EntityManagerFactory, which is configured by the persistence unit "introsde-jpa". This class will be used to create and Entity Manager whenever we need to execute an operation in the Database.The entity manager provides the operations from and to the database, e.g. find objects, persists them, remove objects from the database, etc.

ii. introsde.rest.ehealth.model Contains classe that represent our database in the system. One class for each table in the database and also methods that save, get, update and delete data from each table.

iii. introsde.rest.ehealth.resources Resources implement our service endpoints. I have PersonCollectioResource, personResource,MeasureHistoryValueResource and MeasureHistoryResource.

iv. introsde.rest.ehealth This package contains server implemetation

v. introsde.rest.ehealth.client This package contains Client implementation

vi. introsde.rest.xml i used this package to add extra java class

vii. introsde.rest.ehealth.test.model unit testing for person and life status

The database for this application is SQLite DB and named lifecoach.sqlite which is located in the root folder of the repositery. I have also client-server-json.log, client-server-xml.log and other files in the main repositery which contains all the request and response information.

                       *********** PART 2 ***********
APIs that are implemented

Request #1: GET /person It will return the list of people with the healthprofile of those people who have it.
Request #2: GET /person/{id} It will return the person with the healthprofile if he has.
Request #3: PUT /person/{id} We will create the XML object with firstname, lastname, birthdate as strings and will send to update the person with {id} i.e /person/1 1945-01-01 Chuck Norris

Request #4: POST /person We will create the XML object with firstname, lastname, birthdate as strings and will send to update the person with {id} i.e 1945-01-01 Chuck Norris

Request #5: DELETE /person/{id} It will delete the person. Example: /person/1

Request #6: GET /person/{id}/{measureType} it will return the list of history specifies by {measureType} Example: /person/1/weight

Request #7: GET /person/{id}/{measureType}/{mid} It will return the measure object of {mid} measure_id of {measureType} of {id} person. Example: /person/1/weight/1

Request #8: POST /person/{id}/{measureType} It will create the measure of specific {measureType} of {id} person Example: /person/1/weight 2011-12-09 72.0

Request #9: GET /measureTypes It will return the list of measuretypes with the name only

Request #10: PUT /person/{id}/{measureType}/{mid} It will create the measure of specific {measureType} of {id} person Example: /person/1/weight 2011-12-09 73.0

Request #11: GET /person/{id}/{measureType}?before={beforeDate}&after={afterDate} Thi will return the history of {measureType} (e.g., weight) for person {id} in the specified range of date Example: /person/1/weight?before=2011-12-09&after=2007-12-09

Extra Request #12: GET /person?measureType={measureType}&max={max}&min={min} It will return people whose {measureType} (e.g., weight) value is in the [{min},{max}] range (if only one for the query params is provided, use only that) Example: With max and min: /person?measureType=weight&max=86&min=71 max only: /person?measureType=weight&max=86 min only: /person?measureType=weight&min=71

                        *********** PART 3 ***********
** To execute the Client,Open your terminal window in your local my assignment02 folder and run ant install, and then run ant execute.client and finally press enter to see all the outputs.

Client is implemented with the following calls

Step 1 => Base URL is http://localhost:5500/sdelab/

STEP 2 => Implementation of client

Step 3.1. Send R#1 (GET BASE_URL/person) it will calculate the people and show response "OK" if people are more than 2 in list of people else is will show "Error" and i am saving the first_person_id and last_person_id in

static int first_person_id; static int last_person_id;

Step 3.2. Send R#2 for first_person_id. If the responses for this is 200 or 202, the result is OK.

Step 3.3. Send R#3 for first_person_id changing the firstname. If the responses has the name changed, the result is OK.
Here i am saving the name of first_person_id person in following and comparing that after the call to get the result

static String name;

Step 3.4. Send R#4 to create the following person (first using JSON and then using XML). Store the id of the new person. If the answer is 201 (200 or 202 are also applicable) with a person in the body who has an ID, the result is OK.

Then, storing the id of person after create the person in

static int newID;

Step 3.5. Send R#5 for the person you have just created. Then send R#1 with the id of that person. If the answer is 404, your result must be OK.

Here we have to use R#2 for comparing the id to get the result. In R#1 we get the list of all persons id. We cannot get 404. So we have to send the id to R#2 to search that. In this case if there is person then will get the person. But if not then 404 we will get.

Step 3.6. Follow now with the R#9 (GET BASE_URL/measureTypes). if response contains more than 2 measureTypes - result is OK, else is ERROR (less than 3 measureTypes). I am saving all measureTypes into array:

static String[] measure_types;

Step 3.7. Send R#6 (GET BASE_URL/person/{id}/{measureType}) for the first person with first_person_id obtained at the beginning and the last person with last_person_id, and for each measure types from String[] measure_types obtained from step 3.6 If no response has at least one measure - result is ERROR (no data at all) else result is OK. I stored one measure_id and one measureType.

static int measure_id; static String measureType;

Like this measure_id = list.get(0).getMeasureId(); measureType = measure_types[0]; //obtained in step 3.5 , in this case it is "weight"

Step 3.8. Send R#7 (GET BASE_URL/person/{id}/{measureType}/{mid}) for the stored measure_id and measureType obtained in step 3.7 If the response is 200, result is OK, else is ERROR. I am using 5 types for the history: weight, height, steps, blood presure and heart rate.

Step 3.9. since i have faced data conversion problem i.e datatime, i haven't implemetnted this step.

Step 3.10. This steps not implemented because of the same reason like the above step.

Step 3.11. Send R#11 for a measureType, before and after dates. If status is 200 and there is at least one measure in the body, result is OK, else is ERROR. Here i am using measureType that i store in step 3.7. And add string in the same function for the query. String beforeDate = "2014-12-09"; String afterDate = "2007-12-09";

Step 3.12. Send R#12 for a measureType, min and max values. In assignment page.(Send R#12 using the same parameters as the preivious steps.) That is not possible, because we didn't implement any API for this. If status is 200 and there is at least one person in the body, result is OK, else is ERROR

Here i am using a weight measureType that i store in step 3.7. And add integer values for min = 80and max = 101 in the same function for the query.

                      **** Additional notes ****
                      
I tried to implement all the instruction listed, i commented on some steps (3.9 & 3.10) because of datatime problem i have faced. However, everything works smoothly on postman client.

** This application works with XML. I have implemented the XML conversion of the response and get the xml format, for some APIs i just printing objects as strings on console instead of the xml, since the result of all APIs is very large.

** Regarding JSON, i have got the result in the form of object string and printing out that in the console.

** This application working smoothly with XML and JSON format in other Rest client tool like, Postman.
