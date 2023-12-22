package org.zerock.b01.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UpDownController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @Operation(summary = "Upload POST", description = "POST방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(@Parameter(description = "Files to be uploaded",
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) UploadFileDTO uploadFileDTO) {

        log.info(uploadFileDTO);

        if (uploadFileDTO.getFiles() != null) {

            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);
                String uuid = UUID.randomUUID().toString();
                //c:\\upload\xxxxxxxxxxxxxxx_event-1.jpeg+
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                boolean image = false;

                log.info("path " + savePath);

                try {
                    multipartFile.transferTo(savePath);

                    //이미지 파일이면....
                    if (Files.probeContentType(savePath).startsWith("image")) { //.jpg로끝나는 이미지인 경우 섬네일
                        image = true;

                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(
                        UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originalName)
                                .img(image).build());
            }); //end for
            return list;
        } //end if
        return null;
    } //end upload


    @Operation(summary = "view 파일", description = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));  //타입정보라네? 그러니까 header는 꼭 들어가지 않아도 되는 조건 같음.
        } catch (Exception e){
            return  ResponseEntity.internalServerError().build();
        }
        return  ResponseEntity.ok().headers(headers).body(resource);
    }   //end viewFileGET

    
    @Operation(summary = "remove 파일", description = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable("fileName") String fileName){
        Resource resource = new FileSystemResource(uploadPath + File.separator+fileName); //전체 파일경로
        log.info("궁금해요 resource>>>" + resource);

        String resourceName = resource.getFilename(); //파일이름 그러니까  File.separator+fileName와 동일
        log.info("궁금해요 resourceName >>" + resourceName);
        Map<String, Boolean> resultMap = new HashMap<>();

        boolean removed =false;

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")){   //jpg일 경우 내가 작성한 {fileName}에 s_를 붙혀 지움,, 그전에 s_를 붙혀 하나 더만들었기 때문에.
                                            //C:\\upload \ s_event05.jpeg
                File thumbnailFile = new File(uploadPath + File.separator + "s_"+fileName);
                log.info("궁금해요 thumbnailFile>>>>" + thumbnailFile );
                thumbnailFile.delete();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resultMap.put("result", removed);

        return resultMap;
    }
}




