package me.scene.dinner.board.magazine.ui;

import me.scene.dinner.account.domain.Account;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.common.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MagazineController {

    private final MagazineService magazineService;

    @Autowired
    public MagazineController(MagazineService magazineService) {
        this.magazineService = magazineService;
    }


    @GetMapping("/magazine-form")
    public String shipMagazineForm(Model model) {
        model.addAttribute("magazineForm", new MagazineForm());
        return "page/board/magazine/form";
    }

    @PostMapping("/magazines")
    public String createMagazine(@CurrentUser Account current, @Valid MagazineForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/magazine/form";

        Long id = magazineService.save(current.getUsername(), form.getTitle(), form.getShortExplanation(), form.getLongExplanation(), form.getMagazinePolicy());
        return "redirect:" + ("/magazines/" + id);
    }

    @GetMapping("/magazines/{magazineId}")
    public String showMagazine(@PathVariable Long magazineId, Model model) {
        model.addAttribute("magazine", magazineService.find(magazineId));
        return "page/board/magazine/view";
    }

    @GetMapping("/magazines")
    public String showList(Model model) {
        model.addAttribute("magazines", magazineService.findAll());
        return "page/board/magazine/list";
    }

}
