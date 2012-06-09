package com.griddynamics.jagger.webclient.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.griddynamics.jagger.engine.e1.aggregator.session.model.SessionData;
import com.griddynamics.jagger.webclient.client.SessionDataService;
import com.griddynamics.jagger.webclient.client.dto.PagedSessionDataDto;
import com.griddynamics.jagger.webclient.client.dto.SessionDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author "Artem Kirillov" (akirillov@griddynamics.com)
 * @since 5/29/12
 */
public class SessionDataServiceImpl extends RemoteServiceServlet implements SessionDataService {
    private static final Logger log = LoggerFactory.getLogger(SessionDataServiceImpl.class);
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public PagedSessionDataDto getAll(int start, int length) {
        long timestamp = System.currentTimeMillis();
        EntityManager entityManager = EntityManagerProvider.getEntityManagerFactory().createEntityManager();
        long totalSize;
        List<SessionDataDto> sessionDataDtoList;
        try {
            totalSize = (Long) entityManager.createQuery("select count(sessionData.id) from SessionData as sessionData").getSingleResult();

            @SuppressWarnings("unchecked")
            List<SessionData> sessionDataList = (List<SessionData>)
                    entityManager.createQuery("select sd from SessionData as sd order by sd.startTime asc").setFirstResult(start).setMaxResults(length).getResultList();

            if (sessionDataList == null) {
                return new PagedSessionDataDto(Collections.<SessionDataDto>emptyList(), 0);
            }

            sessionDataDtoList = new ArrayList<SessionDataDto>(sessionDataList.size());
            DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
            for (SessionData sessionData : sessionDataList) {
                sessionDataDtoList.add(new SessionDataDto(
                        sessionData.getSessionId(),
                        dateFormatter.format(sessionData.getStartTime()),
                        dateFormatter.format(sessionData.getEndTime()),
                        sessionData.getActiveKernels(),
                        sessionData.getTaskExecuted(),
                        sessionData.getTaskFailed())
                );
            }

            log.info("There was loaded {} sessions data from {} for {} ms", new Object[]{sessionDataDtoList.size(), totalSize, System.currentTimeMillis() - timestamp});
        } finally {
            entityManager.close();
        }

        return new PagedSessionDataDto(sessionDataDtoList, (int) totalSize);
    }

    @Override
    public SessionDataDto getBySessionId(String sessionId) {
        long timestamp = System.currentTimeMillis();
        EntityManager entityManager = EntityManagerProvider.getEntityManagerFactory().createEntityManager();

        SessionDataDto sessionDataDto = null;
        try {
            SessionData sessionData = (SessionData) entityManager.createQuery("select sd from SessionData as sd where sd.sessionId = (:sessionId)").setParameter("sessionId", sessionId).getSingleResult();

            DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
            sessionDataDto = new SessionDataDto(
                    sessionData.getSessionId(),
                    dateFormatter.format(sessionData.getStartTime()),
                    dateFormatter.format(sessionData.getEndTime()),
                    sessionData.getActiveKernels(),
                    sessionData.getTaskExecuted(),
                    sessionData.getTaskFailed()
            );
            log.info("There was loaded session data with id {} for {} ms", sessionId, System.currentTimeMillis() - timestamp);
        } catch (NoResultException e) {
            log.info("No session data was found for session ID="+sessionId, e);
            return null;
        } catch (Exception e) {
            log.error("Error was occurred during session data with id=" + sessionId + " loading", e);
            throw new RuntimeException(e);
        } finally {
            entityManager.close();
        }

        return sessionDataDto;
    }
}
