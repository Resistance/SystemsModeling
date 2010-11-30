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
  private static final String HISTORY_FILE_NAME = "mancala_hist.txt";

  public static String readAllReverse() {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(HISTORY_FILE_NAME)));
      StringBuilder lines = new StringBuilder();
      String line;
      boolean empty = true;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.isEmpty()) {
          lines.insert(0, '\n');
          lines.insert(0, line);
          empty = false;
        }
      }
      if (!empty) {
        return lines.toString();
      }
    } catch (FileNotFoundException ignored) {
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  public static void append(String s) {
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(new FileOutputStream(HISTORY_FILE_NAME, true));
      writer.println(s);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }
}
