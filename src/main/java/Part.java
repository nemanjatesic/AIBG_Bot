public class Part {
    private int id;
    private TotemType totemType;
    private boolean visible;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TotemType getTotemType() {
        return totemType;
    }

    public void setTotemType(TotemType totemType) {
        this.totemType = totemType;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Part{" +
                "id=" + id +
                ", totemType=" + totemType +
                ", visible=" + visible +
                '}';
    }
}
