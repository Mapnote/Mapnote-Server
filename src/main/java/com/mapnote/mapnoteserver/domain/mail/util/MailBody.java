package com.mapnote.mapnoteserver.domain.mail.util;

public class MailBody {

  public static String signupMailTitle() {

    StringBuilder titleBuilder = new StringBuilder();

    titleBuilder.append("[Mapnote] ");
    titleBuilder.append("회원가입 인증 메일입니다.");

    return titleBuilder.toString();
  }

  public static String signupMailBody(String AuthenticationString) {

    StringBuilder contentBuilder = new StringBuilder();

    contentBuilder.append("<h1>[Mapnote] 이메일 인증</h1>");
    contentBuilder.append("<br> <h3> ");
    contentBuilder.append(" 안녕하세요. </h3><br> <p> Mapnote 회원가입 인증번호는 ");
    contentBuilder.append(AuthenticationString);
    contentBuilder.append(" 입니다. </p><br> <h3> 인증번호를 입력 후 회원가입을 진행해주세요.</h3>");

    return contentBuilder.toString();
  }



}
