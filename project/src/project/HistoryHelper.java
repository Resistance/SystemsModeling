package project;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: gert
 * Date: 30.11.2010
 * Time: 0:24:13
 * To change this template use File | Settings | File Templates.
 */
public class HistoryHelper {
  private static final String HISTORY_FILE_NAME = "history.txt";

  public static String readAllReverse() {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(HISTORY_FILE_NAME)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (reader != null) {
      StringBuilder sb = new StringBuilder();
      String s;
      try {
        while ((s = reader.readLine()) != null) {
          sb.insert(0, '\n');
          sb.insert(0, s);
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return sb.toString();
    }
    return null;
  }

  static void append(String s) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(HISTORY_FILE_NAME, true)));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (writer != null) {
      try {
        writer.println(s);
      } finally {
        writer.close();
      }
    }
  }
}
