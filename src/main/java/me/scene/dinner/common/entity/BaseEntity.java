package me.scene.dinner.common.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;


@MappedSuperclass
@Getter @EqualsAndHashCode(of = "id")
public abstract class BaseEntity {

    @Column(nullable = false)
    @Id @GeneratedValue(strategy = SEQUENCE, generator = "paper_seq_generator")
    @SequenceGenerator(name = "paper_seq_generator", sequenceName = "paper_sequence")
    protected Long id;

}
