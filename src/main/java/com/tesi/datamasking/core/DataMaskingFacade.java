package com.tesi.datamasking.core;

import com.github.javafaker.Faker;
import com.tesi.datamasking.algorithm.CryptDecrypt;
import com.tesi.datamasking.context.DataCrypt;
import com.tesi.datamasking.data.db.DataEntityMapper;
import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.customers.CustomersRepository;
import com.tesi.datamasking.data.db.employees.Employees;
import com.tesi.datamasking.data.db.employees.EmployeesDto;
import com.tesi.datamasking.data.db.employees.EmployeesRepository;
import com.tesi.datamasking.data.db.payslips.PayslipKey;
import com.tesi.datamasking.data.db.payslips.Payslips;
import com.tesi.datamasking.data.db.payslips.PayslipsDto;
import com.tesi.datamasking.data.db.payslips.PayslipsRepository;
import com.tesi.datamasking.data.dto.PseudonymizationSetup;
import com.tesi.datamasking.exception.EmployeeNotFoundException;
import com.tesi.datamasking.util.CustomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DataMaskingFacade extends CoreFacade {

  private final EmployeesRepository employeesRepository;
  private final PayslipsRepository payslipsRepository;
  private final CustomersRepository customersRepository;
  private final Faker faker;
  private final CryptDecrypt cryptDecrypt;
  private final DataEntityMapper mapper;

  private int customerId = 1;
  private int employeeId = 1;

  private static final Logger LOGGER = LoggerFactory.getLogger(DataMaskingFacade.class);

  @Autowired
  public DataMaskingFacade(EmployeesRepository employeesRepository,
      PayslipsRepository payslipsRepository,
      CustomersRepository customersRepository,
      Faker faker,
      CryptDecrypt cryptDecrypt,
      DataEntityMapper mapper) {
    this.payslipsRepository = payslipsRepository;
    this.employeesRepository = employeesRepository;
    this.customersRepository = customersRepository;
    this.faker = faker;
    this.cryptDecrypt = cryptDecrypt;
    this.mapper = mapper;
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

  public List<EmployeesDto> getEmployees() {
    return mapper.mapEmployeesList(employeesRepository.findAll());
  }

  public List<PayslipsDto> getAllPayslips() {
    return mapper.mapPayslipsList(payslipsRepository.findAll());
  }

  public PayslipsDto getSinglePayslip(String employeeCode,
      int month,
      int year) throws EmployeeNotFoundException {
    Optional<Payslips> payslip = payslipsRepository.findById(new PayslipKey(employeeCode, month, year));
    if (payslip.isPresent())
      return mapper.mapPayslips(payslip.get());
    else
      throw new EmployeeNotFoundException(
          MessageFormat.format("Employee with code ({0}) month ({1}) year ({2}) not found", employeeCode, month, year));
  }

  public PayslipsDto getSingleMaskedPayslip(String employeeCode,
      int month,
      int year) throws Exception {
    Optional<Payslips> payslip = payslipsRepository.findById(new PayslipKey(employeeCode, month, year));
    if (payslip.isPresent())
      return getDecryptedPayslips(payslip.get());
    else
      throw new EmployeeNotFoundException(
          MessageFormat.format("Employee with code ({0}) month ({1}) year ({2}) not found", employeeCode, month, year));
  }

  public List<PayslipsDto> getPayslips(String employeeCode) {
    return mapper.mapPayslipsList(payslipsRepository.findByKeyEmployeeCode(employeeCode));
  }

  public List<PayslipsDto> getPayslipsMasked(String employeeCode) throws Exception {
    List<Payslips> payslipsList = payslipsRepository.findByKeyEmployeeCode(employeeCode);
    return getDecryptedPayslipsDtos(payslipsList);

  }

  public List<PayslipsDto> getPayslipsMaskedGivenAmount(String employeeCode,
      BigDecimal amount,
      String operator) throws Exception {
    List<PayslipsDto> payslipsList = getDecryptedPayslipsDtos(payslipsRepository.findByKeyEmployeeCode(employeeCode));
    return getPayslipsGivenAmountInList(payslipsList, amount, operator);
  }

  public List<PayslipsDto> getPayslipsGivenAmountInList(List<PayslipsDto> list,
      BigDecimal amount,
      String operator)
      throws Exception {
    Predicate<PayslipsDto> byAmount = null;
    switch (operator) {
    case "<":
      byAmount = payslips -> payslips.amount.compareTo(amount) < 0;
      break;
    case "<=":
      byAmount = payslips -> payslips.amount.compareTo(amount) <= 0;
      break;
    case "=":
      byAmount = payslips -> payslips.amount.compareTo(amount) == 0;
      break;
    case ">":
      byAmount = payslips -> payslips.amount.compareTo(amount) > 0;
      break;
    case ">=":
      byAmount = payslips -> payslips.amount.compareTo(amount) >= 0;
      break;
    default:
      throw new Exception("Invalid operator: " + operator);
    }
    return list.stream().filter(byAmount).collect(Collectors.toList());

  }

  public List<PayslipsDto> getPayslipsGivenAmount(String employeeCode,
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
    return mapper.mapPayslipsList(payslipsList);
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

  public void decryptAllEmployees(PseudonymizationSetup setup)
      throws Exception {
    List<Employees> allEmployees = getAllEmployees();
    for (Employees employee : allEmployees) {
      decryptSingleEntity(setup, employee, employeesRepository);
    }
  }

  public void cryptSingleEmployee(PseudonymizationSetup setup,
      String id)
      throws Exception {
    cryptSingleEntity(setup, employeesRepository.findById(id).get(), employeesRepository);
  }

  public void decryptSingleEmployee(PseudonymizationSetup setup,
      String id)
      throws Exception {
    decryptSingleEntity(setup, employeesRepository.findById(id).get(), employeesRepository);
  }

  public void cryptAllPayslips(PseudonymizationSetup setup)
      throws Exception {
    Pageable pageRequest = PageRequest.of(0, 9600, Sort.Direction.ASC, "key.employeeCode");
    Page<Payslips> onePage = payslipsRepository.findAll(pageRequest);
    List<Payslips> newUpdatedPayslips = new ArrayList<>();
    int counter = 1;

    while (!onePage.isEmpty()) {
      pageRequest = pageRequest.next();

      List<Payslips> currentpageList = onePage.getContent();
      for (Payslips payslip : currentpageList) {
        newUpdatedPayslips.add(
            (Payslips) cryptSingleEntityAndReturn(setup, payslip, payslipsRepository, cryptDecrypt));
      }

      onePage = payslipsRepository.findAll(pageRequest);
      LOGGER.info("Paging... #" + counter++);
    }
    LOGGER.info("Updating...");
    payslipsRepository.updateWithBatchInsert(newUpdatedPayslips);
    LOGGER.info("...done!");
  }

  public void cryptAllEmployees(PseudonymizationSetup setup)
      throws Exception {
    Pageable pageRequest = PageRequest.of(0, 1000);
    Page<Employees> onePage = employeesRepository.findAll(pageRequest);
    List<Employees> newUpdatedPayslips = new ArrayList<>();
    int counter = 1;

    while (!onePage.isEmpty()) {
      pageRequest = pageRequest.next();

      List<Employees> currentpageList = onePage.getContent();
      for (Employees payslip : currentpageList) {
        newUpdatedPayslips.add(
            (Employees) cryptSingleEntityAndReturn(setup, payslip, payslipsRepository, cryptDecrypt));
      }

      onePage = employeesRepository.findAll(pageRequest);
      LOGGER.info("Paging... #" + counter++);
    }
    LOGGER.info("Updating...");
    employeesRepository.updateWithBatchInsert(newUpdatedPayslips);
    LOGGER.info("...done!");
  }

  public void decryptAllPayslips(PseudonymizationSetup setup)
      throws Exception {
    Pageable pageRequest = PageRequest.of(0, 9600, Sort.Direction.ASC, "key.employeeCode");
    Page<Payslips> onePage = payslipsRepository.findAll(pageRequest);
    List<Payslips> newUpdatedPayslips = new ArrayList<>();
    int counter = 1;

    while (!onePage.isEmpty()) {
      pageRequest = pageRequest.next();

      List<Payslips> currentpageList = onePage.getContent();
      for (Payslips payslip : currentpageList) {
        newUpdatedPayslips.add(
            (Payslips) decryptSingleEntityAndReturn(setup, payslip, payslipsRepository, cryptDecrypt));
      }

      onePage = payslipsRepository.findAll(pageRequest);
      LOGGER.info("Paging... #" + counter++);
    }
    LOGGER.info("Updating...");
    payslipsRepository.updateWithBatchInsert(newUpdatedPayslips);
    LOGGER.info("...done!");
  }

  private void cryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository)
      throws Exception {
    cryptDecrypt.cryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  private void decryptSingleEntity(PseudonymizationSetup setup,
      Object entity,
      JpaRepository repository)
      throws Exception {
    cryptDecrypt.decryptClass(entity, Arrays.asList(setup.fields));
    repository.save(entity);
  }

  public void cryptSinglePayslip(PseudonymizationSetup setup,
      PayslipKey id)
      throws Exception {
    cryptSingleEntity(setup, payslipsRepository.findById(id).get(), payslipsRepository);
  }

  public void decryptSinglePayslip(PseudonymizationSetup setup,
      PayslipKey id)
      throws Exception {
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
      Customers customerSaved = CustomUtils.generateRandomCliente(customerId++, faker);
      customerList.add(customerSaved);
      for (int j = 0; j < employees; j++) {
        Employees employeeSaved = CustomUtils.generateRandomEmployee(customerSaved, employeeId++, faker);
        EnumEmployeeJob enumEmployeeJob = EnumEmployeeJob.getRandomEmployeeJob();
        for (int z = payslip; z > 0; z--) {
          for (int month = 1; month <= 12; month++) {
            payslipsList
                .add(CustomUtils.generateRandomPayslip(employeeSaved, month, 2020 - (z - 1), enumEmployeeJob, faker));
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

  public List<EmployeesDto> getEmployeeByFirstNameAndLastName(String name,
      String lastName) {
    return mapper.mapEmployeesList(employeesRepository.findByFirstNameAndLastName(name, lastName));
  }

  public List<EmployeesDto> getEmployeeMaskedByFirstNameAndLastName(String name,
      String lastName) throws Exception {
    DataCrypt nameDataCrypt = CustomUtils.getDataCrypt(Employees.class, "firstName");
    DataCrypt lastNameDataCrypt = CustomUtils.getDataCrypt(Employees.class, "lastName");
    String nameEncrypted = cryptDecrypt.encryptFieldString(name, nameDataCrypt);
    String lastNameEncrypted = cryptDecrypt.encryptFieldString(lastName, lastNameDataCrypt);

    List<Employees> result = employeesRepository.findByFirstNameAndLastName(nameEncrypted, lastNameEncrypted);
    return getDecryptedEmployeesDtos(result);
  }

  private List<PayslipsDto> getDecryptedPayslipsDtos(List<Payslips> result) throws Exception {
    List<PayslipsDto> decryptedResult = new ArrayList<>();

    for (Payslips payslip : result) {
      cryptDecrypt.decryptClass(payslip);
      decryptedResult.add(mapper.mapPayslips(payslip));
    }
    return decryptedResult;
  }

  private PayslipsDto getDecryptedPayslips(Payslips payslips) throws Exception {
    cryptDecrypt.decryptClass(payslips);
    return mapper.mapPayslips(payslips);
  }

  private List<EmployeesDto> getDecryptedEmployeesDtos(List<Employees> result) throws Exception {
    List<EmployeesDto> decryptedResult = new ArrayList<>();

    for (Employees employee : result) {
      cryptDecrypt.decryptClass(employee);
      decryptedResult.add(mapper.mapEmployees(employee));
    }
    return decryptedResult;
  }

  public List<EmployeesDto> getEmployeeByCustomerCode(String customerCode) {
    return mapper.mapEmployeesList(employeesRepository.findByCustomers_CustomerCode(customerCode));
  }

  public List<EmployeesDto> getEmployeeMaskedByCustomerCode(String customerCode) throws Exception {
    List<Employees> result = employeesRepository.findByCustomers_CustomerCode(customerCode);
    return getDecryptedEmployeesDtos(result);
  }

  @Transactional
  public void insertInBatchMode(long customers) {
    List<Customers> customerList = new ArrayList<>();

    for (int i = 0; i < customers; i++) {
      Customers customerSaved = CustomUtils.generateRandomCliente(customerId++, faker);
      customerList.add(customerSaved);
    }
    customersRepository.insertWithBatchInsert(customerList);
  }


}
