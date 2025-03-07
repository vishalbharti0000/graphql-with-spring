package org.vishal.bharti.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vishal.bharti.dto.EmployeeResponse;
import org.vishal.bharti.entity.Department;
import org.vishal.bharti.entity.Employee;
import org.vishal.bharti.repository.DepartmentRepository;
import org.vishal.bharti.repository.EmployeeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Flux<EmployeeResponse> getAllEmployeesQueryWay() {
        return employeeRepository.findAllFromQuery()
                .log()
//                .doOnNext(employeeDepartmentResponse ->
//                        System.out.println("Fetched: " + employeeDepartmentResponse)
//                ) // Debugging: Log each object
                .map(employeeDepartmentResponse -> EmployeeResponse.builder()
                .id(employeeDepartmentResponse.getId())
                .name(employeeDepartmentResponse.getName())
                .salary(employeeDepartmentResponse.getSalary())
                .department(Department.builder()
                                .id(employeeDepartmentResponse.getDepartmentId())
                                .name(employeeDepartmentResponse.getDepartmentName())
                                .build()
                )
                .build()
        );
    }

    public Mono<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Flux<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public Mono<EmployeeResponse> addEmployee(Employee employee) {
        return departmentRepository.findById(employee.getDepartmentId()) // ✅ Step 1: Check if department exists
                .switchIfEmpty(Mono.error(new RuntimeException("Department not found!")))
                .flatMap(department ->
                        employeeRepository.save(employee) // ✅ Step 2: Save employee
                                .map(savedEmployee -> EmployeeResponse.builder()
                                        .id(savedEmployee.getId())
                                        .name(savedEmployee.getName())
                                        .salary(savedEmployee.getSalary())
                                        .department(department)
                                        .build())
                );
    }

    public Mono<EmployeeResponse> updateEmployee(Long id, String name, Double salary, Long departmentId) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Employee not found!")))
                .flatMap(employee -> {
                    // Update employee fields if provided
                    if (name != null) employee.setName(name);
                    if (salary != null) employee.setSalary(salary);
                    if (departmentId != null) employee.setDepartmentId(departmentId);

                    // Save updated employee
                    return employeeRepository.save(employee);
                })
                // Fetch department if departmentId was updated
                .flatMap(savedEmployee -> getDepartmentById(savedEmployee.getDepartmentId())
                        .defaultIfEmpty(new Department(null, "Unknown Department"))
                        .map(department -> EmployeeResponse.builder()
                                .id(savedEmployee.getId())
                                .name(savedEmployee.getName())
                                .salary(savedEmployee.getSalary())
                                .department(department)
                                .build())
                ).log();
    }


    public Mono<Department> getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId);
    }

    public Mono<String> deleteEmployee(Long id) {
        return employeeRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Employee not found!")))
                .flatMap(employee -> employeeRepository.delete(employee)
                        .thenReturn("Employee with ID " + id + " deleted successfully!")
                );
    }
}
