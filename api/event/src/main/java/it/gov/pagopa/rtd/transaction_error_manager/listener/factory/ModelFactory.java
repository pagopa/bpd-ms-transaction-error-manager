package it.gov.pagopa.rtd.transaction_error_manager.listener.factory;

/**
 * interface to be used for inheritance for model factories from a DTO
 */

public interface ModelFactory<T, U> {

    U createModel(T dto);

}
