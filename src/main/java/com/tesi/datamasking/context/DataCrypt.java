package com.tesi.datamasking.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataCrypt {

  DataType dataType() default DataType.DEFAULT_STRING;

  enum DataType {
    EMAIL, AMOUNT, UNICODE, NUMBER, LONG_STRING, PHONE, DEFAULT_STRING
  }
}
