package me.scene.dinner.domain.board.ui;

import me.scene.dinner.domain.account.domain.Account;
import me.scene.dinner.domain.board.application.MagazineDto;
import me.scene.dinner.domain.board.application.MagazineService;
import me.scene.dinner.infra.security.CurrentUser;
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
        MagazineForm magazineForm = new MagazineForm();
        model.addAttribute("magazineForm", magazineForm);
        return "page/board/magazine/form";
    }

    @PostMapping("/magazines")
    public String createMagazine(@CurrentUser Account current, @Valid MagazineForm form, Errors errors) {
        if (errors.hasErrors()) return "page/board/magazine/form";

        Long id = magazineService.save(current.getId(),
                form.getTitle(), form.getShortExplanation(), form.getLongExplanation(), form.getMagazinePolicy());
        return "redirect:" + ("/magazines/" + id);
    }

    @GetMapping("/magazines/{magazineId}")
    public String showMagazine(@PathVariable Long magazineId, Model model) {
        MagazineDto magazineDto = magazineService.extractDto(magazineId);
        model.addAttribute("magazineDto", magazineDto);
        return "page/board/magazine/view";
    }

}
