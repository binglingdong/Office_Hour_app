package oh.files;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import oh.OfficeHoursApp;
import oh.data.OfficeHoursData;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;

/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Richard McKenna
 */
public class OfficeHoursFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    OfficeHoursApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_TIMESLOTS = "time";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";

    public OfficeHoursFiles(OfficeHoursApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
	OfficeHoursData dataManager = (OfficeHoursData)data;
        dataManager.reset();
       

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

	// LOAD THE START AND END HOURS
	String startHour = json.getString(JSON_START_HOUR);
        String endHour = json.getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);

        // NOW LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = json.getJsonArray(JSON_UNDERGRAD_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email= jsonTA.getString(JSON_EMAIL);
            String type= jsonTA.getString(JSON_TYPE);
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name,email,0,type);
            dataManager.addTA(ta);
        }
        
        JsonArray jsonOHArray= json.getJsonArray(JSON_OFFICE_HOURS);
        for(int i=0; i<jsonOHArray.size();i++){
            JsonObject jsonOH= jsonOHArray.getJsonObject(i);
            String time= jsonOH.getString(JSON_TIMESLOTS);
            String day= jsonOH.getString(JSON_DAY_OF_WEEK);
            String name= jsonOH.getString(JSON_NAME);
            
            TeachingAssistantPrototype ta= dataManager.getTAWithName(name);
            
            TimeSlot slots= dataManager.getTimeSlot(time);
            dataManager.addOH(slots, ta, DayOfWeek.valueOf(day));
        }
        
        
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	OfficeHoursData dataManager = (OfficeHoursData)data;

	// NOW BUILD THE TA JSON OBJCTS TO SAVE
	JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();//that is the content of the array
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName()).add(JSON_EMAIL,ta.getEmail()).add(JSON_TYPE, ta.getType()).build();
	    taArrayBuilder.add(taJson);
        }
	JsonArray undergradTAsArray = taArrayBuilder.build();

        //DO THE SAME THING FOR THE OFFICE HOURS
        JsonArrayBuilder OHArrayBuilder= Json.createArrayBuilder();
        Iterator<TimeSlot> OHIterator= dataManager.officeHoursIterator();
        while(OHIterator.hasNext()){
            TimeSlot time= OHIterator.next();
            HashMap allTheTAsForTheTime= time.getTas();  //for that time slot
            for(DayOfWeek day: DayOfWeek.values()){
                ArrayList<TeachingAssistantPrototype> listOfTa= (ArrayList)allTheTAsForTheTime.get(day);
                for(TeachingAssistantPrototype ta: listOfTa){
                    
                    JsonObject timeJson= Json.createObjectBuilder().add(JSON_START_TIME,time.getStartTime().replace(":", "_"))
                            .add(JSON_DAY_OF_WEEK,day.toString()).add(JSON_NAME, ta.getName())
                            .build();
                    OHArrayBuilder.add(timeJson);
                }
            }  
        }
        JsonArray officeHour= OHArrayBuilder.build();
   
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHour)
		.build();
	
   
        
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();
        

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}