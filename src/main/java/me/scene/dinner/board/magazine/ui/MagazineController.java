package me.scene.dinner.board.magazine.ui;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.board.magazine.application.command.MagazineService;
import me.scene.dinner.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.dinner.board.magazine.application.command.request.MagazineUpdateRequest;
import me.scene.dinner.board.magazine.application.query.dto.MagazineLink;
import me.scene.dinner.board.magazine.application.query.dto.MagazineView;
import me.scene.dinner.board.magazine.application.query.MagazineQueryService;
import me.scene.dinner.board.magazine.ui.form.MagazineForm;
import me.scene.dinner.board.magazine.ui.form.MagazineUpdateForm;
import me.scene.dinner.common.security.Current;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MagazineController {

    private final MagazineService service;
    private final MagazineQueryService query;


    @GetMapping("/magazines")
    public String showList(Model model) {
        List<MagazineLink> magazines = query.findAll();

        model.addAttribute("magazines", magazines);
        return "page/board/magazine/list";
    }

    @GetMapping("/magazines/{id}")
    public String showMagazine(@PathVariable Long id, Model model) {
        MagazineView magazine = query.find(id);

        model.addAttribute("magazine", magazine);
        return "page/board/magazine/view";
    }


    @GetMapping("/magazine-form")
    public String shipMagazineForm(Model model) {
        MagazineForm magazineForm = new MagazineForm();

        model.addAttribute("magazineForm", magazineForm);
        return "page/board/magazine/form";
    }

    @PostMapping("/magazines")
    public String createMagazine(@Current Account current,
                                 @Valid MagazineForm form, Errors errors) {

        if (errors.hasErrors()) return "page/board/magazine/form";

        String username = current.getUsername();
        String title = form.getTitle();
        String shortExplanation = form.getShortExplanation();
        String longExplanation = form.getLongExplanation();
        String policy = form.getPolicy();

        MagazineCreateRequest request = new MagazineCreateRequest(username, title, shortExplanation, longExplanation, policy);
        Long id = service.save(request);

        return "redirect:" + ("/magazines/" + id);
    }


    @GetMapping("/magazines/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {
        MagazineView magazine = query.find(id);

        MagazineUpdateForm updateForm = new MagazineUpdateForm(
                magazine.getId(),
                magazine.getPolicy(),
                magazine.getTitle(),
                magazine.getShortExplanation(),
                magazine.getLongExplanation()
        );

        model.addAttribute("updateForm", updateForm);
        return "page/board/magazine/update";
    }

    @PutMapping("/magazines/{id}")
    public String update(@PathVariable Long id,
                         @Current Account current,
                         @Valid MagazineUpdateForm form, Errors errors) {

        if (errors.hasErrors()) return "page/board/magazine/update";

        String username = current.getUsername();
        String title = form.getTitle();
        String shortExplanation = form.getShortExplanation();
        String longExplanation = form.getLongExplanation();

        MagazineUpdateRequest request = new MagazineUpdateRequest(username, title, shortExplanation, longExplanation);
        service.update(id, request);

        return "redirect:" + ("/magazines/" + id);
    }


    @DeleteMapping("/magazines/{id}")
    public String delete(@PathVariable Long id,
                         @Current Account current) {

        String username = current.getUsername();
        service.delete(id, username);
        return "redirect:" + ("/");
    }

}
