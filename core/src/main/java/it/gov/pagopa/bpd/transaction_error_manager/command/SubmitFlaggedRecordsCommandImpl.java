package it.gov.pagopa.bpd.transaction_error_manager.command;

import eu.sia.meda.async.util.AsyncUtils;
import eu.sia.meda.core.command.BaseCommand;
import eu.sia.meda.core.interceptors.BaseContextHolder;
import it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import it.gov.pagopa.bpd.transaction_error_manager.model.constants.TransactionRecordConstants;
import it.gov.pagopa.bpd.transaction_error_manager.service.BpdCashbackTransactionPublisherService;
import it.gov.pagopa.bpd.transaction_error_manager.service.RtdTransactionPublisherService;
import it.gov.pagopa.bpd.transaction_error_manager.service.TransactionRecordService;
import it.gov.pagopa.bpd.transaction_error_manager.service.mapper.TransactionMapper;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import it.gov.pagopa.bpd.transaction_error_manager.service.BpdTransactionPublisherService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Base implementation of the SaveTransactionCommandInterface, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
class SubmitFlaggedRecordsCommandImpl extends BaseCommand<Boolean> implements SubmitFlaggedRecordsCommand {

    private TransactionRecordService transactionRecordService;
    private RtdTransactionPublisherService rtdTransactionPublisherService;
    private BpdTransactionPublisherService bpdTransactionPublisherService;
    private BpdCashbackTransactionPublisherService bpdCashbackTransactionPublisherService;
    private TransactionMapper transactionMapper;


    public SubmitFlaggedRecordsCommandImpl() {}

    public SubmitFlaggedRecordsCommandImpl(
            TransactionRecordService transactionRecordService,
            RtdTransactionPublisherService rtdTransactionPublisherService,
            BpdTransactionPublisherService bpdTransactionPublisherService,
            BpdCashbackTransactionPublisherService bpdCashbackTransactionPublisherService,
            TransactionMapper transactionMapper) {
        this.transactionRecordService = transactionRecordService;
        this.rtdTransactionPublisherService = rtdTransactionPublisherService;
        this.bpdTransactionPublisherService = bpdTransactionPublisherService;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Implementation of the MEDA Command doExecute method, contains the logic for the inbound transaction
     * management, calls the REST endpoint to check if it the related paymentInstrument is active, and eventually
     * sends the Transaction to the proper outbound channel. In case of an error, send a
     *
     * @return boolean to indicate if the command is successfully executed
     */

    @SneakyThrows
    @Override
    public Boolean doExecute() {

        this.callAsyncService(() -> {
            try {

                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSSXXXXX");
                OffsetDateTime execStart = OffsetDateTime.now();

                List<TransactionRecord> transactionRecordList = transactionRecordService.findRecordsToResubmit();
                for (TransactionRecord transactionRecord : transactionRecordList) {
                    this.callAsyncService(() -> {

                        try {

                            Transaction transaction = transactionMapper.mapTransaction(transactionRecord);
                            RecordHeaders recordHeaders = new RecordHeaders();
                            String requestId = transactionRecord.getOriginRequestId() == null ?
                                    "Resubmitted" :
                                    "Resubmitted;".concat(transactionRecord.getOriginRequestId());
                            recordHeaders.add(TransactionRecordConstants.REQUEST_ID_HEADER, requestId.getBytes());
                            BaseContextHolder.getApplicationContext().setRequestId(requestId);
                            recordHeaders.add(TransactionRecordConstants.USER_ID_HEADER,
                                    "rtd-ms-transaction-error-manager".getBytes());
                            recordHeaders.add(TransactionRecordConstants.LISTENER_HEADER,
                                    transactionRecord.getOriginListener() == null ?
                                            null :
                                            transactionRecord.getOriginListener().getBytes());
                            recordHeaders.add(TransactionRecordConstants.CITIZEN_VALIDATION_DATETIME_HEADER,
                                    transactionRecord.getCitizenValidationDate() == null ?
                                            null :
                                            transactionRecord.getCitizenValidationDate().toString().getBytes());

                            switch (transactionRecord.getOriginTopic()) {
                                case "bpd-trx":
                                    bpdTransactionPublisherService.publishBpdTransactionEvent(transaction, recordHeaders);
                                    break;
                                case "rtd-trx":
                                    rtdTransactionPublisherService.publishRtdTransactionEvent(transaction, recordHeaders);
                                    break;
                                case "bpd-trx-cashback":
                                    bpdCashbackTransactionPublisherService
                                            .publishBpdCashbackTransactionEvent(transaction, recordHeaders);
                                    break;
                            }

                            transactionRecord.setToResubmit(false);
                            transactionRecord.setLastResubmitDate(OffsetDateTime.now());
                            transactionRecordService.saveTransactionRecord(transactionRecord);

                            return true;

                        } catch (Exception e) {
                            logger.error("Error occurred while attempting to submit flagged record: {}, {}, {}, {}, {}",
                                    transactionRecord.getAcquirerCode(),
                                    transactionRecord.getIdTrxAcquirer(),
                                    transactionRecord.getTrxDate(),
                                    transactionRecord.getAcquirerId(),
                                    transactionRecord.getOperationType());
                            logger.error(e.getMessage(), e);
                            throw e;
                        }

                    });

               }

                OffsetDateTime end_exec = OffsetDateTime.now();
                log.info("Executed SubmitFlaggedRecordsCommand for transaction" +
                                "- Started at {}, Ended at {} - Total exec time: {}",
                        dateTimeFormatter.format(execStart),
                        dateTimeFormatter.format(end_exec),
                        ChronoUnit.MILLIS.between(execStart, end_exec));

                return true;

            } catch (Exception e) {
                logger.error("Error occurred while attempting to submit flagged records");
                logger.error(e.getMessage(), e);
                throw e;
            }

        });

        return true;
    }

    @Autowired
    public void setTransactionMapper(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    @Autowired
    public void setTransactionRecordService(
            TransactionRecordService transactionRecordService) {
        this.transactionRecordService = transactionRecordService;
    }

    @Autowired
    public void setRtdTransactionPublisherService(
            RtdTransactionPublisherService rtdTransactionPublisherService) {
        this.rtdTransactionPublisherService = rtdTransactionPublisherService;
    }

    @Autowired
    public void setBpdTransactionPublisherService(
            BpdTransactionPublisherService bpdTransactionPublisherService) {
        this.bpdTransactionPublisherService = bpdTransactionPublisherService;
    }

    @Autowired
    public void setBpdCashbackTransactionPublisherService(
            BpdCashbackTransactionPublisherService bpdCashbackTransactionPublisherService) {
        this.bpdCashbackTransactionPublisherService = bpdCashbackTransactionPublisherService;
    }

    protected void setAsyncUtils(AsyncUtils asyncUtils) {
        this.asyncUtils = asyncUtils;
    }

}
