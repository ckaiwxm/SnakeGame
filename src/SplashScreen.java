import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JPanel {
    public int height;
    public int width;

    public SplashScreen() {
        this.height = 500;
        this.width = 360;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(248,242,255));
        g.fillRect(50, 50, height, width);
        g.setColor(new Color(33,0,66));
        g.setFont(new Font("Helvetica", Font.BOLD, 15));

        g.drawString("~ Welcome to the Snake Game ~", 200, 70);
        g.drawString("Name: Kaiwen Chen", 70, 110);
        g.drawString("User ID: k2999che", 70, 130);

        g.drawString("Try to eat the fruits and avoid collisions ^-^", 70, 160);
        g.drawString("Please press any direction or level key to start", 70, 180);

        g.drawString("Top arrow: turn the snake top", 90, 210);
        g.drawString("Down arrow: turn the snake top", 90, 230);
        g.drawString("Left arrow: turn the snake top", 90, 250);
        g.drawString("Right arrow: turn the snake top", 90, 270);
        g.drawString("P: pause and un-pause the game", 90, 290);
        g.drawString("R: reset to the splash screen", 90, 310);
        g.drawString("1: start level 1", 90, 330);
        g.drawString("2: start level 2", 90, 350);
        g.drawString("3: start level 3", 90, 370);
        g.drawString("Q: quit and display the high score screen", 90, 390);
    }
}
