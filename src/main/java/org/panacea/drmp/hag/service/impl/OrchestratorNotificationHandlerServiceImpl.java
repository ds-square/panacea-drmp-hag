package org.panacea.drmp.hag.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.panacea.drmp.hag.HAGGenerator;
import org.panacea.drmp.hag.controller.APIPostNotifyData;
import org.panacea.drmp.hag.domain.notifications.DataNotification;
import org.panacea.drmp.hag.exception.HAGException;
import org.panacea.drmp.hag.service.OrchestratorNotificationHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class OrchestratorNotificationHandlerServiceImpl implements OrchestratorNotificationHandlerService {

    public static final String INVALID_NOTIFICATION_ERR_MSG = "Invalid Data Notification Body.";

    @Autowired
    HAGGenerator hagGenerator;

    @Override
    public APIPostNotifyData.DataNotificationResponse perform(DataNotification notification) throws HAGException {
//        log.info("Received Data Notification from Orchestrator: {}", notification);
        log.info("[HAG] Received notification from Orchestrator");
        try {
            if (notification.getEnvironment() == null) {
                throw new HAGException("No environment defined for notification.");
            }
            hagGenerator.generateHAG(notification);


            return new APIPostNotifyData.DataNotificationResponse(notification.getEnvironment(), notification.getSnapshotId(), notification.getSnapshotTime());
        } catch (HAGException e) {
            log.info("HAGException occurred: ", e);
            throw new HAGException(INVALID_NOTIFICATION_ERR_MSG, e);
        }

    }


}
