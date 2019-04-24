package model;

import java.util.ArrayList;

public class WorgsRider extends Monster {

    public WorgsRider(long id, ArrayList<Monster> allMonsters) {
        this.id = id;
        this.name = "WorgsRider";
        this.initPosition(allMonsters);
        this.hp = 13;
        this.speed = 20;
        this.armor = 18;
        this.damage = 2;
        this.weaponAttack = 4;
    }
}
