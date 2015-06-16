package benjamin.groehbiel.ch;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/index")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/test",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> test() {
        return Arrays.asList("abc","klm","xyz","pqr");
    }

}