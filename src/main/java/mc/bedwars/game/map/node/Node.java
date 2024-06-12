package mc.bedwars.game.map.node;

public abstract class Node {
    private int x, y;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Node(int x, int y){
        this.x = x;
        this.y = y;
    }

    public abstract String getType();
}