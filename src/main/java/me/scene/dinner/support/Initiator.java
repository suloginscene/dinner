package me.scene.dinner.support;

import lombok.RequiredArgsConstructor;
import me.scene.dinner.account.application.command.AccountService;
import me.scene.dinner.account.application.command.request.SignupRequest;
import me.scene.dinner.account.domain.account.model.Account;
import me.scene.dinner.account.domain.account.repository.AccountRepository;
import me.scene.dinner.account.domain.tempaccount.model.TempAccount;
import me.scene.dinner.account.domain.tempaccount.repository.TempAccountRepository;
import me.scene.dinner.board.article.application.command.ArticleService;
import me.scene.dinner.board.article.application.command.TagService;
import me.scene.dinner.board.article.application.command.request.ArticleCreateRequest;
import me.scene.dinner.board.article.application.command.request.ReplyCreateRequest;
import me.scene.dinner.board.article.domain.article.model.Article;
import me.scene.dinner.board.article.domain.article.repository.ArticleRepository;
import me.scene.dinner.board.magazine.application.command.MagazineService;
import me.scene.dinner.board.magazine.application.command.MemberService;
import me.scene.dinner.board.magazine.application.command.request.MagazineCreateRequest;
import me.scene.dinner.board.magazine.domain.magazine.model.Magazine;
import me.scene.dinner.board.magazine.domain.magazine.repository.MagazineRepository;
import me.scene.dinner.board.magazine.domain.managed.model.ManagedMagazine;
import me.scene.dinner.board.topic.application.command.TopicService;
import me.scene.dinner.board.topic.application.command.request.TopicCreateRequest;
import me.scene.dinner.board.topic.domain.model.Topic;
import me.scene.dinner.board.topic.domain.repository.TopicRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component @RequiredArgsConstructor
public class Initiator implements ApplicationRunner {

    // TODO when settled, remove dev
    private static final List<String> targets = List.of("local", "dev");

    private final Environment environment;
    private final InitService initService;

    @Override
    public void run(ApplicationArguments args) {

        String activeProfile = environment.getActiveProfiles()[0];
        if (!targets.contains(activeProfile)) return;

        initService.init();
    }


    @Component @RequiredArgsConstructor
    private static class InitService {

        private final AccountService accountService;
        private final TempAccountRepository tempAccountRepository;
        private final AccountRepository accountRepository;

        private final MagazineService magazineService;
        private final MemberService memberService;
        private final MagazineRepository magazineRepository;

        private final TopicService topicService;
        private final TopicRepository topicRepository;

        private final ArticleService articleService;
        private final TagService tagService;
        private final ArticleRepository articleRepository;


