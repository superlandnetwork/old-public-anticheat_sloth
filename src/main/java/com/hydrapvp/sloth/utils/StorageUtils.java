package com.hydrapvp.sloth.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import org.bukkit.entity.Player;

public class StorageUtils
{
  public static String getFromFile(String fileName)
  {
    try
    {
      StringBuilder getHotKey = new StringBuilder();
      FileReader getFile = new FileReader(fileName);
      BufferedReader bufferReader = new BufferedReader(getFile);
      String line;
      while ((line = bufferReader.readLine()) != null) {
        getHotKey.append(line);
      }
      bufferReader.close();
      return getHotKey.toString();
    }
    catch (Exception e) {}
    return "-1";
  }
  
  public static void log(Player player, String log)
  {
    String fileName = "plugins/Sloth/Logs/" + player.getName().toLowerCase() + "/logs.log/";
    File file = new File(fileName.replace(fileName.split("/")[(fileName.split("/").length - 1)], ""));
    if (!file.exists()) {
      file.mkdirs();
    }
    try
    {
      PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("plugins/Sloth/Logs/" + player.getName().toLowerCase() + "/logs.log", true)));Throwable localThrowable3 = null;
      try
      {
        pw.println(log);
      }
      catch (Throwable localThrowable1)
      {
        localThrowable3 = localThrowable1;throw localThrowable1;
      }
      finally
      {
        if (pw != null) {
          if (localThrowable3 != null) {
            try
            {
              pw.close();
            }
            catch (Throwable localThrowable2)
            {
              localThrowable3.addSuppressed(localThrowable2);
            }
          } else {
            pw.close();
          }
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
      System.out.println("[Sloth] Warning: Unable to write log for " + player.getName() + "!");
    }
  }
  
  public static void writeToFile(String text, String fileName)
  {
    Writer fileWriter = null;
    try
    {
      File file = new File(fileName.replace(fileName.split("/")[(fileName.split("/").length - 1)], ""));
      if (!file.exists()) {
        file.mkdirs();
      }
      fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
      fileWriter.write(text); return;
    }
    catch (IOException localIOException) {}finally
    {
      try
      {
        fileWriter.close();
      }
      catch (Exception localException2) {}
    }
  }
}
