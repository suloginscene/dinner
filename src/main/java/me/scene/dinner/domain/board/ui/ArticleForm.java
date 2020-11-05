//package me.scene.dinner.domain.board.ui;
//
//import lombok.Getter;
//import lombok.Setter;
//import org.hibernate.validator.constraints.Length;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
//
//@Getter @Setter
//public class ArticleForm {
//
//    @NotBlank(message = "상위 주소가 존재해야 합니다.")
//    private String parentUrl;
//
//    @Pattern(regexp = "^[a-z0-9\\-]{1,20}$", message = "주소는 20자 이내의 소문자, 숫자, -로 이루어져야 합니다.")
//    private String url;
//
//    @Length(min = 1, max = 30, message = "제목은 30자 이내여야 합니다.")
//    private String title;
//
//    @NotBlank(message = "내용이 존재해야 합니다.")
//    private String content;
//
//    @NotBlank(message = "소속 토픽이 존재해야 합니다.")
//    private String topic;
//
//}
