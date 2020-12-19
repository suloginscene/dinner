//package me.scene.dinner.common.temp;
//
//import lombok.RequiredArgsConstructor;
//import me.scene.dinner.account.application.command.AccountService;
//import me.scene.dinner.account.domain.account.Account;
//import me.scene.dinner.account.domain.account.AccountRepository;
//import me.scene.dinner.account.domain.tempaccount.TempAccount;
//import me.scene.dinner.account.domain.tempaccount.TempAccountRepository;
//import me.scene.dinner.board.article.command.application.ArticleService;
//import me.scene.dinner.board.article.command.application.ReplyService;
//import me.scene.dinner.board.article.domain.Article;
//import me.scene.dinner.board.magazine.application.command.MagazineService;
//import me.scene.dinner.board.magazine.application.command.MemberService;
//import me.scene.dinner.board.magazine.domain.common.Magazine;
//import me.scene.dinner.board.magazine.domain.common.MagazineRepository;
//import me.scene.dinner.board.topic.application.command.TopicService;
//import me.scene.dinner.board.topic.domain.Topic;
//import me.scene.dinner.tag.application.TagService;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Set;
//
//@Component @RequiredArgsConstructor
//public class Initiator implements ApplicationRunner {
//
//    // TODO when settled, remove dev
//    private static final List<String> targets = List.of("local", "dev");
//
//    private final Environment environment;
//    private final InitService initService;
//
//    @Override
//    public void run(ApplicationArguments args) {
//
//        String activeProfile = environment.getActiveProfiles()[0];
//        if (!targets.contains(activeProfile)) return;
//
//        initService.init();
//
//    }
//
//
//    @Component @RequiredArgsConstructor
//    private static class InitService {
//
//        private final TempAccountRepository tempAccountRepository;
//        private final AccountService accountService;
//        private final AccountRepository accountRepository;
//        private final MagazineService magazineService;
//        private final MagazineRepository magazineRepository;
//        private final MemberService memberService;
//        private final TopicService topicService;
//        private final ArticleService articleService;
//        private final ReplyService replyService;
//        private final TagService tagService;
//
//        private void init() {
//
//            Account scene = user("scene", "suloginscene@gmail.com", "password_s");
//            Account eon = user("eon", "ahndoeon@naver.com", "password_d");
//            Account test = user("test", "scene_cho@naver.com", "password_t");
//
//
//            Magazine open_java = magazine(scene.getUsername(), "자바 프로그래밍", "자바, 스프링, JPA", "범용적인 글을 올려주세요. 개인 개발 기록은 별도의 매거진으로 만들어주세요.", "OPEN");
//
//            Topic java = topic(open_java.getId(), scene.getUsername(), "자바", "자바 언어에 관한 글을 올려주세요.", "프레임워크에 관한 글은 별도의 토픽에 올려주세요.");
//
//            Article syntax = article(java.getId(), scene.getUsername(), "기초 문법", "자바 기초 문법...", true, "프로그래밍", "자바");
//            reply(syntax.getId(), eon.getUsername(), "수고하세요.");
//
//            Article eight = article(java.getId(), test.getUsername(), "자바 8", "자바 8...", true);
//            reply(eight.getId(), scene.getUsername(), "감사합니다.");
//
//            Topic jpa = topic(open_java.getId(), test.getUsername(), "JPA", "JPA 이해와 사용을 돕기 위한 토픽입니다.", "개별 데이터베이스에 관해서는 다루지 않습니다.");
//
//            article(jpa.getId(), test.getUsername(), "ORM 탄생배경", "패러다임 불일치 문제...", true);
//
//            Article nPlusOne = article(jpa.getId(), eon.getUsername(), "N+1 문제", "나도 몰랐지만 나는 프로그래밍을 잘했구나.", true);
//            reply(nPlusOne.getId(), scene.getUsername(), "엥, 왜?");
//
//
//            Magazine dinner = magazine(scene.getUsername(), "Dinner 개발기", "디너의 탄생설화입니다.", "이 디너는 영국으로부터 시작되어...", "MANAGED");
//
//            Topic dev = topic(dinner.getId(), scene.getUsername(), "기술 파트", "디너 개발 기술에 관한 글입니다.", "스프링을 기반으로 개발하였습니다.");
//
//            Article docker = article(dev.getId(), scene.getUsername(), "개발 서버 구축", "도커를 활용한 개발 서버 구축", true);
//            reply(docker.getId(), eon.getUsername(), "나도 이 매거진에 글을 쓸 수 있음.");
//
//            memberService.addMember(dinner.getId(), scene.getUsername(), eon.getUsername());
//
//
//            Magazine ptc = magazine(eon.getUsername(), "Palm Tree Canteen", "직장인 글쓰기 모임", "오직 나에게 선택받은 자만이 글을 쓸 수 있다. 매니지드 매거진에 테스트 할 거리가 많기 때문이지...", "MANAGED");
//
//            Topic bad = topic(ptc.getId(), eon.getUsername(), "넌 귀여운 노인일 뿐이야.", "내가 했던 가장 나쁜 말은 무엇인가요?", "테스트 데이터 만들면서 느꼈는데, 상세 설명이 필요하지 않은 매거진이나 토픽도 많긴 하겠다.");
//
//            article(bad.getId(), eon.getUsername(), "너무 나쁜 것 같아서 검열 중인 게시물", "많도다...", false, "태그");
//
//            memberService.applyMember(ptc.getId(), scene.getUsername());
//
//
//            Magazine exclusive = magazine(test.getUsername(), "폐쇄적인 외부인", "내 것이오.", "너네가 돈 내는 서버에 너네가 만든 사이트를 내 전용으로 쓰니까 묘하게 기분이 나쁘겠지?", "EXCLUSIVE");
//
//            Topic who = topic(exclusive.getId(), test.getUsername(), "저는 누구냐면요", "쉿", "이 계정의 비밀을 찾아서");
//
//            Article origin = article(who.getId(), test.getUsername(), "사실은 컨셉놀이를 하려고 했었다.", "그런데 더러운 것밖에 생각이 안나서 차마 못 올리겠더라.", true);
//            reply(origin.getId(), eon.getUsername(), "한 번 해보지...(exclusive 여도 댓글은 써지네.)");
//
//        }
//
//        private Account user(String username, String email, String password) {
//            Long tempId = accountService.saveTemp(username, email, password);
//            TempAccount temp = tempAccountRepository.findById(tempId).orElseThrow();
//            accountService.transferToRegular(email, temp.getVerificationToken());
//            return accountRepository.findByUsername(username).orElseThrow();
//        }
//
//        private Magazine magazine(String manager, String title, String shortExplanation, String longExplanation, String magazinePolicy) {
//            Long id = magazineService.save(manager, title, shortExplanation, longExplanation, magazinePolicy);
//            return magazineRepository.findById(id).orElseThrow();
//        }
//
//        private Topic topic(Long magazineId, String manager, String title, String shortExplanation, String longExplanation) {
//            Long id = topicService.save(magazineId, manager, title, shortExplanation, longExplanation);
//            return topicService.find(id);
//        }
//
//        private Article article(Long topicId, String writer, String title, String content, boolean publicized, String... tagNames) {
//            Set<String> tags = Set.of(tagNames);
//            tags.forEach(tagService::save);
//            Long id = articleService.save(topicId, writer, title, content, publicized, tags);
//            return articleService.find(id);
//        }
//
//        private void reply(Long articleId, String writer, String content) {
//            replyService.save(writer, articleId, content);
//        }
//
//    }
//
//}
