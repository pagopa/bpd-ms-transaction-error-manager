package it.gov.pagopa.rtd.transaction_error_manager.publisher;

import eu.sia.meda.event.BaseEventConnector;
import eu.sia.meda.event.transformer.IEventRequestTransformer;
import eu.sia.meda.event.transformer.IEventResponseTransformer;
import it.gov.pagopa.rtd.transaction_error_manager.publisher.model.Transaction;
import org.springframework.stereotype.Service;

/**
 * Class extending the MEDA BaseEventConnector, is responsible for calling a Kafka outbound channel with messages
 * containing a json mapped on the Transaction class
 */

@Service
public class FaTransactionPublisherConnector
        extends BaseEventConnector<Transaction, Boolean, Transaction, Void> {

    /**
     * @param transaction         Transaction instance to be used as message content
     * @param requestTransformer  Trannsformer for the request data
     * @param responseTransformer Transformer for the call response
     * @param args                Additional args to be used in the call
     * @return Exit status for the call
     */
    public Boolean doCall(
            Transaction transaction, IEventRequestTransformer<Transaction,
            Transaction> requestTransformer,
            IEventResponseTransformer<Void, Boolean> responseTransformer,
            Object... args) {
        return this.call(transaction, requestTransformer, responseTransformer, args);
    }

}
