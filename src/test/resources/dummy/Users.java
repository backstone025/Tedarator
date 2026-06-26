package com.backstone.tedarator.test.dummy;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA만 접근 가능
@Table(name = "USER")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "INSTAGRAM_ID", length = 50)
    private String instagramId; // 인스타 아이디

    @Column(name = "USER_PW", length = 4)
    private String userPw;  // 사용자 PW

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "GENDER", length = 10)
    private Gender gender;  // 성별

    @Enumerated(EnumType.STRING)    // 데이터 안정성을 위해, DB에 저장할 경우 문자열로 저장할 것을 명시
    @Column(name = "STATUS", length = 10)
    private Status status;  // 처리 상태

    @Column(name = "CREATED_AT", updatable = false) // 수정 못하게 설정
    private LocalDateTime createdAt;    // 생성 시간

    @Column(name = "PRIVACY_CONSENT")
    private boolean privacyConsent;

    public Users(String instagramId, String userPw) {
        this.instagramId = instagramId;
        this.userPw = userPw;
        this.status = Status.IN_PROGRESS;
    }

    public void updateOnboarding(Gender gender) {
        this.gender = gender;
    }

    public void markSubmitted() {
        this.status = Status.SUBMITTED;
    }

    public void updatePrivacyConsent(Boolean privacyConsent) {
        this.privacyConsent = privacyConsent;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}

