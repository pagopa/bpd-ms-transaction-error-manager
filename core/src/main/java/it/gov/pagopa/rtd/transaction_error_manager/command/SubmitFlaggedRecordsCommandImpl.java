package it.gov.pagopa.rtd.transaction_error_manager.command;

import eu.sia.meda.core.command.BaseCommand;
import it.gov.pagopa.rtd.transaction_error_manager.connector.jpa.model.TransactionRecord;
import it.gov.pagopa.rtd.transaction_error_manager.model.TransactionCommandModel;
import it.gov.pagopa.rtd.transaction_error_manager.model.constants.TransactionRecordConstants;
import it.gov.pagopa.rtd.transaction_error_manager.publisher.model.Transaction;
import it.gov.pagopa.rtd.transaction_error_manager.service.BpdTransactionPublisherService;
import it.gov.pagopa.rtd.transaction_error_manager.service.FaTransactionPublisherService;
import it.gov.pagopa.rtd.transaction_error_manager.service.RtdTransactionPublisherService;
import it.gov.pagopa.rtd.transaction_error_manager.service.TransactionRecordService;
import it.gov.pagopa.rtd.transaction_error_manager.service.mapper.TransactionMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Base implementation of the SaveTransactionCommandInterface, extending Meda BaseCommand class, the command
 * represents the class interacted with at api level, hiding the multiple calls to the integration connectors
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Slf4j
class SubmitFlaggedRecordsCommandImpl extends BaseCommand<Boolean> implements SaveTransactionRecordCommand {

    private TransactionRecordService transactionRecordService;
    private RtdTransactionPublisherService rtdTransactionPublisherService;
    private BpdTransactionPublisherService bpdTransactionPublisherService;
    private FaTransactionPublisherService faTransactionPublisherService;
    private TransactionMapper transactionMapper;


    public SubmitFlaggedRecordsCommandImpl(TransactionCommandModel transactionCommandModel) {}

    public SubmitFlaggedRecordsCommandImpl(
            TransactionRecordService transactionRecordService,
            RtdTransactionPublisherService rtdTransactionPublisherService,
            BpdTransactionPublisherService bpdTransactionPublisherService,
            FaTransactionPublisherService faTransactionPublisherService,
            TransactionMapper transactionMapper) {
        this.transactionRecordService = transactionRecordService;
        this.rtdTransactionPublisherService = rtdTransactionPublisherService;
        this.bpdTransactionPublisherService = bpdTransactionPublisherService;
        this.faTransactionPublisherService = faTransactionPublisherService;
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

        try {

           List<TransactionRecord> transactionRecordList = transactionRecordService.findRecordsToResubmit();
           for (TransactionRecord transactionRecord : transactionRecordList) {
               Transaction transaction = transactionMapper.mapTransaction(transactionRecord);
               RecordHeaders recordHeaders = new RecordHeaders();
               recordHeaders.add(TransactionRecordConstants.REQUEST_ID_HEADER,
                       transactionRecord.getOriginRequestId() != null ?
                               "Resubmitted".getBytes() :
                               "Resubmitted;".concat(transactionRecord.getOriginRequestId()).getBytes());
               switch (transactionRecord.getOriginTopic()) {
                   case "bpd-trx":
                        bpdTransactionPublisherService.publishBpdTransactionEvent(transaction, recordHeaders);
                        break;
                   case "fa-trx":
                       faTransactionPublisherService.publishFaTransactionEvent(transaction, recordHeaders);
                       break;
                   case "rtd-trx":
                       rtdTransactionPublisherService.publishRtdTransactionEvent(transaction, recordHeaders);
                       break;
               }

               transactionRecord.setToResubmit(false);
               transactionRecord.setLastResubmitDate(OffsetDateTime.now());
               transactionRecordService.saveTransactionRecord(transactionRecord);
           }

           return true;

        } catch (Exception e) {
            throw e;
        }

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
    public void setFaTransactionPublisherService(
            FaTransactionPublisherService faTransactionPublisherService) {
        this.faTransactionPublisherService = faTransactionPublisherService;
    }

}
