package com.tensquare.article.pingan;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pingan
 * @date: 2020-04-11 10:41:56
 * @describe:
 */
public class CoinsShare implements Serializable {
    private static final long serialVersionUID = 8880585235006160478L;
    private String payType;
    private BigDecimal coinsShareAmount;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getCoinsShareAmount() {
        return coinsShareAmount;
    }

    public void setCoinsShareAmount(BigDecimal coinsShareAmount) {
        this.coinsShareAmount = coinsShareAmount;
    }
}
