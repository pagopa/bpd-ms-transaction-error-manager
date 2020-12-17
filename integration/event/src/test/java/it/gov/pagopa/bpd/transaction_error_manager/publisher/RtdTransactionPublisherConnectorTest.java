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

@Import({RtdTransactionPublisherConnector.class})
@TestPropertySource(
        locations = "classpath:config/testRtdTransactionPublisher.properties",
        properties = {
                "connectors.eventConfigurations.items.RtdTransactionPublisherConnector.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class RtdTransactionPublisherConnectorTest extends
        BaseEventConnectorTest<Transaction, Boolean, Transaction, Void, RtdTransactionPublisherConnector> {

    @Value("${connectors.eventConfigurations.items.RtdTransactionPublisherConnector.topic}")
    private String topic;

    @Autowired
    private RtdTransactionPublisherConnector rtdTransactionPublisherConnector;

    @Override
    protected RtdTransactionPublisherConnector getEventConnector() {
        return rtdTransactionPublisherConnector;
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