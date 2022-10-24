package com.mapnote.mapnoteserver.domain.mail.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Getter;

@Getter
public class MailBody {

  private static final Map<String, Supplier<String>> mailTitleMap = new HashMap<>();
  private static final Map<String, Function<String, String>> mailBodyMap = new HashMap<>();

  static {
    mailTitleMap.put("signup", () -> signupMailTitle());
    mailBodyMap.put("signup", (authenticationString) -> signupMailBody(authenticationString));
    mailTitleMap.put("findPassword", () -> findPasswordTitle());
    mailBodyMap.put("findPassword", (authenticationString) -> findPasswordBody(authenticationString));
  }

  public static String getMailTitle(String type) {
    return mailTitleMap.get(type).get();
  }

  public static String getMailBody(String type, String authenticationString) {
    return mailBodyMap.get(type).apply(authenticationString);
  }

  private static String signupMailTitle() {

    StringBuilder titleBuilder = new StringBuilder();

    titleBuilder.append("[Mapnote] ");
    titleBuilder.append("회원가입 인증 메일입니다.");

    return titleBuilder.toString();
  }

  private static String signupMailBody(String authenticationString) {

    StringBuilder contentBuilder = new StringBuilder();

    contentBuilder.append("<h1>[Mapnote] 이메일 인증</h1>");
    contentBuilder.append("<br> <h3> ");
    contentBuilder.append(" 안녕하세요. </h3><br> <p> Mapnote 회원가입 인증번호는 ");
    contentBuilder.append(authenticationString);
    contentBuilder.append(" 입니다. </p><br> <h3> 인증번호를 입력 후 회원가입을 진행해주세요.</h3>");

    return contentBuilder.toString();
  }

  private static String findPasswordTitle() {
    StringBuilder titleBuilder = new StringBuilder();

    titleBuilder.append("[Mapnote] ");
    titleBuilder.append("패스워드 찾기 인증 메일입니다.");

    return titleBuilder.toString();
  }

  private static String findPasswordBody(String authenticationString) {
    StringBuilder contentBuilder = new StringBuilder();

    contentBuilder.append("<h1>[Mapnote] 패스워드 찾기 이메일 인증</h1>");
    contentBuilder.append("<br> <h3> ");
    contentBuilder.append(" 안녕하세요. </h3><br> <p> Mapnote 패스워드 찾기 인증번호는 ");
    contentBuilder.append(authenticationString);
    contentBuilder.append(" 입니다. </p><br> <h3> 인증번호를 입력 후 패스워드 찾기를 계속 진행해주세요.</h3>");

    return contentBuilder.toString();
  }
}
