package org.panacea.drmp.hag.domain.graph;

import lombok.Data;

import java.util.List;


@Data
@SuppressWarnings("unused")
public class HumanLayerAttackGraphRepr {
    private String environment;
    private String fileType;
    private String snapshotId;
    private String snapshotTime;
    private List<HPrivilege> nodes;
    private List<HExploitRepr> edges;

    public HumanLayerAttackGraphRepr(String environment, String snapshotId, String snapshotTime, List<HPrivilege> nodes, List<HExploitRepr> edges) {
        this.environment = environment;
        this.fileType = "HumanLayerAttackGraphRepr";
        this.snapshotId = snapshotId;
        this.snapshotTime = snapshotTime;
        this.nodes = nodes;
        this.edges = edges;
    }

}
