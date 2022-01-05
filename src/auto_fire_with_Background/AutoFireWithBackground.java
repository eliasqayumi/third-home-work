package auto_fire_with_Background;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AutoFireWithBackground extends JFrame {

    public AutoFireWithBackground() {
        initUI();
    }

    private void initUI() {
        final Surface surface=new Surface();
        add(surface);
        setTitle("Hit with Background");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Timer timer=surface.getTimer();
                timer.stop();
            }
        });
        setSize(1420, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                AutoFireWithBackground ex = new AutoFireWithBackground();
                ex.setVisible(true);
            }
        });
    }
}