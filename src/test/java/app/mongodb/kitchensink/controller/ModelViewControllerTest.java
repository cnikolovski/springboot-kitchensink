package app.mongodb.kitchensink.controller;

import app.mongodb.kitchensink.service.MemberRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
public class ModelViewControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private MemberRegistrationService memberRegistrationService;

    @Test
    public void testIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attributeExists("member"));
    }

    @Test
    public void testRegister() throws Exception {
        mockMvc.perform(post("/")
                        .param("name", "bob")
                        .param("email", "test@test.com")
                        .param("phoneNumber", "1234567890"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testRegisterWithMissingFields() throws Exception {
        mockMvc.perform(post("/")
                        .param("name", "bob")
                        .param("phoneNumber", "1234567890"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeHasFieldErrors("member", "email"));
    }

    @Test
    public void testRegisterOnException() throws Exception {
        willThrow(new RuntimeException("error message")).given(memberRegistrationService).registerMember(any());
        mockMvc.perform(post("/")
                        .param("name", "bob")
                        .param("email", "test@test.com")
                        .param("phoneNumber", "1234567890"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attributeExists("responseMessage"));
    }
}
