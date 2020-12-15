package it.gov.pagopa.bpd.transaction_error_manager.connector.jpa;


import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class TransactionRecordDAOTest extends BaseCrudJpaDAOTest<TransactionRecordDAO, TransactionRecord, String> {

    @Data
    private static class TransactionRecordCriteria implements CriteriaQuery<TransactionRecord> {
        private String recordId;
    }

    @Autowired
    private TransactionRecordDAO dao;

    @Override
    protected CriteriaQuery<? super TransactionRecord> getMatchAlreadySavedCriteria() {
        TransactionRecordCriteria criteriaQuery = new TransactionRecordCriteria();
        criteriaQuery.setRecordId(String.valueOf(getStoredId()));

        return criteriaQuery;
    }

    @Override
    protected TransactionRecordDAO getDao() {
        return dao;
    }

    @Override
    protected void setId(TransactionRecord entity, String id) {
        entity.setRecordId(id);
    }

    @Override
    protected String getId(TransactionRecord entity) {
        return entity.getRecordId();
    }

    @Override
    protected void alterEntityToUpdate(TransactionRecord entity) {
        entity.setToResubmit(true);
        entity.setLastResubmitDate(OffsetDateTime.now());
        entity.setUpdateDate(OffsetDateTime.now());
    }

    @Override
    protected Function<Integer, String> idBuilderFn() {
        return (bias) -> "hpan" + bias;
    }

    @Override
    protected String getIdName() {
        return "recordId";
    }
}