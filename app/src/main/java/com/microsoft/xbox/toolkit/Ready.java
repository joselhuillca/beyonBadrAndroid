package com.microsoft.xbox.toolkit;

public class Ready
{
    private boolean ready = false;
    private Object syncObj = new Object();

    public boolean getIsReady()
    {
        synchronized (this.syncObj)
        {
            boolean bool = this.ready;
            return bool;
        }
    }

    public void reset()
    {
        synchronized (this.syncObj)
        {
            this.ready = false;
            return;
        }
    }

    public void setReady()
    {
        synchronized (this.syncObj)
        {
            this.ready = true;
            this.syncObj.notifyAll();
            return;
        }
    }

    public void waitForReady()
    {
        waitForReady(0);
    }

    public void waitForReady(int paramInt)
    {
        synchronized (this.syncObj) {
            boolean bool = this.ready;
            if ((bool) || (paramInt > 0)) {}
            try {
                this.syncObj.wait(paramInt);
                for ( ; ; )  {
                    this.syncObj.wait();

                    return;
                }
            }
            catch (InterruptedException localInterruptedException) {
                for (;;) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
