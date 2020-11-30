package model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class HumanPlayer extends Player{

    public HumanPlayer(Game game, String name){
        super(game, name);
        setupBoard();
    }

    public void start() throws InterruptedException {
        workingLoop();
    }

    private void setupBoard(){
        // TODO create ships here
        Ship testShip = new Ship();
        List<Coordinates> tempList = new ArrayList<>();
        tempList.add(new Coordinates(1,0));
        tempList.add(new Coordinates(1,1));
        tempList.add(new Coordinates(1,2));
        tempList.add(new Coordinates(1,3));
        tempList.add(new Coordinates(1,4));
        testShip.setCords(tempList);
        this.myBoard.addShip(testShip);
    }

    public void workingLoop() throws InterruptedException {
        while(true){
            while(this.isWorking() == false){   // Gdy jeszcze nie twoja kolej
                sleep(100);
            }

            System.out.println(getName() + ": Wykonuję ruch.");

            // TODO get user input
            attackPosition(new Coordinates((int)Math.floor(Math.random()*10),(int)Math.floor(Math.random()*10)));
            sleep(1000);

            endOfMyTurn();
        }
    }

}
