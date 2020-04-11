package com.tensquare.article.pingan;

import java.io.Serializable;
import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.pojo
 * @date: 2020-04-11 10:29:02
 * @describe:
 */
public class Settlenment implements Serializable {
    private static final long serialVersionUID = -7964520856371705099L;
    private String paySign;
    private List<PaymentItem> paymentItemList;

    public List<PaymentItem> getPaymentItemList() {
        return paymentItemList;
    }

    public void setPaymentItemList(List<PaymentItem> paymentItemList) {
        this.paymentItemList = paymentItemList;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
