package cn.techaction.mall.pojo;

import java.math.BigDecimal;
import java.util.List;



public class Cart {
    private List<CartItem> lists;
    private BigDecimal totalPrice;

    public List<CartItem> getLists() {
        return lists;
    }

    public void setLists(List<CartItem> lists) {
        this.lists = lists;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
