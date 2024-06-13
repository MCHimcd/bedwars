package mc.bedwars.game.map.node.island;

import mc.bedwars.game.map.node.Node;

public abstract class Island extends Node {
    private final int x, y;


    public Island(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

}