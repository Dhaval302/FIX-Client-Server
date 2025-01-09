package com.dhaval.project.fixApp.FixClient;

import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import quickfix.*;

@Component
public class FixCLientApplication extends quickfix.MessageCracker implements quickfix.Application {

    private static final Logger log = LoggerFactory.getLogger(FixCLientApplication.class);

    private Initiator initiator;

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("Logged in to session: {}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("Logged out of session: {}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        
    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {}

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        try {
            if (message.getHeader().getString(35).equals("8")) {
                System.out.println("Execution Report: " + message);
            } else if (message.getHeader().getString(35).equals("3")) {
                System.out.println("Reject: " + message);
            }
        } catch (FieldNotFound e) {
            e.printStackTrace();
        }
    }

    public void sendNewOrderSingle() {
        try {
            // Create a NewOrderSingle message
            Message message = new Message();
            message.getHeader().setString(35, "D"); // NewOrderSingle
            message.setString(11, "12345"); // ClOrdID
            message.setString(54, "1"); // Side (Buy)
            message.setString(55, "AAPL"); // Symbol
            message.setDouble(44, 150.25); // Price
            message.setInt(32, 100); // OrderQty

            Session.sendToTarget(message, initiator.getSessions().get(0)); // Send message
            System.out.println("Sent NewOrderSingle: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        SessionSettings settings = new SessionSettings(new FileInputStream("src/main/resources/client.cfg"));
        MessageStoreFactory storeFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new FileLogFactory(settings);
        MessageFactory messageFactory = new DefaultMessageFactory();

        initiator = new SocketInitiator(this, storeFactory, settings, logFactory, messageFactory);
        initiator.start();
        sendNewOrderSingle();
    }

    public static void main(String[] args) throws Exception {
        FixCLientApplication client = new FixCLientApplication();
        client.start();
    }

    @Handler
    public void onMessage(quickfix.fix44.ExecutionReport executionReport, SessionID sessionId) {
        log.info("ExecutionReport received: {}", executionReport);
    }

    @Handler
    public void onMessage(quickfix.fix44.Reject reject, SessionID sessionId) {
        log.info("Reject received: {}", reject);
    }
}

