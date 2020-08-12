package com.tesi.datamasking.core;

import com.github.javafaker.Faker;
import com.tesi.datamasking.algorithm.fpe.FPE_Impl;
import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.customers.CustomersRepository;
import com.tesi.datamasking.data.db.employees.Employees;
import com.tesi.datamasking.data.db.employees.EmployeesRepository;
import com.tesi.datamasking.data.db.payslips.Payslips;
import com.tesi.datamasking.data.db.payslips.PayslipsRepository;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class DataMaskingFacade {

  private final EmployeesRepository employeesRepository;
  private final PayslipsRepository payslipsRepository;
  private final CustomersRepository customersRepository;
  private final Faker faker;

  @Autowired
  public DataMaskingFacade(EmployeesRepository employeesRepository,
      PayslipsRepository payslipsRepository,
      CustomersRepository customersRepository,
      Faker faker) {
    this.payslipsRepository = payslipsRepository;
    this.employeesRepository = employeesRepository;
    this.customersRepository = customersRepository;
    this.faker = faker;
  }

  public Employees saveEmployee(Employees employee) {
    return employeesRepository.save(employee);
  }

  public Payslips savePayslip(Payslips payslips) {
    return payslipsRepository.save(payslips);
  }

  public Customers saveCustomer(Customers customer) {
    return customersRepository.save(customer);
  }

  public List<Employees> getAllEmployees() {
    return employeesRepository.findAll();
  }

  public void deleteAllEmployees() {
    employeesRepository.deleteAll();
  }

  public void deleteAllPayslips() {
    payslipsRepository.deleteAll();
  }

  public void deleteAllCustomers() {
    customersRepository.deleteAll();
  }

  public void cryptAllEmployees(PseudonymizationSetup setup,
      String KEY_1,
      String KEY_2) throws IllegalAccessException {
    List<Employees> allCustomers = getAllEmployees();
    FPE_Impl fpe = new FPE_Impl(KEY_1, KEY_2);
    for (Employees customer : allCustomers) {
      fpe.cryptClass(customer, Arrays.asList(setup.fields));
      employeesRepository.save(customer);
    }
  }

  public void decryptAllEmployees(PseudonymizationSetup setup,
      String KEY_1,
      String KEY_2) throws IllegalAccessException {
    List<Employees> allCustomers = getAllEmployees();
    FPE_Impl fpe = new FPE_Impl(KEY_1, KEY_2);
    for (Employees customer : allCustomers) {
      fpe.decryptClass(customer, Arrays.asList(setup.fields));
      employeesRepository.save(customer);
    }
  }

  public void populateRandomData(long customers,
      long employees,
      int payslip) {
    for (int i = 0; i < customers; i++) {
      Customers customerSaved = saveCustomer(generateRandomCliente());
      for (int j = 0; j < employees; j++) {
        Employees employeeSaved = saveEmployee(generateRandomEmployee(customerSaved));
        EnumEmployeeJob enumEmployeeJob = EnumEmployeeJob.getRandomEmployeeJob();
        for (int z = payslip; z >= 0; z--) {
          for (int month = 1; month <= 12; month++) {
            savePayslip(generateRandomPayslip(employeeSaved, month, 2020 - z, enumEmployeeJob));
          }
        }
      }
    }
  }

  private Payslips generateRandomPayslip(Employees dipendente,
      int mese,
      int anno,
      EnumEmployeeJob enumEmployeeJob) {
    Payslips payslip = new Payslips();
    int min = 1;
    int max = 10;

    payslip.payslipYear = anno;
    payslip.payslipMonth = mese;
    payslip.employees = dipendente;
    payslip.column1 = randomValue(faker, min, max);
    payslip.column2 = randomValue(faker, min, max);
    payslip.column3 = randomValue(faker, min, max);
    payslip.column4 = randomValue(faker, min, max);
    payslip.column5 = randomValue(faker, min, max);
    payslip.column6 = randomValue(faker, min, max);
    payslip.column7 = randomValue(faker, min, max);
    payslip.column8 = randomValue(faker, min, max);
    payslip.column9 = randomValue(faker, min, max);
    payslip.column10 = randomValue(faker, min, max);
    payslip.amount = BigDecimal.valueOf(enumEmployeeJob.getBaseAmount());
    payslip.employeeJob = enumEmployeeJob.name();

    return payslip;

  }

  private String randomValue(Faker faker, int min, int max){
    int value = getRandomNumber(min, max);
    switch (value) {
    case 1: return faker.finance().iban();
    case 2: return faker.business().creditCardNumber();
    case 3: return faker.book().author();
    case 4: return faker.address().city();
    case 5: return faker.company().profession();
    case 6: return faker.company().industry();
    case 7: return faker.beer().name();
    case 8: return faker.commerce().department();
    case 9:
      return faker.name().firstName();
    case 10:
      return faker.name().username();
    default:
      return faker.name().fullName();
    }
  }

  private int getRandomNumber(int min,
      int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

  private Customers generateRandomCliente() {
    Customers customer = new Customers();

    customer.companyName = faker.company().name();
    customer.zipCode = Integer.parseInt(faker.address().zipCode());
    customer.city = faker.address().city();
    customer.address = faker.address().streetAddress();
    customer.vatNumber = faker.numerify("###########");
    customer.phone = faker.phoneNumber().phoneNumber();

    return customer;

  }

  private Employees generateRandomEmployee(Customers cliente) {
    Employees employee = new Employees();

    employee.customers = cliente;
    employee.firstName = faker.name().firstName();
    employee.lastName = faker.name().lastName();
    employee.city = faker.address().city();
    employee.address = faker.address().streetAddress();
    employee.email = faker.internet().emailAddress().replace(" ", "");
    employee.phone = faker.phoneNumber().cellPhone();
    employee.zipCode = Integer.parseInt(faker.address().zipCode());

    return employee;
  }
}
