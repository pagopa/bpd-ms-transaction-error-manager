package it.gov.pagopa.bpd.transaction_error_manager.listener.factory;

/**
 * interface to be used for inheritance for model factories from a DTO
 */

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
