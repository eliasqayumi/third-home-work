package Savas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
public class Savas {
}
class Surface extends JPanel implements ActionListener {
    private final double GRAVITY = 9.81;
    private final int DELAY = 20;
    private double width;
    private double height;
    private double xBall = 0;
    private double yBall = 0;
    private double objectStartPoint = 0;
    private double speed;
    private double lastSpeed = 0;
    private double timeCounter = 0;
    private float alpha_rectangle;
    private boolean intersects = false;
    private Rectangle2D rect;
    private Image image;
    private Image catapult;
    private Image bucket;
    private Timer timer;
    private Graphics2D g2d;
    private double newSpeed = 10;
    private double DEGREE = 0;
    private double newDegree = 0;
    private boolean spacePressed = false;
    private  boolean isTrans=false;

    public Surface() {
        initTimer();
        initStart();
    }

    private void initStart() {
        addKeyListener(new TAdapter());
        setFocusable(true);
    }

    private void initTimer() {
        timer = new Timer(DELAY, this);
        timer.start();
        try {
            image = new ImageIcon("background.jpg").getImage();
            bucket = new ImageIcon("bucketIcon.png").getImage();
            catapult = new ImageIcon("catapultIcon2.png").getImage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Timer getTimer() {
        return timer;
    }

    private void doDrawing() {
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        width = getWidth();
        height = getHeight();
        g2d.drawImage(image, 0, 0, (int) width, (int) height, this);
        if (!spacePressed) {
            speed = newSpeed;
            DEGREE = newDegree;
        }
        if (!isTrans) {
            objectStartPoint = getRandom() - height * 0.15;
            alpha_rectangle=1f;
            isTrans=true;
        }

//
//
        if (xBall == 0 || !spacePressed) {
            xBall = height * 0.258 + 150 * Math.cos(Math.PI / 180 * (180 - DEGREE));
            yBall = height * 0.88 - 150 * Math.sin((Math.PI / 180 * (180 - DEGREE)));
        }
//        speed = Math.sqrt((objectStartPoint - height * 0.115) * GRAVITY / (2 * Math.cos(Math.PI / 180 * DEGREE) * Math.sin(Math.PI / 180 * DEGREE))) / 10;
        lastSpeed = speed * Math.sin(Math.PI / 180 *(90-DEGREE)) - GRAVITY * timeCounter;
        dimensionAndInfo();
        rect = new Rectangle2D.Double(objectStartPoint, height * 0.85, height * 0.15, height * 0.1);
        g2d.drawImage(catapult, (int) (height * 0.05), (int) (height * 0.727), 280, 200, this);
        g2d.setPaint(Color.RED);
        g2d.fill(new Ellipse2D.Double(xBall, yBall, height * 0.025, height * 0.025));
        if (spacePressed) {
            xBall += Math.cos(Math.PI / 180 * (90-DEGREE)) * speed;
            yBall -= lastSpeed;
            timeCounter += 0.01;
        }
        g2d.setPaint(Color.black);
        fireGunDesign();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha_rectangle));
        g2d.fill(rect);
        g2d.dispose();
    }

    private void dimensionAndInfo() {
        g2d.drawString("Speed: " + speed + " m/s", 10, 20);
        g2d.draw(new Line2D.Double(height * 0.05, height * 0.05, height * 0.05, height * 0.95));
        g2d.draw(new Line2D.Double(height * 0.05, height * 0.95, width * 0.95, height * 0.95));
    }

    public void fireGunDesign() {
//        fire gun rotation
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(-18 + newDegree), height * 0.02 + 180, height * 0.73 + 120);
        tx.translate(height * 0.02, height * 0.73);
        g2d.drawImage(bucket, tx, this);
    }

    private double getRandom() {
        double randomNum;
        while ((randomNum = new Random().nextInt() % width) <= width * 0.35) ;
        return randomNum;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        doDrawing();
        if (yBall >= height * 0.925)
            restore();
        if (rect.intersects(xBall, yBall, height * 0.01, height * 0.01))
            intersects = true;
        if (intersects) {
            alpha_rectangle += -0.05f;
            if (alpha_rectangle < 0) {
                alpha_rectangle=0;
                objectStartPoint = 0;
                intersects=false;
                isTrans=false;
            }
        }
    }

    public void restore() {
        xBall = 0;
        yBall = 0;
        lastSpeed = 0;
        timeCounter = 0;
        spacePressed=false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }


    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (newDegree < 90)
                        newDegree++;
                    break;
                case KeyEvent.VK_DOWN:
                    if (newDegree > 0)
                        newDegree--;
                    break;
                case KeyEvent.VK_LEFT:
                    if (speed < 15)
                        newSpeed++;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (speed > 0)
                        newSpeed--;
                    break;
                case KeyEvent.VK_SPACE:
                    if (newDegree != 0 && newSpeed != 0)
                        spacePressed = true;
                    break;
            }
        }
    }
}

