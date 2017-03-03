package com.microsoft.xbox.toolkit;

import java.util.HashSet;

public class ThreadSafeHashSet<T>
{
   private HashSet<T> data = new HashSet();
   private Object syncObject = new Object();

   public boolean contains(T paramT)
   {
      synchronized (this.syncObject)
      {
         boolean bool = this.data.contains(paramT);
         return bool;
      }
   }

   public boolean ifNotContainsAdd(T paramT)
   {
      synchronized (this.syncObject)
      {
         boolean bool = this.data.contains(paramT);
         if (!bool) {
            this.data.add(paramT);
         }
         return bool;
      }
   }

   public void remove(T paramT)
   {
      synchronized (this.syncObject)
      {
         this.data.remove(paramT);
         return;
      }
   }
}
