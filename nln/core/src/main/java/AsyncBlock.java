public class AsyncBlock {

    Thread thread = new Thread();

    public void killThread() {
        thread.interrupt();
    }

    public void setDaemonThread(Runnable block) {
        thread = new Thread(block);
        thread.setDaemon(true);
        thread.start();
    }
}
