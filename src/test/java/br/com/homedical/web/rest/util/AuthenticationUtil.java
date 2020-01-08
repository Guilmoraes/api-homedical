package br.com.homedical.web.rest.util;

import br.com.homedical.web.rest.TestUtil;
import br.com.homedical.web.rest.vm.LoginVM;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Pichau on 23/10/2017.
 */
public class AuthenticationUtil {

    public static String getAccessToken(MockMvc mockMvc, String username, String password) throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername(username);
        loginVM.setPassword(password);

        String content = mockMvc
            .perform(post("/api/authenticate")
                .with(csrf())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loginVM)))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        HashMap<String, Object> result = new ObjectMapper().readValue(content, HashMap.class);
        return (String) result.get("id_token");
    }
}
