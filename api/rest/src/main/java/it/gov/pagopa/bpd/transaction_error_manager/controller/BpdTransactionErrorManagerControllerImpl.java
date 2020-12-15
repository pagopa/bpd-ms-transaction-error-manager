package it.gov.pagopa.bpd.transaction_error_manager.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.transaction_error_manager.command.SaveTransactionRecordCommand;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * See {@link BpdTransactionErrorManagerControllerImpl}
 */
@RestController
class BpdTransactionErrorManagerControllerImpl extends StatelessController implements BpdTransactionErrorManagerController {

    private final BeanFactory beanFactory;

    @Autowired
    BpdTransactionErrorManagerControllerImpl(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void resubmitTransactions() throws Exception {
        SaveTransactionRecordCommand command =
                beanFactory.getBean(SaveTransactionRecordCommand.class);
        command.execute();
    }
}
