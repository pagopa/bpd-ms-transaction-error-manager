package it.gov.pagopa.bpd.transaction_error_manager.controller;

import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali payment-instrument Controller")
@RequestMapping("/bpd/transaction-error-manager")
public interface BpdTransactionErrorManagerController {

    @PostMapping(value = "/resubmitTransactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    void resubmitTransactions() throws Exception;

}
