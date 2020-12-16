package it.gov.pagopa.bpd.transaction_error_manager.command;

import eu.sia.meda.BaseTest;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import it.gov.pagopa.bpd.transaction_error_manager.service.BpdCashbackTransactionPublisherService;
import it.gov.pagopa.bpd.transaction_error_manager.service.BpdTransactionPublisherService;
import it.gov.pagopa.bpd.transaction_error_manager.service.RtdTransactionPublisherService;
import it.gov.pagopa.bpd.transaction_error_manager.service.TransactionRecordService;
import it.gov.pagopa.bpd.transaction_error_manager.service.mapper.TransactionMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;

public class SubmitFlaggedRecordsCommandImplTest extends BaseTest {

    @Mock
    TransactionRecordService transactionRecordService;

    @Mock
    RtdTransactionPublisherService rtdTransactionPublisherService;

    @Mock
    BpdTransactionPublisherService bpdTransactionPublisherService;

    @Mock
    BpdCashbackTransactionPublisherService bpdCashbackTransactionPublisherService;

    @Spy
    TransactionMapper transactionMapperSpy;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void initTest() {
        Mockito.reset(
                transactionRecordService,
                rtdTransactionPublisherService,
                bpdTransactionPublisherService,
                transactionMapperSpy);
    }

    @Test
    public void TestExecute_OK_RTD() {
        TransactionRecord transactionRecord = getSavedModel();
        BDDMockito.doReturn(Collections.singletonList(transactionRecord)).when(transactionRecordService)
                .findRecordsToResubmit();
        SubmitFlaggedRecordsCommandImpl saveTransactionCommand = new SubmitFlaggedRecordsCommandImpl(
                transactionRecordService,
                rtdTransactionPublisherService,
                bpdTransactionPublisherService,
                bpdCashbackTransactionPublisherService,
                transactionMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        BDDMockito.verify(rtdTransactionPublisherService)
                .publishRtdTransactionEvent(Mockito.eq(getRequestModel()),Mockito.any());
    }

    @Test
    public void TestExecute_OK_BPD() {
        TransactionRecord transactionRecord = getSavedModel();
        transactionRecord.setOriginTopic("bpd-trx");
        BDDMockito.doReturn(Collections.singletonList(transactionRecord)).when(transactionRecordService)
                .findRecordsToResubmit();
        SubmitFlaggedRecordsCommandImpl saveTransactionCommand = new SubmitFlaggedRecordsCommandImpl(
                transactionRecordService,
                rtdTransactionPublisherService,
                bpdTransactionPublisherService,
                bpdCashbackTransactionPublisherService,
                transactionMapperSpy);
        Boolean executed = saveTransactionCommand.doExecute();
        Assert.assertTrue(executed);
        BDDMockito.verify(bpdTransactionPublisherService)
                .publishBpdTransactionEvent(Mockito.eq(getRequestModel()),Mockito.any());
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
                .lastResubmitDate(null)
                .toResubmit(true)
                .originListener("listener")
                .recordId("recordid")
                .originTopic("rtd-trx")
                .build();
    }


}