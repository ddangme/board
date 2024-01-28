package com.ddangme.board.service;

import com.ddangme.board.domain.Hashtag;
import com.ddangme.board.repository.HashtagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 해시태그")
/*
   @ExtendWith(MockitoExtension.class): Mockito 프레임워크의 확장 기능을 사용하도록 JUnit에 지시한다.
   Mockito는 테스트 중에 가짜(mock) 객체를 생성하고 관리하는 데 사용하며, 이를 통해 테스트를 더 쉽게 작성하고 의존성을 관리할 수 있다.
 */
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {

    /*
       @InjectMocks: 주로 테스트 대상이 되는 클래스의 인스턴스를 생성하고,
       해당 클래스가 의존하는 객체들의 의존성을 자동으로 주입해준다.
       테스트 대상 클래스의 필드 중에서 @Mock 어노테이션이 붙은 Mock 객체를 찾아 의존성을 주입한다.
       테스트 대상 클래스의 생성자나 Setter 메서드를 통해 의존성 주입을 시도한다.
     */
    @InjectMocks
    private HashtagService sut;

    /*
       @Mock private HashtagRepository hashtagRepository: Mockito에서 제공하는 것으로,
       가짜(mock) 객체를 생성하여 'hashtagRepository' 인터페이스에 대한 의존성을 대체한다.
       이를 통해 실제 데이터베이스 호출을 피하고 테스트를 격리된 환경에서 실행할 수 있다.
       @Mock: 테스트에서 사용될 Mock 객체를 생성한다.
       테스트 대상이 되는 클래스가 의존하는 객체나 인터페이스를 Mock으로 대체하여 실제 동작이나 데이터베이스 호출을 피할 수 있다.
       주로 테스트에서 필요한 가짜 객체를 생성하여 의존성을 대체하고자 할 때 사용한다.
     */
    @Mock
    private HashtagRepository hashtagRepository;


    @DisplayName("본문을 파싱하면, 해시태그 이름들을 중복 없이 반환한다.")
    @MethodSource
    @ParameterizedTest(name = "[{index}] \"{0}\" => {1}")
    void givenContent_whenParsing_thenReturnsUniqueHashtagNames(String input, Set<String> expected) {
        // Given

        // When
        Set<String> actual = sut.parseHashtagNames(input);

        // Then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
        // .shouldHaveNoInteractions(): HashtagRepository에 대해 어떤 상호 작용도 일어나지 않았음을 검증하는 부분으로 해당 Mock 객체가 어떤 메서드 호출도 받지 않았음을 확인한다.
        then(hashtagRepository).shouldHaveNoInteractions();
    }

    static Stream<Arguments> givenContent_whenParsing_thenReturnsUniqueHashtagNames() {
        return Stream.of(
                arguments(null, Set.of()),
                arguments("", Set.of()),
                arguments("   ", Set.of()),
                arguments("#", Set.of()),
                arguments("  #", Set.of()),
                arguments("#   ", Set.of()),
                arguments("java", Set.of()),
                arguments("java#", Set.of()),
                arguments("ja#va", Set.of("va")),
                arguments("#java", Set.of("java")),
                arguments("#java_spring", Set.of("java_spring")),
                arguments("#java-spring", Set.of("java")),
                arguments("#_java_spring", Set.of("_java_spring")),
                arguments("#-java-spring", Set.of()),
                arguments("#_java_spring__", Set.of("_java_spring__")),
                arguments("#java#spring", Set.of("java", "spring")),
                arguments("#java #spring", Set.of("java", "spring")),
                arguments("#java  #spring", Set.of("java", "spring")),
                arguments("#java   #spring", Set.of("java", "spring")),
                arguments("#java     #spring", Set.of("java", "spring")),
                arguments("  #java     #spring ", Set.of("java", "spring")),
                arguments("   #java     #spring   ", Set.of("java", "spring")),
                arguments("#java#spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java #spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java#spring #부트", Set.of("java", "spring", "부트")),
                arguments("#java,#spring,#부트", Set.of("java", "spring", "부트")),
                arguments("#java.#spring;#부트", Set.of("java", "spring", "부트")),
                arguments("#java|#spring:#부트", Set.of("java", "spring", "부트")),
                arguments("#java #spring  #부트", Set.of("java", "spring", "부트")),
                arguments("   #java,? #spring  ...  #부트 ", Set.of("java", "spring", "부트")),
                arguments("#java#java#spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java#java#java#spring#부트", Set.of("java", "spring", "부트")),
                arguments("#java#spring#java#부트#java", Set.of("java", "spring", "부트")),
                arguments("#java#스프링 아주 긴 글~~~~~~~~~~~~~~~~~~~~~", Set.of("java", "스프링")),
                arguments("아주 긴 글~~~~~~~~~~~~~~~~~~~~~#java#스프링", Set.of("java", "스프링")),
                arguments("아주 긴 글~~~~~~#java#스프링~~~~~~~~~~~~~~~", Set.of("java", "스프링")),
                arguments("아주 긴 글~~~~~~#java~~~~~~~#스프링~~~~~~~~", Set.of("java", "스프링"))
        );
    }

    @DisplayName("해시태그 이름들을 입력하면, 저장된 해시태그 중 이름에 매칭되는 것들을 중복 없이 반환한다.")
    @Test
    void givenHashtagNames_whenFindingHashtags_thenReturnsHashtagSet() {
        // Given
        Set<String> hashtagNames = Set.of("java", "spring", "boots");
        given(hashtagRepository.findByHashtagNameIn(hashtagNames)).willReturn(List.of(
                Hashtag.of("java"),
                Hashtag.of("spring")
        ));

        // When
        Set<Hashtag> hashtags = sut.findHashtagsByNames(hashtagNames);

        // Then
        assertThat(hashtags).hasSize(2);
        then(hashtagRepository).should().findByHashtagNameIn(hashtagNames);
    }
}