import java.util.List;

public class Player {
    private int id;
    private int x;
    private int y;
    private int initX;
    private int initY;
    private int health;
    private int money;
    private int illegalMoves;
    private int numOfPotions;
    private boolean scorpionPoison;
    private boolean trappedInQuickSand;
    private int durationScorpionPoison;
    private int durationQuickSand;
    private int diggingTime;
    private List<Part> parts;
    private int numOfTraps;
    private boolean paralysed;
    private boolean trapsRevealed;
    private int eyesight;
    private boolean burning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getInitX() {
        return initX;
    }

    public void setInitX(int initX) {
        this.initX = initX;
    }

    public int getInitY() {
        return initY;
    }

    public void setInitY(int initY) {
        this.initY = initY;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getIllegalMoves() {
        return illegalMoves;
    }

    public void setIllegalMoves(int illegalMoves) {
        this.illegalMoves = illegalMoves;
    }

    public int getNumOfPotions() {
        return numOfPotions;
    }

    public void setNumOfPotions(int numOfPotions) {
        this.numOfPotions = numOfPotions;
    }

    public boolean isScorpionPoison() {
        return scorpionPoison;
    }

    public void setScorpionPoison(boolean scorpionPoison) {
        this.scorpionPoison = scorpionPoison;
    }

    public boolean isTrappedInQuickSand() {
        return trappedInQuickSand;
    }

    public void setTrappedInQuickSand(boolean trappedInQuickSand) {
        this.trappedInQuickSand = trappedInQuickSand;
    }

    public int getDurationScorpionPoison() {
        return durationScorpionPoison;
    }

    public void setDurationScorpionPoison(int durationScorpionPoison) {
        this.durationScorpionPoison = durationScorpionPoison;
    }

    public int getDurationQuickSand() {
        return durationQuickSand;
    }

    public void setDurationQuickSand(int durationQuickSand) {
        this.durationQuickSand = durationQuickSand;
    }

    public int getDiggingTime() {
        return diggingTime;
    }

    public void setDiggingTime(int diggingTime) {
        this.diggingTime = diggingTime;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public int getNumOfTraps() {
        return numOfTraps;
    }

    public void setNumOfTraps(int numOfTraps) {
        this.numOfTraps = numOfTraps;
    }

    public boolean isParalysed() {
        return paralysed;
    }

    public void setParalysed(boolean paralysed) {
        this.paralysed = paralysed;
    }

    public boolean isTrapsRevealed() {
        return trapsRevealed;
    }

    public void setTrapsRevealed(boolean trapsRevealed) {
        this.trapsRevealed = trapsRevealed;
    }

    public int getEyesight() {
        return eyesight;
    }

    public void setEyesight(int eyesight) {
        this.eyesight = eyesight;
    }

    public boolean isBurning() {
        return burning;
    }

    public void setBurning(boolean burning) {
        this.burning = burning;
    }
}
