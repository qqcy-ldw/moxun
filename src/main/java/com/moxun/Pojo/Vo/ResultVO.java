package com.moxun.Pojo.Vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultVO {

    private Integer code;
    private String message;
    private Object data;

}
