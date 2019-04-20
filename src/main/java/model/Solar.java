package model;

import java.util.ArrayList;

public class Solar extends Monster {

    public Solar(long id, ArrayList<Monster> allMonsters) {
        this.id = id;
        this.name = "Solar";
        this.team = 2;
        this.initPosition(allMonsters);
        this.range = 110;
        this.hp = 363;
        this.speed = 50;
        this.armor = 44;
        this.damage = 14;
    }

    @Override
    public int damage() {
        return this.damage + this.generateRandom(1,6) + this.generateRandom(1,6);
    }
}
