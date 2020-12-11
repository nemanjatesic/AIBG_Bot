import java.util.List;

public class Map {
    private int width;
    private int height;
    private List<List<Tile>> tiles;
    private Tile[][] tilesMap;
    private boolean loaded = false;

    public Map() {
    }

    private void lazyLoad() {
        if (!loaded) {
            loaded = true;
            tilesMap = new Tile[height][width];
            for (int i = 0 ; i < height ; i++) {
                for (int j = 0 ; j < width ; j++) {
                    Tile tile = tiles.get(i).get(j);
                    if (tile.getTileType() == null) {
                        tilesMap[i][j] = null;
                    } else {
                        tilesMap[i][j] = tile;
                    }
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    public void setTiles(List<List<Tile>> tiles) {
        this.tiles = tiles;
    }

    public void setTile(int i, int j, Tile tile) {
        tilesMap[i][j] = tile;
    }

    public Tile[][] getTilesMap() {
        lazyLoad();
        return tilesMap;
    }

    //    public void setTile(int i, int j, Tile tile) {
//        tiles.get(i).get(j)
//    }

}
