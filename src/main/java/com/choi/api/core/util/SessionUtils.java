package com.choi.api.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
@SuppressWarnings("unused")
public class SessionUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 세션에 객체를 Map으로 변환해서 담는다.
     *
     * @param keyName 세션 명
     * @param object  담을 객체
     */
    @SuppressWarnings("unchecked")
    public static void setSession(String keyName, Object object) {
        Map<String, Object> map = mapper.convertValue(object, Map.class);
        Objects.requireNonNull(RequestUtils.getRequest()).getSession().setAttribute(keyName, map);
    }

    /**
     * 세션에 String 값을 담는다.
     *
     * @param keyName 세션 명
     * @param value 담을 값
     */
    public static void setSessionString(String keyName, String value) {
        Objects.requireNonNull(RequestUtils.getRequest()).getSession().setAttribute(keyName, value);
    }

    /**
     * 세션을 Map으로 읽어서 객체 타입으로 변환하여 조회한다.
     *
     * @param keyName 세션 명
     * @param clazz   변환할 객체 타입
     * @param <T>     제네릭
     * @return Object 변환된 객체
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> T getSession(String keyName, Class<T> clazz) {
        Map<String, Object> map = (Map) Objects.requireNonNull(RequestUtils.getRequest()).getSession()
                .getAttribute(keyName);
        return mapper.convertValue(map, clazz);
    }

    /**
     * 세션을 Map 형식으로 조회한다.
     *
     * @param keyName 세션 명
     * @return Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getSession(String keyName) {
        return getSession(keyName, Map.class);
    }

    /**
     * String 형식의 세션을 읽어서 조회한다.
     *
     * @param keyName 세션 명
     * @return String 저장된 값
     */
    public static String getSessionString(String keyName) {
        return (String) Objects.requireNonNull(RequestUtils.getRequest()).getSession()
                .getAttribute(keyName);
    }

    /**
     * 세션 명에 저장된 객체를 삭제한다.
     * @param keyName 세션 명
     */
    public static void removeAttribute(String keyName) {
        Objects.requireNonNull(RequestUtils.getRequest())
                .getSession().removeAttribute(keyName);
    }
}