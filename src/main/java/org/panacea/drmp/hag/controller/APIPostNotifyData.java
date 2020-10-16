package org.panacea.drmp.hag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.hag.domain.notifications.DataNotification;
import org.panacea.drmp.hag.service.OrchestratorNotificationHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//restcontroller maps the rest endpoint
@RestController
@Slf4j
@RequestMapping(path = "/hag/notify", produces = "application/json")
@Tag(name = "/age/notify", description = "Orchestrator Notification Handling API")

public class APIPostNotifyData {

    @Autowired
    OrchestratorNotificationHandlerService orchestratorNotificationHandler;

    @Operation(description = "Post a Data Notification")
    @PostMapping(value = "/data")
    public DataNotificationResponse postNotifyData(@RequestBody DataNotification request) {
        return orchestratorNotificationHandler.perform(request);
    }

    // Response object
    @Value
    @Schema(description = "Contains the parameters for the DataNotificationResponse to be returned to the orchestrator")
    public static class DataNotificationResponse {
        @Schema(description = "Emulation environment name", required = true)
        private String environment;
        @Schema(description = "Data collection snapshot id", required = true)
        private String snapshotId;
        @Schema(description = "Data collection snapshot timestamp", required = true)
        private String snapshotTime;
    }
}