package com.microsoft.xbox.toolkit;

import java.util.ArrayList;
import java.util.HashSet;

public class MultiSelection<T> {
   private HashSet<T> selection = new HashSet();

   public void add(T var1) {
      this.selection.add(var1);
   }

   public boolean contains(T var1) {
      return this.selection.contains(var1);
   }

   public boolean isEmpty() {
      return this.selection.isEmpty();
   }

   public void remove(T var1) {
      this.selection.remove(var1);
   }

   public void reset() {
      this.selection.clear();
   }

   public ArrayList<T> toArrayList() {
      return new ArrayList(this.selection);
   }
}
