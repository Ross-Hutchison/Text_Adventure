import Game.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        game control = new game();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        control.outputIntroPara();
        control.outputCommandFormats(); // outputs the information on how to control the game

        control.outputGameStartPara();  // the paragraph that shows the game has started

        control.setUpGame();

        while(!control.getGameEnd()) {
            try {
                String input = reader.readLine();
                control.processInput(input);
            }
            catch (IOException e) {
                System.out.println("ERROR: "  + e.getMessage());

            }
        }
        System.out.println(control.getEndMsg());
    }
}
