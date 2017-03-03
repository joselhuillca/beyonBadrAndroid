package com.microsoft.xbox.toolkit;

public class TimeMonitor {
   private long startTicks = 0L;

   public long currentTime() {
      return System.currentTimeMillis() - this.startTicks;
   }

   public void start() {
      this.startTicks = System.currentTimeMillis();
   }
}
