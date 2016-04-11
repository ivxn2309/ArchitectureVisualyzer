package visualyzer.guirenderer;

import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.awt.dnd.InvalidDnDOperationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JPanel;
import visualyzer.guirenderer.objects.SimpleObject;

public class ArchitectureVisualyzer extends Application {
    
    private Pane tiersView;
    private Pane componentView;
    
    private Map<String, Rectangle> rectangles;
    private Map<String, double []> positions;
    List<Line> lines;
    
    private ColorPicker baseColorPicker;
    private ColorPicker hilightColorPicker;
    
    private CheckBox showLines;
    
    @Override
    public void start(Stage stage) throws Exception {
        rectangles = new HashMap<>();
        positions = new HashMap<>();
        lines = new ArrayList<>();
        stage.getIcons().add(new Image("/visualyzer/img/icon.png"));
        stage.setTitle("Architecture Visualizer");
        stage.setMinWidth(800.0);
        stage.setMinHeight(650.0);
        
        Parent root = FXMLLoader.load(getClass().getResource("Visualyzer.fxml"));
        

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/visualyzer/css/style.css");
        tiersView = (Pane) scene.lookup("#tiers_pane");
        componentView = (Pane) scene.lookup("#component_pane");
        
        baseColorPicker = new ColorPicker(Color.valueOf("#eee"));
        hilightColorPicker = new ColorPicker(Color.WHITE);
        baseColorPicker.setId("base_color");
        hilightColorPicker.setId("hili_color");
        baseColorPicker.setOnAction(colorPickerHandler(scene));
        hilightColorPicker.setOnAction(colorPickerHandler(scene));
        
        showLines = new CheckBox("Show Lines");
        showLines.setSelected(false);
        showLines.selectedProperty().addListener(showLinesHandler());
        
        addHighLevelView(scene);
        addLowLevelView(scene);
        
        Pane container = (Pane) scene.lookup("#views_pane");
        container.setVisible(false);
        
        addListListener(scene);
        
        ToolBar tool_bar = (ToolBar) scene.lookup("#tool_bar");
        tool_bar.getItems().add(new Label("Base Color"));
        tool_bar.getItems().add(baseColorPicker);
        tool_bar.getItems().add(new Label("Hilight Color"));
        tool_bar.getItems().add(hilightColorPicker);
        tool_bar.getItems().add(showLines);
        
        stage.setScene(scene);
        stage.show();
    }
    
