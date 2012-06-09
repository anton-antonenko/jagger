package com.griddynamics.jagger.webclient.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.griddynamics.jagger.webclient.client.dto.PagedSessionDataDto;
import com.griddynamics.jagger.webclient.client.dto.SessionDataDto;

/**
 * @author "Artem Kirillov" (akirillov@griddynamics.com)
 * @since 5/29/12
 */
public interface SessionDataServiceAsync {
    void getAll(int start, int length, AsyncCallback<PagedSessionDataDto> async);

    void getBySessionId(String sessionId, AsyncCallback<SessionDataDto> async);
}
