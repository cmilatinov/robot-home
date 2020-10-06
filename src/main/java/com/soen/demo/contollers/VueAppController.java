package com.soen.demo.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VueAppController {

    @GetMapping("/**/{[path:[^.]*}")
    public String serve() {
        return "forward:/index.html";
    }

}
