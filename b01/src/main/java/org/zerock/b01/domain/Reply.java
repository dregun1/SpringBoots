package org.zerock.b01.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="Reply", indexes = {
        @Index(name="idx_reply_board_bno", columnList = "board_bno")
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    //LAZY를 주면 board속성은 가져오지 않음(외래키속성만 가져오는듯?). EAGER을 주면 board속성도 가져옴.
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String replyText;

    private String replyer;

    public void changeText(String text){
        this.replyText = text;
    }
}
