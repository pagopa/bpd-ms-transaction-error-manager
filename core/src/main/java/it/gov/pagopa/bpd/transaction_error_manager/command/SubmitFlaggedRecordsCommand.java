package it.gov.pagopa.bpd.transaction_error_manager.command;

import eu.sia.meda.core.command.Command;

/**
 * public interface for the SaveTransactionRecordCommand extending Meda Command interface
 */

public interface SubmitFlaggedRecordsCommand extends Command<Boolean> {}
