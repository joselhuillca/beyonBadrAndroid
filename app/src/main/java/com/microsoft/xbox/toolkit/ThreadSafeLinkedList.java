package com.microsoft.xbox.toolkit;

import java.util.Collection;
import java.util.LinkedList;

public class ThreadSafeLinkedList<T>
{
   private boolean forceReturnWhenEmpty = false;
   private LinkedList<T> queue = new LinkedList();
   private Object syncObject = new Object();

   public void addAll(Collection<? extends T> paramCollection)
   {
      synchronized (this.syncObject)
      {
         this.queue.addAll(paramCollection);
         this.syncObject.notifyAll();
         return;
      }
   }

   public void addLast(T paramT)
   {
      synchronized (this.syncObject)
      {
         this.queue.addLast(paramT);
         this.syncObject.notifyAll();
         return;
      }
   }

   public void forceReturnWhenEmpty(boolean paramBoolean)
   {
      synchronized (this.syncObject)
      {
         this.forceReturnWhenEmpty = paramBoolean;
         this.syncObject.notifyAll();
         return;
      }
   }

   public boolean isEmpty()
   {
      return this.queue.isEmpty();
   }

   public T pop()
   {
      return (T)removeFirst();
   }

   public T removeFirst()
   {
      Object localObject6 = null;
      Object localObject2 = null;
      Object localObject5 = null;
      Object localObject1 = localObject6;
      Object localObject7;
      try
      {
         localObject7 = this.syncObject;
         localObject1 = localObject6;
         for (;;)
         {
            localObject1 = localObject5;
            try
            {
               if (this.queue.isEmpty())
               {
                  localObject1 = localObject5;
                  if (!this.forceReturnWhenEmpty)
                  {
                     localObject1 = localObject5;
                     this.syncObject.wait();
                     continue;
                  }
               }
               else {
                  break;
               }
            }
            finally {}
         }
         localObject1 = localObject5;
      }
      catch (Exception localException)
      {
         return (T)localObject1;
      }
      Object localObject4 = null;
      if (!this.queue.isEmpty())
      {
         localObject1 = localObject5;
         localObject4 = this.queue.removeFirst();
      }
      localObject1 = localObject4;
      return (T)localObject4;
   }
}
