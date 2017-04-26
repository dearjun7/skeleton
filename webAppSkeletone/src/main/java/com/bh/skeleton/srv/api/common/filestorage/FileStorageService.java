package com.hs.gms.srv.api.common.filestorage;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.gms.srv.api.common.filestorage.vo.CompressSourceFileVO;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.std.common.service.reverseproxy.RequestProxySender;

/**
 * FileStorageService
 * 
 * @author BH Jun
 */
public interface FileStorageService {

    /**
     * Request body안의 Multipart File 바이너리를 Temp 경로에 업로드 한다.
     * 
     * @param request
     *            HttpServletRequest - File 바이너리를 포함한 multipart Request
     * @return List - temp에 업로드된 파일 정보
     * @throws Exception
     */
    public List<FileStorageUploadResultVO> uploadToTemp(HttpServletRequest request) throws Exception;

    /**
     * Request body안의 Multipart File 바이너리를
     * 파라미터 fileDir경로와 fileName 정보로 Storage에 업로드한다.
     * 
     * @param request
     *            HttpServletRequest - File 바이너리를 포함한 multipart Request
     * @param saveFileDir
     *            String - Storage에 저장할 파일 경로
     * @param saveFileName
     *            String - Storage에 저장할 파일 이름
     * @return List - Storage에 업로드된 파일 정보
     * @throws Exception
     */
    public List<FileStorageUploadResultVO> uploadToStorage(HttpServletRequest request, String saveFileDir, String saveFileName)
            throws Exception;

    /**
     * Temp 경로에 업로드된 파일을 Storage로 이동한다.
     * 
     * @param uploadedTempFileList
     *            List<FileStorageUploadResultVO> - temp 파일 정보 List
     * @param saveNewFileDir
     *            String - Storage에 저장할 파일 경로
     * @return List - Storage에 업로드된 파일 정보
     * @throws Exception
     */
    public List<FileStorageUploadResultVO> moveUploadedTempFileToStorage(List<FileStorageUploadResultVO> uploadedTempFileList,
            String saveFileDir) throws Exception;

    /**
     * 업로드 된 파일을 삭제한다.
     * 
     * @param delFilePath
     *            String - 삭제할 파일 경로
     * @return boolean
     * @throws Exception
     */
    public boolean removeUploadedFile(String delFilePath) throws Exception;

    /**
     * 업로드 된 파일을 업로드 공간의 다른 경로로 복사한다.
     * 
     * @param soruceFilePath
     *            String - 파일 경로를 포함한 복사할 대상 파일
     * @param destFilePath
     *            String - 파일 경로를 포함한 복사될 파일 명
     * @param isMakeDestFileName
     *            boolean - destFilePath에 파일명이 포함되어 있는지의 여부<br>
     *            true - 파일 명 불 포함(copy 시 UUID로 파일 명 자동 생성)<br>
     *            false - 파일명 포함
     * @return String
     *         copyCompleteFilePath - 복사가 완료된 파일 명을 포함한 파일 경로
     * @throws EXception
     */
    public String copyUploadedFile(String soruceFilePath, String destFilePath, boolean isMakeDestFileName) throws Exception;

    /**
     * download 요청한 파일을 response로 내려준다.
     * 
     * @param filePath
     *            String
     * @param fileName
     *            String
     * @throws Exception
     */
    public void responseDownloadFile(String filePath, String fileName) throws Exception;

    /**
     * download 요청한 파일을 response로 내려준다.
     * 
     * @param filePath
     *            String
     * @param fileName
     *            String
     * @throws Exception
     */
    public void responseDownloadFile(String filePath, String fileName, String fileAction) throws Exception;

    /**
     * download 요청한 파일을 response로 내려준다.
     * 
     * @param response
     *            HttpServletResponse
     * @param filePath
     *            String
     * @param fileName
     *            String
     * @param fileSize
     *            String
     * @param isInline
     *            boolean
     * @throws Exception
     */
    public void responseDownloadFile(String filePath, String fileName, String fileAction, String fileSize, boolean isInline)
            throws Exception;

    /**
     * @param compSourceFileList
     * @throws Exception
     */
    public void responseCompressDownloadFile(List<CompressSourceFileVO> compSourceFileList) throws Exception;

    /**
     * download 요청한 파일을 File로 return한다.
     * 
     * @param filePath
     *            String
     * @param fileName
     *            String
     * @return File
     * @throws Exception
     */
    public File getFile(String filePath, String fileName) throws Exception;

    /**
     * 요청한 파일이 존재하는지에 대한 결과를 boolean 타입으로 리턴한다.
     * 
     * @param filePath
     *            String
     * @param fileName
     *            String
     * @return true - 파일 존재함<br>
     *         false - 파일 없음
     * @throws Exception
     */
    public boolean isExistFile(String filePath, String fileName) throws Exception;

    /**
     * String 타입 content를 client에게 전송한다.
     * 
     * @param content
     *            String
     * @param mimeType
     *            String
     * @param response
     *            HttpServletResponse
     * @return boolean
     * @throws Exception
     */
    public boolean sendResponseStringContent(String content, String mimeType, HttpServletResponse response) throws Exception;

    /**
     * FileStorage에서 API 서버로 다운로드 받은 File을 API 서버의 비지니스 로직에서 변환이 완료되면,
     * 본 메소드를 통하여 FileStorage 서버로 업로드 한다.
     * 
     * @param file
     *            File
     * @param saveFileDir
     *            String
     * @param saveFileName
     *            String
     * @return FileStorageUploadResultVO - Storage에 업로드된 파일 정보
     * @throws Exception
     */
    public FileStorageUploadResultVO uploadToStorageWithFileTypeObject(File file, String saveFileDir, String saveFileName) throws Exception;

    /**
     * API 서버에서 생성된 File 객체를 Temp 파일 경로에 Upload 한다.
     * 
     * @param file
     *            File
     * @return FileStorageUploadResultVO - temp에 업로드된 파일 정보
     * @throws Exception
     */
    public FileStorageUploadResultVO uploadToTempWithFileTypeObject(File file) throws Exception;

    public RequestProxySender getRequestProxySender();

    public boolean checkFileActionResult(String fileActionResult) throws Exception;

    public String getUploaderURL();

    public List<FileStorageUploadResultVO> convertTempUploadResultToList(String uploadResult, TempFileType tempFileType) throws Exception;
}
