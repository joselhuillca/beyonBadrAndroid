package com.microsoft.xbox.toolkit;

import java.util.HashMap;

public class InvertibleHashMap<K, V> {
   private HashMap<K, V> forward = new HashMap();
   private HashMap<V, K> reverse = new HashMap();

   public boolean containsKey(K var1) {
      return this.forward.containsKey(var1);
   }

   public int getSize() {
      return this.forward.size();
   }

   public V getUsingKey(K var1) {
      return this.forward.get(var1);
   }

   public K getUsingValue(V var1) {
      return this.reverse.get(var1);
   }

   public void put(K var1, V var2) {
      this.forward.put(var1, var2);
      this.reverse.put(var2, var1);
   }

   public void remove(K var1) {
      Object var2 = this.forward.get(var1);
      this.forward.remove(var1);
      this.reverse.remove(var2);
   }
}
