package org.rmj.etabulation.app;

import javafx.application.Application;
import javafx.util.Callback;
import org.guanzon.tabulation.Tabulate;
import org.rmj.appdriver.GRider;
import org.rmj.etabulation.view.Tabulation;

public class LetMeInn {
    public static void main(String [] args){                
        String path;
        
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        
        System.setProperty("sys.default.path.config", path); 
        
        GRider oApp = new GRider();
        
        if (!oApp.loadEnv("Tabulation")) {
            System.err.println(oApp.getErrMsg());
            System.exit(1);
        }

        if (!oApp.logUser("Tabulation", "M001230007")) { //M001080006 //M001111122 //M001230007
            System.err.println(oApp.getErrMsg());
            System.exit(1);
        }   
        
        Tabulation instance = new Tabulation();
        instance.setGRider(oApp);
        
        Application.launch(instance.getClass());
    }
}