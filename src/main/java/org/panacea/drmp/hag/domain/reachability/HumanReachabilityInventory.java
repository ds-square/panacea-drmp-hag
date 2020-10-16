
package org.panacea.drmp.hag.domain.reachability;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class HumanReachabilityInventory {

    private String environment;
    private String fileType;
    private String snapshotId;
    private String snapshotTime;
    private List<SourceEmployee> sourceEmployees;

}
