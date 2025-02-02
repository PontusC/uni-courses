package towerdefence;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * Class that initiates the game, creates all needed objects etc.
 * Also contains the main game loop, and logic to interpolate it to 30 fps.
 */
public class Game{
    public Game(){}

    /**
     * Main loop of the game. Needs to be improved to approximate 30 fps, since some frames might take longer etc.
     */
    public static void main(String[] args) throws InterruptedException {
        Game game = new Game();
        GameState gameState = new GameState();
        GameBoard gameBoard = new GameBoard();
        GameController Controller = new GameController(gameBoard, gameState);
        ScreenManager ScreenManager = game.createFrame(Controller, gameBoard, gameState);

        // Set up timers to approximate 30 fps.
        Long correctFrameTime = 1000000000/30L;
        Long startTime, elapsedTime, timeToWait;

        // Start timing the games duration
        gameState.startGame();

        while(!gameState.gameOver()){
            startTime = System.nanoTime();
            Controller.tick();
            ScreenManager.repaint();
            elapsedTime = System.nanoTime() - startTime;
            timeToWait = correctFrameTime - elapsedTime;
            // Makes sure we dont wait for negative time
            if (timeToWait > 0)
                TimeUnit.NANOSECONDS.sleep(timeToWait);
        }
    }

    /**
     * Method that initiates the JFrame and JPanel to draw on. Needs the game controller as a parameter
     * to reach the towers and units to draw.
     * @param Controller the gamestate controller.
     * @return ScreenManager that controls all drawing.
     */
    public ScreenManager createFrame(GameController Controller, GameBoard GameBoard, GameState GameState){
        // Create a frame, set it to close running program if closed
        JFrame frame = new JFrame("Element TD");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adds KeyListener to the frame
        frame.addKeyListener(Controller);
        frame.addMouseListener(Controller);
        frame.setFocusable(true);

        // Create the screen manager, the JPanel extender
        ScreenManager ScreenManager = new ScreenManager(Controller, GameBoard, GameState);
        frame.add(ScreenManager);
        frame.pack();

        frame.setVisible(true);
        return ScreenManager;
    }
}
