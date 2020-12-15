package pl.edu.agh.iisg.to.battleships.model;

import javax.persistence.*;

@Entity
@Table(name = Player.TABLE_NAME)
public class Player {
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


    public Player(String name, String mail, String password){
        super();
        this.mail = mail;
        this.password = password;
        this.name = name;
    }

    public Player(){}

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
    

    public String getPassword() {
        return password;
    }

}
