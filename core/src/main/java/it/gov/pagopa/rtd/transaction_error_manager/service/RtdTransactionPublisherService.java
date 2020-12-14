package it.gov.pagopa.rtd.transaction_error_manager.service;

import it.gov.pagopa.rtd.transaction_error_manager.publisher.model.Transaction;
import org.apache.kafka.common.header.internals.RecordHeaders;

/**
 * public interface for the PointTransactionPublisherService
 */

public interface RtdTransactionPublisherService {

    /**
     * Method that has the logic for publishing a Transaction to the rtd outbound channel,
     * calling on the appropriate connector
     *
     * @param transaction Transaction instance to be published
     */
    void publishRtdTransactionEvent(Transaction transaction, RecordHeaders recordHeaders);

}
