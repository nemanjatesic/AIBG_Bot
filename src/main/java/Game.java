import com.google.gson.Gson;

public class Game {
    private int id;
    private Map map;
    private Player winner;
    private int turn;
    private int nextPlayer;
    private Player nextPlayerObject;
    private Player otherPlayerObject;
    private TradeCenter tradeCenter;

    public void play(Gson gson) throws Exception{
        String response;
        int counter = 0;
        while (true) {


            if (counter % 2 == 0)
                response = MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, "s");
            else
                response = MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, "d");
            counter++;
            Game game = gson.fromJson(response, Game.class);
            updateGame(game);
        }
    }

    private void printTraps() {
        for (int i = 0 ; i < this.getMap().getHeight() ; i++) {
            for (int j = 0 ; j < this.getMap().getWidth() ; j++) {
                if (this.getMap().getTilesMap()[i][j] != null && this.getMap().getTilesMap()[i][j].getTrap() != null && this.getMap().getTilesMap()[i][j].getTrap().getTrapType() != null) {
                    System.out.print("x ");
                } else {
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void updateGame(Game game) {
        for (int i = 0 ; i < game.getMap().getHeight() ; i++) {
            for (int j = 0 ; j < game.getMap().getWidth() ; j++) {
                Tile currTile = this.getMap().getTilesMap()[i][j];
                Tile newTile = game.getMap().getTilesMap()[i][j];
                if (newTile == null) {
                    continue;
                }
                if (currTile == null) {
                    this.getMap().setTile(i, j, newTile);
                    continue;
                }
                if (newTile.getTrap() != null && newTile.getTrap().getTrapType() == null) {
                    newTile.setTrap(currTile.getTrap());
                }
                this.getMap().setTile(i, j, newTile);
            }
        }
        this.turn = game.turn;
        this.nextPlayer = game.nextPlayer;
        if (game.tradeCenter != null) {
            this.tradeCenter = game.tradeCenter;
        }
        if (game.nextPlayerObject != null) {
            this.nextPlayerObject = game.nextPlayerObject;
        }
        if (game.otherPlayerObject != null) {
            this.otherPlayerObject = game.otherPlayerObject;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(int nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public Player getNextPlayerObject() {
        return nextPlayerObject;
    }

    public void setNextPlayerObject(Player nextPlayerObject) {
        this.nextPlayerObject = nextPlayerObject;
    }

    public Player getOtherPlayerObject() {
        return otherPlayerObject;
    }

    public void setOtherPlayerObject(Player otherPlayerObject) {
        this.otherPlayerObject = otherPlayerObject;
    }

    public TradeCenter getTradeCenter() {
        return tradeCenter;
    }

    public void setTradeCenter(TradeCenter tradeCenter) {
        this.tradeCenter = tradeCenter;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", map=" + map +
                ", winner=" + winner +
                ", turn=" + turn +
                ", nextPlayer=" + nextPlayer +
                ", \nnextPlayerObject=" + nextPlayerObject +
                ", \notherPlayerObject=" + otherPlayerObject +
                ", \ntradeCenter=" + tradeCenter +
                '}';
    }
}
