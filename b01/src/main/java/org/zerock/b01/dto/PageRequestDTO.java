package org.zerock.b01.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;

    private String keyword;

    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split(",");
    }
//여기부분 몰루?
    public Pageable getPageable(String... props) {
        if (props == null || props.length == 0) {
            return PageRequest.of(this.page - 1, this.size);
        } else {
            return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
        }
    }

    private String link;

    public String getLink() {
        if (link == null) {
            try {
                StringBuilder builder = new StringBuilder();
                builder.append("page=" + this.page);
                builder.append("&size=" + this.size);

                if (type != null && type.length() > 0) {
                    builder.append("&type=" + type);
                }
                if (keyword != null) {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "utf-8"));
                }
                link = builder.toString(); // page=1&size=10&type=tcw&keyword=1 <<<이런 식으로 만듬.
            } catch (UnsupportedEncodingException e) {
                // 예외 처리 로직을 추가하세요.
                e.printStackTrace();
            }
        }
        return link;
    }
}
