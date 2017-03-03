package com.microsoft.xbox.toolkit;

import com.microsoft.xbox.toolkit.ThreadManager;

public class XLEAssert {
   public static void assertFalse(String var0, boolean var1) {
      if(!var1) {
         var1 = true;
      } else {
         var1 = false;
      }

      assertTrue(var0, var1);
   }

   public static void assertIsNotUIThread() {
      boolean var0;
      if(Thread.currentThread() != ThreadManager.UIThread) {
         var0 = true;
      } else {
         var0 = false;
      }

      assertTrue(var0);
   }

   public static void assertIsUIThread() {
      boolean var0;
      if(Thread.currentThread() == ThreadManager.UIThread) {
         var0 = true;
      } else {
         var0 = false;
      }

      assertTrue(var0);
   }

   public static void assertNotNull(Object var0) {
      boolean var1;
      if(var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      assertTrue((String)null, var1);
   }

   public static void assertNotNull(String var0, Object var1) {
      boolean var2;
      if(var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      assertTrue(var0, var2);
   }

   public static void assertTrue(String var0, boolean var1) {
      if(!var1) {
         var0 = getCallerLocation();
       }

   }

   public static void assertTrue(boolean var0) {
      assertTrue((String)null, var0);
   }

   private static String getCallerLocation() {
      StackTraceElement[] var2 = Thread.currentThread().getStackTrace();
      int var0 = 0;

      int var1;
      while(true) {
         var1 = var0;
         if(var0 >= var2.length) {
            break;
         }

         if(var2[var0].getClassName().equals(XLEAssert.class.getName()) && var2[var0].getMethodName().equals("getCallerLocation")) {
            var1 = var0;
            break;
         }

         ++var0;
      }

      while(var1 < var2.length && var2[var1].getClassName().equals(XLEAssert.class.getName())) {
         ++var1;
      }

      return var1 < var2.length?var2[var1].toString():"unknown";
   }
}
