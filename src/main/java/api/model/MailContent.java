package api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

/**
 * Created by hirai on 2017/01/27.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@Value
public class MailContent {
    String id;
    String threadId;
    String snippet;

    Payload payload;

    @JsonIgnoreProperties(ignoreUnknown=true)
    @Value
    public static class Payload {
        List<Header> headers;
    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    @Value
    public static class Header {
        String name;
        String value;
    }
}
