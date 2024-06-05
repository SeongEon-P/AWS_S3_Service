package org.zerock.apps3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SampleDTO {
    private MultipartFile[] files; //여기에 files를 포스트맨에서 key로 불러옴
}
