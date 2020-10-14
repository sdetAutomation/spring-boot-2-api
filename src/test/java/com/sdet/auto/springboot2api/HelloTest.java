package com.sdet.auto.springboot2api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void tc0001_helloWorld() throws Exception {
        mvc.perform(get("/helloworld"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, welcome to my springboot app!"));
    }

    @Test
    public void tc0002_helloWorld_bean() throws Exception {
        mvc.perform(get("/helloworld-bean"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"firstname\":\"sdet\",\"lastname\":\"automation\"," +
                        "\"city\":\"partsunknown\"}"));
    }

    @Test
    public void tc0003_home() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, welcome to sdetAutomation's springboot app.  " +
                        "To see the swagger page please add \"/swagger-ui.html\" to the end of the url path!"));
    }
}