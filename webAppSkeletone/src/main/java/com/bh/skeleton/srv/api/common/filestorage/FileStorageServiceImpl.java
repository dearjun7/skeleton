package com.hs.gms.srv.api.common.filestorage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hs.gms.srv.api.common.filestorage.error.FileStorageErrorCode;
import com.hs.gms.srv.api.common.filestorage.vo.CompressSourceFileVO;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.reverseproxy.MethodParamConverter;
import com.hs.gms.std.common.service.reverseproxy.RequestProxySender;
import com.hs.gms.std.common.util.JSONConverter;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * FileStorageService
 * 
 * @author BH Jun
 */
@Component
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageService.class);

    private static final String COMP_FILE_NAME = "download.zip";

    @Value("#{config['gms.common.charset']}")
    private String charset;
    @Value("#{config['gms.filestorage.proxy.url']}")
    private String uploaderURL;
    @Value("#{config['gms.filestorage.proxy.context']}")
    protected String fileStorageContext;
    @Value("#{config['gms.filestorage.proxy.url']}")
    private String fileStorageURL;
    @Value("#{config['gms.filestorage.temp.dir']}")
    private String tmpDir;
    @Value("#{config['gms.filestorage.download.context']}")
    private String downloadURI;

    @Autowired
    private RequestProxySender requestProxySender;

    @Override
    public List<FileStorageUploadResultVO> uploadToTemp(HttpServletRequest request) throws Exception {
        String uploaderURL = this.getUploaderURL() + "/servlet/FileUploadServlet?acton=upload";
        String uploadResult = requestProxySender.executeUpload(request, uploaderURL);

        LOGGER.debug("uploadFile Result(Temp Upload) : " + uploadResult);

        return this.convertTempUploadResultToList(uploadResult, TempFileType.ATTACHFILE);
    }

    @Override
    public List<FileStorageUploadResultVO> uploadToStorage(HttpServletRequest request, String saveFileDir, String saveFileName)
            throws Exception {
        List<FileStorageUploadResultVO> tmpUploadList = this.uploadToTemp(request);

        return this.moveUploadedTempFileToStorage(tmpUploadList, saveFileDir, saveFileName);
    }

    @Override
    public List<FileStorageUploadResultVO> moveUploadedTempFileToStorage(List<FileStorageUploadResultVO> uploadedTempFileList,
            String saveFileDir) throws Exception {
        return this.moveUploadedTempFileToStorage(uploadedTempFileList, saveFileDir, null);
    }

    @Override
    public boolean removeUploadedFile(String delFilePath) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String uploaderURL = this.getUploaderURL() + "/servlet/FileAction?delFileName=";

        return this.checkFileActionResult(requestProxySender.executeToGetRes(request, HttpMethod.DELETE, uploaderURL + delFilePath, false));
    }

    @Override
    public String copyUploadedFile(String sourceFilePath, String destFilePath, boolean isMakeDestFileName) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String uploaderURL = this.getUploaderURL() + "/servlet/FileCopyAction";
        ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), charset);
        JSONObject paramJson = new JSONObject();
        HttpEntity httpEntity = null;
        String proxyResponse = null;

        paramJson.put("sourceFilePath", sourceFilePath);
        paramJson.put("destFilePath", destFilePath);
        paramJson.put("isMakeDestFileName", isMakeDestFileName);

        httpEntity = new StringEntity(paramJson.toString(), contentType);
        proxyResponse = requestProxySender.executeToGetRes(request, HttpMethod.PUT, httpEntity, uploaderURL, false);
        LOGGER.debug("Copy Uploaded File Result : " + proxyResponse);

        return this.checkAndGetFileActionResult(proxyResponse);
    }

    @Override
    public void responseDownloadFile(String filePath, String fileName) throws Exception {
        this.responseDownloadFile(filePath, fileName, null);
    }

    @Override
    public void responseDownloadFile(String filePath, String fileName, String fileAction) throws Exception {
        this.responseDownloadFile(filePath, fileName, fileAction, null, false);
    }

    @Override
    public void responseDownloadFile(String filePath, String fileName, String fileAction, String fileSize, boolean isInline)
            throws Exception {
        String downloadURL = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletRequest downloadReq = new FileStorageDownloadRequestWrapper(request, filePath, fileName, fileSize,
                fileStorageContext + "/servlet/FileAction");

        LOGGER.debug("download File info : filePath - " + filePath + ", fileName - " + fileName + ", fileSize - " + fileSize);

        if(fileAction != null) {
            downloadURL = this.getFileStorageProxyURL(downloadReq) + "&action=" + fileAction;
        } else {
            downloadURL = this.getFileStorageProxyURL(downloadReq);
        }

        requestProxySender.executeToSendRes(downloadReq, null, downloadURL, false, isInline);
    }

    @Override
    public void responseCompressDownloadFile(List<CompressSourceFileVO> compSourceFileList) throws Exception {
        String downloadURL = this.getUploaderURL() + "/servlet/FileAction";
        String action = "?action=makeCompressFile";
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        JSONObject jsonParam = JSONConverter.listToJsonObject(compSourceFileList, "sourceFileList");
        ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), charset);
        HttpEntity httpEntity = new StringEntity(jsonParam.toString(), contentType);
        String compResponse = requestProxySender.executeToGetRes(request, HttpMethod.POST, httpEntity, downloadURL + action, false);
        JSONObject jsonCompRes = JSONObject.fromObject(compResponse);
        String downloadFilePath = jsonCompRes.getJSONObject("response").getString("attachFilePath");

        LOGGER.debug("compResponse : " + compResponse);

        this.responseDownloadFile(downloadFilePath, COMP_FILE_NAME, "compFileDownload");
    }

    @Override
    public File getFile(String filePath, String fileName) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletRequest downloadReq = new FileStorageDownloadRequestWrapper(request, filePath, fileName, null, null);
        String queryParams = (String) MethodParamConverter.GET.getMethodParameters(downloadReq, charset, null);
        String downloadURL = fileStorageURL + "/" + fileStorageContext + "/servlet/FileAction" + queryParams.replaceFirst("&", "?");

        LOGGER.debug("download File info : filePath - " + filePath + ", fileName - " + fileName);

        return requestProxySender.executeToGetFile(downloadReq, HttpMethod.GET, downloadURL);
    }

    @Override
    public boolean isExistFile(String filePath, String fileName) throws Exception {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletRequest downloadReq = new FileStorageDownloadRequestWrapper(request, filePath, fileName, null, null);
        String queryParams = (String) MethodParamConverter.GET.getMethodParameters(downloadReq, charset, null);
        String downloadURL = fileStorageURL + "/" + fileStorageContext + "/servlet/FileAction?action=isFileExist" + queryParams;

        String result = requestProxySender.executeToGetRes(downloadReq, HttpMethod.GET, downloadURL, false);
        JSONObject resultJson = JSONObject.fromObject(result).getJSONObject("response");

        return Boolean.parseBoolean(resultJson.getString("fileExist"));
    }

    @Override
    public boolean sendResponseStringContent(String content, String mimeType, HttpServletResponse response) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ContentType contentType = ContentType.create(mimeType, Charset.forName(charset));

        response.setContentType(contentType.toString());

        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(content.getBytes()));
            outputStream = new BufferedOutputStream(response.getOutputStream());

            while((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            if(outputStream != null) {
                outputStream.close();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }

        return true;
    }

    @Override
    public FileStorageUploadResultVO uploadToStorageWithFileTypeObject(File file, String saveFileDir, String saveFileName)
            throws Exception {
        FileStorageUploadResultVO tempUploadFile = this.uploadToTempWithFileTypeObject(file);

        return this.moveUploadedTempFileToStorage(tempUploadFile, saveFileDir, saveFileName);
    }

    @Override
    public FileStorageUploadResultVO uploadToTempWithFileTypeObject(File file) throws Exception {
        String uploaderURL = this.getUploaderURL() + "/servlet/FileUploadServlet?acton=upload";
        String uploadResult = requestProxySender.executeUpload(file, uploaderURL);

        return this.convertTempUploadResultToList(uploadResult, TempFileType.ATTACHFILE).get(0);
    }

    private String makeMoveUploadedFileParam(String sysFileName, String saveNewFileDir, String saveNewFileName) {
        JSONObject json = new JSONObject();

        json.put("sysFileName", sysFileName);
        json.put("saveNewFileDir", saveNewFileDir);

        if(saveNewFileName != null) {
            json.put("saveNewFileName", saveNewFileName);
        }

        return json.toString();
    }

    private List<FileStorageUploadResultVO> moveUploadedTempFileToStorage(List<FileStorageUploadResultVO> uploadedTempFileVO,
            String saveFileDir, String saveFileName) throws Exception {
        List<FileStorageUploadResultVO> resultList = new ArrayList<FileStorageUploadResultVO>();

        for(FileStorageUploadResultVO tmpVO : uploadedTempFileVO) {
            FileStorageUploadResultVO result = this.moveUploadedTempFileToStorage(tmpVO, saveFileDir, saveFileName);

            resultList.add(result);
        }

        return resultList;
    }

    private FileStorageUploadResultVO moveUploadedTempFileToStorage(FileStorageUploadResultVO uploadedTempFileVO, String saveFileDir,
            String saveFileName) throws Exception {
        FileStorageUploadResultVO result = new FileStorageUploadResultVO();
        String uploaderURL = this.getUploaderURL();
        String moveFileInfo = this.makeMoveUploadedFileParam(uploadedTempFileVO.getSysFileName(), saveFileDir, saveFileName);
        ContentType contentType = ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), charset);
        HttpEntity httpEntity = new StringEntity(moveFileInfo, contentType);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String proxyResponse = requestProxySender.executeToGetRes(request, HttpMethod.PUT, httpEntity, uploaderURL + "/servlet/FileAction",
                false);
        String onlyFileName = null;

        if(saveFileName != null) {
            onlyFileName = saveFileName;
        } else {
            String[] sysFileArr = uploadedTempFileVO.getSysFileName().split("/");
            onlyFileName = sysFileArr[sysFileArr.length - 1];
        }

        LOGGER.debug("createAttachUploadedTempFile Result : " + proxyResponse);

        this.checkFileActionResult(proxyResponse);

        result.setFilePath(saveFileDir + "/" + onlyFileName);
        result.setOrgFileName(uploadedTempFileVO.getOrgFileName());
        result.setAttachFileSize(uploadedTempFileVO.getAttachFileSize());

        return result;
    }

    public boolean checkFileActionResult(String fileActionResult) throws Exception {
        this.checkFileActionResult(fileActionResult, null);

        return true;
    }

    private String checkAndGetFileActionResult(String fileActionResult) throws Exception {
        return this.checkFileActionResult(fileActionResult, "attachFilePath");
    }

    private String checkFileActionResult(String fileActionResult, String findKey) throws Exception {
        String result = null;

        if(fileActionResult == null || "".equals(fileActionResult)) {
            throw new GMSException(FileStorageErrorCode.FILESTORAGE_UPLOAD_FAIL);
        }

        JSONObject jsonResponse = JSONObject.fromObject(fileActionResult).getJSONObject("response");

        if(!jsonResponse.getBoolean("success")) {
            if(Pattern.matches("4[0-9]{2}", jsonResponse.getString("error"))) {
                throw new GMSException(FileStorageErrorCode.FILESTORAGE_UPLOAD_FAIL);
            } else {
                throw new GMSException(FileStorageErrorCode.FILESTORAGE_SYSTEM_ERROR);
            }
        }

        if(findKey != null && !"".equals(findKey)) {
            result = jsonResponse.getString(findKey);
        }

        return result;
    }

    @Override
    public List<FileStorageUploadResultVO> convertTempUploadResultToList(String uploadResult, TempFileType tempFileType) throws Exception {
        List<FileStorageUploadResultVO> result = null;

        try {
            JSONObject json = JSONObject.fromObject(uploadResult);
            String fileType = tempFileType.getTempFileTypeName();

            result = JSONConverter.jsonObjectToList(json.getJSONObject("response"), "files", FileStorageUploadResultVO.class);

            List<String> imgSrcAttachIdList = null;

            if("W".equals(fileType)) {
                imgSrcAttachIdList = JSONConverter.jsonObjectToList(json.getJSONObject("response"), "imgSrcAttachIdList", String.class);
            }

            for(FileStorageUploadResultVO tmpFileVO : result) {
                String tmpFileKey = this.makeTempFileKey();

                tmpFileVO.setTmpFileKey(tmpFileKey);

                if("A".equals(fileType)) {
                    tmpFileVO.setFilePath(tmpDir + tmpFileVO.getSysFileName());
                    tmpFileVO.setAttachURI(downloadURI + tmpDir + "/" + tmpFileKey);
                } else if("W".equals(fileType)) {
                    if("".equals(tmpFileVO.getFileID())) {
                        tmpFileVO.setImgSrcAttachIdList(imgSrcAttachIdList);
                    }
                }

                tmpFileVO.setFileType(fileType);
            }
        } catch(JSONException jex) {
            throw new GMSException(FileStorageErrorCode.FILESTORAGE_UPLOAD_FAIL);
        }

        return result;
    }

    private String makeTempFileKey() {
        Random random = new Random();

        return String.valueOf(System.currentTimeMillis()) + String.valueOf(random.nextInt(1000));
    }

    private String getFileStorageProxyURL(HttpServletRequest request) throws Exception {
        String queryParams = (String) MethodParamConverter.GET.getMethodParameters(request, charset, null);

        return uploaderURL + request.getRequestURI().replace(request.getContextPath(), "") + queryParams.replaceFirst("&", "?");
    }

    @Override
    public RequestProxySender getRequestProxySender() {
        return this.requestProxySender;
    }

    @Override
    public String getUploaderURL() {
        return this.uploaderURL + this.fileStorageContext;
    }
}
