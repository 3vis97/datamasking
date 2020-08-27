package com.tesi.datamasking.util;

import com.tesi.datamasking.context.DataCrypt;

import java.lang.reflect.Field;

public class CustomUtils {

  public static DataCrypt getDataCrypt(Object clazzToScan,
      String fieldValue) throws Exception {
    Field[] classFields = clazzToScan.getClass().getFields();
    for (Field field : classFields) {
      if (field.getName().equals(fieldValue))
        if (field.isAnnotationPresent(DataCrypt.class)) {
          return field.getAnnotation(DataCrypt.class);
        }
    }
    throw new Exception("DataCrypt annotation not found!");
  }
}
