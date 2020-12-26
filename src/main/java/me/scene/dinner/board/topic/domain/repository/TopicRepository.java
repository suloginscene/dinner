package me.scene.dinner.board.topic.domain.repository;

import me.scene.dinner.board.topic.domain.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TopicRepository extends JpaRepository<Topic, Long> {

    @Query("select t" +
            " from Topic t" +
            " join fetch t.magazine" +
            " where t.id = :id")
    Topic findFetch(@Param("id") Long id);

    @Query("select t" +
            " from Topic t" +
            " where t.magazine.id = :magazineId" +
            " order by t.point desc")
    List<Topic> findByMagazineId(@Param("magazineId") Long magazineId);

    default Topic find(Long id) {
        return findById(id).orElseThrow();
    }

}
