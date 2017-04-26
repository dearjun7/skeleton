package com.hs.gms.srv.api.common.filestorage;

/**
 * FileStorageType
 * 
 * @author BH Jun
 */
public enum FileStorageType {
    ATTACH {

        @Override
        public String getTypeDir(String tenantId, String lowDirId) {
            return this.makeSaveFileDir(attachDir, tenantId, lowDirId);
        }

        @Override
        public String getTypeFile(String procId, String procVersion) {
            throw new UnsupportedOperationException("이 메소드의 실행은 지원되지 않습니다.");
        }
    },
    MAP {

        @Override
        public String getTypeDir(String tenantId, String lowDirId) {
            return this.makeSaveFileDir(modelerDir, tenantId, lowDirId);
        }

        @Override
        public String getTypeFile(String procId, String procVersion) {
            return procId + "." + procVersion + ".xml";
        }
    };

    private static String attachDir;
    private static String modelerDir;

    public void setAttachDir(String attachDir) {
        FileStorageType.attachDir = attachDir;
    }

    public void setModelerDir(String modelerDir) {
        FileStorageType.modelerDir = modelerDir;
    }

    public abstract String getTypeDir(String tenantId, String lowDirId);

    public abstract String getTypeFile(String procId, String procVersion);

    protected String makeSaveFileDir(String fileTypeDir, String tenantId, String lowDirId) {
        String resultDir = "";
        String lowDir = "";
        String highDir = String.valueOf(Integer.parseInt(tenantId) % 500);

        if(lowDirId == null || "".equals(lowDirId)) {
            lowDir = String.valueOf(Integer.parseInt(String.valueOf(System.currentTimeMillis() % 500)));
        } else {
            lowDir = String.valueOf(Integer.parseInt(lowDirId) % 500);
        }

        resultDir = fileTypeDir + "/" + highDir + "/" + tenantId + "/" + lowDir;

        return resultDir;
    }
}
