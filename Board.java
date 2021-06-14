import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner; 

public class Board {
    private ArrayList<Space> spaces = new ArrayList<Space>(12);
    private Player currentPlayer = Player.PLAYERONE;
    private Space playerOneStore, playerTwoStore;

    public Board() {
        this(Arrays.asList(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0));
    }

    public Board(List<Integer> startingMarbles) {
        for (int i = 0; i < 14; i++) {
            spaces.add(new Space(startingMarbles.get(i)));
            
            // Set up linked list for all except first and last
            if (i > 0 && i < 13) spaces.get(i-1).setNextSpace(spaces.get(i));
            
            // Finish setting up linked list for first and last
            else if (i > 1){
                spaces.get(12).setNextSpace(spaces.get(13));
                spaces.get(13).setNextSpace(spaces.get(0));
            }
            
            // Declare which spaces are stores
            if (i == 6 || i == 13) {
                spaces.get(i).setStoreStatus(true);
                spaces.get(i).emptyCup();
                if (i == 6) playerOneStore = spaces.get(i);
                else playerTwoStore = spaces.get(i);
            }
            
            // Declare ownership
            if (i < 7) spaces.get(i).setOwner(Player.PLAYERONE);
            else spaces.get(i).setOwner(Player.PLAYERTWO);
        }
    }

    public void printBoard() {
        System.out.println(" _____________________________________________");
        System.out.println("|   | |   | |   | |   | |   | |   | |   | |   |");
        System.out.println(String.format("|   | | %2d| | %2d| | %2d| | %2d| | %2d| | %2d| |   |", spaces.get(12).getMarbles(), spaces.get(11).getMarbles(), spaces.get(10).getMarbles(), spaces.get(9).getMarbles(),spaces.get(8).getMarbles(), spaces.get(7).getMarbles()));
        System.out.println("|   | |___| |___| |___| |___| |___| |___| |   |");
        System.out.println("|   |                                     |   |");
        System.out.println(String.format("| %2d|                                     | %2d|", spaces.get(13).getMarbles(), spaces.get(6).getMarbles()));
        System.out.println("|   |  ___   ___   ___   ___   ___   ___  |   |");
        System.out.println("|   | |   | |   | |   | |   | |   | |   | |   |");
        System.out.println(String.format("|   | | %2d| | %2d| | %2d| | %2d| | %2d| | %2d| |   |", spaces.get(0).getMarbles(), spaces.get(1).getMarbles(), spaces.get(2).getMarbles(), spaces.get(3).getMarbles(),spaces.get(4).getMarbles(), spaces.get(5).getMarbles()));
        System.out.println("|___|_|___|_|___|_|___|_|___|_|___|_|___|_|___|");
        System.out.println();
    }

    public int getNumMarbels(Player o) {
        int total = 0;
        for (Space space : spaces) {
            if (space.getOwner() == o && !space.getStore()) total += space.getMarbles();
        }
        return total;
    }

    public Player getWinner() {
        Space playerOneStore = spaces.get(6);
        Space playerTwoStore = spaces.get(13);
        if (getNumMarbels(Player.PLAYERONE) == 0) {
            for (int i = 7; i < 12; i++) {
                playerOneStore.depositMarbles(spaces.get(i).emptyCup());
            }
            return playerOneStore.getMarbles() > playerTwoStore.getMarbles() ? Player.PLAYERONE : Player.PLAYERTWO;
        }
        if (getNumMarbels(Player.PLAYERTWO) == 0) {
            for (int i = 0; i < 6; i++) {
                playerTwoStore.depositMarbles(spaces.get(i).emptyCup());
            }
            return playerOneStore.getMarbles() > playerTwoStore.getMarbles() ? Player.PLAYERONE : Player.PLAYERTWO;
        }
        return null;
    }

    public boolean validate(int pickedCupIndex) {
        if (currentPlayer == Player.PLAYERONE) {
            if (pickedCupIndex < 0 || pickedCupIndex > 5) return false;
        }
        else {
            if (pickedCupIndex < 7 || pickedCupIndex > 12) return false;
        }
        if (spaces.get(pickedCupIndex).getMarbles() == 0) return false;
        return true;
    }

    public void endTurn(Space curSpace) {
        // Player one
        if (currentPlayer == Player.PLAYERONE) {
            // see if we landed in our own store, if so go again
            if (curSpace.getOwner() == Player.PLAYERONE && curSpace.getStore()) {
                currentPlayer = Player.PLAYERONE;
                return;
            }
            // see if we landed in an empty space then capture marbles across from us
            else if (curSpace.getMarbles() == 1) {
                Space oppositeSpace = spaces.get(12-spaces.indexOf(curSpace));
                if (oppositeSpace.getMarbles() > 0) {
                    playerOneStore.depositMarbles(oppositeSpace.emptyCup());
                    playerOneStore.depositMarbles(curSpace.emptyCup());                    
                }
            }
            // Other player goes.
            currentPlayer = Player.PLAYERTWO;
        }
        // Player two
        else {
            // see if we landed in our own store, if so go again
            if (curSpace.getOwner() == Player.PLAYERTWO && curSpace.getStore()) {
                currentPlayer = Player.PLAYERTWO;
                return;
            }
            // see if we landed in an empty space then capture marbles across from us. Other player goes.
            else if (curSpace.getMarbles() == 1) {
                Space oppositeSpace = spaces.get(12-spaces.indexOf(curSpace));
                if (oppositeSpace.getMarbles() > 0) {
                    playerTwoStore.depositMarbles(oppositeSpace.emptyCup());
                    playerTwoStore.depositMarbles(curSpace.emptyCup());                    
                }
            }
            // Other player goes.
            currentPlayer = Player.PLAYERONE;    
        }
    }

    public void startGame() {
        printBoard();
        Scanner s = new Scanner(System.in);
        while (true) {
            System.out.print(String.format("%s, pick a cup: ", currentPlayer == Player.PLAYERONE ? "Player one" : "Player two"));
            int pickedCupIndex = s.nextInt();
            boolean isValid = validate(pickedCupIndex);
            while (!isValid) {
                System.out.print(String.format("%s, please pick a valid cup: ", currentPlayer == Player.PLAYERONE ? "Player one" : "Player two"));
                pickedCupIndex = s.nextInt();
                isValid = validate(pickedCupIndex);
            }
            Space currentSpace = spaces.get(pickedCupIndex);
            int marbles = currentSpace.emptyCup();
            while (marbles > 0) {
                currentSpace.getNextSpace().depositMarble();
                currentSpace = currentSpace.getNextSpace();
                marbles--;
            }
            endTurn(currentSpace);
            printBoard();
            Player winner = getWinner();
            if (winner != null) {
                System.out.println(String.format("You win, %s!", winner));
                printBoard();
                break;
            }
        }
        s.close();
    }
}
