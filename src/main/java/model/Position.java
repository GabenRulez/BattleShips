package model;

public class Position {
    public Coordinates cords;
    private Ship shipOnPosition;
    private boolean wasHit;

    public Position(Coordinates cords){
        this.cords = cords;
        shipOnPosition = null;
        wasHit = false;
    }

    public void placeShipHere(Ship ship){
        this.shipOnPosition = ship;
    }

    public boolean hasShip(){
        return this.shipOnPosition != null;
    }

    public void setMarked(){
        this.wasHit = true;
    }

    public boolean getHitState(){
        return this.wasHit;
    }

    public Ship getShipOnPosition(){
        return this.shipOnPosition;
    }

}
