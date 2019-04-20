package model;

import java.util.ArrayList;

public class BarbareOrc extends Monster {

    public BarbareOrc(long id, ArrayList<Monster> allMonsters) {
        this.id = id;
        this.name = "BarbareOrc";
        this.initPosition(allMonsters);
        this.hp = 142;
        this.speed = 40;
        this.armor = 17;
    }

}
