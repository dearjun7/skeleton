package com.hs.gms.srv.api.common.filestorage;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.service.cache.UserCacheAccessor;
import com.hs.gms.std.common.service.reverseproxy.RequestProxySender;

import net.sf.json.JSONObject;

/**
 * FileStorageReverseProxy
 * 
 * @author BH Jun
 */
@Controller
public class FileStorageReverseProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageReverseProxy.class);

    @Value("#{config['gms.filestorage.proxy.url']}")
    private String uploaderURL;
    @Value("#{config['gms.filestorage.proxy.context']}")
    private String fileStorageURI;
    @Value("#{config['gms.common.charset']}")
    private String encodingCharSet;

    @Autowired
    private RequestProxySender requestProxySender;
    @Autowired
    private UserCacheAccessor userCacheAccessor;

    @RequestMapping(value = "${gms.filestorage.proxy.context}/**", method = RequestMethod.GET)
    public void proxingGetMethod(HttpServletRequest request) throws GMSException {
        LOGGER.debug(this.getClass().getName() + " - " + request.getMethod());

        try {
            String proxyURL = "";
            String requestURI = request.getRequestURI();

            if("/servlet/FileUploadServlet".equals(requestURI.replace(fileStorageURI, ""))) {
                if("delete".equals(request.getParameter("acton"))) {
                    String tmpFileKey = request.getParameter("tmpFileKey");
                    String itemId = request.getParameter("itemId");

                    FileStorageUploadResultVO tmpFileInfo = this.getAttachTempData(tmpFileKey);
                    String sysFileName = tmpFileInfo.getSysFileName();

                    proxyURL = uploaderURL + request.getRequestURI().replace(request.getContextPath(), "") + "?acton=delete&itemId="
                            + itemId + "&delFileName=" + URLEncoder.encode(sysFileName, encodingCharSet);

                    this.removeAttachTempData(tmpFileKey);

                } else {
                    proxyURL = this.getFileStorageProxyURL(request);
                }
            } else {
                proxyURL = this.getFileStorageProxyURL(request);
            }

            requestProxySender.executeToSendRes(request, HttpMethod.GET, proxyURL, false);
        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    @RequestMapping(value = "${gms.filestorage.proxy.context}/**", method = RequestMethod.POST)
    public void proxingPostMethod(HttpServletRequest request) throws GMSException {
        try {
            LOGGER.debug(this.getClass().getName() + " - " + request.getMethod());
            requestProxySender.executeToSendRes(request, HttpMethod.POST, this.getFileStorageProxyURL(request), false);

        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    @RequestMapping(value = "${gms.filestorage.proxy.context}/**", method = RequestMethod.POST, consumes = "multipart/form-data")
    public void proxingMultiPartRequest(HttpServletRequest request) throws GMSException {
        try {
            LOGGER.debug(this.getClass().getName() + " - " + request.getMethod() + "(multipart/form-data)");
            requestProxySender.executeToSendRes(request, null, this.getFileStorageProxyURL(request), true);
        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    private String getFileStorageProxyURL(HttpServletRequest request) throws Exception {
        String queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        return uploaderURL + request.getRequestURI() + queryString;
    }

    private void removeAttachTempData(String tmpFileKey) throws Exception {
        UserDataVO userData = UserDataContextHolder.getUserData();

        userCacheAccessor.removeUserTmpFileInfo(userData.getTenantId(), userData.getUserId(), tmpFileKey);
    }

    private FileStorageUploadResultVO getAttachTempData(String tmpFileKey) throws Exception {
        FileStorageUploadResultVO result = null;
        UserDataVO userData = UserDataContextHolder.getUserData();
        JSONObject tmpFileInfo = userCacheAccessor.getUserTmpFileInfo(userData.getTenantId(), userData.getUserId(), tmpFileKey);

        if(tmpFileInfo != null) {
            result = (FileStorageUploadResultVO) JSONObject.toBean(tmpFileInfo, FileStorageUploadResultVO.class);
        }

        return result;
    }
}
