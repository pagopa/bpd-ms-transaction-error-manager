package it.gov.pagopa.bpd.transaction_error_manager.service;

import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;

import java.util.List;

/**
 * A service to manage the Business Logic related to TransactionRecord
 */
public interface TransactionRecordService {

    TransactionRecord saveTransactionRecord(TransactionRecord transactionRecord);

    List<TransactionRecord> findRecordsToResubmit();

}
