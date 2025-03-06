package org.vishal.bharti.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.vishal.bharti.entity.Department;

public interface DepartmentRepository extends ReactiveCrudRepository<Department, Long> {
}
