package com.andyhsu.springbootmall.dto;

public class OrderQueryParam {

    private Integer userId;
    private Integer limit;

    private Integer offset;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer usedId) {
        this.userId = usedId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
