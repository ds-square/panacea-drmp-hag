
package org.panacea.drmp.hag.domain.humanvulnerabilities;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class HumanVulnerabilitiesMapping {

    private String employeeId;
    private List<String> vulnerabilityIdList;

    public HumanVulnerabilitiesMapping(String employeeId, List<String> vulnerabilityIdList) {
        this.employeeId = employeeId;
        this.vulnerabilityIdList = vulnerabilityIdList;
    }
}
