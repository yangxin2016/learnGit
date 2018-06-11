package com.security.wrr.model;

import java.util.List;

public class MyDownloadList {
    private List<MyDownload> myDownloadFileList;
    private Long totalCount;
    private Integer totalPage;

    public List<MyDownload> getMyDownloadFileList() {
        return myDownloadFileList;
    }

    public void setMyDownloadFileList(List<MyDownload> myDownloadFileList) {
        this.myDownloadFileList = myDownloadFileList;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
