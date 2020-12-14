package it.gov.pagopa.rtd.transaction_error_manager.model.constants;

import java.util.HashMap;
import java.util.Map;

public class TransactionRecordConstants {

    public static String EXCEPTION_HEADER = "ERROR_DESC";
    public static String LISTENER_HEADER = "LISTENER";
    public static String REQUEST_ID_HEADER = "x-request-id";

    public static String RTD_TOPIC = "rtd-trx";
    public static String BPD_TOPIC = "bpd-trx";
    public static String FA_TOPIC = "fa-topic";

    public static Map<String, String> originListenerToTopic;
    static {
        originListenerToTopic = new HashMap<>();
        originListenerToTopic.put(
                "it.gov.pagopa.fa.transaction.listener.OnTransactionProcessRequestListener",
                "rtd-trx");
        originListenerToTopic.put(
                "it.gov.pagopa.bpd.payment_instrument.listener.OnTransactionFilterRequestListener",
                "rtd-trx");
        originListenerToTopic.put(
                "it.gov.pagopa.bpd.point_processor.listener.OnTransactionProcessRequestListener",
                "bpd-trx");
        originListenerToTopic.put(
                "it.gov.pagopa.bpd.winning_transaction.listener.OnTransactionSaveRequestListener",
                "bpd-trx");
    }

}
