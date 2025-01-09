package com.dhaval.project.fixApp.FixServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import quickfix.*;

import java.io.FileInputStream;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class FixServer {
    private SocketAcceptor socketAcceptor;

    private static final Logger log = LoggerFactory.getLogger(FixServer.class);


    @PostConstruct
    public void start() throws Exception {
        SessionSettings settings = new SessionSettings(new FileInputStream("src/main/resources/server.cfg"));
        Application application = new FixServerApplication();
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        socketAcceptor = new SocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
        socketAcceptor.start();
        log.info("FIX Server started...");
    }

    @PreDestroy
    public void stop() {
        if (socketAcceptor != null) {
            socketAcceptor.stop();
            log.info("FIX Server stopped.");
        }
    }
}

