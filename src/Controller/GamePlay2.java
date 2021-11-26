package Controller;

import Entity.Player;
import Entity.Token;
import Exceptions.InvalidPositionException;
import Exceptions.InvalidRemovalException;
import Exceptions.LoadedSuccessfully;
import Exceptions.SavedSuccessfully;
import Gateways.data.GameSaveData;
import Gateways.data.GameState;
import UseCases.*;

import java.util.ArrayList;

public class GamePlay2 extends GamePlay{
    GameBoardPlacer placer;
    GameBoardRemover remover;
    public CheckMill checkMill;
    public GameBoardManipulator gameBoardManipulator;
    WinnerCalculator winnerCalculator;
    public boolean endOfP2;


    public GamePlay2(){
        placer = new GameBoardPlacer();
        remover = new GameBoardRemover();
        checkMill = new CheckMill();
        gameBoardManipulator = new GameBoardManipulator(placer, remover, checkMill);
        endOfP2 = false;
        winnerCalculator = new WinnerCalculator(this, player1, player2);
    }



    public String getWinner() {
        return winnerCalculator.who_won();
    }


    public ArrayList<String> getTokenCoordinates(String colour){
        ArrayList<String> tokenCoordinates = new ArrayList<>();
        ArrayList<String> gameboardKeys = gameBoardManipulator.getKeys();
        for(String key : gameboardKeys) {
            if (gameBoardManipulator.getCorrespondentValue(key) != null){
                if(gameBoardManipulator.getCorrespondentValue(key).equals(colour)){
                    tokenCoordinates.add(key);
                }
            }
        }
        return tokenCoordinates;
    }

    @Override
    public void move_token(int playerNum, String setTokenPosition) throws ArrayIndexOutOfBoundsException, NullPointerException {
        Player player;
        if (playerNum == 1) {
            player = player1;
        } else {
            player = player2;
        }

        while (true) {
            try {
                Token token = new Token(player.get_username(), player.get_tokencolour());

                //TODO: make a Token
                //InsertToken(token, position)
                gameBoardManipulator.placeToken(token, setTokenPosition);

                // reduce player 1's chips by 1
                player.dec_numchipsleft();
                // player 1 has successfully placed down a token, so break out of the while loop

                checkMill.checkMill(setTokenPosition, player.get_tokencolour(), gameBoardManipulator.getGameBoard());
                break;

            } catch (InvalidPositionException | ArrayIndexOutOfBoundsException | NullPointerException e) {
                System.out.println(e.getMessage());
                // skip the invalid token and ask for prompt again
            }
        }
        // Now check if the player1 has created a mill
        //gameBoardManager.checkHouse();
    }

    @Override
    public void updateEndOfPhase() {
        endOfP2 = player1.int_player_numchipsonboard == 0 & player2.int_player_numchipsleft == 0;
    }

    public String saveGame(){
        boolean saved_data = false;
        GameSaveData save_file = new GameSaveData(player1, player2, gameBoardManipulator.getGameBoard());
        try {
            GameState.save(save_file, "gamestate1.save");
            return "Game saved successfully";
        } catch (Exception e) {
            return "Couldn't save:" + e.getMessage();
        }
    }

    public String[] loadGame(){
        try {
            GameSaveData saveData = (GameSaveData) GameState.load("gamestate1.save");
            System.out.println(saveData.player1saved.get_username());
            System.out.println(saveData.player2saved.get_username());
            gameBoardManipulator.setGameBoard(saveData.savedGameboard);
            return new String[]{saveData.getSavedPlayerUsername(1), saveData.getSavedPlayerUsername(2)};
        } catch (Exception e) {
            return new String[]{"Couldn't load:" + e.getMessage()};
        }
    }

    /**
     * Removes token at position removeTokenPosition.
     *
     * @param playerNum indicates whether it is player1 or player2
     * @param removeTokenPosition position in the form of [A-C][1-8] of the token to be removed
     * @return empty string if token was removed; excpetion message otherwise.
     */
    public String remove_token(int playerNum, String removeTokenPosition) {
        Player player;
        if (playerNum == 1) {
            player = player1;
        } else {
            player = player2;
        }

        try {
            if (removeTokenPosition.equals("save")) {
                boolean saved_data = false;
                GameSaveData save_file = new GameSaveData(gameBoardManipulator.getGameBoard());
                try {
                    GameState.save(save_file, "gamestate1.save");
                    saved_data = true;
                } catch (Exception e) {
                    System.out.println("Couldn't save:" + e.getMessage());
                }
                if (saved_data) {
                    throw new SavedSuccessfully("Game saved successfully");
                }
            }
            if (removeTokenPosition.equals("load")) {
                boolean loaded_data = false;
                try {
                    GameSaveData saveData = (GameSaveData) GameState.load("gamestate1.save");
                    gameBoardManipulator.setGameBoard(saveData.savedGameboard);
                    loaded_data = true;
                } catch (Exception e) {
                    System.out.println("Couldn't load:" + e.getMessage());
                }
                if (loaded_data) {
                    throw new LoadedSuccessfully("Game loaded successfully");
                }
            }
            gameBoardManipulator.removeToken(removeTokenPosition, player.get_tokencolour());
        } catch (SavedSuccessfully | LoadedSuccessfully | InvalidPositionException | ArrayIndexOutOfBoundsException | NullPointerException | InvalidRemovalException e) {
            return e.getMessage();
            // skip the invalid token and ask for prompt again
        }
        return "";
        //shows state after removing opponents token
        //TODO: print gameboard
        //TODO: change duplicate code to suitable method
        //TODO: add loading at start of game and add loading of player names etc as well
        //TODO: save which player's turn it was and start from that player's turn when gamestate is loaded. Might have to rework gameplay1 slightly
    }


}
