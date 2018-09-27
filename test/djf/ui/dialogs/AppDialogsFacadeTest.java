/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.ui.dialogs;

import djf.AppPropertyType;
import djf.AppTemplate;
import djf.modules.AppLanguageModule;
import java.io.File;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bingling.dong
 */
public class AppDialogsFacadeTest {
    
    public AppDialogsFacadeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of showAboutDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowAboutDialog() {
        System.out.println("showAboutDialog");
        AppTemplate app = null;
        AppDialogsFacade.showAboutDialog(app);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showExportDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowExportDialog() throws Exception {
        System.out.println("showExportDialog");
        AppTemplate app = null;
        AppDialogsFacade.showExportDialog(app);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showHelpDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowHelpDialog() {
        System.out.println("showHelpDialog");
        AppTemplate app = null;
        AppDialogsFacade.showHelpDialog(app);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showLanguageDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowLanguageDialog() throws Exception {
        System.out.println("showLanguageDialog");
        AppLanguageModule languageSettings = null;
        AppDialogsFacade.showLanguageDialog(languageSettings);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showMessageDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowMessageDialog() {
        System.out.println("showMessageDialog");
        Stage parent = null;
        Object titleProperty = null;
        Object contentTextProperty = null;
        AppDialogsFacade.showMessageDialog(parent, titleProperty, contentTextProperty);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showOpenDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowOpenDialog() {
        System.out.println("showOpenDialog");
        Stage window = null;
        AppPropertyType openTitleProp = null;
        File expResult = null;
        File result = AppDialogsFacade.showOpenDialog(window, openTitleProp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showSaveDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowSaveDialog() {
        System.out.println("showSaveDialog");
        Stage window = null;
        AppPropertyType saveTitleProp = null;
        File expResult = null;
        File result = AppDialogsFacade.showSaveDialog(window, saveTitleProp);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showStackTraceDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowStackTraceDialog() {
        System.out.println("showStackTraceDialog");
        Stage parent = null;
        Exception exception = null;
        Object appErrorTitleProperty = null;
        Object appErrorContentProperty = null;
        AppDialogsFacade.showStackTraceDialog(parent, exception, appErrorTitleProperty, appErrorContentProperty);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showTextInputDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowTextInputDialog() {
        System.out.println("showTextInputDialog");
        Stage parent = null;
        Object titleProperty = null;
        Object contentProperty = null;
        StringProperty nameProp = null;
        AppDialogsFacade.showTextInputDialog(parent, titleProperty, contentProperty, nameProp);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showWelcomeDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowWelcomeDialog() {
        System.out.println("showWelcomeDialog");
        AppTemplate app = null;
        String expResult = "";
        String result = AppDialogsFacade.showWelcomeDialog(app);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of showYesNoCancelDialog method, of class AppDialogsFacade.
     */
    @Test
    public void testShowYesNoCancelDialog() {
        System.out.println("showYesNoCancelDialog");
        Stage parent = null;
        Object titleProperty = null;
        Object contentTextProperty = null;
        ButtonType expResult = null;
        ButtonType result = AppDialogsFacade.showYesNoCancelDialog(parent, titleProperty, contentTextProperty);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
