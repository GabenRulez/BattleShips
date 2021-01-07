package pl.edu.agh.iisg.to.battleships.model.email;


import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.model.EasyConfigParser;


public class EmailSender {

    static private final String emailConfigPathResource = "/emailConfig";

    static EasyConfigParser parser = new EasyConfigParser( Main.class.getResource(emailConfigPathResource).getPath().replace("%20", " ") );
    // .replace() jest tu po to, by program działał, gdy w ścieżce do pliku znajdą się spacje

    public EmailSender(){
        parser.listConfig();
    }





}
