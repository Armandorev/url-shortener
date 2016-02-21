package it.w0rd.api.auth;

import it.w0rd.Application;
import it.w0rd.filters.AdminAuthFilter;
import it.w0rd.persistence.PersistenceInitializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class, initializers = PersistenceInitializer.class)
@WebIntegrationTest("server.port:0")
public class SingleAdminAuthenticationIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setupWac() throws AdminAuthenticationCredentialsError {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new AdminAuthFilter()).build();
    }

    @Test
    public void checkNoCredentialsSet() throws Exception {
        mockMvc.perform(
                get("/api/admin/words")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Test
    public void checkWrongCredentials() throws Exception {
        mockMvc.perform(
                get("/api/admin/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + getAuthKey("admin", "wrong-password")))
                .andExpect(status().is(HttpServletResponse.SC_UNAUTHORIZED));
    }

    @Test
    public void checkCorrectCredentials() throws Exception {
        mockMvc.perform(
                get("/api/admin/words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic " + getAuthKey("admin", "default")))
                .andExpect(status().is(HttpServletResponse.SC_OK));
    }

    private String getAuthKey(String user, String password) {
        return new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
    }

}
