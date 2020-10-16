package org.panacea.drmp.hag.service;


import org.panacea.drmp.hag.domain.employees.EmployeeInventory;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilityCatalog;
import org.panacea.drmp.hag.domain.reachability.HumanReachabilityInventory;
import org.panacea.drmp.hag.domain.securityprofiles.CyberSecurityProfileInventory;

public interface HAGInputRequestService {

    EmployeeInventory performEmployeeInventoryRequest(String snapshotId);

    CyberSecurityProfileInventory performCyberSecurityProfileInventoryRequest(String snapshotId);

    HumanVulnerabilityCatalog performHumanVulnerabilityCatalog(String snapshotId);

    HumanReachabilityInventory performReachabilityInventoryRequest(String snapshotId);

}
