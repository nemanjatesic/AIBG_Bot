import com.google.gson.Gson;

import java.util.*;

// -2 -2 dig
// -3 -3 collect

public class Game {
    private int id;
    private Map map;
    private Player winner;
    private int turn;
    private int nextPlayer;
    private Player nextPlayerObject;
    private Player otherPlayerObject;
    private TradeCenter tradeCenter;
    private GameState gameState;
    private char direction;
    private char nextDirection;
    private int movesBeforeChange;
    private List<Pair> movesSequence;
    private boolean[][] visDig;
    private boolean flagDig;
    private boolean flagGoShop;
    private boolean flagActuallyGoing;

    public void play(Gson gson)
    {
        try {
            String response;
            int counter = 0;
            while (true)
            {
                printMatrix();
//            if (gameState == GameState.SEARCHING)
//            {
//                response = search();
//            }
//            if (counter % 2 == 0)
//                response = MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, "s");
//            else
//                response = MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, "d");
//            counter++;
                String searchStr = magic();
                if (searchStr.equals("-2"))
                {
                    sellTotems(gson);
                    flagDig = false;
                    flagGoShop = false;
                    flagActuallyGoing = false;
                    movesSequence.clear();
                    continue;
                }
                response = MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, searchStr);
                Game game = gson.fromJson(response, Game.class);
                updateGame(game);
            }
        }catch (Exception e) {
            Random random = new Random();
            int ran = random.nextInt(4);
            String[] move = {"w", "a", "s", "d"};
            try {
                MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, move[ran]);
            }catch (Exception ignored) {
            }
            play(gson);
        }
    }

    private void sellTotems(Gson gson) throws Exception
    {
        String response = "";
        for (String s : sellActions()) {
            response = MyHttp.sendAction(Constants.PLAYER_ID, Constants.GAME_ID, s);
            Game game = gson.fromJson(response, Game.class);
            updateGame(game);
        }
    }

    private List<String> sellActions()
    {
        List<String> list = new ArrayList<>();
        HashMap<TotemType, Integer> hashMap = new HashMap<>();
        for (TotemType tt : TotemType.values()) {
            int count = 0;
            for (Part part : nextPlayerObject.getParts()) {
                if(part.getTotemType() == tt) {
                    count++;
                }
            }
            hashMap.put(tt, count);
        }
        int diff = 0;
        for (TotemType tt : hashMap.keySet()) {
             if (hashMap.get(tt) > 0)
             {
                 diff++;
             }
        }
        if (diff == 2 && hashMap.get(TotemType.NEUTRAL) == 1)
        {
            list.add("sellTotem");
            return list;
        }
        HashMap<TotemType, Integer> hashMapCombined = new HashMap<>();
        for (TotemType tt : TotemType.values())
        {
            hashMapCombined.put(tt, 0);
        }
        for (Part pPlayer : nextPlayerObject.getParts())
        {
            hashMapCombined.put(pPlayer.getTotemType(), hashMapCombined.get(pPlayer.getTotemType()) + 1);
        }
        for (Part pShop : tradeCenter.getPartsTC())
        {
            hashMapCombined.put(pShop.getTotemType(), hashMapCombined.get(pShop.getTotemType()) + 1);
        }

        TotemType savedType = null;
        for (TotemType totemType : TotemType.values())
        {
            if (hashMapCombined.get(totemType) == 2 && totemType != TotemType.NEUTRAL)
            {
                savedType = totemType;
                break;
            }
        }

        if (hashMapCombined.get(TotemType.NEUTRAL) == 0 || savedType == null)
        {
            for (Part pPlayer : nextPlayerObject.getParts())
            {
                list.add("sellPart-" + pPlayer.getId());
            }
            return list;
        }
        if (hashMap.get(TotemType.NEUTRAL) == 0)
        {
            for (Part pPlayer : nextPlayerObject.getParts())
            {
                if (pPlayer.getTotemType() != savedType && pPlayer.getTotemType() != TotemType.NEUTRAL)
                {
                    String s = "trade-" + pPlayer.getId() + "-";
                    for (Part p : tradeCenter.getPartsTC())
                    {
                        if (p.getTotemType() == TotemType.NEUTRAL)
                        {
                            s += p.getId();
                            list.add(s);
                            break;
                        }
                    }
                }
            }
        }
        for (Part pPlayer : nextPlayerObject.getParts())
        {
            if (pPlayer.getTotemType() != savedType && (pPlayer.getTotemType() != TotemType.NEUTRAL || hashMap.get(TotemType.NEUTRAL) > 1))
            {
                if (pPlayer.getTotemType() == TotemType.NEUTRAL)
                {
                    hashMap.put(TotemType.NEUTRAL, hashMap.get(TotemType.NEUTRAL) - 1);
                }
                String s = "trade-" + pPlayer.getId() + "-";
                for (Part p : tradeCenter.getPartsTC())
                {
                    if (p.getTotemType() == savedType)
                    {
                        s += p.getId();
                        list.add(s);
                        break;
                    }
                }
            }
        }
        return list;
    }

    private boolean searchDig()
    {
        for (int i = 0; i < 25; i++)
        {
            for (int j = 0 ; j < 25; j++)
            {
                if (map.getTilesMap()[i][j] != null && map.getTilesMap()[i][j].getTileType() == TileType.DIGTILE && !visDig[i][j])
                {
                    return true;
                }
            }
        }
        return false;
    }

    private void goDig()
    {
        Queue<Pair> q = new LinkedList<>();
        int korI = nextPlayerObject.getY(), korJ = nextPlayerObject.getX();
        int movI[] = {0,0,1,-1};
        int movJ[] = {1,-1,0,0};
        int dis[][] = new int[25][25];
        Pair movesMat[][] = new Pair[25][25];
        int saveI = -1, saveJ = -1;
        for (int i = 0; i < 25; i++)
        {
            for (int j = 0; j < 25; j++)
            {
                dis[i][j] = -1;
            }
        }
        dis[korI][korJ] = 0;
        movesMat[korI][korJ] = new Pair(-1, -1);
        if (otherPlayerObject != null && otherPlayerObject.getId() != -1)
        {
            dis[otherPlayerObject.getY()][otherPlayerObject.getX()] = 2000;
        }
        q.add(new Pair(korI, korJ));
        while (!q.isEmpty())
        {
            int i = q.peek().first, j = q.peek().second;
            q.poll();
            for (int rr = 0; rr < 4; rr++)
            {
                int ii = i + movI[rr], jj = j + movJ[rr];
                if (ii >= 0 && ii < 25  && jj >= 0 && jj < 25 && dis[ii][jj] == -1 && map.getTilesMap()[ii][jj] != null)
                {
                    if(map.getTilesMap()[ii][jj].getTileType() == TileType.DIGTILE && !visDig[ii][jj])
                    {
                        movesMat[ii][jj] = new Pair(i, j);
                        dis[ii][jj] = dis[i][j] + 1;
                        saveI = ii;
                        saveJ = jj;
                        q.clear();
                        break;
                    }
                    if(map.getTilesMap()[ii][jj].getTileType() != TileType.BLOCKTILE && !isTrap(ii, jj))
                    {
                        movesMat[ii][jj] = new Pair(i, j);
                        dis[ii][jj] = dis[i][j] + 1;
                        q.add(new Pair(ii,jj));
                    }
                }
            }
        }
        //TODO optimize for traps
        int backI = saveI, backJ = saveJ;
        while (backI != -1 && backJ != -1)
        {
            movesSequence.add(new Pair(backI, backJ));
            int tmpI = backI, tmpJ = backJ;
            backI = movesMat[tmpI][tmpJ].first;
            backJ = movesMat[tmpI][tmpJ].second;
        }
        Collections.reverse(movesSequence);
        if (!movesSequence.isEmpty())
        {
            movesSequence.remove(0);
        }
        if (!movesSequence.isEmpty())
        {
            for (int i = 0; i < nextPlayerObject.getDiggingTime(); i++)
            {
                movesSequence.add(new Pair(-2,-2));
            }
            movesSequence.add(new Pair(-3,-3));
        }
        return;
    }

    private boolean goShop()
    {
        Queue<Pair> q = new LinkedList<>();
        int korI = nextPlayerObject.getY(), korJ = nextPlayerObject.getX();
        int movI[] = {0,0,1,-1};
        int movJ[] = {1,-1,0,0};
        int dis[][] = new int[25][25];
        Pair movesMat[][] = new Pair[25][25];
        int saveI = -1, saveJ = -1;
        for (int i = 0; i < 25; i++)
        {
            for (int j = 0; j < 25; j++)
            {
                dis[i][j] = -1;
            }
        }
        dis[korI][korJ] = 0;
        movesMat[korI][korJ] = new Pair(-1, -1);
        if (otherPlayerObject != null && otherPlayerObject.getId() != -1)
        {
            dis[otherPlayerObject.getY()][otherPlayerObject.getX()] = 2000;
        }
        q.add(new Pair(korI, korJ));
        while (!q.isEmpty())
        {
            int i = q.peek().first, j = q.peek().second;
            q.poll();
            for (int rr = 0; rr < 4; rr++)
            {
                int ii = i + movI[rr], jj = j + movJ[rr];
                if (ii >= 0 && ii < 25  && jj >= 0 && jj < 25 && dis[ii][jj] == -1 && map.getTilesMap()[ii][jj] != null)
                {
                    if(map.getTilesMap()[ii][jj].getTileType() != TileType.BLOCKTILE && ii>9 && ii<15 && jj>9 && jj<15)
                    {
                        movesMat[ii][jj] = new Pair(i, j);
                        dis[ii][jj] = dis[i][j] + 1;
                        saveI = ii;
                        saveJ = jj;
                        q.clear();
                        break;
                    }
                    else if(map.getTilesMap()[ii][jj].getTileType() != TileType.BLOCKTILE && !isTrap(ii, jj))
                    {
                        movesMat[ii][jj] = new Pair(i, j);
                        dis[ii][jj] = dis[i][j] + 1;
                        q.add(new Pair(ii,jj));
                    }
                }
            }
        }
        //TODO optimize for traps
        int backI = saveI, backJ = saveJ;
        if (saveI == -1 && saveJ == -1)
        {
            return false;
        }
        while (backI != -1 && backJ != -1)
        {
            movesSequence.add(new Pair(backI, backJ));
            int tmpI = backI, tmpJ = backJ;
            backI = movesMat[tmpI][tmpJ].first;
            backJ = movesMat[tmpI][tmpJ].second;
        }
        Collections.reverse(movesSequence);
        if (!movesSequence.isEmpty())
        {
            movesSequence.remove(0);
        }
        return true;
    }

    private String magic()
    {
        String res = "-1";
        Queue<Pair> q = new LinkedList<>();
        int korI = nextPlayerObject.getY(), korJ = nextPlayerObject.getX();
        if (korI > 9 && korI < 15 && korJ > 9 && korJ < 15 && nextPlayerObject.getParts().size() == 3)
        {
            return "-2";
        }
        if (flagGoShop && !flagActuallyGoing)
        {
            boolean tmp = goShop();
            if (tmp)
            {
                flagActuallyGoing = true;
            }
        }
        if(!flagGoShop && !flagDig && searchDig())
        {
            flagDig = true;
            movesSequence.clear();
            goDig();
        }
        if (!movesSequence.isEmpty())
        {
            boolean flagReset = false;
            if(movesSequence.get(0).first == korI && movesSequence.get(0).second == korJ + 1)
            {
                if (otherPlayerObject != null && otherPlayerObject.getId() != -1 && otherPlayerObject.getX() == korJ + 1 && otherPlayerObject.getY() == korI)
                {
                    flagReset = true;
                }
                res = "d";
            }
            else if(movesSequence.get(0).first == korI + 1 && movesSequence.get(0).second == korJ)
            {
                if (otherPlayerObject != null && otherPlayerObject.getId() != -1 && otherPlayerObject.getX() == korJ && otherPlayerObject.getY() == korI + 1)
                {
                    flagReset = true;
                }
                res = "s";
            }
            else if(movesSequence.get(0).first == korI && movesSequence.get(0).second == korJ - 1)
            {
                if (otherPlayerObject != null && otherPlayerObject.getId() != -1 && otherPlayerObject.getX() == korJ - 1 && otherPlayerObject.getY() == korI)
                {
                    flagReset = true;
                }
                res = "a";
            }
            else if(movesSequence.get(0).first == korI - 1 && movesSequence.get(0).second == korJ)
            {
                if (otherPlayerObject != null && otherPlayerObject.getId() != -1 && otherPlayerObject.getX() == korJ && otherPlayerObject.getY() == korI - 1)
                {
                    flagReset = true;
                }
                res = "w";
            }
            else if(movesSequence.get(0).first == -2 && movesSequence.get(0).second == -2)
            {
                if(map.getTilesMap()[korI][korJ].isDug())
                {
                    movesSequence.clear();
                    flagDig = false;
                    return magic();
                }
                res = "dig";
            }
            else if(movesSequence.get(0).first == -3 && movesSequence.get(0).second == -3)
            {
                res = "collect";
                if (this.getMap().getTilesMap()[korI][korJ].getTileType() == TileType.DIGTILE) {
                    visDig[korI][korJ] = true;
                    if(nextPlayerObject.getParts().size() == 2)
                    {
                        flagGoShop = true;
                    }
                }
            }
            if(flagReset)
            {
                movesSequence.clear();
                flagDig = false;
                flagActuallyGoing = false;
                return magic();
            }
            movesSequence.remove(0);
            if(movesSequence.isEmpty())
            {
                flagDig = false;
            }
            return res;
        }
        flagDig = false;
        int movI[] = {0,0,1,-1};
        int movJ[] = {1,-1,0,0};
        int dis[][] = new int[25][25];
        Pair movesMat[][] = new Pair[25][25];
        int saveI = -1, saveJ = -1;
        for (int i = 0; i < 25; i++)
        {
            for (int j = 0; j < 25; j++)
            {
                dis[i][j] = -1;
            }
        }
        dis[korI][korJ] = 0;
        movesMat[korI][korJ] = new Pair(-1, -1);
        if (otherPlayerObject != null && otherPlayerObject.getId() != -1)
        {
            dis[otherPlayerObject.getY()][otherPlayerObject.getX()] = 2000;
        }
        q.add(new Pair(korI, korJ));
        while (!q.isEmpty())
        {
            int i = q.peek().first, j = q.peek().second;
            q.poll();
            for (int rr = 0; rr < 4; rr++)
            {
                int ii = i + movI[rr], jj = j + movJ[rr];
                if (ii >= 0 && ii < 25  && jj >= 0 && jj < 25 && dis[ii][jj] == -1)
                {
                    if(map.getTilesMap()[ii][jj] == null)
                    {
                        movesMat[ii][jj] = new Pair(i, j);
                        dis[ii][jj] = dis[i][j] + 1;
                        saveI = ii;
                        saveJ = jj;
                        q.clear();
                        break;
                    }
                    if(map.getTilesMap()[ii][jj].getTileType() != TileType.BLOCKTILE && !isTrap(ii, jj))
                    {
                        movesMat[ii][jj] = new Pair(i, j);
                        dis[ii][jj] = dis[i][j] + 1;
                        q.add(new Pair(ii,jj));
                    }
                }
            }
        }
        //TODO optimize for traps
        int backI = saveI, backJ = saveJ;
        while (backI != -1 && backJ != -1)
        {
            movesSequence.add(new Pair(backI, backJ));
            int tmpI = backI, tmpJ = backJ;
            backI = movesMat[tmpI][tmpJ].first;
            backJ = movesMat[tmpI][tmpJ].second;
        }
        Collections.reverse(movesSequence);
        if (!movesSequence.isEmpty())
        {
            movesSequence.remove(0);
        }
        if (!movesSequence.isEmpty())
        {
            if(movesSequence.get(0).first == korI && movesSequence.get(0).second == korJ + 1)
            {
                res = "d";
            }
            else if(movesSequence.get(0).first == korI + 1 && movesSequence.get(0).second == korJ)
            {
                res = "s";
            }
            else if(movesSequence.get(0).first == korI && movesSequence.get(0).second == korJ - 1)
            {
                res = "a";
            }
            else if(movesSequence.get(0).first == korI - 1 && movesSequence.get(0).second == korJ)
            {
                res = "w";
            }
            movesSequence.remove(0);
            return res;
        }
        return res;
    }

    private String handleThreePartsInBag()
    {
        return "";
    }


    public void init(Gson gson) throws Exception
    {
        movesSequence = new ArrayList<>();
        visDig = new boolean[25][25];
        for (int i = 0; i < 25; i++)
        {
            for (int j = 0; j < 25; j++)
                visDig[i][j] = false;
        }
        flagDig = false;
        flagGoShop = false;
        flagActuallyGoing = false;
        Player player = null;
        if (nextPlayerObject.getId() == Constants.PLAYER_ID)
            player = nextPlayerObject;
        else
            player = otherPlayerObject;
        if (player.getInitX() == 0)
        {
            direction = 'd';
            nextDirection = 's';
        }
        else
        {
            direction = 'a';
            nextDirection = 'w';
        }
        movesBeforeChange = 3;
        play(gson);
    }

    private void printMatrix()
    {
        for (int i = 0; i < 25; i++)
        {
            for (int j = 0; j < 25; j++)
            {
                if (map.getTilesMap()[i][j] != null)
                {
                    System.out.print("x ");
                }
                else
                {
                    System.out.print("o ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isTrap(int i, int j)
    {
        return this.getMap().getTilesMap()[i][j].getTrap() != null && this.getMap().getTilesMap()[i][j].getTrap().getTrapType() != null;
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
