package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.domain.Reply;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.ReplyDTO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ReplyServiceTests {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister(){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .bno(99L)
                .build();

        log.info(replyService.register(replyDTO));
    }

    @Test
    public void testRead(){
        ReplyDTO replyDTO = replyService.read(9L);
        log.info(replyDTO);
    }

    @Test
    public void testModify(){

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(1L)
                .replyText("댓글 내용 변경 테스트")
                .build();

        replyService.modify(replyDTO);
    }

    @Test
    public void testDelete(){
        replyService.remove(1L);
    }

    @Test
    public void testGetListOfBoard(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();


        replyService.getListofBoard(99L, pageRequestDTO);

    }
}