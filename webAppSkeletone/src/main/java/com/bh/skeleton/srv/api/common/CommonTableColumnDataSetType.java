package com.hs.gms.srv.api.common;

import java.util.Date;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;

/**
 * CommonTableColumnDataSetType
 * 
 * @author BH: Jun
 */
public enum CommonTableColumnDataSetType {
    INSERT {

        @Override
        public void setCommonData(CommonTableColumnVO commonCloumnVO, Date nowDate) {
            UserDataVO userData = UserDataContextHolder.getUserData();

            commonCloumnVO.setTenantId(userData.getTenantId());
            commonCloumnVO.setCreator(userData.getUserId());
            commonCloumnVO.setCreatorName(userData.getUserName());
            commonCloumnVO.setCreateDeptId(userData.getDeptId());
            commonCloumnVO.setCreateDate(nowDate);
        }

    },
    UPDATE {

        @Override
        public void setCommonData(CommonTableColumnVO commonCloumnVO, Date nowDate) {
            UserDataVO userData = UserDataContextHolder.getUserData();

            commonCloumnVO.setTenantId(userData.getTenantId());
            commonCloumnVO.setModifier(userData.getUserId());
            commonCloumnVO.setModifierName(userData.getUserName());
            commonCloumnVO.setModifyDeptId(userData.getDeptId());
            commonCloumnVO.setModifyDate(nowDate);
        }

    };

    public abstract void setCommonData(CommonTableColumnVO commonCloumnVO, Date nowDate);
}
