
package org.panacea.drmp.hag.domain.employees;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class EmployeeInventory {

    private List<Employee> employees;
    private String environment;
    private String fileType;
    private String snapshotId;
    private String snapshotTime;

}
