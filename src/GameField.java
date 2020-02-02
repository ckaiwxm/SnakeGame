import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class GameField extends JPanel {
    public JFrame f;
    public GameOverScreen gameOverScreen;
    public SplashScreen splashScreen;

    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public boolean alive;
    public boolean pause;
    public int level;

    public int fieldHeight;
    public int totalHeight;
    public int fieldWidth;
    public int objSize;

    public ImageIcon groundIcon;
    public ImageIcon fruitIcon;
    public ImageIcon headUpIcon;
    public ImageIcon headDownIcon;
    public ImageIcon headLeftIcon;
    public ImageIcon headRightIcon;

    public ArrayList<Axis> snake;

    public int fps;
    public int speed;
    public int time;
    public Timer fpsTimer;
    public Timer timer;
    public Timer showTimer;

    public int fruitCountdown;
    public int fruitCount;
    public Axis fruit;

    public GameField(JFrame frame) {
        this.f = frame;
        this.gameOverScreen = null;
        this.splashScreen = new SplashScreen();

        InitModel(1);
        InitIcon();
        InitSnake();
        InitFruit();
        InitAction();
        InitTimer();

        this.setFocusable(true);
    }

    public void InitModel(int level) {
        this.up = false;
        this.down = false;
        this.left = false;
        this.right = true;
        this.alive = false;
        this.pause = false;
        this.level = level;

        this.totalHeight = 800;
        this.fieldHeight = 600;
        this.fieldWidth = 600;
        this.objSize = 20;
    }

    public void InitIcon() {
        this.groundIcon = getImageIcon("/lib/ground.jpg", objSize, objSize);
        this.fruitIcon = getImageIcon("/lib/apple.png", objSize, objSize);
        this.headUpIcon = getImageIcon("/lib/head_up.png", objSize, objSize);
        this.headDownIcon = getImageIcon("/lib/head_down.png", objSize, objSize);
        this.headLeftIcon = getImageIcon("/lib/head_left.png", objSize, objSize);
        this.headRightIcon = getImageIcon("/lib/head_right.png", objSize, objSize);
    }

    public void InitTimer() {
        fps = 50;

        this.fpsTimer = new Timer();
        TimerTask fpsTimerTask = new TimerTask()  {
            @Override
            public void run() {
                repaint();
            }
        };
        fpsTimer.schedule(fpsTimerTask, 0, (1000/fps));

        InitCrawlTimer();
        InitShowTimer();
    }

    public void InitCrawlTimer() {
        if (level == 1) {
            speed = 2;
        }
        else if (level == 2) {
            speed = 4;
        }
        else if (level == 3) {
            speed = 6;
        }
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (alive) {
                    crawl();
                    eat();
                    collide();
                }
            }
        };
        timer.schedule(timerTask, 0, (1000/speed));
    }

    public void InitShowTimer() {
        time = 30;
        this.showTimer = new Timer();
        TimerTask showTimerTask = new TimerTask() {
            @Override
            public void run() {
                if ((level == 1 || level == 2) && !pause && alive) {
                    if (time-- == 0) {
                        time = 30;
                        level += 1;
                        fruitCountdown = level == 2? 10 : 15;
                        timer.cancel();
                        InitCrawlTimer();
                    }
                }
            }
        };
        showTimer.schedule(showTimerTask, 0, 1000);
    }

    public void InitSnake() {
        this.snake = new ArrayList<Axis>();
        this.snake.add(new Axis(100, 200));
        this.snake.add(new Axis(80, 200));
        this.snake.add(new Axis(60, 200));
    }

    public void InitFruit() {
        if (level == 1) {
            this.fruitCountdown = 5;
        }
        else if (level == 2) {
            this.fruitCountdown = 10;
        }
        else if (level == 3) {
            this.fruitCountdown = 15                                                                                                                                                                                ;
        }

        this.fruitCount = 0;
        Axis newFruit = genFruitAxis(new Axis(40, 40));
        while (fruitOverlapSnake(newFruit)) {
            newFruit = genFruitAxis(newFruit);
        }
        this.fruit = newFruit;
    }

    public void InitAction() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key = e.getKeyCode();

                if (!alive) {
                    if (key == KeyEvent.VK_1 ||
                        key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN ||
                        key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                        restart(1);
                    }
                    else if (key == KeyEvent.VK_2) {
                        restart(2);
                    }
                    else if (key == KeyEvent.VK_3) {
                        restart(3);
                    }
                    else if (key == KeyEvent.VK_Q) {
                        dead();
                    }
                } else {
                    if (key == KeyEvent.VK_R) {
                        alive = false;
                    }
                    else if (key == KeyEvent.VK_1) {
                        int tmpFruitCount = fruitCount;
                        level = 1;
                        InitFruit();
                        fruitCount = tmpFruitCount;
                        timer.cancel();
                        timer.purge();
                        showTimer.cancel();
                        showTimer.purge();
                        InitCrawlTimer();
                        InitShowTimer();
                    }
                    else if (key == KeyEvent.VK_2) {
                        int tmpFruitCount = fruitCount;
                        level = 2;
                        InitFruit();
                        fruitCount = tmpFruitCount;
                        timer.cancel();
                        timer.purge();
                        showTimer.cancel();
                        showTimer.purge();
                        InitCrawlTimer();
                        InitShowTimer();
                    }
                    else if (key == KeyEvent.VK_3) {
                        int tmpFruitCount = fruitCount;
                        level = 3;
                        InitFruit();
                        fruitCount = tmpFruitCount;
                        timer.cancel();
                        timer.purge();
                        showTimer.cancel();
                        showTimer.purge();
                        InitCrawlTimer();
                        InitShowTimer();
                    }
                    else if (key == KeyEvent.VK_P) {
                        if (pause) {
                            pause = false;
                        } else {
                            pause = true;
                        }
                    }
                    else if (key == KeyEvent.VK_UP && !up && !down && !pause) {
                        up = true;
                        down = false;
                        left = false;
                        right = false;
                    }
                    else if (key == KeyEvent.VK_DOWN && !down && !up && !pause) {
                        up = false;
                        down = true;
                        left = false;
                        right = false;
                    }
                    else if (key == KeyEvent.VK_LEFT && !left && !right && !pause) {
                        up = false;
                        down = false;
                        left = true;
                        right = false;
                    }
                    else if (key == KeyEvent.VK_RIGHT && !right && !left && !pause) {
                        up = false;
                        down = false;
                        left = false;
                        right = true;
                    }
                    else if (key == KeyEvent.VK_Q) {
                        dead();
                    }
                }
            }
        });
    }

    public void restart(int level) {
        InitModel(level);
        this.alive = true;
        this.setFocusable(true);
        InitSnake();
        InitFruit();
        timer.cancel();
        timer.purge();
        showTimer.cancel();
        showTimer.purge();
        InitCrawlTimer();
        InitShowTimer();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(fieldWidth, totalHeight);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw ground
        for(int i = 0; i < 30; i++) {
            for(int j = 0; j < 30; j++) {
                g.drawImage(groundIcon.getImage(), i*objSize, j*objSize, this);
            }
        }

        // Draw snake
        for(int i = 0; i < snake.size(); i++) {
            if (i == 0) {
                if (up) {
                    g.drawImage(headUpIcon.getImage(), snake.get(i).x, snake.get(i).y, this);
                }
                else if (down) {
                    g.drawImage(headDownIcon.getImage(), snake.get(i).x, snake.get(i).y, this);
                }
                else if (left) {
                    g.drawImage(headLeftIcon.getImage(), snake.get(i).x, snake.get(i).y, this);
                }
                else if (right) {
                    g.drawImage(headRightIcon.getImage(), snake.get(i).x, snake.get(i).y, this);
                }
            } else {
                g.setColor(new Color(179,157,219));
                g.fillRect(snake.get(i).x, snake.get(i).y, objSize, objSize);
            }
        }

        // Draw Fruit
        if (fruitCountdown > 0) {
            g.drawImage(fruitIcon.getImage(), fruit.x, fruit.y, this);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.fillRect(0, 600, 600, 200);
        g.setColor(new Color(33,0,66));
        g.setFont(new Font("Helvetica", Font.BOLD, 15));
        if (level == 1 || level == 2) {
            g.drawString("Time: " + time, 20, 630);
        }
        else if (level == 3) {
            g.drawString("Time: infinite", 20, 630);
        }
        g.drawString("Level: " + level, 20, 650);
        g.drawString("Score: " + fruitCount, 20, 670);
        g.drawString("Speed: " + speed, 20, 690);
        g.drawString("Fruit Left: " + fruitCountdown, 20, 710);

        // Draw Pause
        if (pause) {
            g.setFont(new Font("Helvetica", Font.BOLD, 40));
            g.setColor(new Color(94,53,177));
            g.drawString("PAUSE", 230, 300);
        }

        // Draw Splash
        if (!alive) {
            splashScreen.paintComponent(g);
        }
    }

    public void crawl() {
        if (!pause) {
            for(int i = snake.size() - 1; i > 0; i--) {
                Axis prevBody = new Axis(snake.get(i-1).x, snake.get(i-1).y);
                snake.set(i, prevBody);
            }

            Axis oldHead = snake.get(0);
            if (up) {
                Axis newHead = new Axis(oldHead.x, oldHead.y-objSize);
                snake.set(0, newHead);
            }
            else if (down) {
                Axis newHead = new Axis(oldHead.x, oldHead.y+objSize);
                snake.set(0, newHead);

            }
            else if (left) {
                Axis newHead = new Axis(oldHead.x-objSize, oldHead.y);
                snake.set(0, newHead);
            }
            else if (right) {
                Axis newHead = new Axis(oldHead.x+objSize, oldHead.y);
                snake.set(0, newHead);
            }
        }
    }

    public void eat() {
        if (snake.get(0).x == fruit.x && snake.get(0).y == fruit.y) {
            fruitCountdown -= 1;
            fruitCount += 1;
            snake.add(new Axis(-20, -20));
            genFruit();
        }
    }

    public void collide() {
        for(int i = snake.size() - 1; i > 0; i--) {
            Axis body = snake.get(i);
            if (snake.get(0).x == body.x && snake.get(0).y == body.y) {
                dead();
            }
        }

        if (snake.get(0).x < 0 || snake.get(0).x > (fieldWidth - objSize) ||
            snake.get(0).y < 0 || snake.get(0).y > (fieldHeight - objSize)) {
            dead();
        }
    }

    public void dead() {
        if (gameOverScreen == null) {
            gameOverScreen = new GameOverScreen(fruitCount);
            f.setVisible(false);
        }
    }

    public void genFruit() {
        if (fruitCountdown > 0) {
            Axis newFruit = genFruitAxis(fruit);
            while (fruitOverlapSnake(newFruit)) {
                newFruit = genFruitAxis(newFruit);
            }
            this.fruit = newFruit;
        }
    }

    public Axis genFruitAxis(Axis oldFruit) {
        int fruitX = oldFruit.x + (objSize * 2);
        int fruitY = oldFruit.y + (objSize * 3);
        if (fruitX < 0 || fruitX > (fieldWidth - objSize)) {
            fruitX = 0;
        }
        if (fruitY < 0 || fruitY > (fieldHeight - objSize)) {
            fruitY = 0;
        }
        Axis fruitAxis = new Axis(fruitX, fruitY);
        return fruitAxis;
    }

    public boolean fruitOverlapSnake(Axis newFruit) {
        for (Axis body:snake) {
            if (body.x == newFruit.x && body.y == newFruit.y) {
                return true;
            }
        }
        return false;
    }

    // Refer from my cs349 A2 code
    private ImageIcon getImageIcon(String path, int width, int height) {
        ImageIcon imgIcon = new ImageIcon(new ImageIcon(getClass()
                .getResource(path))
                .getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH));
        return imgIcon;
    }
}
