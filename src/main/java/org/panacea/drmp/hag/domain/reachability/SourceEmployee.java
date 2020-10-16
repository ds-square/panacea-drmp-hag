
package org.panacea.drmp.hag.domain.reachability;

import lombok.Data;

import java.util.List;

@Data
@SuppressWarnings("unused")
public class SourceEmployee {
    private String id;
    private List<ReachedEmployee> reachedEmployee;

    public void setId(String id) {
        this.id = id;
    }

    public void setReachedEmployee(List<ReachedEmployee> reachedEmployee) {
        this.reachedEmployee = reachedEmployee;
    }
}
