package it.gov.pagopa.bpd.transaction_error_manager.service;

import eu.sia.meda.event.transformer.SimpleEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.BpdTransactionPublisherConnector;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import it.gov.pagopa.bpd.transaction_error_manager.service.transformer.HeaderAwareRequestTransformer;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the PointTransactionPublisherService, defines the service used for the interaction
 * with the PointTransactionPublisherConnector
 */

@Service
class BpdTransactionPublisherServiceImpl implements BpdTransactionPublisherService {

    private final BpdTransactionPublisherConnector bpdTransactionPublisherConnector;
    private final HeaderAwareRequestTransformer<Transaction> simpleEventRequestTransformer;
    private final SimpleEventResponseTransformer simpleEventResponseTransformer;

    @Autowired
    public BpdTransactionPublisherServiceImpl(BpdTransactionPublisherConnector bpdTransactionPublisherConnector,
                                              HeaderAwareRequestTransformer<Transaction> simpleEventRequestTransformer,
                                              SimpleEventResponseTransformer simpleEventResponseTransformer) {
        this.bpdTransactionPublisherConnector = bpdTransactionPublisherConnector;
        this.simpleEventRequestTransformer = simpleEventRequestTransformer;
        this.simpleEventResponseTransformer = simpleEventResponseTransformer;
    }

    /**
     * Calls the PointTransactionPublisherService, passing the transaction to be used as message payload
     *
     * @param transaction OutgoingTransaction instance to be used as payload for the outbound channel used bu the related connector
     */

    @Override
    public void publishBpdTransactionEvent(Transaction transaction, RecordHeaders recordHeaders) {
        bpdTransactionPublisherConnector.doCall(
                transaction, simpleEventRequestTransformer, simpleEventResponseTransformer, recordHeaders);
    }
}
