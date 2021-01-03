package me.scene.paper.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.paper.board.magazine.application.command.MemberService;
import me.scene.paper.common.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;


    @GetMapping("/magazines/{id}/members")
    public String memberPage(@PathVariable Long id, Model model) {
        List<String> memberNames = service.memberNames(id);

        model.addAttribute("id", id);
        model.addAttribute("members", memberNames);
        return "page/board/magazine/members";
    }


    @PutMapping("/magazines/{id}/members/{name}")
    public String addMember(@PathVariable Long id, @PathVariable String name,
                            @Principal String username) {

        service.addMember(id, username, name);

        return "redirect:" + ("/magazines/" + id + "/members");
    }

    @DeleteMapping("/magazines/{id}/members/{name}")
    public String removeMember(@PathVariable Long id, @PathVariable String name,
                               @Principal String username) {

        service.removeMember(id, username, name);

        return "redirect:" + ("/magazines/" + id + "/members");
    }


    @PostMapping("/magazines/{id}/members/apply")
    public String applyMember(@PathVariable Long id,
                              @Principal String username) {

        service.applyMember(id, username);

        // TODO alert applied
        return "redirect:" + ("/magazines" + id);
    }

    @PostMapping("/magazines/{id}/members/quit")
    public String quitMember(@PathVariable Long id,
                             @Principal String username) {

        service.quitMember(id, username);

        return "redirect:" + ("/magazines/" + id);
    }

}
