package application.model.bdd;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigBdd {


    public static String getConfig(String config){
        try (InputStream input = ConfigBdd.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();

            if (input == null) {
                System.err.println("Fichier config.properties introuvable");
                return "";
            }

            prop.load(input);

            return prop.getProperty(config);


        } catch (  IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
