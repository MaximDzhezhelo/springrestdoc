package smartjava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

public class MyResultHandler implements ResultHandler {
    private Logger logger = LoggerFactory.getLogger(MyResultHandler.class);


    static public ResultHandler myHandler() {
        return new MyResultHandler();
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        MockHttpServletRequest request = result.getRequest();
        MockHttpServletResponse response = result.getResponse();
        logger.error("HTTP method: {}, status code: {}", request.getMethod(), response.getStatus());
    }
}