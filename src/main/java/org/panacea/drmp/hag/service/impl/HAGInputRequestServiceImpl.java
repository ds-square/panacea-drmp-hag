package org.panacea.drmp.hag.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.hag.domain.employees.EmployeeInventory;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilityCatalog;
import org.panacea.drmp.hag.domain.reachability.HumanReachabilityInventory;
import org.panacea.drmp.hag.domain.securityprofiles.CyberSecurityProfileInventory;
import org.panacea.drmp.hag.service.HAGInputRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class HAGInputRequestServiceImpl implements HAGInputRequestService {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${employeeInventory.endpoint}")
	private String employeeInventoryURL;

	@Value("${employeeInventory.fn}")
	private String employeeInventoryFn;

	@Value("${cyberSecurityProfileInventory.endpoint}")
	private String cyberSecurityProfileInventoryURL;

	@Value("${cyberSecurityProfileInventory.fn}")
	private String cyberSecurityProfileInventoryFn;

	@Value("${humanVulnerabilityCatalog.endpoint}")
	private String humanVulnerabilityCatalogURL;

	@Value("${humanVulnerabilityCatalog.fn}")
	private String humanVulnerabilityCatalogFn;

	@Value("${humanReachabilityInventory.endpoint}")
	private String humanReachabilityInventoryURL;

	@Value("${humanReachabilityInventory.fn}")
	private String humanReachabilityInventoryFn;

	@Override
	public EmployeeInventory performEmployeeInventoryRequest(String snapshotId) {
//		log.info("[HAG] GETting " + employeeInventoryURL + '/' + snapshotId);
		ResponseEntity<EmployeeInventory> responseEntity = restTemplate.exchange(
				employeeInventoryURL + '/' + snapshotId, // + '/' + employeeInventoryFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<EmployeeInventory>() {
				});
		EmployeeInventory employeeInventory = responseEntity.getBody();

		return employeeInventory;
	}

	@Override
	public CyberSecurityProfileInventory performCyberSecurityProfileInventoryRequest(String snapshotId) {
//		log.info("[HAG] GETting " + cyberSecurityProfileInventoryURL + '/' + snapshotId);
		ResponseEntity<CyberSecurityProfileInventory> responseEntity = restTemplate.exchange(
				cyberSecurityProfileInventoryURL + '/' + snapshotId, // + '/' + cyberSecurityProfileInventoryFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<CyberSecurityProfileInventory>() {
				});
		CyberSecurityProfileInventory cyberSecurityProfileInventory = responseEntity.getBody();

		return cyberSecurityProfileInventory;
	}

	@Override
	public HumanVulnerabilityCatalog performHumanVulnerabilityCatalog(String snapshotId) {
//		log.info("[HAG] GETting " + humanVulnerabilityCatalogURL + '/' + snapshotId);
		ResponseEntity<HumanVulnerabilityCatalog> responseEntity = restTemplate.exchange(
				humanVulnerabilityCatalogURL + '/' + snapshotId, // + '/' + humanVulnerabilityCatalogFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<HumanVulnerabilityCatalog>() {
				});
		HumanVulnerabilityCatalog humanVulnerabilityCatalog = responseEntity.getBody();

		return humanVulnerabilityCatalog;
	}

	@Override
	public HumanReachabilityInventory performReachabilityInventoryRequest(String snapshotId) {
//		log.info("[HAG] GETting " + humanReachabilityInventoryURL + '/' + snapshotId);
		ResponseEntity<HumanReachabilityInventory> responseEntity = restTemplate.exchange(
				humanReachabilityInventoryURL + '/' + snapshotId, // + '/' + humanReachabilityInventoryFn,
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<HumanReachabilityInventory>() {
				});
		HumanReachabilityInventory humanReachabilityInventory = responseEntity.getBody();

		return humanReachabilityInventory;
	}
}
