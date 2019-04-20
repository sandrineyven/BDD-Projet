package model;

import scala.Serializable;

import java.util.ArrayList;
import java.util.List;

public abstract class Monster implements Serializable {

    public long id;
    public String name;
    public int team = 1;
    public double x = 0;
    public double y = 0;
    public double z = 0;
    public double speed = 10;
    public boolean isAlive = true;
    public int hp = 500;
    public int range = 10;
    public int armor = 18;
    public int damage = 10;

    @Override
    public String toString() {
        return "[" + this.id + "] " + this.name + " : " + this.hp +" HP"+ " - ("
                + this.x + " ; " + this.y + " ; " + this.z + ")";
    }

    public int generateRandom(int min, int max) {
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }


    public void initPosition(ArrayList<Monster> allMonsters) {

        //Taille du plateau de jeu
        double minX = -100;
        double maxX = 100;

        double minY = -100;
        double maxY = 100;

        //Repartition des monstres d'une part et d'autre
        if(this.team == 1){
            minX = 0;
        }else{
            maxX = 0;
        }

        boolean isFree = true;
        while (isFree) {
            double x = Math.random() * maxX + minX;
            double y = Math.random() * (maxY - minY) + minY;
            if (!checkIsFree(x, y, allMonsters)) {
                this.x = x;
                this.y = y;
                isFree = false;
            }
        }

    }

    private boolean checkIsFree(double x, double y, ArrayList<Monster> monsters) {
        int distMin = 1;
        for (Monster m : monsters) {
            double distX = x - m.x;
            double distY = y - m.y;
            if (distX * distX + distY * distY == distMin * distMin)
                return true;
        }
        return false;
    }


    public String decideAction(ArrayList<Monster> monsters) {

        List<Monster> enemies = detectEnemies(monsters);
        for (Monster enemy : enemies) {
            if (canTouchEnemy(enemy)) return "attack";

        }

        return "move";
    }

    public List<Monster> detectEnemies(ArrayList<Monster> allMobs) {
        ArrayList<Monster> enemies = new ArrayList<>();
        for (Monster enemy : allMobs) {
            if (enemy.team != this.team && enemy.isAlive && canTouchEnemy(enemy)) enemies.add(enemy);
        }
        if(enemies.size() > 1)
            return enemies.subList(0,1);
        else
        return enemies;
    }

    private Monster detectCloserEnemy(ArrayList<Monster> allMonsters){
        int lowestDistance = Integer.MAX_VALUE;
        Monster closestEnemy = null;
        for(Monster enemy : allMonsters) {
            if(enemy.team == this.team || !enemy.isAlive) continue;
            double dx = enemy.x - this.x;
            double dy = enemy.y - this.y;
            double dz = enemy.z - this.z;
            double distance = Math.sqrt(dx*dx + dy*dy + dz*dz);
            if(distance < lowestDistance){ closestEnemy = enemy; }
        }
        return closestEnemy;
    }

    public double[] getMoveToPerform(ArrayList<Monster> allMonsters) {

        Monster closestEnemy = detectCloserEnemy(allMonsters);
        double xDest = closestEnemy.x;
        double yDest = closestEnemy.y;
        double zDest = closestEnemy.z;

        double deltaX = xDest - this.x;
        double deltaY = yDest - this.y;
        double deltaZ = zDest - this.z;
        double goalDistance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        if (goalDistance > speed) // The enemy is too far
        {
            double ratio = speed / goalDistance;
            double xMove = ratio * deltaX;
            double yMove = ratio * deltaY;
            double zMove = ratio * deltaZ;
            double newX = this.x + xMove;
            double newY = this.y + yMove;
            double newZ = 0.0;
            return new double[]{newX, newY, newZ};
        } else // The enemy can be reached in one move
        {
            return new double[]{xDest - 1, yDest - 1, 0};
        }

    }


    public void move(double[] newPosition) {
        this.x = newPosition[0];
        this.y = newPosition[1];
        this.z = newPosition[2];
    }

    public boolean canTouchEnemy(Monster enemy) {
        double x = Math.abs(this.x - enemy.x);
        double y = Math.abs(this.y - enemy.y);
        double z = Math.abs(this.z - enemy.z);
        double distance = Math.sqrt(x * x + y * y + z * z);
        if (distance <= this.range) {
                return true;
        }

        return false;

    }

    public int damage() {

        return this.damage + this.generateRandom(1,8);
    }

    public void isAttacked(int damage) {

        if(damage >= this.hp) hp = 0;
        else this.hp -= damage;
        if(this.hp <= 0){
            this.isAlive = false;
        }
    }
}
