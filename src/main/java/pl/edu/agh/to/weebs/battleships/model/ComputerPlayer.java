package pl.edu.agh.to.weebs.battleships.model;

public class ComputerPlayer extends Player{

    public ComputerPlayer(Game game, String name){
        super(game, name);
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