        private void init() {

            Account scene = user("scene", "suloginscene@gmail.com", "password_s");
            Account doeon = user("doeon", "ahndoeon@naver.com", "password_d");


            Magazine ptc = magazine(doeon, "팜 트리 캔틴", "모든 좋은 글들에 열려있습니다.", "누구나 다양한 주제와 형식으로 글을 쓸 수 있습니다.", "OPEN");

            Topic myPeople = topic(ptc, doeon, "내가 좋아하는 사람", "모든 유형의 글이 허용됩니다.", "어떤 종류의 좋아함에 관한 글이라도 좋습니다.");
            article(myPeople, doeon, "내가 좋아하는 사람", "내가 좋아하는 사람에 관한 소설", true, "소설", "사람");
            article(myPeople, scene, "김정현", "똑똑한 사람", true, "논픽션");

            Topic lost = topic(ptc, doeon, "분실", "다음 글을 읽고 자유롭게 글을 쓰시오.", "또 물건이 그 가치를 조금도 잃지 않고 분실되는 운명을 갖는 경우도 있는데, 그것은 다른 두려운 손실을 막기 위해 어떤 물건을 운명의 희생으로 바치려는 의도가 있을 때이다. 그러므로 분실이라는 것은 흔히 우리가 희망하던 희생이다");
            Article center = article(lost, scene, "분실물 센터", "분실에 관한 시", true, "시");
            reply(center, doeon, "재밌네요.");
            like(doeon, center);
            Article inGarden = article(lost, doeon, "정원에서", "글을 읽고 쓴 소설", true, "소설", "공간");
            reply(inGarden, scene, "좋은 글이네요.");
            like(scene, inGarden);
            reply(inGarden, doeon, "감사합니다.");

            Topic unsoundMasterPiece = topic(ptc, scene, "유해 명작", "모든 유형의 글이 허용됩니다.", "실재하는 작품이 다루어져야 합니다. 단 작품 외적인 사실관계는 허구라도 상관없습니다.");
            article(unsoundMasterPiece, doeon, "작성 중", "작성 중입니다.", false, "평론");
            article(unsoundMasterPiece, scene, "작성 중", "작성 중입니다.", false, "소설");


            ManagedMagazine paper = (ManagedMagazine) magazine(scene, "페이퍼", "페이퍼를 소개합니다.", "페이퍼의 지향점과 사용방법, 그리고 개발과정을 알아보세요.", "MANAGED");
            member(paper, scene, doeon);

            Topic introduction = topic(paper, doeon, "페이퍼란?", "페이퍼를 소개합니다.", "페이퍼의 지향점과 사용방법을 알아보세요.");
            article(introduction, doeon, "페이퍼의 목표", "좋은 글을 추구합니다.", true, "paper");
            article(introduction, doeon, "매거진 사용방법", "매거진은 세 가지 성격으로 분류됩니다.", true, "paper");
            article(introduction, doeon, "토픽 사용방법", "토픽은 세부 주제 또는 제시어로 활용될 수 있습니다.", true, "paper");
            Article article = article(introduction, doeon, "아티클 사용방법", "아티클은 비공개할 수 있고, 태그를 붙일 수 있습니다.", true, "paper");
            reply(article, scene, "구독은 어떻게 할 수 있나요?");
            like(scene, article);

            Topic dev = topic(paper, scene, "개발과정", "페이퍼의 개발과정을 소개합니다.", "자바, 스프링, JPA 등의 기술이 사용되었습니다.");
            article(dev, scene, "개발 서버 구축", "개발 서버는 리눅스와 도커로 구축되어있습니다.", true, "paper", "linux");
            article(dev, scene, "회원 정보 관리", "가입에는 이메일 인증이 필수이며, 비밀번호는 암호화되어 저장됩니다.", true, "paper", "spring");
            article(dev, scene, "도메인 모델", "조회용 양방향 연관관계보다 api/ajax를 활용하였습니다.", true, "paper", "jpa");
            article(dev, scene, "패키지 디자인", "이벤트를 활용하여 의존관계의 방향을 조정하였습니다.", true, "paper", "oop");
        }


        private Account user(String username, String email, String password) {
            SignupRequest request = new SignupRequest(username, email, password);
            accountService.signup(request);

            TempAccount temp = tempAccountRepository.findAccountByEmail(email);
            String token = temp.getVerificationToken();
            accountService.verify(email, token);

            return accountRepository.find(username);
        }

        private Magazine magazine(Account manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
            MagazineCreateRequest request = new MagazineCreateRequest(manager.getUsername(), title, shortExplanation, longExplanation, magazinePolicy);
            Long id = magazineService.save(request);
            return magazineRepository.find(id);
        }

        private Topic topic(Magazine magazine, Account manager, String title, String shortExplanation, String longExplanation) {
            TopicCreateRequest request = new TopicCreateRequest(manager.getUsername(), magazine.getId(), title, shortExplanation, longExplanation);
            Long id = topicService.save(request);
            return topicRepository.find(id);
        }

        private Article article(Topic topic, Account writer, String title, String content, boolean publicized, String... tagNames) {
            Set<String> tags = Set.of(tagNames);
            tags.forEach(tagService::save);

            ArticleCreateRequest request = new ArticleCreateRequest(writer.getUsername(), topic.getId(), title, content, publicized, tags);
            Long id = articleService.save(request);
            return articleRepository.find(id);
        }

        private void reply(Article article, Account writer, String content) {
            ReplyCreateRequest request = new ReplyCreateRequest(writer.getUsername(), article.getId(), content);
            articleService.save(request);
        }

        private void like(Account reader, Article article) {
            articleService.like(reader.getUsername(), article.getId());
        }

        private void member(ManagedMagazine magazine, Account owner, Account member) {
            memberService.addMember(magazine.getId(), owner.getUsername(), member.getUsername());
        }

    }

}