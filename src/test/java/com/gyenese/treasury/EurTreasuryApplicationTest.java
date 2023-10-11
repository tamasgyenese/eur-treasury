package com.gyenese.treasury;

import com.gyenese.treasury.service.KafkaListenerTypeExcludeFilter;
import com.gyenese.treasury.service.TreasuryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
@AutoConfigureMockMvc
@TypeExcludeFilters(KafkaListenerTypeExcludeFilter.class)
public class EurTreasuryApplicationTest {

    private static final String URI = "/api/v1/treasury";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TreasuryService treasuryService;

    @Test
    public void testGetBalanceSuccessFully() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(URI + "/balance/1/EUR"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertEquals(result,"{\"id\":1,\"accountId\":1,\"amount\":32.45,\"currency\":\"EUR\"}");
    }

    @Test
    public void testGetBalanceAccountNotFound() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(URI + "/balance/332/EUR"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        String result = resultActions.andReturn().getResolvedException().getMessage();
        assertEquals(result,"Account not found with the given id: 332");
    }

    @Test
    public void testGetBalanceBalanceNotFound() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(URI + "/balance/1/USD"))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        String result = resultActions.andReturn().getResolvedException().getMessage();
        assertEquals(result,"Balance not found with the given id: 001 for currency: USD");
    }
}
