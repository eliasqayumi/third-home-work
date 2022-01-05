package target_fire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Random;

class Surface extends JPanel implements ActionListener {

    private final double GRAVITY = 9.81;
    private final int DELAY = 20;
    private double width;
    private double height;
    private double xBall = 0;
    private double yBall = 0;
    private double objectStartPoint = 0;
    private double timeCounter = 0;
    private double speed;
    private double Degree;
    private float alpha_rectangle;
    private boolean intersects = false;
    private Rectangle2D rect;
    private Timer timer;
    private Graphics2D g2d;
    private boolean spacePressed = false;
    private boolean isOpaque = true;
    private double newDegree = 45;
    private double newSpeed = 10;

    public Surface() {
        alpha_rectangle = 1f;
        initTimer();
        addKeyListener(new TAdapter());
        setFocusable(true);
    }

    private void initTimer() {
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public Timer getTimer() {
        return timer;
    }

    private void doDrawing() {
        width = getWidth();
        height = getHeight();
        if (isOpaque) {
            objectStartPoint = getRandom() - height * 0.1;
            isOpaque = false;
        }
        if (xBall <= 0 || !spacePressed) {
            xBall = height * 0.115 + 28 * Math.cos(Math.PI / 180 * newDegree);
            yBall = height * 0.887 - 28 * Math.sin(Math.PI / 180 * newDegree);
        }
        if (!spacePressed) {
            Degree = newDegree;
            speed = newSpeed;
        }
//        Making Render
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);

        g2d.drawString("Speed: " + newSpeed + " m/s", 10, 20);
        g2d.drawString("Degree: " + newDegree + " º ", 10, 40);
//        dimension
        g2d.draw(new Line2D.Double(height * 0.05, height * 0.05, height * 0.05, height * 0.95));
        g2d.draw(new Line2D.Double(height * 0.05, height * 0.95, width * 0.95, height * 0.95));
//        Objects
        rect = new Rectangle2D.Double(objectStartPoint, height * 0.85, height * 0.1, height * 0.1);
        g2d.setPaint(Color.RED);
        g2d.fill(new Ellipse2D.Double(xBall, yBall, height * 0.025, height * 0.025));
        g2d.setPaint(Color.black);
        if (intersects) {
            alpha_rectangle += -0.5f;
            if (alpha_rectangle < 0) {
                alpha_rectangle = 0;
                isOpaque = true;
            }
        }
        firingGunDesign();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha_rectangle));
        g2d.fill(rect);
        g2d.dispose();
    }


    public void firingGunDesign() {
//      object of fire gun
        Area area = new Area(new Rectangle2D.Double(0, 0, 70, 20));
        area.add(new Area(new Ellipse2D.Double(50, -5, 20, 30)));
        area.add(new Area(new Ellipse2D.Double(-5, -5, 50, 30)));

//        Rotation of fire gun
        AffineTransform tx = new AffineTransform();
        tx.rotate(-Math.toRadians(newDegree), height * 0.127, height * 0.9);
        tx.translate(height * 0.127 - 35, height * 0.90 - 10);
        GeneralPath path = new GeneralPath();
        path.append(tx.createTransformedShape(area), false);
        g2d.fill(path);
        g2d.setPaint(Color.RED);
        g2d.draw(path);
        g2d.setPaint(Color.black);

//        design of wheel
        Area ellipse2D = new Area(new Ellipse2D.Double(height * 0.1, height * 0.89, height * 0.06, height * 0.06));
        ellipse2D.subtract(new Area(new Ellipse2D.Double(height * 0.11, height * 0.90, height * 0.04, height * 0.04)));
        ellipse2D.add(new Area(new Rectangle2D.Double(height * 0.11, height * 0.918, height * 0.04, height * 0.005)));
        ellipse2D.add(new Area(new Rectangle2D.Double(height * 0.127, height * 0.90, height * 0.005, height * 0.04)));
        g2d.fill(ellipse2D);

        g2d.setPaint(Color.RED);
        g2d.draw(ellipse2D);
        g2d.setPaint(Color.BLACK);
    }

    private double getRandom() {
        double randomNum;
        while ((randomNum = new Random().nextInt() % width) <= width * 0.30) ;
        return randomNum;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        doDrawing();
        if (yBall >= height * 0.925 || xBall + height * 0.05 > width)
            restore();
        if (rect.intersects(xBall, yBall, height * 0.01, height * 0.01))
            intersects = true;
    }

    public void restore() {
        xBall = 0;
        yBall = 0;
        timeCounter = 0;
        alpha_rectangle = 1f;
        intersects = false;
        spacePressed = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (spacePressed && speed != 0 && Degree != 0) {
            xBall += Math.cos(Math.PI / 180 * Degree) * speed;
            yBall -= speed * Math.sin(Math.PI / 180 * Degree) - GRAVITY * (timeCounter += 0.01);
        }
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

