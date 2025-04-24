package com.bblets.baibuy.models;

import lombok.Data;

@Data
public class MessageDto {
    private String content;
    private Integer productId;
    private Integer receiverId;
}
