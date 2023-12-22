package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("title.." + i)
                    .content("content..." + i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());

        });
    }

    @Test
    public void testSelect() {
        long bno = 100L;
        //       Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
        Board board = boardRepository.findById(bno).orElseThrow();
        log.info(board);
    }

    @Test
    public void testUpdate() {
        Long bno = 100L;

        Board board = boardRepository.findById(bno).orElseThrow();
        log.info("첫 번째" + board);
        board.change("update title.....", "update content");
        log.info("두 번째" + board);
        boardRepository.save(board);
        log.info("세 번째" + board);
    }

    @Test
    public void testDelete() {
        Long bno = 100L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testGetList() {
        // boardRepository.findAll().forEach(List-> log.info(List));

        List<Board> list = boardRepository.findAll();
        for (Board b : list)
            log.info(b);
    }

    @Test
    public void testPaging() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info(result.getTotalElements()); //전체 행 갯수 조회
        log.info(result.getTotalPages()); //전체 페이지 조회
        log.info(result.getNumber()); //페이지 조회

        result.getContent().forEach(list -> log.info(list)); //페이지 조회
    }

    @Test
    public void testWriter() {
        boardRepository.findBoardByWriter("user0").forEach(list -> log.info(list));
    }

    @Test
    public void testWriterAndTitle() {
        boardRepository.findByWriterAndTitle("user1", "title..1")
                .forEach(list -> log.info(list));
    }

    @Test
    public void testTitleLike() {
        boardRepository.findByTitleLike("%1%")
                .forEach(list -> log.info(list));
    }

    @Test
    public void testWriter2() {
        boardRepository.findByWriter2("user1").forEach(list -> log.info(list));
    }

    @Test
    public void testTitle2() {
        boardRepository.findByTitle2("1").forEach(list -> log.info(list));
    }


    @Test
    public void testKeyword() {
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findByKeyword("1", pageable);
        log.info(result.getTotalElements());
        log.info(result.getTotalPages());
        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1() {
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }


    @Test
    public void testSearchAll() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info("-------------------------");
        log.info(result.getTotalElements());
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasNext());
        log.info("-------------------------");
    }

    @Test
    public void testSearchReplyCount() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno")
                .descending());

        Page<BoardListReplyCountDTO> result =
                boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info("Next : " + result.hasNext());
        log.info("Prev : " + result.hasPrevious());

        result.getContent().forEach(list -> log.info(list));

    }

    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                .title("Image test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }//endfor
        boardRepository.save(board);
    }

    @Test
    public void testReadWithImage() {
        Board board = boardRepository.findByIdWithImages(1L).orElseThrow();

        log.info(board);
        log.info("----------------");
        log.info(board.getImageSet());
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages() {
        Board board = boardRepository.findByIdWithImages(1L).orElseThrow();

        //기존의 첨부파일들은 삭제
        board.clearImages();

        //새로운 첨부파일들
        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }
        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {
        Long bno = 1L;
        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll() {
        IntStream.rangeClosed(1, 100).forEach(i -> {

            Board board = Board.builder()
                    .title("Title.." + i)
                    .content("Content.." + i)
                    .writer("writer.." + i)
                    .build();
            for (int j = 0; j < 3; j++) {
                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), i + "file" + j + ".jpg");
            }
            boardRepository.save(board);

        });
    }

    @Test
    @Transactional
    public void testSearchImageReplyCount(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("=================================");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));

    }
}