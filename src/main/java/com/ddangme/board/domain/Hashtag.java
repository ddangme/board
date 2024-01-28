package com.ddangme.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true) // toString( ) 메서드를 생성할 때 부모 클래스의 toString( ) 메서드도 포함시키도록 지정한다.
// @Table: 데이터베이스 테이블에 대한 설정을 지정한다.
// @Index: 특정 컬럼의 값에 기반하여 테이블의 행들을 빠르게 찾을 수 있도록 하는 데이터베이스 성능 최적화 기술 중 하나.
@Table(indexes = {
        @Index(columnList = "hashtagName", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Hashtag extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 게시글과의 연관 관계
    @ToString.Exclude // 해당 필드를 toString( ) 메서드에서 제외시킨다.
    @ManyToMany(mappedBy = "hashtags")
    private Set<Article> articles = new LinkedHashSet<>();

    @Setter
    @Column(nullable = false)
    private String hashtagName; // 해시태그 명

    private Hashtag(String hashtagName) {
        this.hashtagName = hashtagName;
    }

    public static Hashtag of(String hashtagName) {
        return new Hashtag(hashtagName);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
