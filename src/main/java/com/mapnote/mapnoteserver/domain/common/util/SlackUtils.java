package com.mapnote.mapnoteserver.domain.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackField;
import net.gpedro.integrations.slack.SlackMessage;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class SlackUtils {

  private final SlackApi slackApi;
  private final TaskExecutor taskExecutor;

  public SlackUtils(SlackApi slackApi, TaskExecutor taskExecutor) {
    this.slackApi = slackApi;
    this.taskExecutor = taskExecutor;
  }

  public void sendSlackMessage(HttpServletRequest req, Exception e) {
    Runnable runnable = () -> {
      SlackAttachment slackAttachment = new SlackAttachment();

      slackAttachment.setFallback("Error");
      slackAttachment.setColor("danger");
      slackAttachment.setTitle("⚠️ ERROR DETECT");
      slackAttachment.setTitleLink(req.getContextPath());
      slackAttachment.setText(e.getStackTrace().toString());
      slackAttachment.setColor("danger");
      slackAttachment.setFields(getSlackField(req));

      SlackMessage slackMessage = new SlackMessage();
      slackMessage.setAttachments(Collections.singletonList(slackAttachment));
      slackMessage.setIcon(":ghost:");
      slackMessage.setText("⚠️ SERVER EXCEPTION DETECT");
      slackMessage.setUsername("Server Exception!!");

      slackApi.call(slackMessage);
    };
    taskExecutor.execute(runnable);
  }

  private static List getSlackField (HttpServletRequest req) {

    List<SlackField> fields = new ArrayList<>();

    fields.add(new SlackField().setTitle("Request URL").setValue(req.getRequestURI().toString()));
    fields.add(new SlackField().setTitle("Request Method").setValue(req.getMethod()));
    fields.add(new SlackField().setTitle("Request Time").setValue(new Date().toString()));
    fields.add(new SlackField().setTitle("Request IP").setValue(req.getRemoteUser()));

    return fields;
  }

}
