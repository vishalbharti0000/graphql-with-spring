package org.vishal.bharti.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Table("employees")
@Data
@NoArgsConstructor
public class Employee {
    @Id
    private Long id;
    private String name;
    private double salary;
    private Long departmentId;
    public Employee(Long id, String name, double salary, Long departmentId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.departmentId = departmentId;
    }
}