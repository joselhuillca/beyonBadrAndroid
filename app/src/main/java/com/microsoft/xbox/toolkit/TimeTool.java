package com.microsoft.xbox.toolkit;

import com.microsoft.xbox.toolkit.TimeSample;
import java.util.ArrayList;
import java.util.Iterator;

public class TimeTool {
   private static final double NSTOSEC = 1.0E-9D;
   private static TimeTool instance = new TimeTool();
   private ArrayList allSamples = new ArrayList();

   public static TimeTool getInstance() {
      return instance;
   }

   public void clear() {
      this.allSamples.clear();
   }

   public long getAverageLPS() {
      return (long)(1.0D / ((double)this.getAverageTime() * 1.0E-9D));
   }

   public long getAverageTime() {
      long var1 = 0L;
      long var3 = (long)this.allSamples.size();

      TimeSample var6;
      for(Iterator var5 = this.allSamples.iterator(); var5.hasNext(); var1 += var6.getElapsed()) {
         var6 = (TimeSample)var5.next();
         var6.getElapsed();
      }

      return var1 / var3;
   }

   public long getMaximumTime() {
      long var1 = 0L;
      Iterator var5 = this.allSamples.iterator();

      while(var5.hasNext()) {
         long var3 = ((TimeSample)var5.next()).getElapsed();
         if(var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }

   public long getMinimumLPS() {
      return (long)(1.0D / ((double)this.getMaximumTime() * 1.0E-9D));
   }

   public long getSampleCount() {
      return (long)this.allSamples.size();
   }

   public TimeSample start() {
      TimeSample var1 = new TimeSample();
      this.allSamples.add(var1);
      return (TimeSample)this.allSamples.get(this.allSamples.size() - 1);
   }
}
