package com.hs.gms.srv.api.common.directory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hs.gms.srv.api.account.UserDataContextHolder;
import com.hs.gms.srv.api.account.vo.UserDataVO;
import com.hs.gms.srv.api.org.OrgDAO;
import com.hs.gms.srv.api.org.vo.OrgVO;
import com.hs.gms.std.common.test.AbstractApplicationContext;

public class DirectoryDAOTest extends AbstractApplicationContext {

    @Autowired
    OrgDAO orgDAO;

    @Before
    public void setUp() {
        UserDataVO userDataVO = new UserDataVO();

        userDataVO.setTenantId(super.testTenantId);
        userDataVO.setUserName(super.testUserName);
        userDataVO.setUserId(super.testUserId);
        userDataVO.setDeptId(super.testDeptId);

        UserDataContextHolder.setUserData(userDataVO);
    }

    @Test
    public void testSelectOrgUser() {
        OrgVO org = orgDAO.selectOrgView("U", "000000001");
        Assert.assertNotNull(org);
    }

    @Test
    public void testSelectOrgDept() {
        OrgVO org = orgDAO.selectOrgView("D", "000000101");
        Assert.assertNotNull(org);
    }

    @Test
    public void testSelectOrgGroup() {
        OrgVO org = orgDAO.selectOrgView("G", "000000101");
        Assert.assertNotNull(org);
    }
}
