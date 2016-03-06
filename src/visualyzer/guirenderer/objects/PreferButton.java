package visualyzer.guirenderer.objects;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;

public class PreferButton extends Button {
    public PreferButton(String text, ImageView image) {
        super(text, image);
        
        ((ImageView)super.getGraphic()).setFitHeight(30.0);
        ((ImageView)super.getGraphic()).setFitWidth(30.0);
        ((ImageView)super.getGraphic()).setPickOnBounds(true);
        
        super.setMnemonicParsing(false);
        super.setTextAlignment(TextAlignment.CENTER);
        super.setWrapText(true);
        super.setPrefSize(80.0, 45.0);
        
        super.minHeight(USE_PREF_SIZE);
        super.minWidth(USE_PREF_SIZE);
        
        super.setMaxHeight(USE_PREF_SIZE);
        super.setMaxWidth(1000);        
    }
}
