package diningphilosophers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Philosopher extends Thread {
    private final static int delai = 1000;
    private final ChopStick myLeftStick;
    private final ChopStick myRightStick;
    private boolean running = true;

    public Philosopher(String name, ChopStick left, ChopStick right) {
        super(name);
        myLeftStick = left;
        myRightStick = right;
    }

    private void think() throws InterruptedException {
        System.out.println("M."+this.getName()+" pense... ");
        sleep(delai+new Random().nextInt(delai+1));
        System.out.println("M."+this.getName()+" arrête de penser");
    }

    private void eat() throws InterruptedException {
        System.out.println("M."+this.getName() + " mange...");
        sleep(delai+new Random().nextInt(delai+1));
        //System.out.println("M."+this.getName()+" arrête de manger");
    }

    @Override
public void run() {
    while (running) {
        try {
            think();
            boolean hasLeft = false;
            boolean hasRight = false;

            switch(new Random().nextInt(2)) {
                case 0:
                    hasLeft = myLeftStick.take(500); // attend 500 ms
                    if (hasLeft) {
                        think(); // attend un peu (augmente risque de blocage)
                        hasRight = myRightStick.take(500);
                        if (!hasRight) {
                            myLeftStick.release();
                        }
                    }
                    break;
                case 1:
                    hasRight = myRightStick.take(500);
                    if (hasRight) {
                        think();
                        hasLeft = myLeftStick.take(500);
                        if (!hasLeft) {
                            myRightStick.release();
                        }
                    }
                    break;
            }

            if (hasLeft && hasRight) {
                eat();
                myLeftStick.release();
                myRightStick.release();
            } else {
                System.out.println("M."+this.getName()+" n'a pas pu prendre les deux baguettes. Il réessaiera plus tard.");
            }

        } catch (InterruptedException ex) {
            Logger.getLogger("Table").log(Level.SEVERE, "{0} Interrupted", this.getName());
        }
    }
}


    // Permet d'interrompre le philosophe "proprement" :
    // Il relachera ses baguettes avant de s'arrêter
    public void leaveTable() {
        running = false;
    }

}
