package it.gov.pagopa.bpd.transaction_error_manager.service;

import eu.sia.meda.event.transformer.SimpleEventRequestTransformer;
import eu.sia.meda.event.transformer.SimpleEventResponseTransformer;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.RtdTransactionPublisherConnector;
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
class RtdTransactionPublisherServiceImpl implements RtdTransactionPublisherService {

    private final RtdTransactionPublisherConnector rtdTransactionPublisherConnector;
    private final HeaderAwareRequestTransformer<Transaction> simpleEventRequestTransformer;
    private final SimpleEventResponseTransformer simpleEventResponseTransformer;

    @Autowired
    public RtdTransactionPublisherServiceImpl(RtdTransactionPublisherConnector rtdTransactionPublisherConnector,
                                                HeaderAwareRequestTransformer<Transaction> simpleEventRequestTransformer,
                                                SimpleEventResponseTransformer simpleEventResponseTransformer) {
        this.rtdTransactionPublisherConnector = rtdTransactionPublisherConnector;
        this.simpleEventRequestTransformer = simpleEventRequestTransformer;
        this.simpleEventResponseTransformer = simpleEventResponseTransformer;
    }

    /**
     * Calls the PointTransactionPublisherService, passing the transaction to be used as message payload
     *
     * @param transaction OutgoingTransaction instance to be used as payload for the outbound channel used bu the related connector
     */

    @Override
    public void publishRtdTransactionEvent(Transaction transaction, RecordHeaders recordHeaders) {
        rtdTransactionPublisherConnector.doCall(
                transaction, simpleEventRequestTransformer, simpleEventResponseTransformer, recordHeaders);
    }
}
