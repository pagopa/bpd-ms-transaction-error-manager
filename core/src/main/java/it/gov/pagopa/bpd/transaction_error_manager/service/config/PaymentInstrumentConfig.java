package it.gov.pagopa.bpd.transaction_error_manager.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/paymentInstrument.properties")
public class PaymentInstrumentConfig {
}
