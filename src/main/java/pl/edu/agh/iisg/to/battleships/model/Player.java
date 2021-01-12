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

    @Column(name ="rating")
    private Integer rating;

    @Column(name ="isAdmin")
    private Boolean isAdmin = false;

    public Player(String name, String mail, String password){
        super();
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.rating = Config.DEFAULT_RATING;
    }

    public Player(String name, String mail, String password, Boolean isAdmin) {
        this(name, mail, password);
        this.isAdmin = isAdmin;
    }

    public Player(){}

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public Integer updateRating(Integer level, Boolean result){
        if(this.rating == null) this.setRating(1000);
        Integer oldRating = this.rating;
        int resultInt = result ? 1 : -1;
        Integer opponentRating = switch(level) {
            case 1 -> 1100;
            case 2 -> 1300;
            case 3 -> 1500;
            default -> 1200;
        };
        double ratingChangeFactor = (opponentRating-oldRating)/400.0;
        if (ratingChangeFactor < -1){
            ratingChangeFactor = -0.95;
        }
        else if (ratingChangeFactor > 1){
            ratingChangeFactor = 0.95;
        }
        Integer ratingChange = Math.toIntExact(Math.round((ratingChangeFactor + resultInt) * 40.0));
        this.setRating(oldRating+ratingChange);
        return ratingChange;
    }

}
