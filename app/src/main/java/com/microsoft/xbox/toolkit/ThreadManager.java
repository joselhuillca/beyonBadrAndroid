package com.microsoft.xbox.toolkit;

import android.os.Handler;

// Referenced classes of package com.microsoft.xbox.toolkit:
//            Ready

public class ThreadManager
{

    public static Handler Handler;
    public static Thread UIThread;

    static class MyThread implements   Runnable {

        public MyThread (Runnable _runnable, Ready  ready){
            super();
            runnable = _runnable;
            actionComplete = ready;
        }
        final Ready  actionComplete;
        final Runnable runnable;

        public void run()
        {
            runnable.run();
            actionComplete.setReady();
        }


    }

    public ThreadManager()
    {
    }

    public static void UIThreadPost(Runnable runnable)
    {
        UIThreadPostDelayed(runnable, 0L);
    }

    public static void UIThreadPostDelayed(Runnable runnable, long l)
    {
        Handler.postDelayed(runnable, l);
    }

    public static void UIThreadSend(Runnable runnable)
    {
        if(UIThread == Thread.currentThread())
        {
            runnable.run();
            return;
        } else
        {
            Ready ready = new Ready();
            Handler.post( new MyThread(runnable, ready) );
            ready.waitForReady();
            return;
        }
    }
}
