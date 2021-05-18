package it.gov.pagopa.bpd.transaction_error_manager.service;

import eu.sia.meda.BaseTest;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.TransactionRecordDAO;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

public class TransactionRecordServiceImplTest extends BaseTest {

    @Mock
    private TransactionRecordDAO transactionRecordDAOMock;

    private TransactionRecordService transactionRecordService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(transactionRecordDAOMock);
        transactionRecordService = new TransactionRecordServiceImpl(transactionRecordDAOMock);
    }

    @Test
    public void getList_OK() {
        BDDMockito.doReturn(Collections.emptyList())
                .when(transactionRecordDAOMock).findByToResubmit(Mockito.eq(true));
        List<TransactionRecord> transactionRecordServiceList =
                transactionRecordService.findRecordsToResubmit();
        Assert.assertNotNull(transactionRecordServiceList);
        BDDMockito.verify(transactionRecordDAOMock).findByToResubmit(Mockito.eq(true));
    }

    @Test
    public void getList_KO() {
        BDDMockito.when(transactionRecordDAOMock.findByToResubmit(Mockito.eq(true))).thenAnswer(
                invocation -> {
                    throw new Exception();
                });
        expectedException.expect(Exception.class);
        transactionRecordService.findRecordsToResubmit();
        BDDMockito.verify(transactionRecordDAOMock).findByToResubmit(Mockito.eq(true));
    }

    @Test
    public void save_OK() {
        TransactionRecord transactionRecord = TransactionRecord.builder().build();
        BDDMockito.doReturn(transactionRecord).when(transactionRecordDAOMock).save(Mockito.eq(transactionRecord));
        transactionRecord = transactionRecordService.saveTransactionRecord(transactionRecord);
        Assert.assertNotNull(transactionRecord);
        BDDMockito.verify(transactionRecordDAOMock).save(Mockito.eq(transactionRecord));
    }

    @Test
    public void save_KO() {
        BDDMockito.when(transactionRecordDAOMock.save(Mockito.any())).thenAnswer(
                invocation -> {
                    throw new Exception();
                });
        expectedException.expect(Exception.class);
        transactionRecordService.saveTransactionRecord(TransactionRecord.builder().build());
        BDDMockito.verify(transactionRecordDAOMock).save(Mockito.any());
    }

}