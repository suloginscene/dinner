package me.scene.dinner.domain.board.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ArticleFormValidator implements Validator {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleFormValidator(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(ArticleForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ArticleForm articleForm = (ArticleForm) target;
        String url = articleForm.getParentUrl() + articleForm.getUrl();

        if (articleRepository.existsByUrl(url)) {
            errors.rejectValue("url", "duplicated.url", "이미 사용 중인 주소입니다.");
        }

    }

}
