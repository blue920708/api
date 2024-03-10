package com.choi.api.core.util;

import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@SuppressWarnings("unused")
public class RequestUtils {

  /**
   * 현재 요청의 HttpServletRequest를 구한다.
   *
   * @return HttpServletRequest httpServletRequest 객체
   */
  public static HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (Objects.isNull(requestAttributes)
        || !(requestAttributes instanceof ServletRequestAttributes)) {
      return null;
    }
    return ((ServletRequestAttributes) requestAttributes).getRequest();
  }

  /**
   * 현재 요청의 HttpServletResponse를 구한다.
   *
   * @return HttpServletResponse
   */
  public static HttpServletResponse getResponse() {
    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    if (Objects.isNull(requestAttributes)
        || !(requestAttributes instanceof ServletRequestAttributes)) {
      return null;
    }
    return ((ServletRequestAttributes) requestAttributes).getResponse();
  }

  /**
   * request 헤더에서 특정 이름의 값을 구한다.
   *
   * @param name 헤더 명
   * @return String 헤더 값
   */
  public static String getRequestHeader(String name) {
    HttpServletRequest httpServletRequest = getRequest();
    if (httpServletRequest != null) {
      return httpServletRequest.getHeader(name);
    } else {
      return null;
    }
  }

  /**
   * 사용자 아이피를 구한다.
   *
   * @return String 아이피 값
   */
  public static String getIp() {
    HttpServletRequest request = getRequest();
    String ip = Objects.requireNonNull(request).getHeader("X-Forwarded-For");
    if (ip == null) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * 모바일 앱(APP)에서 들어온 요청인지 여부를 반환한다.
   *
   * @return boolean
   */
  public static boolean isApp() {
    return getUserAgent().contains("freeT");
  }

  /**
   * 접속한 디바이스가 아이폰 인지 확인한다.
   */
  public static boolean isIos(){
    String userAgent = getUserAgent().toLowerCase();
    return (userAgent.indexOf("iphone") != -1 || userAgent.indexOf("ipad") != -1);
  }

  /**
   * 접속한 디바이스가 앱이면서 아이폰 인지 확인한다.
   */
  public static boolean isAppIos(){
    try {
      return "i".equalsIgnoreCase(getUserAgent().toLowerCase().replace("freet(", "")
        .replace(")","").split(",")[0]);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 접속한 디바이스가 안드로이드 인지 확인한다.
   */
  public static boolean isAndroid(){
    return getUserAgent().toLowerCase().contains("android");
  }

  /**
   * 사용자 에이전트를 구한다.
   *
   * @return String 사용자 에이전트 값
   */
  public static String getUserAgent() {
    return Objects.requireNonNull(getRequest()).getHeader("User-Agent");
  }



  /**
   * 접속한 디바이스 플랫폼 리턴
   *
   * @return A: 프리티 앱(안드로이드), I: 프리티 앱(아이폰) , MO: 모바일 웹, PC: PC
   */
  public static String getDevicePlatform() {
    if (isApp()) {
      if (isAndroid()) {
        return "A";
      } else {
        return "I";
      }
    } else if (isAndroid() || isIos()) {
      return "MO";
    } else {
      return "PC";
    }
  }

  /**
   * 주문 경로 처리 (Y:모바일, N:PC, A:APP(안드로이드), I:APP(IOS))
   * 
   * @return
   */
  public static String getOrderPath() {
    String platform = getDevicePlatform();
    if ("A".equals(platform) || "I".equals(platform)) {
      return platform;
    } else if ("MO".equals(platform)) {
      return "Y";
    } else {
      return "N";
    }
  }

}
