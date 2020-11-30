package pl.edu.agh.to.weebs.battleships.model;

import pl.edu.agh.to.weebs.battleships.model.enums.GameStatus;

public class Game {
    HumanPlayer human;
    ComputerPlayer computer;

    Player firstPlayer;
    Player secondPlayer;
    Player winner;

    GameStatus currentState;

    public Game(){
    }

    public void start(){
        try{
            initialize();
            gameLoop();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    public GameStatus getCurrentState(){
        return this.currentState;
    }

    public void initialize(){
        human = new HumanPlayer(this,"Human"); // TODO get username
        computer = new ComputerPlayer(this,"T-800");
        winner = null;

//        if( Math.random() < 0.5 ) {
//            firstPlayer = human;
//            secondPlayer = computer;
//        }
//        else {
//            firstPlayer = computer;
//            secondPlayer = human;
//        }
    }

    public void gameLoop() throws InterruptedException {


//        Thread humanThread = new Thread(){
//            public void run(){
//                try{
//                    human.start();
//                }
//                catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        Thread computerThread = new Thread(){
//            public void run(){
//                try{
//                    computer.start();
//                }
//                catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        humanThread.start();
//        computerThread.start();
//
//
//        while( true ){
//            firstPlayer.myTurn();
//            while(firstPlayer.isWorking()){
//                Thread.sleep(100);
//            }
//
//            winner = checkGameState();
//            if(winner != null) break;
//
//
//            secondPlayer.myTurn();
//            while(secondPlayer.isWorking()){
//                Thread.sleep(100);
//            }
//
//            winner = checkGameState();
//            if(winner != null) break;
//        }
//
//        System.out.println(winner.getName() + " wygrał!");
//        //humanThread.join();
        //computerThread.join();
    }

    Board getPlayersBoardStatus(){
        return this.human.getBoard();
    }

    public void initializeAttack(Player target, Coordinates targetCoords){

    }

}
