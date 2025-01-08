package com.dhaval.project.FIX_Client_Server.FixServer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class FixServer {
    private SocketAcceptor socketAcceptor;

    @PostConstruct
    public void start() throws Exception {
        SessionSettings settings = new SessionSettings("classpath:server.cfg");
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

