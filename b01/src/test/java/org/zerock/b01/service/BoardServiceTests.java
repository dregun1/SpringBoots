package org.zerock.b01.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("퀘스트 제목")
                .content("테스트 제목")
                .writer("테스트 저자")
                .build();

        Long bno = boardService.register(boardDTO);
        log.info("bno ==>" + bno);
    }

    @Test
    public void testReadOne(){
        BoardDTO boardDTO = boardService.readOne(102L);
        log.info("boardDTO =>" + boardDTO);
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("수정 제목")
                .content("수정 내용")
                .build();

        //첨부파일 추가
        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));
        boardService.modify(boardDTO);
    }

    @Test
    public void testDelete(){
        boardService.remove(100L);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.list(pageRequestDTO);
        log.info(pageResponseDTO);

    }

    @Test
    public  void testRegisterWithImages(){
        BoardDTO boardDTO = BoardDTO.builder()
                .title("File.....Sample Title")
                .content("Sample Content...")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_bbb.jpg"
                        ));
        Long bno = boardService.register(boardDTO);
        log.info("bno: " + bno);

    }

    @Test
    public void testReadAll(){
        BoardDTO boardDTO = boardService.readOne(101L);

        log.info(boardDTO);
        boardDTO.getFileNames().forEach(fileName-> log.info(fileName));
    }

}