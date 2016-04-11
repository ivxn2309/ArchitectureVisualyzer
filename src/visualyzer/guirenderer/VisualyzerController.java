package visualyzer.guirenderer;

import com.sun.javafx.collections.ElementObservableListDecorator;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.jdom2.Element;
import visualyzer.guirenderer.objects.SimpleObject;
import visualyzer.resultsreader.XMLParser;

public class VisualyzerController implements Initializable {
    
    private FileChooser fileChooser;
    ObservableList<Object> data;
    
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
    private ListView<Object> element_list;
    @FXML
    private ToggleGroup view_selection;
    @FXML
    private VBox view_selection_box;
    @FXML
    private RadioButton tier_view_button;
    @FXML
    private RadioButton comp_view_button;
    @FXML
    private RadioButton class_view_button;
    @FXML
    private Pane views_pane;
    @FXML
    private Pane tiers_pane;
    @FXML
    private Pane component_pane;
    
    private ScrollBar scrollX;
    private ScrollBar scrollY;
    
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileChooser = new FileChooser();
        show_left_panel.setSelected(true);
        show_right_panel.setSelected(true);
        tier_view_button.setSelected(true);
        
        tier_view_button.setUserData("Tiers");
        comp_view_button.setUserData("Component");
        
        tiers_pane.setVisible(true);
        component_pane.setVisible(false);
        
        scrollX = new ScrollBar();
        scrollY = new ScrollBar();
        
        scrollX.valueProperty().addListener(getScrollListener("X"));
        scrollY.valueProperty().addListener(getScrollListener("Y"));
        
        tool_bar.getItems().add(new Label("Scroll X Axis"));
        tool_bar.getItems().add(scrollX);
        
        tool_bar.getItems().add(new Label("Scroll Y Axis"));
        tool_bar.getItems().add(scrollY);
        
        addViewsSelectionListener();
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
        
        if(file == null) return;
        
        XMLParser parser = new XMLParser(file.getAbsolutePath());
        Element analysis = parser.getRootNode();
        String parentTag = analysis.getAttributeValue("parent"); // The name of the set of objects
        String childrenTag = analysis.getAttributeValue("child"); // Each object in the set
        String grandsonTag = analysis.getAttributeValue("grandson"); // Pieces of the objects
        String greatGrandsonTag = analysis.getAttributeValue("greatgrandson"); //Associations between the pieces
        
        List<Element> elements = parser.getChildrenObjects(parentTag);
        data = FXCollections.observableArrayList();
        elements.stream().map((e) -> e.getChildren(childrenTag)).forEach((objects) -> {
            objects.stream().map((metaObject) -> {
                SimpleObject object = metaToObject(metaObject);                
                List<Element> parts = metaObject.getChildren(grandsonTag);
                parts.stream().map((part) -> {
                    SimpleObject subobject = metaToObject(part);
                    List<Element> associated = part.getChildren(greatGrandsonTag);
                    associated.stream().forEach((rel) -> {
                        subobject.addAssociation(rel.getValue());
                    });
                    return subobject;
                }).forEach((subobject) -> {
                    object.addChild(subobject);
                });
                return object;
            }).forEach((object) -> {
                data.add(object);
            });
        });
        element_list.getSelectionModel().select(-1);
        element_list.setItems(data);
        views_pane.setVisible(true);
    }
    
    private SimpleObject metaToObject(Element metaObject) {
        try {
            SimpleObject object = new SimpleObject();
            object.setName(metaObject.getAttributeValue("name"));
            object.setTier(metaObject.getAttributeValue("tier"));
            object.setMvc(metaObject.getAttributeValue("mvc"));
            object.setRole(metaObject.getAttributeValue("role"));
            object.setPath(metaObject.getAttributeValue("path"));
            return object;
        }
        catch (Exception ex){
        }
        
        return null;
    }
    
    private void addViewsSelectionListener() {
        view_selection.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) -> {
            if (view_selection.getSelectedToggle() != null) {
                String value = view_selection.getSelectedToggle().getUserData().toString();
                System.out.println("Selected: " + value);
                boolean isTierView = value.equals("Tiers");
                tiers_pane.setVisible(isTierView);
                component_pane.setVisible(!isTierView);
            }
        });
    }
    
    private ChangeListener getScrollListener(String axis) {
        return (ChangeListener<Number>) (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            if(axis.equals("X")) {
                tiers_pane.setLayoutX(-new_val.doubleValue() * 4);
                component_pane.setLayoutX(-new_val.doubleValue() * 4);
            }
            else {
                tiers_pane.setLayoutY(-new_val.doubleValue() * 4);
                component_pane.setLayoutY(-new_val.doubleValue() * 4);
            }
        };
    }
    
}
