package com.tesi.datamasking.web;

import com.google.common.base.Stopwatch;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

public class CoreController {

  protected String formatPattern(String method,
      Stopwatch stopWatch) {
    return MessageFormat
        .format(method + " completed in {0} seconds, {1} millseconds", stopWatch.elapsed(TimeUnit.SECONDS),
            stopWatch.elapsed(TimeUnit.MILLISECONDS));
  }

}
