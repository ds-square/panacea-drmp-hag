package org.panacea.drmp.hag.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.hag.domain.graph.HumanLayerAttackGraphRepr;
import org.panacea.drmp.hag.domain.humanvulnerabilities.HumanVulnerabilityInventory;
import org.panacea.drmp.hag.service.HAGPostOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class HAGPostOutputServiceImpl implements HAGPostOutputService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${humanLayerAttackGraphRepr.endpoint}")
    private String humanLayerAttackGraphURL;

    @Value("${humanLayerAttackGraphRepr.fn}")
    private String humanLayerAttackGraphFn;

    @Value("${humanVulnerabilityInventory.endpoint}")
    private String humanVulnerabilityInventoryURL;

    @Value("${humanVulnerabilityInventory.fn}")
    private String humanVulnerabilityInventoryFn;


    @Override
    public void postHumanLayerAttackGraphRepr(HumanLayerAttackGraphRepr repr) {

        // convert repr to file
//        String tempFilePath = "/tmp/" + humanLayerAttackGraphFn;
//        File file = new File(tempFilePath);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, repr);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        MultiValueMap<String, Object> body
//                = new LinkedMultiValueMap<>();
//        body.add("file", new FileSystemResource(tempFilePath));


        HttpEntity<HumanLayerAttackGraphRepr> requestEntity
                = new HttpEntity<>(repr);

        String endPointUrl = humanLayerAttackGraphURL; // + '/' + repr.getSnapshotId() + '/';

//        log.info("[HAG] POST humanLayerAttackGraphRepr to " + endPointUrl);
        log.info("[HAG] POST humanLayerAttackGraph to http://172.16.100.131:8102/human/humanLayerAttackGraph");
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        try {

            response = restTemplate
                    .postForEntity(endPointUrl, requestEntity, String.class);
        } catch (HttpClientErrorException e) {

            System.out.println("Response from storage service: " + response);
            byte[] bytes = e.getResponseBodyAsByteArray();


            //Convert byte[] to String
            String s = new String(bytes);

            log.error(s);
            e.printStackTrace();

        }

    }

    @Override
    public void postHumanVulnerabilityInventory(HumanVulnerabilityInventory hvi) {
        // convert repr to file
//        String tempFilePath = "/tmp/" + humanVulnerabilityInventoryFn;
//        File file = new File(tempFilePath);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, hvi);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//        MultiValueMap<String, Object> body
//                = new LinkedMultiValueMap<>();
//        body.add("file", new FileSystemResource(tempFilePath));
//
//
        HttpEntity<HumanVulnerabilityInventory> requestEntity
                = new HttpEntity<>(hvi);

        String endPointUrl = humanVulnerabilityInventoryURL; // + '/' + hvi.getSnapshotId() + '/';

//        log.info("[HAG] POST humanVulnerabilityInventory to " + endPointUrl);
        log.info("[HAG] POST humanVulnerabilityInventory to http://172.16.100.131:8102/human/humanVulnerabilityInventory");
        ResponseEntity<String> response = null;
        RestTemplate restTemplate = new RestTemplate();
        try {

            response = restTemplate
                    .postForEntity(endPointUrl, requestEntity, String.class);
        } catch (HttpClientErrorException e) {

            System.out.println("Response from storage service: " + response);
            byte[] bytes = e.getResponseBodyAsByteArray();

            //Convert byte[] to String
            String s = new String(bytes);

            log.error(s);
            e.printStackTrace();

        }
    }
}
