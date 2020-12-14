package it.gov.pagopa.rtd.transaction_error_manager.service;

import eu.sia.meda.service.BaseService;
import it.gov.pagopa.rtd.transaction_error_manager.connector.jpa.TransactionRecordDAO;
import it.gov.pagopa.rtd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @See TransactionRecordService
 */
@Service
class TransactionRecordServiceImpl extends BaseService implements TransactionRecordService {

    private final TransactionRecordDAO transactionRecordDAO;

    @Autowired
    public TransactionRecordServiceImpl(TransactionRecordDAO transactionRecordDAO) {
        this.transactionRecordDAO = transactionRecordDAO;
    }

    @Override
    public TransactionRecord saveTransactionRecord(TransactionRecord transactionRecord) {
        if (transactionRecord.getRecordId() == null) {
            transactionRecord.setRecordId(UUID.randomUUID().toString()
                    .concat(OffsetDateTime.now().toString()));
        }
        return transactionRecordDAO.save(transactionRecord);
    }

    @Override
    public List<TransactionRecord> findRecordsToResubmit() {
        return transactionRecordDAO.findByToResubmit(true);
    }

}
