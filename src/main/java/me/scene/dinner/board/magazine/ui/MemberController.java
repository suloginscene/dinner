package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.magazine.application.member.MemberService;
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


    @GetMapping("/magazines/{magazineId}/members/page")
    public String memberPage(@PathVariable Long magazineId, Model model) {
        List<String> memberNames = service.memberNames(magazineId);
        model.addAttribute("id", magazineId);
        model.addAttribute("members", memberNames);
        return "page/board/magazine/members";
    }


    @PostMapping("/magazines/{magazineId}/members")
    public String applyMember(@PathVariable Long magazineId, @Current Account current) {
        service.applyMember(magazineId, current.getUsername());
        return "redirect:" + ("/sent-to-manager?magazineId=" + magazineId);
    }

    @DeleteMapping("/magazines/{magazineId}/members")
    public String quitMember(@PathVariable Long magazineId, @Current Account current) {
        String currentUsername = current.getUsername();
        service.quitMember(magazineId, currentUsername);
        return "redirect:" + ("/magazines/" + magazineId);
    }


    @GetMapping("/magazines/{magazineId}/{member}")
    public String addMember(@PathVariable Long magazineId, @PathVariable String member, @Current Account current) {
        service.addMember(magazineId, current.getUsername(), member);
        return "redirect:" + ("/magazines/" + magazineId + "/members");
    }

    @DeleteMapping("/magazines/{magazineId}/{member}")
    public String removeMember(@PathVariable Long magazineId, @PathVariable String member, @Current Account current) {
        service.removeMember(magazineId, current.getUsername(), member);
        return "redirect:" + ("/magazines/" + magazineId + "/members");
    }

}
