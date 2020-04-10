package com.feedhub.app.concurrent;

import android.os.Process;

public class BackgroundThread extends Thread {

    public BackgroundThread(Runnable runnable) {
        super(runnable);
    }

    public BackgroundThread() {
        super();
    }

    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        super.run();
    }
}
