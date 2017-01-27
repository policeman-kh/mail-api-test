package api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

/**
 * Created by hirai on 2017/01/27.
 */
@Value
public class MailMessageList {

    @JsonProperty("messages")
    List<MailMessage> mailMessages;
    int resultSizeEstimate;

    @Value
    public static class MailMessage {
        @JsonProperty("id")
        String id;
        @JsonProperty("threadId")
        String threadId;
    }
}
