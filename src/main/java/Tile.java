public class Tile {
    private int x;
    private int y;
    private TileType tileType;
    private boolean dug;
    private int diggingLevel;
    private Part part;
    private Trap trap;

    public Trap getTrap() {
        return trap;
    }

    public void setTrap(Trap trap) {
        this.trap = trap;
    }

    public Tile() {
        x = -1;
        y = -1;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
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

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }

    public boolean isDug() {
        return dug;
    }

    public void setDug(boolean dug) {
        this.dug = dug;
    }

    public int getDiggingLevel() {
        return diggingLevel;
    }

    public void setDiggingLevel(int diggingLevel) {
        this.diggingLevel = diggingLevel;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "x=" + x +
                ", y=" + y +
                ", tileType=" + tileType +
                ", dug=" + dug +
                ", diggingLevel=" + diggingLevel +
                ", part=" + part +
                '}';
    }
}
