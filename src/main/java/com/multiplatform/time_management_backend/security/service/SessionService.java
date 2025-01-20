package com.multiplatform.time_management_backend.security.service;

import com.multiplatform.time_management_backend.configuration.CustomRedisCacheConfigurations;
import com.multiplatform.time_management_backend.exeption.NotFoundException;
import com.multiplatform.time_management_backend.user.model.Session;
import com.multiplatform.time_management_backend.user.model.User;
import com.multiplatform.time_management_backend.user.repository.SessionRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final CacheManager cacheManager;
    private Cache sessionCache;

    public SessionService(SessionRepository sessionRepository, CacheManager cacheManager) {
        this.sessionRepository = sessionRepository;
        this.cacheManager = cacheManager;
        this.sessionCache = cacheManager.getCache(CustomRedisCacheConfigurations.SESSION_IDS_CACHE_NAME);
    }

    public Session create(User user, String userAgent) {
        return sessionRepository.save(new Session(user, userAgent, LocalDateTime.now().plusDays(7)));
    }

    public Session findById(String string) throws NotFoundException {
        return sessionRepository.findById(string).orElseThrow(() -> new NotFoundException("Session not found"));
    }

    public void terminate(Session session) {
        session.setExpiredAt(LocalDateTime.now());
        sessionRepository.save(session);
        if (sessionCache == null) {
            sessionCache = cacheManager.getCache(CustomRedisCacheConfigurations.SESSION_IDS_CACHE_NAME);
        }
        if (sessionCache != null) {
            sessionCache.put(session.getId(), session.getExpiredAt()
                    .toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(Instant.now())));
        }
    }
}
