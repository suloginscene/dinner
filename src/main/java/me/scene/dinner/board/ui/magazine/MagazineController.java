package me.scene.dinner.board.ui.magazine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.scene.dinner.account.application.CurrentUser;
import me.scene.dinner.account.domain.account.Account;
import me.scene.dinner.board.application.magazine.MagazineBestListCache;
import me.scene.dinner.board.application.magazine.MagazineService;
import me.scene.dinner.board.domain.magazine.Magazine;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor @Slf4j
public class MagazineController {

    private final MagazineService magazineService;
    private final MagazineBestListCache bestListCache;

    @GetMapping("/magazine-form")
    public String shipMagazineForm(Model model) {
        model.addAttribute("magazineForm", new MagazineForm());
        return "page/board/magazine/form";
    }

    @PostMapping("/magazines")
    public String createMagazine(@CurrentUser Account current, @Valid MagazineForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/magazine/form";

        Long id = magazineService.save(current.getUsername(), form.getTitle(), form.getShortExplanation(), form.getLongExplanation(), form.getPolicy());
        return "redirect:" + ("/magazines/" + id);
    }

    @GetMapping("/magazines/{magazineId}")
    public String showMagazine(@PathVariable Long magazineId, Model model) {
        Magazine magazine = magazineService.find(magazineId);
        model.addAttribute("magazine", magazine);
        model.addAttribute("members", magazine.getMembers());
        return "page/board/magazine/view";
    }

    @GetMapping("/magazines/{magazineId}/form")
    public String updateForm(@PathVariable Long magazineId, @CurrentUser Account current, Model model) {
        Magazine magazine = magazineService.find(magazineId);
        magazine.confirmManager(current.getUsername());

        model.addAttribute("id", magazineId);
        model.addAttribute("updateForm", updateForm(magazine));
        return "page/board/magazine/update";
    }

    private MagazineForm updateForm(Magazine m) {
        MagazineForm f = new MagazineForm();
        f.setTitle(m.getTitle());
        f.setShortExplanation(m.getShortExplanation());
        f.setLongExplanation(m.getLongExplanation());
        f.setPolicy(m.getPolicy().name());
        return f;
    }

    @PutMapping("/magazines/{magazineId}")
    public String update(@PathVariable Long magazineId, @CurrentUser Account current, @Valid MagazineForm form, Errors errors) {
        if (errors.hasErrors()) return "redirect:" + ("/magazines/" + magazineId + "/form");

        magazineService.update(magazineId, current.getUsername(), form.getTitle(), form.getShortExplanation(), form.getLongExplanation());
        return "redirect:" + ("/magazines/" + magazineId);
    }

    @DeleteMapping("/magazines/{magazineId}")
    public String delete(@PathVariable Long magazineId, @CurrentUser Account current) {
        magazineService.delete(magazineId, current.getUsername());
        return "redirect:" + ("/");
    }

    @PostMapping("/magazines/{magazineId}/members")
    public String applyMember(@PathVariable Long magazineId, @CurrentUser Account current) {
        magazineService.applyMember(magazineId, current.getUsername());
        return "redirect:" + ("/sent-to-manager?magazineId=" + magazineId);
    }

    @DeleteMapping("/magazines/{magazineId}/members")
    public String quitMember(@PathVariable Long magazineId, @CurrentUser Account current) {
        String currentUsername = current.getUsername();
        magazineService.quitMember(magazineId, currentUsername);
        return "redirect:" + ("/magazines/" + magazineId);
    }

    @GetMapping("/magazines/{magazineId}/members")
    public String manageMembers(@PathVariable Long magazineId, @CurrentUser Account current, Model model) {
        Magazine magazine = magazineService.find(magazineId);
        magazine.confirmManager(current.getUsername());
        magazine.confirmPolicyManaged();

        model.addAttribute("id", magazineId);
        model.addAttribute("members", magazine.getMembers());
        return "page/board/magazine/members";
    }

    @GetMapping("/magazines/{magazineId}/{member}")
    public String addMember(@PathVariable Long magazineId, @PathVariable String member, @CurrentUser Account current) {
        magazineService.addMember(magazineId, current.getUsername(), member);
        return "redirect:" + ("/magazines/" + magazineId + "/members");
    }

    @DeleteMapping("/magazines/{magazineId}/{member}")
    public String removeMember(@PathVariable Long magazineId, @PathVariable String member, @CurrentUser Account current) {
        magazineService.removeMember(magazineId, current.getUsername(), member);
        return "redirect:" + ("/magazines/" + magazineId + "/members");
    }

    @GetMapping("/magazines")
    public String showList(Model model) {
        model.addAttribute("magazines", magazineService.findAll());
        return "page/board/magazine/list";
    }

    @GetMapping("/api/magazines")
    public @ResponseBody List<Magazine> bestList() {
        return bestListCache.get();
    }

    @GetMapping("/api/magazines/{username}")
    public @ResponseBody List<Magazine> byUser(@PathVariable String username) {
        return magazineService.findByManager(username);
    }

}
