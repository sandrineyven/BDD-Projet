package model;

import java.util.ArrayList;

public class Warlord extends Monster {

    public Warlord(long id, ArrayList<Monster> allMonsters) {
        this.id = id;
        this.name = "Warlord";
        this.initPosition(allMonsters);
        this.hp = 141;
        this.speed = 30;
        this.armor = 27;
    }

}
