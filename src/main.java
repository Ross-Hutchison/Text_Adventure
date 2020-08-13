import Game.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        game control = new game();

        control.outputIntroPara();
        control.outputCommandFormats(); // outputs the information on how to control the game

        control.outputGameStartPara();  // the paragraph that shows the game has started

        control.setUpGame();

        while (!control.getGameEnd()) {
            control.turn();
        }
        System.out.println("\n--------------------\n" + control.getEndMsg() + "\n--------------------");
    }
}
