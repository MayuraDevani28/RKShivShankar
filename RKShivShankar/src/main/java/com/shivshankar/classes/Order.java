package com.shivshankar.classes;

import java.util.ArrayList;

public class Order {
    String OrderId, OrderNo, OrderDate, Total, Status, CustName;
    ArrayList<SC3Object> listImages;

    public Order(String orderId, String orderNo, String orderDate, String total, String status, ArrayList<SC3Object> listImages, String cname) {
        super();
        OrderNo = orderNo;
        OrderId = orderId;
        OrderDate = orderDate;
        Total = total;
        Status = status;
        this.listImages = listImages;
        CustName = cname;
    }


    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public ArrayList<SC3Object> getListImages() {
        return listImages;
    }

    public void setListImages(ArrayList<SC3Object> listImages) {
        this.listImages = listImages;
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

}
