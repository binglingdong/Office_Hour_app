package junit_test_beds;

import java.util.ArrayList;
import static junit_test_beds.XMLU_Unit_Tests.VALID_XML_RESOURCE_PATH;
import org.junit.Assert;
import org.junit.Test;
import properties_manager.PropertiesManager;

/**
 * PM_Unit_Tests.java
 * 
 * This file provides a test bed for the PropertiesManager class, testing both valid
 * and invalid usage to make sure it provides well defined behavior for both.
 * 
 * @author McKillaGorilla
 * @version 2.0
 */
public class PM_Unit_Tests {
    public static final String UNIT_TEST_PATH = "test/junit_test_beds/";
    public static final String MY_STRING            = "MY_STRING";
    public static final String MY_STRING_2          = "MY_STRING_2";
    public static final String MY_STRING_OPTIONS    = "MY_STRING_OPTIONS";
    public static final String MY_STRING_OPTIONS_2  = "MY_STRING_OPTIONS_2";
    public static final String TEST_STRING          = "Hello, World";
    public static final String TEST_STRING_2        = "Goodbye, Cruel World";
    public static final String JANUARY  = "January";
    public static final String FEBRUARY = "February";
    public static final String MARCH    = "March";
    public static final String MONDAY   = "Monday";
    public static final String TUESDAY  = "Tuesday";
         
    @Test
    public void testLoadAndGetProperties() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, UNIT_TEST_PATH);
            props.loadProperties(VALID_XML_RESOURCE_PATH);
            Assert.assertEquals(TEST_STRING, props.getProperty(MY_STRING));
        }
        catch (Exception ex) {
            Assert.fail();
        }
    }
    
    @Test
    public void testLoadAndGetPropertyOptions() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, UNIT_TEST_PATH);
            props.loadProperties(VALID_XML_RESOURCE_PATH);
            ArrayList<String> propOptions = props.getPropertyOptionsList(MY_STRING_OPTIONS);
            Assert.assertEquals(JANUARY, propOptions.get(0));
            Assert.assertEquals(FEBRUARY, propOptions.get(1));
            Assert.assertEquals(MARCH, propOptions.get(2));
        }
        catch (Exception ex) {
            Assert.fail();
        }
    }
    
    @Test
    public void testClearProperties() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, UNIT_TEST_PATH);
            props.loadProperties(VALID_XML_RESOURCE_PATH);
            props.clear();
            Assert.assertEquals(0, props.getNumProperties());
            Assert.assertEquals(0, props.getNumPropertyOptionsLists());
        }
        catch (Exception ex) {
            Assert.fail();
        }        
    }
    
    @Test
    public void testRemoveProperties() {   
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, UNIT_TEST_PATH);
            props.loadProperties(VALID_XML_RESOURCE_PATH);
            props.removeProperty(MY_STRING);
            Assert.assertEquals(null, props.getProperty(MY_STRING));
            Assert.assertEquals(2, props.getNumProperties());
            Assert.assertEquals(TEST_STRING_2, props.getProperty(MY_STRING_2));
        }
        catch (Exception ex) {
            Assert.fail();
        }
    }
    
    @Test
    public void testRemovePropertyOptions() {  
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, UNIT_TEST_PATH);
            props.loadProperties(VALID_XML_RESOURCE_PATH);
            props.removePropertyOptionsList(MY_STRING_OPTIONS);
            Assert.assertEquals(null, props.getPropertyOptionsList(MY_STRING_OPTIONS));
            Assert.assertEquals(1, props.getNumPropertyOptionsLists());
            ArrayList<String> propOptions = props.getPropertyOptionsList(MY_STRING_OPTIONS_2);
            Assert.assertEquals(MONDAY, propOptions.get(0));
            Assert.assertEquals(TUESDAY, propOptions.get(1));
        }
        catch (Exception ex) {
            Assert.fail();
        }
    }
}
