import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        new SnakeGame();
    }
}

class SnakeGame extends JFrame {
    private GameField gameField;

    public SnakeGame() {
        super("Snake Game");

        // Init
        gameField = new GameField(this);


        // Set View
        this.add(gameField, BorderLayout.CENTER);
        this.setSize(new Dimension(600, 800));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
