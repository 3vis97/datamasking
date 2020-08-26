package com.tesi.datamasking.core;

import com.github.javafaker.Faker;
import com.google.common.base.Stopwatch;
import com.tesi.datamasking.algorithm.CryptDecrypt;
import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.customers.CustomersRepository;
import com.tesi.datamasking.data.db.employees.Employees;
import com.tesi.datamasking.data.db.employees.EmployeesRepository;
import com.tesi.datamasking.data.db.payslips.PayslipKey;
import com.tesi.datamasking.data.db.payslips.Payslips;
import com.tesi.datamasking.data.db.payslips.PayslipsRepository;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import com.tesi.datamasking.exception.EmployeeNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DataMaskingFacade {

  private final EmployeesRepository employeesRepository;
  private final PayslipsRepository payslipsRepository;
  private final CustomersRepository customersRepository;
  private final Faker faker;
  private final CryptDecrypt cryptDecrypt;

  private int customerId = 1;
  private int employeeId = 1;

  private static final Logger LOGGER = LoggerFactory.getLogger(DataMaskingFacade.class);

  @Autowired
  public DataMaskingFacade(EmployeesRepository employeesRepository,
      PayslipsRepository payslipsRepository,
      CustomersRepository customersRepository,
      Faker faker,
      CryptDecrypt cryptDecrypt) {
    this.payslipsRepository = payslipsRepository;
    this.employeesRepository = employeesRepository;
    this.customersRepository = customersRepository;
    this.faker = faker;
    this.cryptDecrypt = cryptDecrypt;
  }

  private Employees saveEmployee(Employees employee) {
    return employeesRepository.save(employee);
  }

  private Payslips savePayslip(Payslips payslips) {
    return payslipsRepository.save(payslips);
  }

  private void saveAllPayslip(List<Payslips> payslipsList) {
    payslipsRepository.saveAll(payslipsList);
  }

  private Customers saveCustomer(Customers customer) {
    return customersRepository.save(customer);
  }

  private List<Employees> getAllEmployees() {
    return employeesRepository.findAll();
  }

  public List<Payslips> getAllPayslips() {
    return payslipsRepository.findAll();
  }

  public Payslips getSinglePayslip(String employeeCode,
      int month,
      int year) throws EmployeeNotFoundException {
    Optional<Payslips> payslip = payslipsRepository.findById(new PayslipKey(employeeCode, month, year));
    if (payslip.isPresent())
      return payslip.get();
    else
      throw new EmployeeNotFoundException(
          MessageFormat.format("Employee with code ({0}) month ({1}) year ({2}) not found", employeeCode, month, year));
  }

  public List<Payslips> getPayslips(String employeeCode) {
    return payslipsRepository.findByKeyEmployeeCode(employeeCode);
  }

  public List<Payslips> getPayslipsGivenAmount(String employeeCode,
      BigDecimal amount,
      String operator) {
    List<Payslips> payslipsList = new ArrayList<>();

    switch (operator) {
    case "<":
      payslipsList = payslipsRepository.findByKeyEmployeeCodeAndAmountLessThan(employeeCode, amount);
      break;
    case "<=":
      payslipsList = payslipsRepository.findByKeyEmployeeCodeAndAmountLessThanEqual(employeeCode, amount);
      break;
    case "=":
      payslipsList = payslipsRepository.findByKeyEmployeeCodeAndAmountIs(employeeCode, amount);
      break;
    case ">":
      payslipsList = payslipsRepository.findByKeyEmployeeCodeAndAmountGreaterThan(employeeCode, amount);
      break;
    case ">=":
      payslipsList = payslipsRepository.findByKeyEmployeeCodeAndAmountGreaterThanEqual(employeeCode, amount);
      break;
    }
    return payslipsList;
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

  public void cryptAllEmployees(PseudonymizationSetup setup)
      throws IllegalAccessException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
      IllegalBlockSizeException {
    List<Employees> allEmployees = getAllEmployees();
    for (Employees employee : allEmployees) {
      decryptSingleEntity(setup, employee, employeesRepository);
    }
  }

  public void decryptAllEmployees(PseudonymizationSetup setup)
      throws IllegalAccessException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
      IllegalBlockSizeException {
    List<Employees> allEmployees = getAllEmployees();
    for (Employees employee : allEmployees) {
      cryptSingleEntity(setup, employee, employeesRepository);
    }
  }

  public void cryptSingleEmployee(PseudonymizationSetup setup,
      String id)
      throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
      IllegalAccessException {
    cryptSingleEntity(setup, employeesRepository.findById(id).get(), employeesRepository);
  }

  public void decryptSingleEmployee(PseudonymizationSetup setup,
      String id)
      throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
      IllegalAccessException {
    decryptSingleEntity(setup, employeesRepository.findById(id).get(), employeesRepository);
  }

  public void cryptAllPayslips(PseudonymizationSetup setup)
      throws IllegalAccessException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
      IllegalBlockSizeException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    List<Payslips> allPayslips = getAllPayslips();
    stopwatch.stop();
    LOGGER.info("cryptAllPayslips QUERY-GETALL completed in " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    stopwatch.reset();
    stopwatch.start();
    for (Object payslip : allPayslips) {
      cryptSingleEntity(setup, payslip, payslipsRepository);
    }
    stopwatch.stop();
    LOGGER.info("cryptAllPayslips CRYPTALL completed in " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  public void decryptAllPayslips(PseudonymizationSetup setup)
      throws IllegalAccessException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException,
      IllegalBlockSizeException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    List<Payslips> allPayslips = getAllPayslips();
    stopwatch.stop();
    LOGGER.info("decryptAllPayslips QUERY-GETALL completed in " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    stopwatch.reset();
    stopwatch.start();
    for (Object payslip : allPayslips) {
      decryptSingleEntity(setup, payslip, payslipsRepository);
    }
    stopwatch.stop();
    LOGGER.info("decryptAllPayslips CRYPTALL completed in " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  private void cryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository)
      throws IllegalAccessException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      InvalidKeyException {
    cryptDecrypt.cryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  private void decryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository)
      throws IllegalAccessException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
      InvalidKeyException {
    cryptDecrypt.decryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  public void cryptSinglePayslip(PseudonymizationSetup setup,
      PayslipKey id)
      throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
      IllegalAccessException {
    cryptSingleEntity(setup, payslipsRepository.findById(id).get(), payslipsRepository);
  }

  public void decryptSinglePayslip(PseudonymizationSetup setup,
      PayslipKey id)
      throws InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
      IllegalAccessException {
    decryptSingleEntity(setup, payslipsRepository.findById(id).get(), payslipsRepository);
  }



  public void performVoidEmployeeUpdate(String employeeCode) {
    employeesRepository.save(employeesRepository.findById(employeeCode).get());
  }

  public void populateRandomData(long customers,
      long employees,
      int payslip) {
    List<Customers> customerList = new ArrayList<>();
    List<Payslips> payslipsList = new ArrayList<>();
    LOGGER.info("Starting creating random fake data...");
    for (int i = 0; i < customers; i++) {
      Customers customerSaved = generateRandomCliente(customerId++);
      customerList.add(customerSaved);
      for (int j = 0; j < employees; j++) {
        Employees employeeSaved = generateRandomEmployee(customerSaved, employeeId++);
        EnumEmployeeJob enumEmployeeJob = EnumEmployeeJob.getRandomEmployeeJob();
        for (int z = payslip; z > 0; z--) {
          for (int month = 1; month <= 12; month++) {
            payslipsList.add(generateRandomPayslip(employeeSaved, month, 2020 - (z - 1), enumEmployeeJob));
          }
        }
      }
      LOGGER.info("...completed " + (i + 1) + " of " + customers + " customers!");
    }
    LOGGER.info("Persist in db...");
    customersRepository.insertWithBatchInsert(customerList);
    payslipsRepository.insertWithBatchInsert(payslipsList);
    LOGGER.info("...done!");
  }

  public List<Employees> getEmployeeByFirstNameAndLastName(String name,
      String lastName) {
    return employeesRepository.findByFirstNameAndLastName(name, lastName);
  }

  public List<Employees> getEmployeeByCustomerCode(String customerCode) {
    return employeesRepository.findByCustomerCode(customerCode);
  }

  @Transactional
  public void insertInBatchMode(long customers) {
    List<Customers> customerList = new ArrayList<>();

    for (int i = 0; i < customers; i++) {
      Customers customerSaved = generateRandomCliente(customerId++);
      customerList.add(customerSaved);
    }
    customersRepository.insertWithBatchInsert(customerList);
  }

  private Payslips generateRandomPayslip(Employees employee,
      int month,
      int year,
      EnumEmployeeJob enumEmployeeJob) {
    Payslips payslip = new Payslips();
    int min = 1;
    int max = 10;

    payslip.key = new PayslipKey(employee.employeeCode, month, year);
    payslip.employees = employee;
    payslip.column1 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column2 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column3 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column4 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column5 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column6 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column7 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column8 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column9 = StringUtils.left(randomValue(faker, min, max), 150);
    payslip.column10 = StringUtils.left(randomValue(faker, min, max), 150);
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

  private Customers generateRandomCliente(int customerId) {
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

  private Employees generateRandomEmployee(Customers cliente,
      int employeeId) {
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

  private String generateNextCustomerCode(int customerId) {
    return "C" + String.format("%05d", customerId);
  }

  private String generateNextEmployeeCode(int employeeId) {
    return "E" + String.format("%07d", employeeId);
  }
}
