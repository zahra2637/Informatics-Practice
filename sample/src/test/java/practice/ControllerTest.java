package practice;
import com.sample.app.entity.PersonDto;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ControllerTest extends AbstractTest {
    public static final String CREATE_REQUEST_URL ="http://localhost:8080/api/create";
    public static final String GET_REQUEST_URL = "http://localhost:8080/api/get";
    public static final String DELETE_REQUEST_URL ="http://localhost:8080/api/delete?personId=1";
    @Override
    public void setUp() {
        super.setUp();
    }
    @Test
    void createTest() throws Exception {
   for (int j = 0; 3 > j; j++) {
       PersonDto request = new PersonDto();
         request.setFirstName(RandomStringUtils.randomAlphabetic(5));
        request.setLastName(RandomStringUtils.randomAlphabetic(8));
        request.setAge(Integer.parseInt(RandomStringUtils.randomNumeric(2)));
        int response = super.postRequestTest(CREATE_REQUEST_URL, request);
        Assertions.assertEquals(HttpServletResponse.SC_OK, response);
          }
    }
    @Test
    void deleteTest() throws Exception {
        int response = super.deleteRequestTest(DELETE_REQUEST_URL);
        Assertions.assertEquals(HttpServletResponse.SC_OK, response);
    }
    @Test
    void findAllTest() throws Exception {
        int response = super.findAllRequestTest(GET_REQUEST_URL);
        Assertions.assertEquals(HttpServletResponse.SC_OK, response);
    }
//    @Test
//    void nullFirstNameTest() throws Exception {
//        PersonDto request = new PersonDto();
//        request.setFirstName("");
//        request.setLastName(RandomStringUtils.randomNumeric(8));
//        request.setAge(Integer.parseInt(RandomStringUtils.randomNumeric(2)));
//        int response = super.postRequestTest(CREATE_REQUEST_URL, request);
//        Assertions.assertEquals(HttpServletResponse.SC_BAD_REQUEST, response);
//    }

}
