package com.npci.integration.service;

import com.npci.integration.models.TransactionStatus;
import com.npci.integration.models.Transactions;
import com.npci.integration.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    TransactionRepository transactionRepository;

    public Boolean processPayment(Transactions transaction) {

        // Select best gateway (Mock logic: choose any available gateway)
   /*     Optional<PaymentGateway> gatewayOpt = paymentGatewayRepository.findAll().stream().findFirst();
        if (gatewayOpt.isEmpty()) {
            failTransaction(transaction, "No available payment gateway.");
            return;
        }

        PaymentGateway gateway = gatewayOpt.get();*/
        transaction.setStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);

        // Store log entry
//        storeTransactionLog(transaction, LogStatus.FORWARDED, "Payment sent to gateway: " + gateway.getGatewayName());

        // Call payment gateway API
//        boolean paymentSuccess = callPaymentGateway(transaction, gateway);

        /*if (true) {
            completeTransaction(transaction);
        } else {
            failTransaction(transaction, "Payment gateway processing failed.");
        }*/
        return true;
    }
}
