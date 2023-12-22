package org.zerock.b01.service;

import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;
import org.zerock.b01.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);
    
    //특정 게시글(bno) 전체 댓글 리스트 출력
    PageResponseDTO<ReplyDTO> getListofBoard(Long bno, PageRequestDTO pageRequestDTO);
}
