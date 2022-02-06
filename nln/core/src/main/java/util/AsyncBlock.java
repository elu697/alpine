package util;

public class AsyncBlock {

    Thread thread = new Thread();

    public void killThread() {
        thread.interrupt();
    }

    private boolean endFlag = false;
    private int threadCount = 0;

    public void setDaemonThread(Runnable block) {
        thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
        threadCount++;
    }

    public int endThread() {
        threadCount--;
        return threadCount;
    }

    public void runLoop() {
        endFlag = false;
        while (!endFlag) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void endLoop() {
        endFlag = true;
    }
}
