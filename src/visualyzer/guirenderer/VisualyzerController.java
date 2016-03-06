package visualyzer.guirenderer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

public class VisualyzerController implements Initializable {
    
    private FileChooser fileChooser;
    ObservableList<String> data = FXCollections.observableArrayList(
            "chocolate", "salmon", "gold", "coral", "darkorchid",
            "darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
            "blueviolet", "brown");
    
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
    @FXML
    private Label status_bar;
    @FXML
    private ToolBar tool_bar;
    @FXML
    private ListView<String> element_list;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser = new FileChooser();
        show_left_panel.setSelected(true);
        show_right_panel.setSelected(true);
        
        element_list.setItems(data);
    }    

    @FXML
    private void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void toggleLeftPanel(ActionEvent event) {
        boolean show = show_left_panel.isSelected();
        element_list.setVisible(show);
    }

    @FXML
    private void toggleRightPanel(ActionEvent event) {
        boolean show = show_right_panel.isSelected();
        tool_bar.setVisible(show);
    }


    @FXML
    private void showAbout(ActionEvent event) {
    }

    @FXML
    private void importXML(ActionEvent event) {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File file = fileChooser.showOpenDialog(border_pane.getScene().getWindow());
    }
    
}
