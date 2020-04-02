package io.biza.heimdall.shared.util;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.helpers.MessageFormatter;

public class MessageUtil {

  public static String format(String inputMessage, Object... objects) {
    return MessageFormatter.arrayFormat(inputMessage, objects).getMessage();
  }



}
