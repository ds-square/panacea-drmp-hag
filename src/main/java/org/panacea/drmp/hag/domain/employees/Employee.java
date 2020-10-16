
package org.panacea.drmp.hag.domain.employees;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class Employee {

    private String employeeName;
    private String id;
    private List<Location> locations;
    private List<Role> roles;

}
