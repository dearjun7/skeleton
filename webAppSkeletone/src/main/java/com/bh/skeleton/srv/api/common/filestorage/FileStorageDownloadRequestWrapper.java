package com.hs.gms.srv.api.common.filestorage;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * FileStorageDownloadRequestWrapper
 * 
 * @author BH Jun
 */
public class FileStorageDownloadRequestWrapper extends HttpServletRequestWrapper {

    private String filePath;
    private String fileSize;
    private String fileName;
    private final String filePathName = "filePath";
    private final String fileSizeName = "fileSize";
    private final String fileNameName = "fileName";
    private String requestURI;

    public FileStorageDownloadRequestWrapper(HttpServletRequest request, String filePath, String fileName, String fileSize,
            String requestURI) {
        super(request);
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.requestURI = requestURI;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Enumeration<String> parameterNames = super.getParameterNames();

        if(this.filePath != null) {
            List<String> paramList = Collections.list(parameterNames);
            paramList.add(filePathName);
            parameterNames = Collections.enumeration(paramList);
        }

        if(this.fileSize != null) {
            List<String> paramList = Collections.list(parameterNames);
            paramList.add(fileSizeName);
            parameterNames = Collections.enumeration(paramList);
        }

        if(this.filePath != null) {
            List<String> paramList = Collections.list(parameterNames);
            paramList.add(fileNameName);
            parameterNames = Collections.enumeration(paramList);
        }

        return parameterNames;
    }

    @Override
    public String getParameter(String name) {
        String value;

        if(this.filePath != null && "filePath".equals(name)) {
            value = this.filePath;
        } else if(this.fileSize != null && "fileSize".equals(name)) {
            value = this.fileSize;
        } else if(this.fileName != null && "fileName".equals(name)) {
            value = this.fileName;
        } else {
            value = super.getParameter(name);
        }

        return value;
    }

    public String getRequestURI() {
        String uri;

        if(this.requestURI != null) {
            uri = this.requestURI;
        } else {
            uri = super.getRequestURI();
        }

        return uri;
    }
}
