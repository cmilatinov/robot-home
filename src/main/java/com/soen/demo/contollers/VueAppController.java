package com.soen.demo.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:8080")
@Controller
public class VueAppController {

    @GetMapping("/**/{[path:[^.]*}")
    public String serve() {
        return "forward:/index.html";
    }

}
