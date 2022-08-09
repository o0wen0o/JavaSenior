/**
 * @author o0wen0o
 * @create 2022-08-09 1:41 PM
 */
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
