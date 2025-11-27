package com.example.flightservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PaginationResponse {
    @JsonProperty("page")
    private Integer page;

    @JsonProperty("pageSize")
    private Integer pageSize;

    @JsonProperty("totalElements")
    private Long totalElements;

    @JsonProperty("items")
    private List<FlightResponse> items;

    // 默认构造函数
    public PaginationResponse() {}

    // 全参数构造函数
    public PaginationResponse(Integer page, Integer pageSize, Long totalElements, List<FlightResponse> items) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.items = items;
    }

    // Getters and Setters
    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    public Long getTotalElements() { return totalElements; }
    public void setTotalElements(Long totalElements) { this.totalElements = totalElements; }

    public List<FlightResponse> getItems() { return items; }
    public void setItems(List<FlightResponse> items) { this.items = items; }

}