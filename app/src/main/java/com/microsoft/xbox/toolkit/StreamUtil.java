package com.microsoft.xbox.toolkit;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StreamUtil {
   public static void CopyStream(OutputStream var0, InputStream var1) throws IOException {
      byte[] var3 = new byte[16384];

      while(true) {
         int var2 = var1.read(var3);
         if(var2 <= 0) {
            var0.flush();
            return;
         }

         var0.write(var3, 0, var2);
      }
   }

   public static void CopyStreamWithLimit(OutputStream var0, InputStream var1, int var2) throws IOException {
      byte[] var6 = new byte[16384];
      int var3 = 0;

      int var4;
      do {
         int var5 = var1.read(var6);
         var4 = var3;
         if(var5 <= 0) {
            break;
         }

         var0.write(var6, 0, var5);
         var4 = var3 + var5;
         var3 = var4;
      } while(var4 <= var2);

      var0.flush();
      XLELog.Info("CopyStreamWithLimit", String.format("length of stream copied : %d", new Object[]{Integer.valueOf(var4)}));
   }

   public static byte[] CreateByteArray(InputStream var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      try {
         CopyStream(var1, var0);
      } catch (IOException var2) {
         return null;
      }

      return var1.toByteArray();
   }

   public static byte[] HexStringToByteArray(String var0) {
      if(var0 == null) {
         throw new IllegalArgumentException("hexString invalid");
      } else {
         String var3 = var0;
         if(var0.length() % 2 != 0) {
            var3 = "0" + var0;
         }

         boolean var2;
         if(var3.length() % 2 == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         XLEAssert.assertTrue(var2);
         byte[] var4 = new byte[var3.length() / 2];

         for(int var1 = 0; var1 < var3.length(); var1 += 2) {
            var4[var1 / 2] = Byte.parseByte(var3.substring(var1, var1 + 2), 16);
         }

         return var4;
      }
   }


   public static String ReadAsString(InputStream paramInputStream)
   {
      StringBuilder localStringBuilder = new StringBuilder();
      BufferedReader inputStream = new BufferedReader(new InputStreamReader(paramInputStream));
      try
      {
         for (;;)
         {
            String str = inputStream.readLine();
            if (str == null) {
               break;
            }
            localStringBuilder.append(str);
            localStringBuilder.append('\n');
         }
         return localStringBuilder.toString();
      }
      catch (IOException e)
      {
         return null;
      }
   }
}
