package org.panacea.drmp.hag.domain.graph;

import java.util.Objects;

public class HPrivilege {
    public String employeeId;
    public String uuid;
    public String privLevel;

    public HPrivilege() {

    }

    public HPrivilege(String employeeId, String uuid, String privLevel) {
        this.employeeId = employeeId;
        this.uuid = uuid;
        this.privLevel = privLevel;
    }

    @Override
    public String toString() {
        return "HPrivilege{" +
                "deviceHostname='" + employeeId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", privLevel='" + privLevel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HPrivilege that = (HPrivilege) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
