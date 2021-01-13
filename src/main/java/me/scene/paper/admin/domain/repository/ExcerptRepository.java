package me.scene.paper.admin.domain.repository;

import me.scene.paper.admin.domain.model.Excerpt;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {

}
