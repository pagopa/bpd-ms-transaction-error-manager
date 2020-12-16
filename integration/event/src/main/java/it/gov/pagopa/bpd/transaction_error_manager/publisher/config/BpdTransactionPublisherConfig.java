package it.gov.pagopa.bpd.transaction_error_manager.publisher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the TransactionErrorPublisherConnector
 */

@Configuration
@PropertySource("classpath:config/bpdTransactionPublisher.properties")
public class BpdTransactionPublisherConfig {
}
