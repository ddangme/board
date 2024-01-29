package com.ddangme.board.dto.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/*
   Record: 키워드를 사용한 데이터 클래스, 불변성을 가지며 주로 데이터 전송 객체(DTO)로 사용된다.
   - 불변성: 한 번 생성된 객체는 수정할 수 없다. 필드는 final로 선언되고, 수정이 불가능한 메소드들이 자동으로 생성되기 때문이다. 이로써 데이터의 안정성이 보장되고 예측 가능한 동작을 얻을 수 있다.
   - 자동 생성 메소드: equals( ), hashCode( ), toString( )과 같은 메소드들을 자동으로 생성한다. 주로 데이터를 비교하거나 출력할 때 사용되며, 반복적인 코드 작성을 방지한다.
   - 컴파일러 자동 생성 코드: 컴파일러에 의해 자동으로 생성되는 코드가 많다. 이로 인해 코드가 간결하고, 개발자는 불필요한 코드 작성을 최소화할 수 있다.
   - 디폴트 생성자 및 새로운 생성자: 기본 생성자와 모든 필드를 초기화하는 생성자를 자동으로 생성한다. 또한, 'with' 메소드도 자동으로 생성되어 기존 Record 객체의 일부 필드를 변경한 새로운 객체를 생성할 수 있다.
   - 명시적인 타입 선언: Record 클래스에서는 필드의 타입을 명시적으로 선언해야 한다. 이는 자동으로 생성되는 메소드들에서 타입을 추론할 수 있도록 돕는다.

   Record 클래스를 사용하면 간결하고 가독성이 좋은 코드를 작성할 수 있으며, 자동으로 생성되는 메소드들을 통해 개발자가 일일이 구현할 필요가 없다.
 */
public record KakaoOAuth2Response(
        Long id,
        LocalDateTime connectedAt,
        Map<String, Object> properties, // 사용자의 추가 속성 정보를 담는다.
        KakaoAccount kakaoAccount   // 사용자의 카카오 계정 정보를 나타내는 객체
) {
    public record KakaoAccount(
            Boolean profileNicknameNeedsAgreement,
            Profile profile,
            Boolean hashEmail,
            Boolean emailNeedsAgreement,
            Boolean isEmailValid,
            boolean isEmailVerified,
            String email
    ) {
        public record Profile(String nickname) {
            public static Profile from(Map<String, Object> attributes) {
                return new Profile(String.valueOf(attributes.get("nickname")));
            }
        }

        public static KakaoAccount from(Map<String, Object> attributes) {
            return new KakaoAccount(
                    Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                    Profile.from((Map<String, Object>) attributes.get("profile")),
                    Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                    Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                    String.valueOf(attributes.get("email"))

            );
        }

        public String nickname() {
            return this.profile.nickname();
        }
    }

    public static KakaoOAuth2Response from(Map<String, Object> attributes) {
        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                (Map<String, Object>) attributes.get("properties"),
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }

    public String email() {
        return this.kakaoAccount().email();
    }

    public String nickname() {
        return this.kakaoAccount.nickname();
    }
}
