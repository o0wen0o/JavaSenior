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
