package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartDTO {
    private long userid;
    private long productid;
    private String product_description;
    private int quantity;

    // 기본 생성자
    public CartDTO() {}

    // userId, productId, quantity만 사용하는 생성자 추가
    public CartDTO(long userid, long productid, int quantity) {
        this.userid = userid;
        this.productid = productid;
        this.quantity = quantity;
    }

    // productId와 userId만 사용하는 생성자 추가
    public CartDTO(long userid, long productid) {
        this.userid = userid;
        this.productid = productid;
    }
}