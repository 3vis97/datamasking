package com.tesi.datamasking.data.db;

import com.tesi.datamasking.data.db.customers.Customers;
import com.tesi.datamasking.data.db.customers.CustomersDto;
import com.tesi.datamasking.data.db.employees.Employees;
import com.tesi.datamasking.data.db.employees.EmployeesDto;
import com.tesi.datamasking.data.db.payslips.Payslips;
import com.tesi.datamasking.data.db.payslips.PayslipsDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface DataEntityMapper {

  Customers mapCustomers(CustomersDto customerDto);

  CustomersDto mapCustomers(Customers customers);

  List<Customers> mapCustomersListDto(List<CustomersDto> customerDtos);

  List<CustomersDto> mapCustomersList(List<Customers> customers);

  Employees mapEmployees(EmployeesDto employeesDto);

  EmployeesDto mapEmployees(Employees employees);

  List<Employees> mapEmployeesListDto(List<EmployeesDto> employeesDto);

  List<EmployeesDto> mapEmployeesList(List<Employees> employees);

  Payslips mapPayslips(PayslipsDto payslipsDto);

  PayslipsDto mapPayslips(Payslips payslips);

  List<Payslips> mapPayslipsListDto(List<PayslipsDto> payslipsDto);

  List<PayslipsDto> mapPayslipsList(List<Payslips> payslips);

}
