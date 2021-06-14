public class Space {
    private int numMarbles;
    private Space nextSpace;
    private boolean isStore;
    private Player owner;

    public Space() {
        this.numMarbles = 4;
        this.nextSpace = null;
        this.isStore = false;
    }

    public Space(int numMarbles) {
        this.numMarbles = numMarbles;
        this.nextSpace = null;
        this.isStore = false;
    }

    public void setOwner(Player o) {
        this.owner = o;
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getMarbles() {
        return this.numMarbles;
    }

    public void setNextSpace(Space s) {
        this.nextSpace = s;
    }

    public Space getNextSpace() {
        return this.nextSpace;
    }

    public boolean getStore() {
        return this.isStore;
    }

    public void setStoreStatus(boolean status) {
        this.isStore = status;
    }

    public int emptyCup() {
        int marbels = this.numMarbles;
        this.numMarbles = 0;
        return marbels;
    }

    public void depositMarble() {
        this.numMarbles++;
    }

    public void depositMarbles(int marbles) {
        this.numMarbles += marbles;
    }

    public String toString() {
        return String.format("This space has %d marbles and it's store status is %b", this.numMarbles, this.isStore);
    }
}