
package org.panacea.drmp.hag.domain.securityprofiles;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class CyberSecurityProfile {

    private String employeeId;
    private double individualSecurityAttitude;
    private double securityBehavior;
    private double securityCultureAtWork;
    private double securityTrainingLevel;
    private double trustInBuildingSecurity;
    private double trustInColleagues;

}
