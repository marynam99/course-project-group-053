import java.text.MessageFormat;
import java.util.HashMap;

public class GameBoard {
    // Defines the game board used for Nine Men Morris, which holds objects representing our tokens
    private final HashMap<String, String[]> gameBoard;

    // keeps track of how many empty slots are currently on the gameboard
    private int gameBoardCapacity;

    // regex pattern for empty slots on board
    public static String EMPTY_SLOT_PATTERN = "[ABC][1-8]";

    // array of all possible gameboard coordinates
    public static String[] GAMEBOARD_COORDINATES = {"A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8",
                                                    "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
                                                    "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8"};

    /**
     * Initializes an empty Nine Men Morris gameboard.
     *
     * gameBoard is a hashmap, mapping coordinates to each position in each box of the gameboard
     *
     */
    public GameBoard() {
        // set up hash table, with keys as boxes (A = outer box, B = middle box, C = inner box) and values as
        // arrays representing slots in each box
        HashMap<String, String[]> gbHash = new HashMap<>();
        gbHash.put("A", new String[8]);  // A = outer box
        gbHash.put("B", new String[8]);  // B = middle box
        gbHash.put("C", new String[8]);  // C = inner box

        // populate each array for key in hash table, with items [LETTER][1-8]
        for (String key: gbHash.keySet()) {
            String[] boxArr = gbHash.get(key);
            for (int i = 1; i <= boxArr.length; i++) {
                // -1 offset for boxArr indexing, to account for array indexing starting from zero
                boxArr[i - 1] = key + i;
            }
        }

        gameBoard = gbHash;
        gameBoardCapacity = 24;
    }

    @Override
    public String toString() {
        // create array of string values of tokens in each box in gameBoard
        String[] gameBoardTokens = new String[24];

        // populate array with all tokens in gameBoard
        int i = 0;
        for (String key: gameBoard.keySet()) {
            String[] boxArr = gameBoard.get(key);
            for (String boxItem: boxArr) {
                gameBoardTokens[i] = boxItem;
                i++;
            }
        }

        // NOTE: IntelliJ complains about the style of the string below, but implementing IntelliJ's suggestions breaks
        // the code. I have kept this string as is.
        // - Jason
        return MessageFormat.format(
                "{0}----------------------{1}----------------------{2}\n"+
                        "|                                                |\n"+
                "|                                                |\n"+
                "|     {8}----------------{9}----------------{10}     |\n"+
                "|     |                 |                  |     |\n"+
                "|     |                 |                  |     |\n"+
                "|     |     {16}----------{17}----------{18}     |     |\n"+
                "|     |     |                        |     |     |\n"+
        "{3}    {11}    {19}                      {20}    {12}     {4}\n"+
                "|     |     |                        |     |     |\n"+
                "|     |     {21}----------{22}----------{23}     |     |\n"+
                "|     |                 |                  |     |\n"+
                "|     |                 |                  |     |\n"+
                "|     {13}----------------{14}----------------{15}     |\n"+
                "|                                                |\n"+
                "|                                                |\n"+
        "{5}----------------------{6}----------------------{7}\n",
                (Object[]) gameBoardTokens);
    }

    private String[] splitCoordinates(String targetPosition) {
        return targetPosition.split("(?<=\\D)(?=\\d+\\b)");
    }
    private void decreaseCapacity() { gameBoardCapacity++; }
    private void increaseCapacity() { gameBoardCapacity--; }

    /**
     * Place a Player's token in a specified box and box position in GameBoard
     *
     * @param token string representing the colored token the player will place on the GameBoard
     * @param targetPosition string representing coordinates in gameBoard (ex: A8, C4) to place token
     */
    public void setToken(String token, String targetPosition) {
        // split targetPosition string into letter and integer index within box
        String[] coordinates = splitCoordinates(targetPosition);

        // get array of box elements corresponding to box of specified letter
        // then, set token to given numerical index in box
        // offset index within box by 1, to account for indexing starting from zero
        gameBoard.get(coordinates[0])[Integer.parseInt(coordinates[1]) - 1] = token;
        decreaseCapacity();
    }

    /**
     * Remove a Player's token from a specified box and box position in GameBoard
     *
     * @param targetPosition string representing coordinates in gameBoard (ex: A8, C4) to place token
     *
     */
    public void removeToken(String targetPosition) {
        // split targetPosition string into letter and integer index within box
        String[] coordinates = splitCoordinates(targetPosition);

        // get array of box elements corresponding to box of specified letter
        // then, set item in box back to default coordinate values (i.e: the slot is now free)
        // offset index within box by 1, to account for indexing starting from zero
        gameBoard.get(coordinates[0])[Integer.parseInt(coordinates[1]) - 1] = targetPosition;
        increaseCapacity();
    }

    /**
     * Retrieve the string of the player token placed in a particular box, at a particular position in GameBoard.
     * Return EMPTY_GAMEBOARD_SLOT if no player token is placed at specified location
     *
     * @param targetPosition string representing coordinates in gameBoard (ex: A8, C4) to place token
     *
     */
    public String getTokenAtPosition(String targetPosition) {
        // split targetPosition string into letter and integer index within box
        String[] coordinates = splitCoordinates(targetPosition);

        return gameBoard.get(coordinates[0])[Integer.parseInt(coordinates[1]) - 1];
    }

}
