package me.scene.paper.admin.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminController {

    @GetMapping("/admin")
    public String main() {
        return "page/admin/main";
    }

}
