/**
 * @author o0wen0o
 * @create 2022-08-09 1:41 PM
 */
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
