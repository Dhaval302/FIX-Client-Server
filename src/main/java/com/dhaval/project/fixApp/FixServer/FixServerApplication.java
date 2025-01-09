package com.dhaval.project.fixApp.FixServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.*;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.NewOrderSingle;


public class FixServerApplication extends quickfix.MessageCracker implements quickfix.Application {

    private static final Logger log = LoggerFactory.getLogger(FixServerApplication.class);

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
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound {
    try {
        // Log the incoming message
        log.info("Received message: " + message);

        // Handle different message types
        String msgType = message.getHeader().getString(35);
        
        if (msgType.equals("D")) { // NewOrderSingle
            // Process NewOrderSingle logic
            // Validation and response logic
            log.info("Processing NewOrderSingle message: " + message);
            // Send a response message (ExecutionReport or Reject)
            sendExecutionReport(message, sessionID);
        } else if (msgType.equals("0")) { // Heartbeat
            log.info("Received Heartbeat: " + message);
            onMessage((Heartbeat) message, sessionID);
        } else {
            log.warn("Received unsupported message type: " + msgType);
        }

    } catch (FieldNotFound e) {
        log.error("Error processing message: " + e.getMessage(), e);
    }
}

private void sendExecutionReport(Message message, SessionID sessionID) {
    try {
        // Create ExecutionReport message
        Message executionReport = new Message();
        executionReport.getHeader().setString(35, "8"); // ExecutionReport
        executionReport.setString(11, message.getString(11)); // ClOrdID
        executionReport.setString(39, "0"); // OrdStatus (0 = New)
        executionReport.setString(55, message.getString(55)); // Symbol
        executionReport.setString(54, message.getString(54)); // Side
        executionReport.setDouble(44, message.getDouble(44)); // Price
        executionReport.setInt(32, message.getInt(32)); // OrderQty

        // Send response back to client
        Session.sendToTarget(executionReport);
        log.info("Sent ExecutionReport: " + executionReport);

    } catch (Exception e) {
        log.error("Error creating ExecutionReport: " + e.getMessage(), e);
    }
}


    @Handler
    public void onMessage(NewOrderSingle order, SessionID sessionId) {
        log.info("Received order: {}", order);
        // Perform validations and respond with ExecutionReport or Reject.
    }

    @Handler
    public void onMessage(Heartbeat heartbeat, SessionID sessionId) {
        log.info("Heartbeat received on server: {}", heartbeat);
        // Perform validations and respond with ExecutionReport or Reject.
    }
}
