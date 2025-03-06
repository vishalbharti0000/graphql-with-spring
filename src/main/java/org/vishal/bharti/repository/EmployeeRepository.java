package org.vishal.bharti.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vishal.bharti.entity.Employee;
import org.vishal.bharti.repository.transformer.EmployeeDepartmentResponse;
import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {
    Flux<Employee> findByDepartmentId(Long departmentId);

    @Query("select e.id as id, e.name as name, e.salary salary" +
            ", d.id as departmentId, d.name as departmentName " +
            "from employees e join departments d " +
            "on e.department_id = d.id")
    Flux<EmployeeDepartmentResponse> findAllFromQuery();
}
