package it.gov.pagopa.bpd.transaction_error_manager.publisher;

import eu.sia.meda.event.BaseEventConnectorTest;
import eu.sia.meda.util.TestUtils;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

/**
 * Test class for the PointTransactionPublisherConnector class
 */

@Import({BpdCashbackTransactionPublisherConnector.class})
@TestPropertySource(
        locations = "classpath:config/testBpdCashbackTransactionPublisher.properties",
        properties = {
                "connectors.eventConfigurations.items.BpdCashbackTransactionPublisherConnector.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class BpdCashbackTransactionPublisherConnectorTest extends
        BaseEventConnectorTest<Transaction, Boolean, Transaction, Void, BpdCashbackTransactionPublisherConnector> {

    @Value("${connectors.eventConfigurations.items.BpdCashbackTransactionPublisherConnector.topic}")
    private String topic;

    @Autowired
    private BpdCashbackTransactionPublisherConnector bpdCashbackTransactionPublisherConnector;

    @Override
    protected BpdCashbackTransactionPublisherConnector getEventConnector() {
        return bpdCashbackTransactionPublisherConnector;
    }

    @Override
    protected Transaction getRequestObject() {
        return TestUtils.mockInstance(new Transaction());
    }

    @Override
    protected String getTopic() {
        return topic;
    }

}