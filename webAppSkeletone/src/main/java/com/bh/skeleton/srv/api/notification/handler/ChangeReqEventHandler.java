package com.hs.gms.srv.api.notification.handler;

import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ChangeReqEventHandler.java
 * 
 * @author BH Jun
 */
@Component
public class ChangeReqEventHandler implements EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeReqEventHandler.class);

    @Override
    public void handleMessage(Object message) {
        LOGGER.info((String) message);
    }

}
