package it.gov.pagopa.bpd.transaction_error_manager.controller;

import it.gov.pagopa.bpd.transaction_error_manager.command.SaveTransactionRecordCommand;
import it.gov.pagopa.bpd.transaction_error_manager.command.SubmitFlaggedRecordsCommand;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BpdTransactionErrorManagerControllerImpl.class)
@WebMvcTest(value = BpdTransactionErrorManagerControllerImpl.class, secure = false)
@EnableWebMvc
public class RtdTransactionErrorManagerControllerImplTest {

    private final String BASE_URL = "/bpd/transaction-error-manager";

    @MockBean
    private SubmitFlaggedRecordsCommand saveTransactionRecordCommand;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void resubmitTransactions_OK() {

        try {
            Mockito.doReturn(true).when(saveTransactionRecordCommand).execute();
            mockMvc.perform(
                    MockMvcRequestBuilders.post(BASE_URL.concat("/resubmitTransactions"))
                            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

    }

}