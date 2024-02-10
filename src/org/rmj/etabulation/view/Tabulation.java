
package org.rmj.etabulation.view;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.guanzon.tabulation.Callback;
import org.guanzon.tabulation.Tabulate;
import org.rmj.appdriver.GRider;

/**
 *
 * @author Maynard
 */
public class Tabulation extends Application {
     public final static String pxeMainFormTitle = "E - Tabulation";
     public final static String pxeMainForm = "MainForm.fxml";
     public final static String pxeStageIcon = "org/rmj/etabulation/images/icon.png";
     public static GRider oApp;
    
    @Override
    public void start(Stage stage) throws Exception {
     /*
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
     */
     
          FXMLLoader view = new FXMLLoader();
          view.setLocation(getClass().getResource(pxeMainForm));

          MainFormController controller = new MainFormController();
          controller.setGRider(oApp);

          view.setController(controller);        
          Parent parent = view.load();
          Scene scene = new Scene(parent);
          
          //get the screen size
          Screen screen = Screen.getPrimary();
          Rectangle2D bounds = screen.getVisualBounds();

          stage.setScene(scene);
          stage.initStyle(StageStyle.UNDECORATED);
          stage.getIcons().add(new Image(pxeStageIcon));
          stage.setTitle(pxeMainFormTitle);

          // set stage as maximized but not full screen
          stage.setX(bounds.getMinX());
          stage.setY(bounds.getMinY());
          stage.setWidth(bounds.getWidth());
          stage.setHeight(bounds.getHeight());
          stage.centerOnScreen();
          stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
     public void setGRider(GRider foValue){
         oApp = foValue;
     }

}
