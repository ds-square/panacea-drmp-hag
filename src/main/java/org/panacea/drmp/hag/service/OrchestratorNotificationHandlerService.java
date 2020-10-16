package org.panacea.drmp.hag.service;

import org.panacea.drmp.hag.controller.APIPostNotifyData.DataNotificationResponse;
import org.panacea.drmp.hag.domain.notifications.DataNotification;
import org.panacea.drmp.hag.exception.HAGException;

public interface OrchestratorNotificationHandlerService {

    DataNotificationResponse perform(DataNotification dataNotification) throws HAGException;

}
