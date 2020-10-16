package org.panacea.drmp.hag;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.hag.domain.employees.Employee;
import org.panacea.drmp.hag.domain.employees.EmployeeInventory;
import org.panacea.drmp.hag.domain.graph.HExploitRepr;
import org.panacea.drmp.hag.domain.graph.HPrivilege;
import org.panacea.drmp.hag.domain.graph.HumanLayerAttackGraphRepr;
import org.panacea.drmp.hag.domain.graph.Vulnerability;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilitiesMapping;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerability;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilityCatalog;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilityInventory;
import org.panacea.drmp.hag.domain.notifications.DataNotification;
import org.panacea.drmp.hag.domain.reachability.HumanReachabilityInventory;
import org.panacea.drmp.hag.domain.reachability.ReachedEmployee;
import org.panacea.drmp.hag.domain.reachability.SourceEmployee;
import org.panacea.drmp.hag.domain.securityprofiles.CyberSecurityProfile;
import org.panacea.drmp.hag.domain.securityprofiles.CyberSecurityProfileInventory;
import org.panacea.drmp.hag.exception.HAGException;
import org.panacea.drmp.hag.service.HAGInputRequestService;
import org.panacea.drmp.hag.service.HAGPostOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class HAGGenerator {

    @Autowired
    HAGInputRequestService hagInputRequestService;

    @Autowired
    HAGPostOutputService hagPostOutputService;

    private EmployeeInventory employeeInventory;
    private CyberSecurityProfileInventory cyberSecurityProfileInventory;
    private HumanVulnerabilityCatalog humanVulnerabilityCatalog;
    private HumanReachabilityInventory humanReachabilityInventory;


    @Synchronized
    public void generateHAG(DataNotification notification) {
        try {
            this.getInput(notification.getSnapshotId());
        } catch (HAGException e) {
            log.error(e.getMessage());
        }

        // Build data structures
        Map<String, HumanVulnerability> vulnerabilities = new HashMap<String, HumanVulnerability>();
        Map<String, Employee> employees = new HashMap<String, Employee>();
        Map<String, CyberSecurityProfile> cpsMap = new HashMap<String, CyberSecurityProfile>();
        Map<String, Set<String>> humanVulnerabilitiesMap = new HashMap<String, Set<String>>();
        List<HumanVulnerabilitiesMapping> humanVulnerabilitiesMapping = new ArrayList<HumanVulnerabilitiesMapping>();

        Set<String> uuids = new HashSet<String>();
        List<HPrivilege> nodes = new ArrayList<>();

        Map<EdgeKey, HExploitRepr> edges = new HashMap<EdgeKey, HExploitRepr>();

        //Build indexed hashmap of all vulnerabilities and Cyber Security Profiles
        for (HumanVulnerability i : humanVulnerabilityCatalog.getHumanVulnerabilities())
            vulnerabilities.put(i.getVulnId(), i);
        for (CyberSecurityProfile i : cyberSecurityProfileInventory.getCyberSecurityProfiles())
            cpsMap.put(i.getEmployeeId(), i);

        //Build hashmap of exposed vulnerabilities on hosts: <host,port> <cvelist>
        for (Employee e : employeeInventory.getEmployees()) {
            String employeeId = e.getId();
            employees.put(employeeId, e);
            Set<String> vulnList = profileToVulnList(cpsMap.get(employeeId));
            humanVulnerabilitiesMap.put(employeeId, vulnList);
            HumanVulnerabilitiesMapping hvm = new HumanVulnerabilitiesMapping(employeeId, new ArrayList<String>(vulnList));
            humanVulnerabilitiesMapping.add(hvm);
        }

//        log.info(humanVulnerabilitiesMap.toString());

        List<SourceEmployee> sourceEmployeeList = humanReachabilityInventory.getSourceEmployees();

        SourceEmployee internalAttacker = createInternalAttacker();
        sourceEmployeeList.add(internalAttacker);

        //Examine Reachability
        for (SourceEmployee se : sourceEmployeeList) {
            HExploitRepr e;
            String source;
            String dest;
            String sourceEmployeeId = se.getId();
            Employee s = employees.get(sourceEmployeeId);


            // for each interface i on source device
            for (ReachedEmployee i : se.getReachedEmployee()) {
                //                    String destLanID = r.getLanID();
                //                    String destHostname = r.getHostName();
                String destEmployeeId = i.getReachedId();
                String destLinkType = i.getLinkType();
//                if (sourceEmployeeId.contains("INTERNAL")) {
//                    log.info("[HAGGenerator] " + sourceEmployeeId + " - Analyzing vulnerabilities on " + destEmployeeId);
//                } else {
//                    log.info("[HAGGenerator] " + sourceEmployeeId + "-" + s.getEmployeeName() + " - Analyzing vulnerabilities on " + destEmployeeId);
//                }
//                    for (ReachedPort p : r.getReachedPorts()) {
//                        Set<String> reachedVulnerabilities = networkHostVulnerabilities.get(new VulnPortKey(r.getHostName(), p.getPort()));
                Set<String> reachedVulnerabilities = humanVulnerabilitiesMap.get(destEmployeeId);
                if (reachedVulnerabilities != null) {
                    for (String vulnId : reachedVulnerabilities) {
                        HumanVulnerability v = vulnerabilities.get(vulnId);
                        if (v == null) {
                            log.error("[HAGGenerator] >> Vulnerability " + vulnId + " is not present in Vulnerability Inventory");
                        } else {
                            //EDGE LOGIC
//                            log.info("[HAGGenerator] Analyzing human vulnerability " + vulnId);
                            AV av = AV.valueOf(v.getAccessVector());
                            AV preCondition = AV.valueOf(v.getPreCondition());
                            Privilege postCondition = Privilege.valueOf(v.getPostCondition());
                            //FIXME Discover if is better to put AccessVector or preconditions in Link Type
                            Vulnerability hv = new Vulnerability(vulnId, v.getAccessVector());
                            //TODO Improve the association AccessVector <-> LinkType
                            if (av == AV.valueOf(destLinkType)) {
                                source = generateNodeUUID(sourceEmployeeId, Privilege.OWN.name());
                                dest = generateNodeUUID(destEmployeeId, postCondition.name());
                                if (!uuids.contains(source)) {
                                    nodes.add(createNode(sourceEmployeeId, source, Privilege.OWN));
                                    uuids.add(source);
                                }
                                if (!uuids.contains(dest)) {
                                    nodes.add(createNode(destEmployeeId, dest, postCondition));
                                    uuids.add(dest);
                                }

                                EdgeKey ek = new EdgeKey(source, dest);

                                e = edges.get(ek);
                                if (e != null) {
                                    e.addVulnerability(hv);
//                                    log.info("Updating edge " + ek.toString() + " - " + edges.get(ek));
                                } else {
                                    e = new HExploitRepr(source, dest);
                                    e.addVulnerability(hv);
                                    edges.put(ek, e);
//                                    log.info("Adding edge " + e.toString() + " - " + edges.get(ek));
                                }

                            }
                        }
                    }
                } else {
                    log.error("[HAGGenerator] >> reached node " + destEmployeeId + " does not have vulnerabilities.");
                }
            }
        }


        HumanLayerAttackGraphRepr graph = new HumanLayerAttackGraphRepr(notification.getEnvironment(), notification.getSnapshotId(), notification.getSnapshotTime(), nodes, new ArrayList(edges.values()));
        HumanVulnerabilityInventory humanVulnerabilityInventory = new HumanVulnerabilityInventory(notification.getEnvironment(), notification.getSnapshotId(), notification.getSnapshotTime(), humanVulnerabilitiesMapping);
/*        ObjectMapper objectMapper = new ObjectMapper();
        try {
            log.info(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(graph));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/

//        this.printEdges(graph.getEdges());
        hagPostOutputService.postHumanLayerAttackGraphRepr(graph);
        hagPostOutputService.postHumanVulnerabilityInventory(humanVulnerabilityInventory);
    }

    private HPrivilege createNode(String employeeId, String uuid, Privilege privilegeLevel) {
        return new HPrivilege(employeeId, uuid, privilegeLevel.name());
    }
//
//    private void printEdges(List<NExploitRepr> edges) {
//        edges.forEach(e -> log.info("[EDGE] " + e.getSource() + "-" + e.getDestination() + " --- " + e.getVulnerabilities().toString()));
//    }
//

    private Set<String> profileToVulnList(CyberSecurityProfile cyberSecurityProfile) {
        HashSet<String> vulnList = new HashSet<String>();
        if (cyberSecurityProfile.getSecurityBehavior() < 5 &&
                cyberSecurityProfile.getSecurityTrainingLevel() < 5 &&
                cyberSecurityProfile.getTrustInColleagues() > 5) {
            vulnList.add("sharing_credential");
        }
        if (cyberSecurityProfile.getSecurityCultureAtWork() < 5 && cyberSecurityProfile.getTrustInBuildingSecurity() > 5) {
            vulnList.add("no_logout");
        }
        if (cyberSecurityProfile.getSecurityTrainingLevel() < 5 &&
                cyberSecurityProfile.getTrustInColleagues() > 5 &&
                cyberSecurityProfile.getTrustInBuildingSecurity() > 5 &&
                cyberSecurityProfile.getIndividualSecurityAttitude() < 5) {
            vulnList.add("unprotected_credential");
        }
        return vulnList;
    }

    private void getInput(String version) {

        // get input data from REST service
        this.employeeInventory = hagInputRequestService.performEmployeeInventoryRequest(version);
        log.info("[HAG] GET employeeInventory from http://172.16.100.131:8102/human/employeeInventory/");
        this.cyberSecurityProfileInventory = hagInputRequestService.performCyberSecurityProfileInventoryRequest(version);
        log.info("[HAG] GET cyberSecurityProfileInventory from http://172.16.100.131:8102/human/cyberSecurityProfileInventory");
        this.humanVulnerabilityCatalog = hagInputRequestService.performHumanVulnerabilityCatalog(version);
        log.info("[HAG] GET humanVulnerabilityCatalog from http://172.16.100.131:8102/human/humanVulnerabilityInventory");
        this.humanReachabilityInventory = hagInputRequestService.performReachabilityInventoryRequest(version);
        log.info("[HAG] GET humanReachabilityInventory from http://172.16.100.131:8102/human/humanReachabilityInventory");

//        log.info("[HumanReachability] " + this.humanReachabilityInventory);
//        log.info("[HumanVulnerability] " + this.humanVulnerabilityCatalog);
//        log.info("[Employee] " + this.employeeInventory);
//        log.info("[CyberSecurityProfile]" + this.cyberSecurityProfileInventory);
    }


    private String generateNodeUUID(String employee, String privilege) {
        return privilege + '@' + employee;
    }

    private SourceEmployee createInternalAttacker() {
        SourceEmployee internalAttacker = new SourceEmployee();
        internalAttacker.setId("INTERNAL_ATTACKER");
        ArrayList<ReachedEmployee> reachedEmployeeList = new ArrayList<ReachedEmployee>();
        for (Employee e : employeeInventory.getEmployees()) {
            ReachedEmployee r = new ReachedEmployee();
            r.setReachedId(e.getId());
            r.setLinkType("PROXIMITY");
            reachedEmployeeList.add(r);
        }
        internalAttacker.setReachedEmployee(reachedEmployeeList);
        return internalAttacker;
    }

    enum Privilege {
        EXECUTE(0),
        USE(1),
        OWN(2);
        public int level;

        Privilege(int level) {
            this.level = level;
        }
    }

    enum AV {
        PROXIMITY(0),
        COWORKING(1);
        public int level;

        AV(int type) {
            this.level = type;
        }
    }
}


class EdgeKey {
    String src;
    String dest;

    public EdgeKey(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeKey edgeKey = (EdgeKey) o;
        return src.equals(edgeKey.src) &&
                dest.equals(edgeKey.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest);
    }
}
//
//
//class VulnPortKey {
//    String host;
//    long port;
//
//    public VulnPortKey(String host, long port) {
//        this.host = host;
//        this.port = port;
//    }
//
//    @Override
//    public String toString() {
//        return "VulnPortKey{" +
//                "host='" + host + '\'' +
//                ", port=" + port +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        VulnPortKey that = (VulnPortKey) o;
//        return port == that.port &&
//                host.equals(that.host);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(host, port);
//    }
//}


