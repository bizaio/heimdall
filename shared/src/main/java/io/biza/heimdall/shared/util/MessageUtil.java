package io.biza.heimdall.shared.util;

import org.slf4j.helpers.MessageFormatter;

public class MessageUtil {

  public static String format(String inputMessage, Object... objects) {
    return MessageFormatter.format(inputMessage, objects).toString();
  }



}
