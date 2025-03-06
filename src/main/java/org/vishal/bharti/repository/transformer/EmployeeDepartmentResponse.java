package org.vishal.bharti.repository.transformer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDepartmentResponse {
    private Long id;
    private String name;
    private double salary;
    @Column("departmentId")
    private Long departmentId;
    @Column("departmentName")
    private String departmentName;
}
