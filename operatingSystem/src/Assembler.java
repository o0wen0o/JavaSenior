/**
 * @author o0wen0o
 * @create 2022-08-09 1:40 PM
 */
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
