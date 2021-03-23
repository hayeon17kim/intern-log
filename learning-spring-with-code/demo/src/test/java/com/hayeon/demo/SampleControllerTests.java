package com.hayeon.demo;

import com.google.gson.Gson;
import com.hayeon.demo.domain.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
// Test for Controller
@WebAppConfiguration
@ContextConfiguration(classes = {
        com.hayeon.demo.config.RootConfig.class,
        com.hayeon.demo.config.ServletConfig.class
})
@Slf4j
public class SampleControllerTests {

    @Setter(onMethod_ = {@Autowired})
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testConvert() throws Exception {

        Ticket ticket = new Ticket();
        ticket.setTno(123);
        ticket.setOwner("admin");
        ticket.setGrade("AAA");

        String jsonStr = new Gson().toJson(ticket);
        
        log.info(jsonStr);

        mockMvc.perform(post("/sample")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonStr))
            .andExpect(status().is(200));
    }
}
