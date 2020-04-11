package com.tensquare.article.pingan;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-04-11 10:33:49
 * @describe:
 */
public class PaymentItem implements Serializable {
    private static final long serialVersionUID = -1885151249931864618L;

    private String payType;
    private BigDecimal paymentAmount;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
}
