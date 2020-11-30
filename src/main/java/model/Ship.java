package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship {
    Map<Coordinates, Boolean> allCords; // wartość true oznacza że trafiony segment
    boolean shotDown;

    public Ship(){
        this.allCords = new HashMap<>();
        shotDown = false;
    }

    public void deleteCords(){
        this.allCords = new HashMap<>();
    }

    public void setCords(List<Coordinates> allCords){
        for (Coordinates cords: allCords) {
            this.allCords.put(cords, false);
        }
    }

    public List<Coordinates> getCords(){
        List<Coordinates> temp = new ArrayList<>();
        temp.addAll(allCords.keySet());
        return temp;
    }

    public void getHit(Coordinates cords){
        this.allCords.replace(cords, true);
    }
}
