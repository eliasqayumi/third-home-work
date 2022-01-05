import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.Random;

public class DemoMain {
    public static void main(String[] args) {
        ArrayList<Double> xList = new ArrayList();
        ArrayList<Double> yList = new ArrayList();
        Random random = new Random();
        double number = Math.abs(random.nextInt() % 450);
        while (xList.size() != 10) {
            for (double i = 0; i <= 20; i++)
                while (xList.contains(i + number) || xList.contains(number - i)) {
                        number = Math.abs(random.nextInt() % 450);
                        i = -1;
                }
            xList.add(number);
        }
//        while (yList.size() != 10) {
//            while (yList.contains(number = -(Math.abs(random.nextInt() % 400)))) ;
//            yList.add(number);
//        }
        for (Double iter : xList) {
            System.out.println("X deger " + iter);
        }
//        for (Double iter : yList) {
//            System.out.println("Y deger " + iter);
//        }
    }
}
