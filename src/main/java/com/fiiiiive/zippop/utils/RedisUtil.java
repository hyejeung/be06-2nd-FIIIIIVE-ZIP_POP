package com.fiiiiive.zippop.utils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    // SortedSet 초기화 및 만료 시간 설정
    public void init(String key, long expirationTimeInSeconds) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key, "start", System.currentTimeMillis());
        zSetOperations.remove(key, "start");
        redisTemplate.expire(key, expirationTimeInSeconds, TimeUnit.MINUTES);
    }

    // SortedSet 값 저장
    public void save(String key, String value, long timestamp) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add(key, value, timestamp);
    }

    // SortedSet 전체 값 조회
    public Set<ZSetOperations.TypedTuple<Object>> getAllValues(String key) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.rangeWithScores(key, 0, -1);
    }

    // SortedSet 사이즈 조회
    public Long zCard(String key) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.zCard(key);
    }

    // SortedSet 내 값의 순위 조회
    public Long getzRank(String key, String value) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        return zSetOperations.rank(key, value);
    }
    // SortedSet 내 값의 순위 조회
    public void remove(String key, String value) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.remove(key, value);
    }

    public String moveFirstWaitingToReserve(String key1, String key2) {
        ZSetOperations<String, Object> zSetOperations = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<Object>> waitingList = zSetOperations.rangeWithScores(key2, 0, 0);
        if (waitingList != null && !waitingList.isEmpty()) {
            ZSetOperations.TypedTuple<Object> firstWaitingUser = waitingList.iterator().next();
            String userEmail = (String) firstWaitingUser.getValue();
            double score = firstWaitingUser.getScore();
            zSetOperations.remove(key2, userEmail);
            zSetOperations.add(key1, userEmail, score);
            return userEmail;
        }
        return null;
    }
}