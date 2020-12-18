package it.gov.pagopa.bpd.transaction_error_manager.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.sia.meda.event.service.ErrorPublisherService;
import eu.sia.meda.eventlistener.BaseEventListenerTest;
import it.gov.pagopa.bpd.transaction_error_manager.command.SaveTransactionRecordCommand;
import it.gov.pagopa.bpd.transaction_error_manager.listener.factory.SaveTransactionCommandModelFactory;
import it.gov.pagopa.bpd.transaction_error_manager.publisher.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


/**
 * Test class for the OnTransactionFilterRequestListener method
 */

@Import({OnTransactionErrorRequestListener.class})
@TestPropertySource(
        locations = "classpath:config/testTransactionRequestListener.properties",
        properties = {
                "listeners.eventConfigurations.items.OnTransactionErrorRequestListener.bootstrapServers=${spring.embedded.kafka.brokers}"
        })
public class OnPointProcessorTransactionErrorRequestListenerTest extends BaseEventListenerTest {


    @SpyBean
    ObjectMapper objectMapperSpy;
    @SpyBean
    OnTransactionErrorRequestListener onTransactionFilterRequestListenerSpy;
    @SpyBean
    SaveTransactionCommandModelFactory saveTransactionCommandModelFactorySpy;
    @MockBean
    SaveTransactionRecordCommand saveTransactionRecordCommandMock;

    @Value("${listeners.eventConfigurations.items.OnTransactionErrorRequestListener.topic}")
    private String topic;

    @Before
    public void setUp() throws Exception {

        Mockito.reset(
                onTransactionFilterRequestListenerSpy,
                saveTransactionCommandModelFactorySpy,
                saveTransactionRecordCommandMock);
        Mockito.doReturn(true).when(saveTransactionRecordCommandMock).execute();

    }

    @Override
    protected Object getRequestObject() {
        return Transaction.builder()
                .idTrxAcquirer("1")
                .acquirerCode("001")
                .trxDate(OffsetDateTime.parse("2020-04-09T16:22:45.304Z"))
                .amount(BigDecimal.valueOf(1313.13))
                .operationType("00")
                .hpan("hpan")
                .merchantId("0")
                .circuitType("00")
                .mcc("813")
                .idTrxIssuer("0")
                .amountCurrency("833")
                .correlationId("1")
                .acquirerId("0")
                .bin("000001")
                .terminalId("0")
                .build();
    }

    @Override
    protected String getTopic() {
        return topic;
    }

    @Override
    protected void verifyInvocation(String json) {
        try {
            BDDMockito.verify(saveTransactionCommandModelFactorySpy, Mockito.atLeastOnce())
                    .createModel(Mockito.any());
            BDDMockito.verify(objectMapperSpy, Mockito.atLeastOnce())
                    .readValue(Mockito.anyString(), Mockito.eq(Transaction.class));
            BDDMockito.verify(saveTransactionRecordCommandMock, Mockito.atLeastOnce()).execute();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Override
    protected ErrorPublisherService getErrorPublisherService() {
        return null;
    }

}