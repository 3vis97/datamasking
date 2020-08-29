package com.tesi.datamasking.util;

import com.tesi.datamasking.context.DataCrypt;

import java.lang.reflect.Field;

public class CustomUtils {

  public static DataCrypt getDataCrypt(Class clazzToScan,
      String fieldName) throws Exception {
    Field[] classFields = clazzToScan.getFields();
    for (Field field : classFields) {
      if (field.getName().equals(fieldName))
        if (field.isAnnotationPresent(DataCrypt.class)) {
          return field.getAnnotation(DataCrypt.class);
        }
    }
    throw new Exception("DataCrypt annotation not found!");
  }
}
