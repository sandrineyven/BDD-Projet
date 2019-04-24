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
        //arc
        this.weaponAttack = 31;
        this.nbAttack = 3;
    }

    @Override
    public int damage(Monster enemy) {

        int dice = this.generateRandom(1, 20);
        int touch = this.weaponAttack + dice;

        if ((touch > enemy.armor) || (dice == 20)) {
            return this.damage + this.generateRandom(1, 6) + this.generateRandom(1, 6);
        } else {
            return 0;
        }
    }
}
