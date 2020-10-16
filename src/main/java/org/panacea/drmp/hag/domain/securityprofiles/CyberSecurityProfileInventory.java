
package org.panacea.drmp.hag.domain.securityprofiles;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class CyberSecurityProfileInventory {

    private List<CyberSecurityProfile> cyberSecurityProfiles;
    private String environment;
    private String fileType;
    private String snapshotId;
    private String snapshotTime;

}
