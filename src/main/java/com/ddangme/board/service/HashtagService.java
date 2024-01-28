package com.ddangme.board.service;

import com.ddangme.board.domain.Hashtag;
import com.ddangme.board.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
@RequiredArgsConstructor
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashtagRepository.findByHashtagNameIn(hashtagNames));
    }

    public Set<String> parseHashtagNames(String content) {
        if (content == null) {
            return Set.of();
        }

        Pattern pattern = Pattern.compile("#[\\w가-힣]+");

        Matcher matcher = pattern.matcher(content.strip()); // 메서드로 전달된 입력 문자열이 'matcher'에 저장된다.
        Set<String> result = new HashSet<>();

        // matcher.find: 정규표현식에 일치하는 부분이 존재하는지 확인한다.
        // matcher.group(): 현재 일치하는 부분의 문자열을 얻는다.
        while (matcher.find()) {
            result.add(matcher.group().replace("#", ""));
        }

        return Set.copyOf(result);
    }

    public void deleteHashtagWithoutArticles(Long hashtagId) {
        /*
           findById(): 즉시 로딩을 수행하며, 데이터베이스에서 엔터티를 로딩한 후에 반환합니다.
           getReferenceById(): 지연 로딩을 수행하며, 엔터티에 대한 참조만을 반환하고 필요할 때 실제 로딩이 발생합니다.
         */
        Hashtag hashtag = hashtagRepository.getReferenceById(hashtagId);

        if (hashtag.getArticles().isEmpty()) {
            hashtagRepository.delete(hashtag);
        }
    }
}
