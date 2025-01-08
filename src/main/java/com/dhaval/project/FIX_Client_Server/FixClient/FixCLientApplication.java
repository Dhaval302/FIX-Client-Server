package com.dhaval.project.FIX_Client_Server.FixClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import quickfix.*;

@Slf4j
@Component
public class FixCLientApplication extends quickfix.MessageCracker implements quickfix.Application {

    @Override
    public void onCreate(SessionID sessionId) {
        log.info("Session created: {}", sessionId);
    }

    @Override
    public void onLogon(SessionID sessionId) {
        log.info("Logged in to session: {}", sessionId);

        // Send a NewOrderSingle message
        sendNewOrderSingle(sessionId);
    }

    @Override
    public void onLogout(SessionID sessionId) {
        log.info("Logged out of session: {}", sessionId);
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

    public void sendNewOrderSingle(SessionID sessionId) {
        try {
            quickfix.fix44.NewOrderSingle newOrder = new quickfix.fix44.NewOrderSingle(
                    new quickfix.field.ClOrdID("12345"),
                    new quickfix.field.Side(quickfix.field.Side.BUY),
                    new quickfix.field.TransactTime(),
                    new quickfix.field.OrdType(quickfix.field.OrdType.LIMIT)
            );
            newOrder.set(new quickfix.field.Price(150.25));
            newOrder.set(new quickfix.field.OrderQty(50));

            Session.sendToTarget(newOrder, sessionId);
            log.info("NewOrderSingle sent: {}", newOrder);
        } catch (SessionNotFound e) {
            log.error("Session not found: {}", e.getMessage());
        }
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

