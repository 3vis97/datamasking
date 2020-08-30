package com.tesi.datamasking.util;

import com.github.javafaker.Faker;
import com.tesi.datamasking.context.DataCrypt;
import com.tesi.datamasking.core.EnumEmployeeJob;
import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.employees.Employees;
import com.tesi.datamasking.data.db.payslips.PayslipKey;
import com.tesi.datamasking.data.db.payslips.Payslips;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;

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

  public static Payslips generateRandomPayslip(Employees employee,
      int month,
      int year,
      EnumEmployeeJob enumEmployeeJob,
      Faker faker) {
    Payslips payslip = new Payslips();
    int min = 1;
    int max = 10;

    payslip.key = new PayslipKey(employee.employeeCode, month, year);
    payslip.employees = employee;
    payslip.column1 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column2 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column3 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column4 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column5 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column6 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column7 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column8 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column9 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.column10 = StringUtils.left(randomValue(faker, min, max), 100);
    payslip.amount = BigDecimal.valueOf(enumEmployeeJob.getBaseAmount());
    payslip.employeeJob = enumEmployeeJob.name();

    return payslip;

  }

  public static String randomValue(Faker faker,
      int min,
      int max) {
    int value = getRandomNumber(min, max);
    switch (value) {
    case 1:
      return faker.finance().iban();
    case 2:
      return faker.business().creditCardNumber();
    case 3:
      return faker.book().author();
    case 4:
      return faker.address().city();
    case 5:
      return faker.company().profession();
    case 6:
      return faker.company().industry();
    case 7:
      return faker.beer().name();
    case 8:
      return faker.commerce().department();
    case 9:
      return faker.name().firstName();
    case 10:
      return faker.name().username();
    default:
      return faker.name().fullName();
    }
  }

  private static int getRandomNumber(int min,
      int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  public static Customers generateRandomCliente(int customerId,
      Faker faker) {
    Customers customer = new Customers();

    customer.customerCode = generateNextCustomerCode(customerId);
    customer.companyName = faker.company().name();
    customer.zipCode = Integer.parseInt(faker.address().zipCode());
    customer.city = faker.address().city();
    customer.address = faker.address().streetAddress();
    customer.vatNumber = faker.numerify("###########");
    customer.phone = faker.phoneNumber().phoneNumber();

    return customer;

  }

  public static Employees generateRandomEmployee(Customers cliente,
      int employeeId,
      Faker faker) {
    Employees employee = new Employees();

    employee.customers = cliente;
    employee.employeeCode = generateNextEmployeeCode(employeeId);
    employee.firstName = faker.name().firstName();
    employee.lastName = faker.name().lastName();
    employee.city = faker.address().city();
    employee.address = faker.address().streetAddress();
    employee.email = faker.internet().emailAddress().replace(" ", "");
    employee.phone = faker.phoneNumber().cellPhone();
    employee.zipCode = Integer.parseInt(faker.address().zipCode());

    return employee;
  }

  private static String generateNextCustomerCode(int customerId) {
    return "C" + String.format("%05d", customerId);
  }

  private static String generateNextEmployeeCode(int employeeId) {
    return "E" + String.format("%07d", employeeId);
  }
}
