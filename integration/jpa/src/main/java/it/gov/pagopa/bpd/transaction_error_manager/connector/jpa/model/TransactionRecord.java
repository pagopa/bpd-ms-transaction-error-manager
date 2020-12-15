package it.gov.pagopa.bpd.transaction_error_manager.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"recordId"}, callSuper = false)
@Table(name = "rtd_transaction_record")
public class TransactionRecord extends BaseEntity {

    @Id
    String recordId;

    @Column(name = "id_trx_acquirer_s")
    String idTrxAcquirer;

    @Column(name = "acquirer_c")
    String acquirerCode;

    @Column(name = "trx_timestamp_t")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime trxDate;

    @Column(name="hpan_s")
    String hpan;

    @Column(name = "operation_type_c")
    String operationType;

    @Column(name = "circuit_type_c")
    String circuitType;

    @Column(name="id_trx_issuer_s")
    String idTrxIssuer;

    @Column(name="correlation_id_s")
    String correlationId;

    @Column(name="amount_i")
    BigDecimal amount;

    @Column(name="amount_currency_c")
    String amountCurrency;

    @Column(name="mcc_c")
    String mcc;

    @Column(name="mcc_descr_s")
    String mccDescription;

    @Column(name = "score_n")
    BigDecimal score;

    @Column(name="award_period_id_n")
    Long awardPeriodId;

    @Column(name="acquirer_id_s")
    String acquirerId;

    @Column(name="merchant_id_s")
    String merchantId;

    @Column(name="bin_s")
    String bin;

    @Column(name="terminal_id_s")
    String terminalId;

    @Column(name="fiscal_code_s")
    String fiscalCode;

    @Column(name="origin_topic_s")
    String originTopic;

    @Column(name="origin_listener_s")
    String originListener;

    @Column(name="origin_request_id_s")
    String originRequestId;

    @Column(name="exception_message_s")
    String exceptionMessage;

    @Column(name="last_resubmit_date")
    OffsetDateTime lastResubmitDate;

    @Column(name="to_resubmit_b")
    Boolean toResubmit;

}




