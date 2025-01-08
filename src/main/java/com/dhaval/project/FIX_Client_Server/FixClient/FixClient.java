package com.dhaval.project.FIX_Client_Server.FixClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.*;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class FixClient {
    private SocketInitiator socketInitiator;

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

