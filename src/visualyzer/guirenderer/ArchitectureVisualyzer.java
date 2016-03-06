package visualyzer.guirenderer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ArchitectureVisualyzer extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new Image("/visualyzer/img/icon.png"));
        stage.setTitle("Architecture Visualizer");
        
        Parent root = FXMLLoader.load(getClass().getResource("Visualyzer.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/visualyzer/css/style.css");
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
