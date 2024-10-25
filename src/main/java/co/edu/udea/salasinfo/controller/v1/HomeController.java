package co.edu.udea.salasinfo.controller.v1;

import lombok.Generated;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Generated
@RequestMapping("/v1/home")
public class HomeController {
    @GetMapping
    public String welcome() {
        return "index";
    }
}
