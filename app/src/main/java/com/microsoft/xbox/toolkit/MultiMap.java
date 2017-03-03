package com.microsoft.xbox.toolkit;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

public class MultiMap {
   private Hashtable data = new Hashtable();
   private Hashtable dataInverse = new Hashtable();

   private void removeKeyIfEmpty(Object var1) {
      HashSet var2 = this.get(var1);
      if(var2 != null && var2.isEmpty()) {
         this.data.remove(var1);
      }

   }

   public int TESTsizeDegenerate() {
      int var1 = 0;
      Iterator var2 = this.data.keySet().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if(((HashSet)this.data.get(var3)).size() == 0) {
            ++var1;
         }
      }

      return var1;
   }

   public void clear() {
      this.data.clear();
      this.dataInverse.clear();
   }

   public boolean containsKey(Object var1) {
      return this.data.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.getKey(var1) != null;
   }

   public HashSet get(Object var1) {
      return (HashSet)this.data.get(var1);
   }

   public Object getKey(Object var1) {
      return this.dataInverse.get(var1);
   }

   public boolean keyValueMatches(Object var1, Object var2) {
      HashSet var3 = this.get(var1);
      return var3 == null?false:var3.contains(var2);
   }

   public void put(Object var1, Object var2) {
      if(this.data.get(var1) == null) {
         this.data.put(var1, new HashSet());
      }

      boolean var3;
      if(!this.dataInverse.containsKey(var2)) {
         var3 = true;
      } else {
         var3 = false;
      }

      XLEAssert.assertTrue(var3);
      ((HashSet)this.data.get(var1)).add(var2);
      this.dataInverse.put(var2, var1);
   }

   public void removeKey(Object var1) {
      Iterator var2 = ((HashSet)this.data.get(var1)).iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         XLEAssert.assertTrue(this.dataInverse.containsKey(var3));
         this.dataInverse.remove(var3);
      }

      this.data.remove(var1);
   }

   public void removeValue(Object var1) {
      Object var2 = this.getKey(var1);
      ((HashSet)this.data.get(var2)).remove(var1);
      this.dataInverse.remove(var1);
      this.removeKeyIfEmpty(var2);
   }

   public int size() {
      return this.data.size();
   }
}
