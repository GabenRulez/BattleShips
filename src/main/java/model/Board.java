package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    Position[][] positions;
    List<Ship> ships;

    public Board(){
        this.positions = new Position[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                this.positions[i][j] = new Position(new Coordinates(i,j));
            }
        }
        this.ships = new ArrayList<>();
    }

    public void addShip(Ship ship){
        this.ships.add(ship);
        for(Coordinates cords : ship.getCords()){
            this.positions[cords.x][cords.y].placeShipHere(ship);
        }
    }

    public boolean shoot(Coordinates cords){
        Position temp = positions[cords.x][cords.y];
        temp.setMarked();
        boolean hasShip = temp.hasShip();
        if(hasShip){
            temp.getShipOnPosition().getHit(cords);
        }
        return hasShip;
    }
}
