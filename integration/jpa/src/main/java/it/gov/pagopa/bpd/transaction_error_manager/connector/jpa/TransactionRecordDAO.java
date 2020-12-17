package it.gov.pagopa.bpd.transaction_error_manager.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface TransactionRecordDAO extends CrudJpaDAO<TransactionRecord, String> {

    List<TransactionRecord> findByToResubmit(Boolean toResubmit);

}
