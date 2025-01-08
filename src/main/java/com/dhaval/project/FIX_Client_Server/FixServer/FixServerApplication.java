package com.dhaval.project.FIX_Client_Server.FixServer;

import lombok.extern.slf4j.Slf4j;
import quickfix.*;
import quickfix.fix44.NewOrderSingle;

@Slf4j
public class FixServerApplication extends quickfix.MessageCracker implements quickfix.Application {

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("Client logged in: {}", sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("Client logged out: {}", sessionId);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {}

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {}

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {}

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        crack(message, sessionId);
    }

    @Handler
    public void onMessage(NewOrderSingle order, SessionID sessionId) {
        log.info("Received order: {}", order);
        // Perform validations and respond with ExecutionReport or Reject.
    }
}
