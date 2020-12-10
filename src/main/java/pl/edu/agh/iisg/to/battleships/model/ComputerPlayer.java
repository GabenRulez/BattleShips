package pl.edu.agh.iisg.to.battleships.model;

public class ComputerPlayer extends Player{

    Integer difficultyLevel;

    public ComputerPlayer(Game game, Integer difficultyLevel){
        super(game);
        this.difficultyLevel = difficultyLevel;
    }

//    public void start() throws InterruptedException {
//        while(true){
//            while(this.isWorking() == false){
//                sleep(100);
//            }
//
//            System.out.println(getName() + ": Wykonuję ruch.");
//
//            // TODO get AI input
//            makeMove(new Coordinates((int)Math.floor(Math.random()*10),(int)Math.floor(Math.random()*10)));
//
//            endOfMyTurn();
//        }
//    }
}
