package diningphilosophers;

public class ChopStick {
    private static int stickCount = 0;
    private boolean iAmFree = true;
    private final int myNumber;

    public ChopStick() {
        myNumber = ++stickCount;
    }

    synchronized public boolean take(long timeoutMillis) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeoutMillis;

        while (!iAmFree && timeoutMillis > 0) {
            wait(timeoutMillis);
            timeoutMillis = endTime - System.currentTimeMillis();
        }
        if (iAmFree) {
            iAmFree = false;
            System.out.println("baguette " + myNumber + " prise");
            return true;
        } else {
            // Échec : temps écoulé
            return false;
        }
    }

    synchronized public void release() {
        // assert !iAmFree;
        System.out.println("baguette " + myNumber + " relâchée");
        iAmFree = true;
        notifyAll(); // On prévient ceux qui attendent que la baguette soit libre
    }

   @Override
    public String toString() {
        return "baguette #" + myNumber;
    }
    
}
