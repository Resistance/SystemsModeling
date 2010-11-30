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
  private static String EMPTY_MESSAGE = "-- No entries yet. Play more!";

  public static String readAllReverse() {
    BufferedReader reader = getFileReader();

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
    PrintWriter writer = getFileWriter();

    if (writer != null) {
      try {
        writer.println(s);
      } finally {
        writer.close();
      }
    }
  }
  
  private static BufferedReader getFileReader() {

    try {
      // Create file if it does not exist
      boolean created = new File(HISTORY_FILE_NAME).createNewFile();
      if (created) {
        PrintWriter writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(HISTORY_FILE_NAME, true)));
        writer.println(EMPTY_MESSAGE);
        writer.close();
      }
      return new BufferedReader(new InputStreamReader(new FileInputStream(HISTORY_FILE_NAME)));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return null;
  }

  private static PrintWriter getFileWriter() {
    PrintWriter writer = null;

    try {
      // Create file if it does not exist
      File history = new File(HISTORY_FILE_NAME);
      boolean created = history.createNewFile();

      if (!created) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(HISTORY_FILE_NAME)));

        if (reader.readLine().equals(EMPTY_MESSAGE)) {
          history.delete();
          history.createNewFile();
        }
      }

      return new PrintWriter(new BufferedOutputStream(new FileOutputStream(HISTORY_FILE_NAME, true)));
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return null;
  }
}
