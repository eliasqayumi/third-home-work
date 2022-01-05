package AutoFire;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class AutoFire extends JFrame {

    public AutoFire() {
        initUİ();
    }

    private void initUİ() {
        final Surface surface=new Surface();
        add(surface);
        setTitle("Auto fire");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Timer timer=surface.getTimer();
                timer.stop();
            }
        });
        setSize(1020, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                AutoFire ex = new AutoFire();
                ex.setVisible(true);
            }
        });
    }
}