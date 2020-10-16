
package org.panacea.drmp.hag.domain.reachability;

import lombok.Data;

@Data
@SuppressWarnings("unused")
public class ReachedEmployee {

    private String linkType;
    private String reachedId;

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public void setReachedId(String reachedId) {
        this.reachedId = reachedId;
    }
}
