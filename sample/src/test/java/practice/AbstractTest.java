package practice;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.app.PracticeApplication;
import com.sample.app.entity.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PracticeApplication.class)
@AutoConfigureMockMvc
public  abstract class AbstractTest extends BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${token.headerName}")
    private String tokenHeaderName;
    @Value("${user.userName}")
    private String username;
    @Value("${user.password}")
    private String password;

    protected void setUp()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    protected byte[] mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsBytes(obj);
    }

    protected int postRequestTest(String url, PersonDto dto) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(url).accept(MediaType.ALL)
                .content(getObjectMapper().writeValueAsBytes(dto)).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(tokenHeaderName, "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        MvcResult result = mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        int response = result.getResponse().getStatus();
        return response;

    }
    protected int deleteRequestTest(String url) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url).accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header(tokenHeaderName, "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        MvcResult result = mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        int response = result.getResponse().getStatus();
        return response;

    }
    protected int findAllRequestTest(String url) throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(url).accept(MediaType.ALL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(tokenHeaderName, "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));
        MvcResult result = mockMvc.perform(requestBuilder).andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        int response = result.getResponse().getStatus();
        return response;

    }


}
