package visualyzer.guirenderer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author ivxn2
 */
public class VisualyzerController implements Initializable {
    @FXML
    private BorderPane border_pane;
    @FXML
    private MenuBar menu_bar;
    @FXML
    private MenuItem close_menu;
    @FXML
    private CheckMenuItem show_left_panel;
    @FXML
    private CheckMenuItem show_right_panel;
    @FXML
    private MenuItem about_menu;
    @FXML
    private MenuItem import_xml;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void close(ActionEvent event) {
    }

    @FXML
    private void toggleLeftPanel(ActionEvent event) {
    }

    @FXML
    private void toggleRightPanel(ActionEvent event) {
    }


    @FXML
    private void showAbout(ActionEvent event) {
    }

    @FXML
    private void importXML(ActionEvent event) {
    }
    
}
