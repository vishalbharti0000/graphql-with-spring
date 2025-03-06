package org.vishal.bharti.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.vishal.bharti.dto.EmployeeResponse;
import org.vishal.bharti.entity.Department;
import org.vishal.bharti.entity.Employee;
import org.vishal.bharti.service.EmployeeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

//    @QueryMapping
//    public Flux<EmployeeResponse> getEmployees() {
//        return service.getAllEmployees()
//                .flatMap(employee -> service.getDepartmentById(employee.getDepartmentId())
//                        .defaultIfEmpty(new Department(null, "Unknown Department")) // ✅ Handle null case
//                        .map(department -> EmployeeResponse.builder()
//                                .id(employee.getId())
//                                .name(employee.getName())
//                                .salary(employee.getSalary())
//                                .department(department)
//                                .build()
//                        ));
//    }

    @QueryMapping
    public Flux<EmployeeResponse> getEmployees() {
        return service.getAllEmployeesQueryWay();
    }

    @QueryMapping
    public Mono<EmployeeResponse> getEmployeeById(@Argument Long id) {
        return service.getEmployeeById(id)
                .flatMap(employee -> service.getDepartmentById(employee.getDepartmentId())
                        .map(department -> EmployeeResponse.builder()
                                .id(employee.getId())
                                .name(employee.getName())
                                .salary(employee.getSalary())
                                .department(department)
                                .build()
                        ));
    }

    @MutationMapping
    public Mono<EmployeeResponse> addEmployee(@Argument String name, @Argument double salary, @Argument Long departmentId) {
        return service.addEmployee(new Employee(null, name, salary, departmentId));
    }
}

