package com.security.wrr.model;

import java.util.List;

public class MyUploadList {
    private List<FileInfo> myUploadFileList;
    private Long totalCount;
    private Integer totalPage;

    public List<FileInfo> getMyUploadFileList() {
        return myUploadFileList;
    }

    public void setMyUploadFileList(List<FileInfo> myUploadFileList) {
        this.myUploadFileList = myUploadFileList;
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
