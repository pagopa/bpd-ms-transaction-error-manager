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

@Import({BpdTransactionPublisherConnector.class})
@TestPropertySource(
        locations = "classpath:config/testBpdTransactionPublisher.properties",
        properties = {
                "connectors.eventConfigurations.items.BpdTransactionPublisherConnector.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class BpdTransactionPublisherConnectorTest extends
        BaseEventConnectorTest<Transaction, Boolean, Transaction, Void, BpdTransactionPublisherConnector> {

    @Value("${connectors.eventConfigurations.items.BpdTransactionPublisherConnector.topic}")
    private String topic;

    @Autowired
    private BpdTransactionPublisherConnector bpdTransactionPublisherConnector;

    @Override
    protected BpdTransactionPublisherConnector getEventConnector() {
        return bpdTransactionPublisherConnector;
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