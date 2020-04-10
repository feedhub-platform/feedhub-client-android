package com.feedhub.app.common;

import com.feedhub.app.concurrent.BackgroundThread;

public class TaskManager {

    public static void execute(Runnable runnable) {
        new BackgroundThread(runnable).start();
    }

}
