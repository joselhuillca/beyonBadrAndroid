package com.microsoft.xbox.toolkit;

import java.util.Hashtable;

public class ThreadSafeHashtable<K, V>
{
   private Hashtable<K, V> data = new Hashtable();
   private Hashtable<V, K> dataInverted = new Hashtable();
   private Object syncObject = new Object();

   public boolean containsKey(K paramK)
   {
      synchronized (this.syncObject)
      {
         boolean bool = this.data.containsKey(paramK);
         return bool;
      }
   }

   public boolean containsValue(V paramV)
   {
      synchronized (this.syncObject)
      {
         boolean bool = this.dataInverted.containsKey(paramV);
         return bool;
      }
   }

   public V get(K paramK)
   {
      synchronized (this.syncObject)
      {
         V param = this.data.get(paramK);
         return param;
      }
   }

   public Object getLock()
   {
      return this.syncObject;
   }

   public void put(K paramK, V paramV)
   {
      synchronized (this.syncObject)
      {
         this.data.put(paramK, paramV);
         this.dataInverted.put(paramV, paramK);
         return;
      }
   }

   public void remove(K paramK)
   {
      synchronized (this.syncObject)
      {
         Object localObject2 = this.data.get(paramK);
         this.data.remove(paramK);
         this.dataInverted.remove(localObject2);
         return;
      }
   }

   public void removeValue(V paramV)
   {
      synchronized (this.syncObject)
      {
         Object localObject2 = this.dataInverted.get(paramV);
         this.data.remove(localObject2);
         this.dataInverted.remove(paramV);
         return;
      }
   }
}
