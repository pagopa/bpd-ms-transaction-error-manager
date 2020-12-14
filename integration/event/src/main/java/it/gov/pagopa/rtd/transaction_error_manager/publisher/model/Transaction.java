package it.gov.pagopa.rtd.transaction_error_manager.publisher.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Model for transaction to be sent in the outbound channel
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"idTrxAcquirer", "acquirerCode", "trxDate", "operationType"}, callSuper = false)
public class Transaction {

    String idTrxAcquirer;

    String acquirerCode;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    OffsetDateTime trxDate;

    String hpan;

    String operationType;

    String circuitType;

    String idTrxIssuer;

    String correlationId;

    BigDecimal amount;

    String amountCurrency;

    String mcc;

    String acquirerId;

    String merchantId;

    String terminalId;

    String bin;

    String fiscalCode;

    BigDecimal score;

    Long awardPeriodId;

}
