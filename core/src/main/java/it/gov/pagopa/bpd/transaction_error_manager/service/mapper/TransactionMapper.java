package it.gov.pagopa.bpd.transaction_error_manager.service.mapper;

import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Class to be used to map a {@link Transaction} from an* {@link TransactionRecord}
 */

@Service
public class TransactionMapper {

    public TransactionRecord mapTransactionRecord(Transaction transaction) {

        TransactionRecord transactionRecord = null;

        if (transaction != null) {
            transactionRecord = TransactionRecord.builder().build();
            BeanUtils.copyProperties(transaction, transactionRecord);
        }

        return transactionRecord;

    }

    public Transaction mapTransaction(TransactionRecord transactionRecord) {

        Transaction transaction = null;

        if (transactionRecord != null) {
            transaction = Transaction.builder().build();
            BeanUtils.copyProperties(transactionRecord, transaction);
        }

        return transaction;

    }

}
