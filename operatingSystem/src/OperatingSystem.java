import java.util.concurrent.Semaphore;

/**
 * @author o0wen0o
 * @create 2022-07-26 8:47 PM
 */

class Component implements Runnable {
    private int countComponent = 0;
    private int millisSleep = 0;

    public Component(int millisSleep) {
        this.millisSleep = millisSleep;
    }

    @Override
    public void run() {
        while (countComponent < OperatingSystem.TOTAL_COMPONENT_PRODUCED) {
            String threadName = Thread.currentThread().getName();
            OperatingSystem.sleep(millisSleep);

            System.out.println(threadName + ": Unit-" + (countComponent + 1) + " produced");
            countComponent++;

            switch (threadName) { // determine whether increase permit A or B
                case "Component-A":
                    OperatingSystem.semComponentA.release(1); // increase permit of semComponentA by 1
                    break;

                case "Component-B":
                    OperatingSystem.semComponentB.release(1); // increase permit of semComponentB by 1
                    break;
            }
        }
    }
}

class Assembler implements Runnable {
    private int countFinalProduct = 0;
    private int millisSleep = 0;

    public Assembler(int millisSleep) {
        this.millisSleep = millisSleep;
    }

    @Override
    public void run() {
        while (countFinalProduct < OperatingSystem.TOTAL_COMPONENT_PRODUCED) {
            try {
                // wait A
                OperatingSystem.semComponentA.acquire(1); // wait 1 Component A
                // wait B
                OperatingSystem.semComponentB.acquire(1); // wait 1 Component B

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OperatingSystem.sleep(millisSleep);

            System.out.println(Thread.currentThread().getName() + ": Final Product-" + (countFinalProduct + 1) + " completed ");
            countFinalProduct++;

            OperatingSystem.semFinalProduct.release(1); // increase permit of semFinalProduct by 1

            // System.out.println("\nAvailable Permit A: " + OperatingSystem.semComponentA.availablePermits());
            // System.out.println("Available Permit B: " + OperatingSystem.semComponentB.availablePermits());
            // System.out.println();
        }
    }
}

class Packer implements Runnable {
    private int countBox = 0;
    private int millisSleep = 0;

    public Packer(int millisSleep) {
        this.millisSleep = millisSleep;
    }

    @Override
    public void run() {
        while (countBox < OperatingSystem.TOTAL_COMPONENT_PRODUCED / 6) { // every 6 final product produce 1 box
            try {
                OperatingSystem.semFinalProduct.acquire(6); // wait 6 final product
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OperatingSystem.sleep(millisSleep);

            System.out.println(Thread.currentThread().getName() + ": Box-" + (countBox + 1) + " packed");
            countBox++;

            OperatingSystem.semBox.release(1); // increase permit of semBox by 1
        }
    }
}

class Labeller implements Runnable {
    private int countLabelledBox = 0;
    private int millisSleep = 0;

    public Labeller(int millisSleep) {
        this.millisSleep = millisSleep;
    }

    @Override
    public void run() {
        while (countLabelledBox < OperatingSystem.TOTAL_COMPONENT_PRODUCED / 6) { // every 6 final product produce 1 box
            try {
                OperatingSystem.semBox.acquire(1); // wait 6 final product
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            OperatingSystem.sleep(millisSleep); // assume label a box need some time

            System.out.println(Thread.currentThread().getName() + ": Box-" + (countLabelledBox + 1) + " labelled");
            countLabelledBox++;
        }
    }
}

public class OperatingSystem {
    public static final int TOTAL_COMPONENT_PRODUCED = 12;
    public static final Semaphore semComponentA = new Semaphore(0); // allow 1 thread run in same time
    public static final Semaphore semComponentB = new Semaphore(0);
    public static final Semaphore semFinalProduct = new Semaphore(0);
    public static final Semaphore semBox = new Semaphore(0);

    public static void main(String[] args) {
        Thread componentA = new Thread(new Component(1000), "Component-A");
        Thread componentB = new Thread(new Component(2000), "Component-B");
        Thread assembler = new Thread(new Assembler(2000), "Assembler");
        Thread packer = new Thread(new Packer(2000), "Packer");
        Thread labeller = new Thread(new Labeller(3000), "Labeller"); // assume label a box need 3 seconds

        componentA.start();
        componentB.start();
        assembler.start();
        packer.start();
        labeller.start();

        // main thread will exit while other threads still running, can?
        // does not destroy the semaphore, can? no function to destroy

        // no message "Waiting for components"
        // buffer for final product need?
    }

    /**
     * cope with the try-catch for every Thread.sleep()
     *
     * @param millisSleep in millis seconds
     */
    public static void sleep(int millisSleep) {
        try {
            Thread.sleep(millisSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
