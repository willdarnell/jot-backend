package jotbackend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class ExampleController {

    @RequestMapping("/api/example")
    @CrossOrigin(origins = "http://localhost:3000")
    public String index() {
        return "Hi I'm the backend!";
    }

    @RequestMapping(value = "/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public String test() {
        return "Test";
    }
}