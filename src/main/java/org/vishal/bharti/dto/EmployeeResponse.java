package org.vishal.bharti.dto;

import lombok.Builder;
import lombok.Data;
import org.vishal.bharti.entity.Department;

@Data
@Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private double salary;
    private Department department;
}
