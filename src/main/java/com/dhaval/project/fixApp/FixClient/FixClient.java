package com.dhaval.project.fixApp.FixClient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import quickfix.*;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class FixClient {
    private SocketInitiator socketInitiator;

    private static final Logger log = LoggerFactory.getLogger(FixClient.class);

    @PostConstruct
    public void start() throws Exception {
        SessionSettings settings = new SessionSettings(new FileInputStream("src/main/resources/client.cfg"));
        Application application = new FixCLientApplication();
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        socketInitiator = new SocketInitiator(application, storeFactory, settings, logFactory, messageFactory);
        socketInitiator.start();
        log.info("FIX Client started...");
    }

    @PreDestroy
    public void stop() {
        if (socketInitiator != null) {
            socketInitiator.stop();
            log.info("FIX Client stopped.");
        }
    }
}

