package kr.co.hconnect.vo;

import kr.co.hconnect.domain.BaseResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class InterviewDetailVO extends BaseResponse {

    /**
     * 문진상세순번
     */
    private int interviewDetailSeq;
    /**
     * 문진순번
     */
    private int interviewSeq;
    /**
     * 문항번호
     */
    @NotBlank(message = "{validation.null.answerSeq}")
    private String answerSeq;
    /**
     * 답변
     */
    private String answerValue;
    /**
     * 등록자id
     */
    private String regId;

    private String interviewType;
}
