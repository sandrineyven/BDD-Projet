package model;

import scala.Serializable;

public abstract class Monster implements Serializable {

    long id;
    String name;
    int x = 0;
    int y = 0;
    int z = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString()
    {
        return "[" + this.id + "] " + this.name + " : " + " - ("
                + this.x + " ; " + this.y + " ; " + this.z + ")";
    }
}
