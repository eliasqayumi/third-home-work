package AutoFire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.Random;

class Surface extends JPanel implements ActionListener {
    private final double DEGREE = 45;
    private final double GRAVITY = 9.81;
    private final int DELAY = 20;
    private double width;
    private double height;
    private double xBall = 0;
    private double yBall = 0;
    private double objectStartPoint = 0;
    private double speed;
    private double timeCounter = 0;
    private float alpha_rectangle;
    private boolean intersects = false;
    private Rectangle2D rect;

    private Timer timer;
    private Graphics2D g2d;

    public Surface() {
        alpha_rectangle = 1f;
        initTimer();
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
        if (objectStartPoint == 0)
            objectStartPoint = getRandom() - height * 0.15;
        if (xBall <= 0) {
            xBall = height * 0.095 + 22 * Math.cos(Math.PI / 180 * DEGREE);
            yBall = height * 0.89 - 22 * Math.sin(Math.PI / 180 * DEGREE);
        }
//        Rendering the objects
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
//          fizik forumulu
        speed = Math.sqrt((objectStartPoint - height * 0.115) * GRAVITY / (2 * Math.cos(Math.PI / 180 * DEGREE) * Math.sin(Math.PI / 180 * DEGREE))) / 10;
        g2d.drawString("Speed: " + speed + " m/s", 10, 20);
//        draw y dimension line
        g2d.draw(new Line2D.Double(height * 0.05, height * 0.05, height * 0.05, height * 0.95));
//        draw x dimension line
        g2d.draw(new Line2D.Double(height * 0.05, height * 0.95, width * 0.95, height * 0.95));
//        draw a random object
        rect = new Rectangle2D.Double(objectStartPoint, height * 0.85, height * 0.15, height * 0.1);
//        draw the ball
        g2d.setPaint(Color.RED);
        g2d.fill(new Ellipse2D.Double(xBall, yBall, height * 0.025, height * 0.025));
        xBall += Math.cos(Math.PI / 180 * DEGREE) * speed;
        yBall -= speed * Math.sin(Math.PI / 180 * DEGREE) - GRAVITY *(timeCounter += 0.01);
        if (intersects) {
            alpha_rectangle += -0.5f;
            if (alpha_rectangle < 0)
                alpha_rectangle = 0;
        }
        g2d.setPaint(Color.black);
        fireGunDesign();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha_rectangle));
        g2d.fill(rect);
        g2d.dispose();
    }

    public void fireGunDesign() {
//        fire gun dimension and design
        Area mainPartRect = new Area(new Rectangle2D.Double(0, 0, 70, 20));
        mainPartRect.add(new Area(new Ellipse2D.Double(50, -5, 20, 30)));
        mainPartRect.add(new  Area(new Ellipse2D.Double(-5, -5, 50, 30)));

//        fire gun rotation
        AffineTransform tx = new AffineTransform();
        tx.rotate(-Math.cos(Math.PI / 180 * DEGREE), height * 0.105, height * 0.842);
        tx.translate(height * 0.05 - 30, height * 0.9 - 20);
        GeneralPath path = new GeneralPath();
        path.append(tx.createTransformedShape(mainPartRect), false);
        g2d.fill(path);
//        draw line outside fire gun
        g2d.setPaint(Color.BLUE);
        g2d.draw(path);

//        wheel design
        g2d.setPaint(Color.black);
        Area wheelCircle = new Area(new Ellipse2D.Double(height * 0.06, height * 0.89, height * 0.06, height * 0.06));
        wheelCircle.subtract(new Area(new Ellipse2D.Double(height * 0.07, height * 0.90, height * 0.04, height * 0.04)));
        wheelCircle.add(new Area(new Rectangle2D.Double(height * 0.07, height * 0.918, height * 0.04, height * 0.005)));
        wheelCircle.add( new Area(new Rectangle2D.Double(height * 0.087, height * 0.90, height * 0.005, height * 0.04)));
        g2d.fill(wheelCircle);
        g2d.setPaint(Color.RED);
        g2d.draw(wheelCircle);
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
        if (yBall >= height * 0.925)
            restore();
        if (rect.intersects(xBall, yBall, height * 0.01, height * 0.01))
            intersects = true;
    }

    public void restore() {
        xBall = 0;
        yBall = 0;
        objectStartPoint = 0;
        timeCounter = 0;
        alpha_rectangle = 1f;
        intersects = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

