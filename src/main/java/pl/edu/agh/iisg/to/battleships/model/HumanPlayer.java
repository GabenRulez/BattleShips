package pl.edu.agh.iisg.to.battleships.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.hibernate.annotations.Columns;

import javax.persistence.*;

@Entity
@Table(name = HumanPlayer.TABLE_NAME)
public class HumanPlayer extends Player{
    public static final String TABLE_NAME = "players";

    @Column(name ="name")
    private String name;


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name="ID")
    private int id;


    @Column(name ="mail")
    private String mail;

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Column(name ="password")
    private String password;


    public HumanPlayer(String name, String mail, String password){
        super();
        this.mail = mail;
        this.password = password;
        this.name = name;
    }

    public HumanPlayer(){}

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }



    public String getPassword() {
        return password;
    }

//
//    public void start() throws InterruptedException {
//        workingLoop();
//    }



}
