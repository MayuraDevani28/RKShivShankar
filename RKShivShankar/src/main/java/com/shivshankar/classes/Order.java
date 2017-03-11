package com.shivshankar.classes;

public class Order {
    String OrderId, OrderNo, OrderDate, Total, CustomerName, Status;
    boolean ShowShipNowBtn;

    public Order(String orderId, String orderNo, String orderDate, String total, String customerName, String status, boolean showShipNowBtn) {
        super();
        OrderNo = orderNo;
        OrderId = orderId;
        OrderDate = orderDate;
        Total = total;
        CustomerName = customerName;
        Status = status;
        ShowShipNowBtn = showShipNowBtn;
    }


    public boolean isShowShipNowBtn() {
        return ShowShipNowBtn;
    }

    public void setShowShipNowBtn(boolean showShipNowBtn) {
        ShowShipNowBtn = showShipNowBtn;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }
}
