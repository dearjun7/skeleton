package com.hs.gms.srv.api.common.attach;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.srv.api.common.attach.type.AttachType;
import com.hs.gms.srv.api.common.attach.valid.AttachParametersValidator;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.srv.api.common.filestorage.vo.FileStorageUploadResultVO;
import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.CommonResultDataVO;
import com.hs.gms.std.common.response.ResponseVO;

/**
 * AttachController
 * 
 * @author BH Jun
 */
@RestController
public class AttachController extends GMSCommonController {

    @Autowired
    private AttachUploadService uploadService;
    @Autowired
    protected AttachDownloadService downloadService;
    @Autowired
    private AttachParametersValidator paramsValidator;

    @RequestMapping(value = "/attach/upload", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/json")
    public ResponseVO<CommonResultDataVO> uploadAttachFile(HttpServletRequest request, @ModelAttribute @Valid AttachVO attachVO,
            BindingResult bindingResult) throws GMSException {

        paramsValidator.checkAttachRequestValidation(attachVO, bindingResult);

        try {
            AttachType.valueOf(attachVO.getAttachType().toUpperCase()).createAttachData(uploadService, request, attachVO);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode());
    }

    @RequestMapping(value = "/attach/upload/temp", method = RequestMethod.POST, consumes = "multipart/form-data", produces = "application/json")
    public ResponseVO<FileStorageUploadResultVO> uploadTempFile(HttpServletRequest request) throws GMSException {
        List<FileStorageUploadResultVO> result = null;

        try {
            result = uploadService.createAttachTempFile(request);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result);
    }

    @RequestMapping(value = "/attach/upload/temp/register", method = RequestMethod.POST, produces = "application/json")
    public ResponseVO<CommonResultDataVO> registerUploadedTempFile(@ModelAttribute @Valid AttachVO attachVO, BindingResult bindingResult)
            throws GMSException {

        paramsValidator.checkAttachRequestValidation(attachVO, bindingResult);

        try {
            uploadService.createAttachFileFromUploadedTempFile(attachVO);
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, super.getProcessSuccessCode());
    }

    @RequestMapping(value = "/attach/download/{attachId}", method = RequestMethod.GET)
    public void downloadAttachFile(@PathVariable(value = "attachId") String attachId) {
        try {
            downloadService.getAttachFileDownload(attachId);

        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    @RequestMapping(value = "/attach/download/temp/{tmpFileKey}", method = RequestMethod.GET)
    public void downloadTempFile(@PathVariable(value = "tmpFileKey") String tmpFileKey) {
        try {
            downloadService.getAttachTempFileDownload(tmpFileKey);

        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    @RequestMapping(value = "/attach/download", method = RequestMethod.GET)
    public void downloadCompressAttachFile(@RequestParam(value = "attachId") List<String> attachIdList) {
        try {
            downloadService.getCompressedAttachFileDownload(attachIdList);

        } catch(Exception e) {
            throw new GMSException(e);
        }
    }
}
