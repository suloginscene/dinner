package me.scene.dinner.common.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter @EqualsAndHashCode(of = "id")
public abstract class BaseEntity {

    @Id @GeneratedValue
    protected Long id;

}
