package se.bjurr.violations.lib.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Utils {
  public static <T> T checkNotNull(T reference, String errorMessage) {
    if (reference == null) {
      throw new NullPointerException(errorMessage);
    }
    return reference;
  }

  public static String emptyToNull(String str) {
    if (str == null || str.isEmpty()) {
      return null;
    }
    return str;
  }

  public static <T> T firstNonNull(T f, T s) {
    if (f != null) {
      return f;
    }
    return s;
  }

  @SuppressWarnings("static-access")
  public static InputStream getResource(String filename) {
    return Thread.currentThread().getContextClassLoader().getSystemResourceAsStream(filename);
  }

  public static boolean isNullOrEmpty(String str) {
    return str == null || str.isEmpty();
  }

  @SuppressWarnings("resource")
  public static String toString(InputStream inputStream) throws IOException {
    Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
    String result = scanner.hasNext() ? scanner.next() : "";
    scanner.close();
    inputStream.close();

    return result;
  }

  public static String toString(URL resource) throws IOException {
    try {
      return toString(resource.openStream());
    } catch (IOException e) {
      throw e;
    }
  }
}
