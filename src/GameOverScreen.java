import javax.swing.*;
import java.awt.*;
import java.io.*;

class GameOverScreen extends JFrame {
    GameOver gameOver;
    int score;
    int highScore;
    int height;
    int width;

    public GameOverScreen(int fruitCount) {
        this.score = fruitCount;
        this.highScore = 0;
        this.height = 600;
        this.width = 600;

        File cache = new File("cache.txt");
        if (cache.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("cache.txt"));
                String highStr = reader.readLine();
                if (highStr != null && !highStr.equals("")&& !highStr.equals(" ")) {
                    highScore = Integer.parseInt(highStr);
                }
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        highScore = Math.max(highScore, score);

        // Delete old cache
        File oldCache = new File("cache.txt");
        if (oldCache.exists()) {
            oldCache.delete();
        }

        // Create a new cache
        File newCache = new File("cache.txt");
        try {
            newCache.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter("cache.txt");
            writer.write(Integer.toString(highScore) + System.getProperty( "line.separator"));
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Set View
        this.gameOver = new GameOver();
        this.add(gameOver, BorderLayout.CENTER);
        this.setSize(new Dimension(width, height));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public class GameOver extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(33,0,66));
            g.setFont(new Font("Helvetica", Font.BOLD, 50));

            g.drawString("GAME OVER", 150, 200);
            g.drawString("Score: " + score, 150, 260);
            g.drawString("High Score: " + highScore, 150, 320);
        }
    }
}