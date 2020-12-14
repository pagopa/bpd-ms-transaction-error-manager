package it.gov.pagopa.rtd.transaction_error_manager.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.rtd.transaction_error_manager.command.SaveTransactionRecordCommand;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * See {@link RtdTransactionErrorManagerController}
 */
@RestController
class RtdTransactionErrorManagerControllerImpl extends StatelessController implements RtdTransactionErrorManagerController {

    private final BeanFactory beanFactory;

    @Autowired
    RtdTransactionErrorManagerControllerImpl(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void resubmitTransactions() throws Exception {
        SaveTransactionRecordCommand command =
                beanFactory.getBean(SaveTransactionRecordCommand.class);
        command.execute();
    }
}
