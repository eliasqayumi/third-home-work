package target_fire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TargetFire extends JFrame {
    public TargetFire() {
        initUI();
    }

    private void initUI() {
        final Surface surface = new Surface();
        add(surface);
        setTitle("Full Project");
        setSize(1020, 720);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Timer timer = surface.getTimer();
                timer.stop();
            }
        });
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
        TargetFire targetFire = new TargetFire();
        targetFire.setVisible(true);
            }
        });
    }
}
