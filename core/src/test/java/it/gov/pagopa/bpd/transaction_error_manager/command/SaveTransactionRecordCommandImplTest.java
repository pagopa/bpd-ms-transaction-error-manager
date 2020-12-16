package it.gov.pagopa.bpd.transaction_error_manager.command;

import eu.sia.meda.BaseTest;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import it.gov.pagopa.bpd.transaction_error_manager.model.TransactionCommandModel;
import it.gov.pagopa.bpd.transaction_error_manager.model.constants.TransactionRecordConstants;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import it.gov.pagopa.bpd.transaction_error_manager.service.TransactionRecordService;
import it.gov.pagopa.bpd.transaction_error_manager.service.mapper.TransactionMapper;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class SaveTransactionRecordCommandImplTest extends BaseTest {

    @Mock
    TransactionRecordService transactionRecordService;

    @Spy
    TransactionMapper transactionMapperSpy;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(transactionRecordService, transactionMapperSpy);
    }

    @Test
    public void TestExecute_OK_VoidData() {

        Transaction transaction = getRequestModel();
        TransactionRecord transactionRecord = getSavedModel();
        Headers headers = new RecordHeaders();

        ArgumentCaptor<TransactionRecord> argument = ArgumentCaptor.forClass(TransactionRecord.class);
        BDDMockito.doReturn(transactionRecord).when(transactionRecordService)
                .saveTransactionRecord(Mockito.eq(transactionRecord));
        SaveTransactionRecordCommandImpl saveTransactionCommand = new SaveTransactionRecordCommandImpl(
                TransactionCommandModel.builder().payload(transaction).headers(headers).build(),
                transactionRecordService,
                transactionMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        Mockito.verify(transactionMapperSpy).mapTransactionRecord(Mockito.eq(transaction));
        Mockito.verify(transactionRecordService).saveTransactionRecord(argument.capture());
        Assert.assertNotNull(argument.getValue().getRecordId());
        Assert.assertNull(argument.getValue().getExceptionMessage());
        Assert.assertEquals(argument.getValue().getToResubmit(),false);
        Assert.assertEquals(argument.getValue().getOriginTopic(),"rtd-trx");

    }

    @Test
    public void TestExecute_OK_PIRecord() {

        Transaction transaction = getRequestModel();
        TransactionRecord transactionRecord = getSavedModel();
        Headers headers = new RecordHeaders();
        headers.add(TransactionRecordConstants.EXCEPTION_HEADER,"test".getBytes());
        headers.add(TransactionRecordConstants.LISTENER_HEADER,
                "it.gov.pagopa.bpd.payment_instrument.listener.OnTransactionFilterRequestListener".getBytes());
        headers.add(TransactionRecordConstants.REQUEST_ID_HEADER,"requestId".getBytes());

        ArgumentCaptor<TransactionRecord> argument = ArgumentCaptor.forClass(TransactionRecord.class);
        BDDMockito.doReturn(transactionRecord).when(transactionRecordService)
                .saveTransactionRecord(Mockito.eq(transactionRecord));

        SaveTransactionRecordCommandImpl saveTransactionCommand = new SaveTransactionRecordCommandImpl(
                TransactionCommandModel.builder().payload(transaction).headers(headers).build(),
                transactionRecordService,
                transactionMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        Mockito.verify(transactionMapperSpy).mapTransactionRecord(Mockito.eq(transaction));
        Mockito.verify(transactionRecordService).saveTransactionRecord(argument.capture());
        Assert.assertNotNull(argument.getValue().getRecordId());
        Assert.assertEquals(argument.getValue().getExceptionMessage(),"test");
        Assert.assertEquals(argument.getValue().getToResubmit(),false);
        Assert.assertEquals(argument.getValue().getOriginTopic(),"rtd-trx");
    }

    @Test
    public void TestExecute_OK_WTRecord() {

        Transaction transaction = getRequestModel();
        TransactionRecord transactionRecord = getSavedModel();
        Headers headers = new RecordHeaders();
        headers.add(TransactionRecordConstants.EXCEPTION_HEADER,"test".getBytes());
        headers.add(TransactionRecordConstants.LISTENER_HEADER,
                "it.gov.pagopa.bpd.winning_transaction.listener.OnTransactionSaveRequestListener".getBytes());
        headers.add(TransactionRecordConstants.REQUEST_ID_HEADER,"requestId".getBytes());

        ArgumentCaptor<TransactionRecord> argument = ArgumentCaptor.forClass(TransactionRecord.class);
        BDDMockito.doReturn(transactionRecord).when(transactionRecordService)
                .saveTransactionRecord(Mockito.eq(transactionRecord));

        SaveTransactionRecordCommandImpl saveTransactionCommand = new SaveTransactionRecordCommandImpl(
                TransactionCommandModel.builder().payload(transaction).headers(headers).build(),
                transactionRecordService,
                transactionMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        Mockito.verify(transactionMapperSpy).mapTransactionRecord(Mockito.eq(transaction));
        Mockito.verify(transactionRecordService).saveTransactionRecord(argument.capture());
        Assert.assertNotNull(argument.getValue().getRecordId());
        Assert.assertEquals(argument.getValue().getExceptionMessage(),"test");
        Assert.assertEquals(argument.getValue().getToResubmit(),false);
        Assert.assertEquals(argument.getValue().getOriginTopic(),"bpd-trx-cashback");
    }


    @Test
    public void TestExecute_KO_Validation() {

        Transaction transaction = getRequestModel();
        transaction.setAcquirerCode(null);
        Headers headers = new RecordHeaders();

        SaveTransactionRecordCommandImpl saveTransactionCommand = new SaveTransactionRecordCommandImpl(
                TransactionCommandModel.builder().payload(transaction).headers(headers).build(),
                transactionRecordService,
                transactionMapperSpy);
        exceptionRule.expect(AssertionError.class);
        saveTransactionCommand.doExecute();
        Mockito.verifyZeroInteractions(transactionRecordService);

    }

    @Test
    public void TestExecute_KO_NullException() {

        Transaction transaction = getRequestModel();
        transaction.setAcquirerCode(null);

        SaveTransactionRecordCommandImpl saveTransactionCommand = new SaveTransactionRecordCommandImpl(
                TransactionCommandModel.builder().payload(transaction).build(),
                transactionRecordService,
                transactionMapperSpy);
        exceptionRule.expect(NullPointerException.class);
        saveTransactionCommand.doExecute();
        Mockito.verifyZeroInteractions(transactionRecordService);

    }

    protected Transaction getRequestModel() {
        return Transaction.builder()
                .idTrxAcquirer("1")
                .acquirerCode("001")
                .trxDate(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .amount(BigDecimal.valueOf(1313.13))
                .operationType("00")
                .hpan("hpan")
                .merchantId("0")
                .circuitType("00")
                .mcc("813")
                .idTrxIssuer("0")
                .amountCurrency("833")
                .correlationId("1")
                .acquirerId("0")
                .awardPeriodId(1L)
                .score(BigDecimal.ONE)
                .bin("000001")
                .terminalId("0")
                .fiscalCode("fiscalCode")
                .build();
    }

    protected TransactionRecord getSavedModel() {
        return TransactionRecord.builder()
                .idTrxAcquirer("1")
                .acquirerCode("001")
                .trxDate(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .amount(BigDecimal.valueOf(1313.13))
                .operationType("01")
                .hpan("hpan")
                .merchantId("0")
                .circuitType("00")
                .mcc("813")
                .idTrxIssuer("0")
                .amountCurrency("833")
                .correlationId("1")
                .acquirerId("0")
                .awardPeriodId(1L)
                .score(BigDecimal.ONE)
                .bin("000001")
                .terminalId("0")
                .fiscalCode("fiscalCode")
                .build();
    }

}