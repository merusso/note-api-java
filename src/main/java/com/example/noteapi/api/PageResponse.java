package com.example.noteapi.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Collection;

@Getter
public class PageResponse<T> {

    public PageResponse(Collection<T> items, int pageSize, String pageToken, String nextPageToken) {
        this.items = items;
        this.pageSize = pageSize;
        this.pageToken = pageToken;
        this.nextPageToken = nextPageToken;
    }

    private Collection<T> items;
    private long pageSize;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String pageToken;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nextPageToken;
}
