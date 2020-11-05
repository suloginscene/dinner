package me.scene.dinner.board.topic.application;

import me.scene.dinner.account.application.AccountService;
import me.scene.dinner.board.magazine.application.MagazineService;
import me.scene.dinner.board.magazine.domain.Magazine;
import me.scene.dinner.board.topic.domain.Topic;
import me.scene.dinner.board.topic.domain.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional(readOnly = true)
public class TopicService {

    private final TopicRepository topicRepository;
    private final AccountService accountService;
    private final MagazineService magazineService;

    @Autowired
    public TopicService(TopicRepository topicRepository, AccountService accountService, MagazineService magazineService) {
        this.topicRepository = topicRepository;
        this.accountService = accountService;
        this.magazineService = magazineService;
    }

    @Transactional
    public Long save(Long magazineId, Long managerId, String title, String shortExplanation, String longExplanation) {
        Magazine magazine = magazineService.find(magazineId);
        Topic topic = Topic.create(magazine, managerId, title, shortExplanation, longExplanation);
        topic = topicRepository.save(topic);
        return topic.getId();
    }

    public Topic find(Long id) {
        return topicRepository.findById(id).orElseThrow(() -> new TopicNotFoundException(id));
    }

    public TopicDto extractDto(Long topicId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new TopicNotFoundException(topicId));

        Magazine magazine = topic.getMagazine();
        String manager = accountService.find(topic.getManagerId()).getUsername();
        return TopicDto.create(topicId, magazine.getId(), magazine.getTitle(), manager,
                topic.getTitle(), topic.getShortExplanation(), topic.getLongExplanation());
    }

}
