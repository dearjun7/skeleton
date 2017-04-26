package com.hs.gms.srv.api.common.filestorage;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * FileStorageTypeInitializer
 * 
 * @author BH Jun
 */
@Component
public class FileStorageTypeInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageTypeInitializer.class);

    @Value("#{config['gms.filestorage.attach.dir']}")
    private String attachDir;

    @Value("#{config['gms.filestorage.map.dir']}")
    private String modelerDir;

    @PostConstruct
    public void postConstruct() {
        this.initFileStorageType();
    }

    private void initFileStorageType() {
        FileStorageType.ATTACH.setAttachDir(attachDir);
        LOGGER.debug("initFileStorageType ATTACH Type : " + attachDir);
        FileStorageType.MAP.setModelerDir(modelerDir);
        LOGGER.debug("initFileStorageType MODELER Type : " + modelerDir);
    }
}
