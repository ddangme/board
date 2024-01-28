package com.ddangme.board.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @ToString.Exclude
    /*
    @JoinTable: 다대다 관계에서 연결 테이블을 정의할 때 사용한다. 연결 테이블은 실제 데이터베이스에 두 엔터티 간의 관계를 지정하는 테이블이다.
    name: 연결 테이블의 이름
    joinColumn: 현재 클래스와 연결 테이블에서 매핑되는 컬럼을 지정한다.
    inverseJoinColumns: 연결된 엔터티와 연결 테이블에서 매핑되는 컬럼을 지정한다.
     */
    @JoinTable(
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    /*
    cascade: 연관된 엔터티에 대한 변경이 발생할 때 해당 변경을 관리하기 위한 설정이다.
    여기서는 Hashtag과 Article의 다대다 관계에서 Hashtag을 추가 또는 병합할 때 연관된 Article도 함께 처리하도록 지정한다.
    CascadeType.ALL: 모든 변경 작업에 대해 연쇄적으로 영향을 미칩니다. (INSERT, UPDATE, DELETE)
    CascadeType.PERSIST: 영속성 컨텍스트에 엔터티를 추가할 때 연쇄적으로 영향을 미칩니다. (INSERT)
    CascadeType.MERGE: 엔터티의 변경사항을 병합할 때 연쇄적으로 영향을 미칩니다. (UPDATE)
    CascadeType.REMOVE: 엔터티를 삭제할 때 연쇄적으로 영향을 미칩니다. (DELETE)
    CascadeType.REFRESH: 엔터티를 리프레시할 때 연쇄적으로 영향을 미칩니다. (SELECT)
    CascadeType.DETACH: 엔터티를 영속성 컨텍스트에서 분리할 때 연쇄적으로 영향을 미칩니다.
    */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Hashtag> hashtags = new LinkedHashSet<>();


    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    public void clearHashtags() {
        this.getHashtags().clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
