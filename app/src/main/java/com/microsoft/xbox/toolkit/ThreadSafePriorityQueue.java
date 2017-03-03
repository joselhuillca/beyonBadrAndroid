package com.microsoft.xbox.toolkit;

import java.util.HashSet;
import java.util.PriorityQueue;

public class ThreadSafePriorityQueue<T>
{
   private HashSet<T> hashSet = new HashSet();
   private PriorityQueue<T> queue = new PriorityQueue();
   private Object syncObject = new Object();

   public T pop() throws InterruptedException {
      Object localObject5 = null;
      Object localObject2 = null;
      Object localObject1 = localObject5;
      Object localObject6;

     localObject6 = this.syncObject;
     localObject1 = localObject5;
     for (;;)
     {
        localObject1 = localObject2;
       if (this.queue.isEmpty())
       {
          localObject1 = localObject2;
          this.syncObject.wait();
       }
         else {
           break;
       }
     }

      Object localObject4 = this.queue.remove();
      localObject1 = localObject4;
      this.hashSet.remove(localObject4);
      localObject1 = localObject4;
      return (T)localObject4;
   }

    public synchronized T take() throws InterruptedException {
        while(queue.isEmpty()) {
            wait();
        }

        T item = queue.remove();
        notify(); // notifyAll() for multiple producer/consumer threads
        return item;
    }

   public void push(T paramT)
   {
      synchronized (this.syncObject)
      {
         if (!this.hashSet.contains(paramT))
         {
            this.queue.add(paramT);
            this.hashSet.add(paramT);
            this.syncObject.notifyAll();
         }
         return;
      }
   }
}
