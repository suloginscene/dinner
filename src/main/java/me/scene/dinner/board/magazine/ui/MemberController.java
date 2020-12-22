package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.board.magazine.application.command.MemberService;
import me.scene.dinner.common.security.Current;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService service;


    @GetMapping("/magazines/{id}/members/page")
    public String memberPage(@PathVariable Long id, Model model) {
        List<String> memberNames = service.memberNames(id);

        model.addAttribute("id", id);
        model.addAttribute("members", memberNames);
        return "page/board/magazine/members";
    }


    @PostMapping("/magazines/{id}/members")
    public String applyMember(@PathVariable Long id,
                              @Current Account current) {

        String username = current.getUsername();
        service.applyMember(id, username);

        return "redirect:" + ("/sent-to-manager?magazineId=" + id);
    }

    @DeleteMapping("/magazines/{id}/members")
    public String quitMember(@PathVariable Long id,
                             @Current Account current) {

        String username = current.getUsername();
        service.quitMember(id, username);

        return "redirect:" + ("/magazines/" + id);
    }


    @GetMapping("/magazines/{id}/{member}")
    public String addMember(@PathVariable Long id, @PathVariable String member,
                            @Current Account current) {

        String username = current.getUsername();
        service.addMember(id, username, member);

        return "redirect:" + ("/magazines/" + id + "/members");
    }

    @DeleteMapping("/magazines/{id}/{member}")
    public String removeMember(@PathVariable Long id, @PathVariable String member,
                               @Current Account current) {

        String username = current.getUsername();
        service.removeMember(id, username, member);

        return "redirect:" + ("/magazines/" + id + "/members");
    }

}
