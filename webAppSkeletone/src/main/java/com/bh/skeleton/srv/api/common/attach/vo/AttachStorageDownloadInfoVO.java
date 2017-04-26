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
 * BH Jun 2016. 5. 17. First Draft
 */
package com.hs.gms.srv.api.common.attach.vo;

import java.io.Serializable;

/**
 * AttachStorageDownloadInfoVO.java
 * 
 * @author BH Jun
 */
public class AttachStorageDownloadInfoVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5439896701262470921L;

    private String downloadURL;
    private String attachType;
    private String attachName;
    private String attachContent;
    private String attachSize;
    private String attachId;

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getAttachType() {
        return attachType;
    }

    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getAttachContent() {
        return attachContent;
    }

    public void setAttachContent(String attachContent) {
        this.attachContent = attachContent;
    }

    public String getAttachSize() {
        return attachSize;
    }

    public void setAttachSize(String attachSize) {
        this.attachSize = attachSize;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
}
