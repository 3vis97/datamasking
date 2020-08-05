package com.tesi.datamasking.context;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DataCrypt {

  public DataType dataType() default DataType.DEFAULT_STRING;

  public enum DataType {
    FIRST_NAME, LAST_NAME, PHONE, EMAIL, AMOUNT, DEFAULT_STRING
  }
}
