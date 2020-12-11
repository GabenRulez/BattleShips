package pl.edu.agh.iisg.to.battleships.model;

import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.enums.GameStatus;
import pl.edu.agh.iisg.to.battleships.session.SessionService;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = Game.TABLE_NAME)
public class Game {
    public static final String TABLE_NAME = "games";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private HumanPlayer human;

    @Transient
    private ComputerPlayer computer;

    @Column(name = "playerWon")
    private Boolean humanWon;

    @Transient
    private GameStatus currentState;

    @Transient
    private Boolean isFinished;


    @Column(name = "difficulty")
    private Integer difficultyLevel;

    public Game(HumanPlayer player){
        if(player == null){
            player = getDefaultPlayer();
        }

        this.currentState = GameStatus.GAME_NOT_STARTED;
        this.difficultyLevel = 1;
        humanWon = false;
        isFinished = false;

        computer = new ComputerPlayer(this, difficultyLevel);

        this.human = player;
        this.human.setCurrentGame(this);
    }

    public Game(){};

    private HumanPlayer getDefaultPlayer(){

        SessionService.openSession();
        HumanPlayerDao playerDao = new HumanPlayerDao();
        HumanPlayer player;
        Optional<HumanPlayer> defaultPlayer = playerDao.findByMail("a@a.com");
        if(defaultPlayer.isEmpty()){
            defaultPlayer = playerDao.create("testowy", "a@a.com", "test");
            player = defaultPlayer.orElseThrow(() -> new PersistenceException("Cannot create default player!"));
        }
        else{
            player = defaultPlayer.get();
        }

        SessionService.closeSession();
        return player;
    }


    public HumanPlayer getHuman() {
        return human;
    }

    public ComputerPlayer getComputer() {
        return computer;
    }


    public void start() {
        // TODO: Handle authentication and database fetch
        this.currentState = GameStatus.GAME_SHIP_PLACEMENT;
    }

    public void setCurrentState(GameStatus currentState) {
        this.currentState = currentState;
    }
    public GameStatus getCurrentState(){
        return this.currentState;
    }

    public void initializeAttack(Player target, Coordinates targetCoords){

    }

    public Long getId() {
        return id;
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

}
