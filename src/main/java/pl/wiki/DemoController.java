package pl.wiki;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {


    @GetMapping(path = "/")
    public String welcomePage(){
        return "welcome";
    }

    @GetMapping("/demo")
    public String demoPage(){
        return "article";
    }

    @GetMapping("/articleList")
    public String articleListPage(){
        return "articleList";
    }
}
