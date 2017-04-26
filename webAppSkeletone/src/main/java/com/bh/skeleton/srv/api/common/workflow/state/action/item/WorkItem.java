package com.hs.gms.srv.api.common.workflow.state.action.item;

public interface WorkItem<T> {

    public void work(T workRequestStateVO) throws Exception;
}
