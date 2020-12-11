public class Tile {
    private int x;
    private int y;
    private String tileType;
    private boolean dug;
    private int diggingLevel;
    private Part part;

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

    public String getTileType() {
        return tileType;
    }

    public void setTileType(String tileType) {
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
}
