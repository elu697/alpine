package util;

public class AsyncBlock {

    private Thread thread = new Thread();
    private boolean endFlag = false;
    static int threadCount = 0;

    public void setDaemonThread(Runnable block) {
        thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
        threadCount++;
    }

    public void killThread() {
        thread.interrupt();
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
