import java.util.concurrent.Semaphore;

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
