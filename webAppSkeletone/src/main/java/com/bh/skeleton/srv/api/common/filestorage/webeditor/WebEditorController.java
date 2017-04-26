/**
 * Any software product designated as "MyWorks Proprietary Software,"
 * including computer software and may include associated media, printed
 * materials, and
 * "online" or electronic documentation ("SOFTWARE PRODUCT") is a copyrighted
 * and
 * proprietary property of MyWorks CO., LTD (“MyWorks”).
 ** The SOFTWARE PRODUCT must
 * (i) be used for MyWorks’s approved business purposes only,
 * (ii) not be contaminated by open source codes,
 * (iii) must not be used in any ways that will require it to be disclosed or
 * licensed freely to third parties or public,
 * (vi) must not be subject to reverse engineering, decompling or diassembling.
 ** MyWorks does not grant the recipient any intellectual property rights,
 * indemnities or warranties and
 * takes on no obligations regarding the SOFTWARE PRODUCT
 * except as otherwise agreed to under a separate written agreement with the
 * recipient,
 ** Revision History
 * Author Date Description
 * ------------------- ---------------- --------------------------
 * BH Jun 2016. 10. 25. First Draft
 */
package com.hs.gms.srv.api.common.filestorage.webeditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hs.gms.srv.api.common.attach.AttachDownloadService;
import com.hs.gms.srv.api.common.attach.valid.AttachParametersValidator;
import com.hs.gms.srv.api.common.attach.vo.AttachVO;
import com.hs.gms.std.common.controller.GMSCommonController;
import com.hs.gms.std.common.error.GMSException;
import com.hs.gms.std.common.response.ResponseVO;

/**
 * WebEditorController.java
 * 
 * @author BH Jun
 */
@RestController
public class WebEditorController extends GMSCommonController {

    @Autowired
    private AttachDownloadService downloadService;
    @Autowired
    private WebEditorService webEditorService;
    @Autowired
    private AttachParametersValidator paramsValidator;

    @RequestMapping(value = "${gms.filestorage.proxy.context}/webeditor", method = RequestMethod.GET)
    public void getWebEditor(HttpServletRequest request, @ModelAttribute @Valid AttachVO attachVO, BindingResult bindingResult) {
        paramsValidator.checkAttachRequestValidation(attachVO, bindingResult, false);

        try {
            webEditorService.getWebEditor(request);
        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    @RequestMapping(value = "${gms.filestorage.proxy.context}/webeditor/temp", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public ResponseVO<Map<String, List<String>>> createTempWebEditorFile(HttpServletRequest request) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        try {
            result.put("webEditorTmpFileKeys", webEditorService.createTempWebEditorFiles(request));
        } catch(Exception e) {
            throw new GMSException(e);
        }

        return super.makeResponseData(HttpStatus.OK, result);
    }

    @RequestMapping(value = "${gms.filestorage.proxy.context}/webeditor/gethtml", method = RequestMethod.GET)
    public void getWebEditorView(@ModelAttribute @Valid AttachVO attachVO, BindingResult bindingResult) {
        paramsValidator.checkAttachRequestValidation(attachVO, bindingResult, false);

        try {
            webEditorService.getWebEditorRegisteredView(attachVO);
        } catch(Exception e) {
            throw new GMSException(e);
        }
    }

    @RequestMapping(value = "${gms.filestorage.proxy.context}/webeditor/download/{attachId}", method = RequestMethod.GET)
    public void downloadAttachFile(@PathVariable(value = "attachId") String attachId) {
        try {
            downloadService.getAttachFileDownload(attachId);

        } catch(Exception e) {
            throw new GMSException(e);
        }
    }
}
