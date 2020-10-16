package org.panacea.drmp.hag.service;

import org.panacea.drmp.hag.domain.graph.HumanLayerAttackGraphRepr;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilityInventory;

public interface HAGPostOutputService {
    void postHumanLayerAttackGraphRepr(HumanLayerAttackGraphRepr repr);

    void postHumanVulnerabilityInventory(HumanVulnerabilityInventory hvi);
}

