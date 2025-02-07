package webbShopDataBase;

public class OrderInfo {

    private Integer orderId;
    private String status;

    public OrderInfo(Integer orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}