    private ChangeListener showLinesHandler() {
        return new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                toggleLines(newValue);
            }
        };
    }
    
    private void toggleLines(boolean visible) {
        for(Line line: lines) {
            line.setVisible(visible);
        }
    }
    
    private EventHandler colorPickerHandler(Scene scene) {
        return new EventHandler() {
            @Override
            public void handle(Event event) {
                unmarkRectangles();
                ListView list = (ListView) scene.lookup("#element_list");
                int sel = list.getSelectionModel().getSelectedIndex();
                list.getSelectionModel().select(-1);
                list.getSelectionModel().select(sel);
            }
        };
    }
    
    private void addListListener(Scene scene) {
        ListView list = (ListView) scene.lookup("#element_list");
        ChangeListener listener = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                componentView.getChildren().remove(0, componentView.getChildren().size());
                lines = new ArrayList<>();
                unmarkRectangles();
                SimpleObject selected = (SimpleObject) newValue;
                if(selected != null) {
                    for(String id : selected.getLocationWithID()) {
                        markRectangle(id);
                    }
                    setLowLevelView(scene, selected);
                }
                toggleLines(showLines.isSelected());
            }
        };
        list.getSelectionModel().selectedItemProperty().addListener(listener);
    }
    
    private void unmarkRectangles(){
        for(Rectangle rectangle : rectangles.values()) {
            rectangle.setFill(baseColorPicker.getValue());
            rectangle.setStroke(Paint.valueOf("#000"));
            rectangle.setStrokeWidth(2);
        }
    }
    
    private void markRectangle(String id) {
        Rectangle rectangle = rectangles.get(id);
        rectangle.setFill(hilightColorPicker.getValue());
        rectangle.setStroke(Paint.valueOf("#900"));
        rectangle.setStrokeWidth(3);
    }
    
    private void addHighLevelView(Scene scene) {
        Line line = new Line(175, 10, 175, 490);
        line.setStrokeWidth(2);

        System.out.println("Value: " + baseColorPicker.getValue());
        Color color = baseColorPicker.getValue();
        
        Pane client = createRectangle("client_r", "Browser Client", 0, 0, 350, 50, false, color);
        Pane body = createRectangle("body_r", "", 0, 75, 350, 350, false, color);
        Pane db = createRectangle("db_r", "Database", 0, 450, 350, 50, false, color);
        
        Pane presn = createRectangle("presn_r", "Presentation Layer", 10, 85, 330, 160, true, color);
        Pane model = createRectangle("model_r", "Model", 10, 255, 330, 160, true, color);
        
        Pane view = createRectangle("view_r", "View", 20, 105, 150, 130, false, color);
        Pane cont = createRectangle("cont_r", "Controller", 180, 105, 150, 130, false, color);
        
        Pane buss = createRectangle("buss_r", "Logic Layer", 20, 275, 150, 130, false, color);
        Pane data = createRectangle("data_r", "Data Layer", 180, 275, 150, 130, false, color);
        
        tiersView.getChildren().add(line);
        tiersView.getChildren().add(client);
        tiersView.getChildren().add(body);
        tiersView.getChildren().add(db);
        
        tiersView.getChildren().add(presn);
        tiersView.getChildren().add(model);
        
        tiersView.getChildren().add(view);
        tiersView.getChildren().add(cont);
        
        tiersView.getChildren().add(buss);
        tiersView.getChildren().add(data);
        
        Rectangle rectangle = (Rectangle) scene.lookup("#client_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#body_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#db_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#presn_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#model_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#view_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#cont_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#buss_r");
        rectangles.put(rectangle.getId(), rectangle);
        
        rectangle = (Rectangle) scene.lookup("#data_r");
        rectangles.put(rectangle.getId(), rectangle);
    }
    
    private Pane createRectangle(String id, String label, double posX, double posY, double width, double height, boolean labelOnTop, Color fill) {
        Rectangle rectangle = new Rectangle(0, 0, width, height);
        rectangle.setId(id);
        rectangle.setFill(fill);
        rectangle.setStroke(Paint.valueOf("#000"));
        rectangle.setStrokeWidth(2);
        rectangle.setArcHeight(10);
        rectangle.setArcWidth(10);
        Text text = new Text(label);
        text.setLayoutX(10);
        text.setLayoutY(15);
        
        Pane pane = new StackPane();
        if(labelOnTop) pane = new Pane();
        
        pane.getChildren().add(rectangle);
        pane.getChildren().add(text);
        pane.setLayoutX(posX);
        pane.setLayoutY(posY);
        return pane;
    }
    
    private void addLowLevelView(Scene scene) {
        
    }
    
    private void setLowLevelView(Scene scene, SimpleObject object) {
        Color color = baseColorPicker.getValue();
        String tier = object.getTierName();
        if(tier.equals("0") || tier.equals("1,2,3")) 
            tier = "Application";
        
        List<SimpleObject> parts = object.getChildren();
        if(parts == null) return;
        
        //Add associated as parts if not exists
        List<SimpleObject> newParts = new ArrayList<>();
        for(SimpleObject part: parts) {
            List<String> originals = part.getAssociations();
            if(originals == null) continue;
            
            List<String> associations = new ArrayList<>();
            for(String tmp: originals)
                associations.add(tmp);
            
            for(SimpleObject other: parts) {
                associations.remove(other.getName());
            }
            //Only left the entities that are inherited
            if(associations.isEmpty()) continue;
            for(String name: associations) {
                SimpleObject newPart = new SimpleObject(name);
                newPart.setRole("");
                newParts.add(newPart);
            }
        }
        for(SimpleObject newPart: newParts) {
            parts.add(newPart);
        }
        
        //Retrieve all the roles
        List<String> roles = new ArrayList<>();
        for(SimpleObject part: parts) {
            boolean repeated = false;
            for(String role: roles) {
                if(part.getRole().equalsIgnoreCase(role)){
                    repeated = true;
                    break;
                }
            }
            if(!repeated) roles.add(part.getRole());
        }
        
        //Count the objects by role
        int [] sizes = new int [roles.size()];
        for(SimpleObject part: parts) {
            String role = part.getRole();
            for(int i = 0; i < roles.size(); i++) {
                if(roles.get(i).equalsIgnoreCase(role)) {
                    sizes[i]++;
                    break;
                }
            }
        }
        
        //Compute the dimension for each role
        int [] widths = new int [sizes.length];
        int [] heights = new int [sizes.length];
        for(int i = 0; i < sizes.length; i++) {
            double tmp = Math.sqrt(sizes[i]);
            widths[i] = (int)Math.ceil(tmp);
            tmp = (double)sizes[i] / (double)widths[i];
            heights[i] = (int)Math.ceil(tmp);
        }
        
        // The amount of roles per line
        int roleCols = (int)Math.ceil(Math.sqrt(roles.size()));
        int roleRows = (int)Math.ceil((double)roles.size() / (double)roleCols);
        
        // The tier container
        double tierWidth = 40 + ((roleCols - 1) * 20);
        double tierHeight = 40 + ((roleRows) - 1) * 20;
        for(int i = 0; i < roleCols; i++) {
            tierWidth += (widths[i] * 150) + ((widths[i] + 1) * 20);
        }
        for(int row = 0, i = 0; row < roleRows; row++) {
            int maxHeightPerLine = 0;
            for(; i < roleCols * (row + 1) && i < heights.length; i++) {
                int roleHeight = (heights[i] * 50) + ((heights[i] + 1) * 20);
                maxHeightPerLine = roleHeight > maxHeightPerLine ? roleHeight : maxHeightPerLine;
            }
            tierHeight += maxHeightPerLine;
        }
        Pane tierPane = createRectangle("rect_tier", tier, -100, -20, tierWidth, tierHeight, true, color);
        componentView.getChildren().add(tierPane);
        
        Rectangle rectangle = (Rectangle) scene.lookup("#rect_tier");
        if(rectangle != null)
            rectangles.put(rectangle.getId(), rectangle);
        
        // Begin the roles render
        double x = -80;
        double y = 0;
        double maxHeight = 0;
        for(int i = 0, col = 1; i < roles.size(); i++, col++) {
            double width = (widths[i] * 150) + ((widths[i] + 1) * 20);
            double height = (heights[i] * 50) + ((heights[i] + 1) * 20);
            if(height > maxHeight) maxHeight = height;
            
            // Allocation of role
            Pane pane = createRectangle("rect_" + roles.get(i).replaceAll("[\\W\\s.]", ""), roles.get(i), x, y, width, height, true, hilightColorPicker.getValue());
            if(roles.get(i).length() > 0)
                componentView.getChildren().add(pane);
            
            rectangle = (Rectangle) scene.lookup("#rect_" + roles.get(i).replaceAll("[\\W\\s.]", ""));
            if(rectangle != null) {
                rectangles.put(rectangle.getId(), rectangle);
                markRectangle(rectangle.getId());
            }
            
            // Allocation of the role's parts
            int j = 0;
            double innerX = x + 20;
            double innerY = y + 20;
            for(SimpleObject part: parts) {
                if(part.getRole().equalsIgnoreCase(roles.get(i))) {
                    Pane innerPane = createRectangle("rect_" + part.getName().replaceAll("[\\W\\s.]", ""), part.getName(), innerX, innerY, 150, 50, false, hilightColorPicker.getValue());
                    componentView.getChildren().add(innerPane);
                    
                    rectangle = (Rectangle) scene.lookup("#rect_" + part.getName().replaceAll("[\\W\\s.]", ""));
                    if(rectangle != null) {
                        rectangles.put(rectangle.getId(), rectangle);
                        positions.put(rectangle.getId(), new double []{innerX, innerY});
                    }
            
                    if(j != 0 && (j + 1) % widths[i] == 0) {
                        innerX = x + 20;
                        innerY += 70;
                    }
                    else {
                        innerX += 170;
                    }                    
                    j++;
                }
            }
            //End of allocation of parts for this role
            
            x += width + 20;
            if(col % roleCols == 0) {
                x = -80;
                y = maxHeight + 20;
            }
        }
        
        //Draw associations between parts
        for(SimpleObject part: parts) {
            List<String> associations = part.getAssociations();
            if(associations == null || associations.isEmpty()) continue;
            double [] sourcePos = positions.get("rect_" + part.getName().replaceAll("[\\W\\s.]", ""));
            for(String associated: associations) {
                double [] targetPos = positions.get("rect_" + associated.replaceAll("[\\W\\s.]", ""));
                drawAssociation(sourcePos[0], sourcePos[1], targetPos[0], targetPos[1], scene);
            }
        }
    }
    
    private Line drawAssociation(double sourceX, double sourceY, double targetX, double targetY, Scene scene) {
        double x1, y1;
        double x2, y2;
        double [] points = new double [] {
            75,0, // 0
            151,25, // 1
            75,51, // 2
            0,25 // 3
        };
        
        //Random offset in the points to avoid the overlay
        int minOffset = -15;
        int maxOffset = 15;
        int rnd = ThreadLocalRandom.current().nextInt(minOffset, maxOffset + 1);
        points[0] += rnd;
        
        rnd = ThreadLocalRandom.current().nextInt(minOffset, maxOffset + 1);
        points[3] += rnd;
        
        rnd = ThreadLocalRandom.current().nextInt(minOffset, maxOffset + 1);
        points[4] += rnd;
        
        rnd = ThreadLocalRandom.current().nextInt(minOffset, maxOffset + 1);
        points[7] += rnd;
        
        //Determine where is the source and the target
        int sourcePoint = 0;
        int targetPoint = 2;
        if(targetY > (sourceY + 100)) {
            sourcePoint = 2;
            targetPoint = 0;
        }
        else if(targetY > (sourceY - 100)) {
            if (targetX > sourceX) {
                sourcePoint = 1;
                targetPoint = 3;
            }
            else {
                sourcePoint = 3;
                targetPoint = 1;
            }
        }
        
        //Retrieve de coordinates
        x1 = sourceX + (points[sourcePoint * 2]);
        y1 = sourceY + (points[(sourcePoint * 2) + 1]);
        
        x2 = targetX + (points[targetPoint * 2]);
        y2 = targetY + (points[(targetPoint * 2) + 1]);
        
        //Draw the line
        Line line = new Line(x1, y1, x2, y2);
        String id = "line_" + (int)x1 + "_" + (int)y1 + "_" + (int)x2 + "_" + (int)y2;
        line.setId(id);
        line.setStrokeWidth(2);
        componentView.getChildren().add(line);
        lines.add((Line) scene.lookup("#" + id));
        
        return line;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
