package api.service;

import api.model.AccessToken;
import api.model.MailContent;
import api.model.MailMessageList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hirai on 2017/01/27.
 */
@Service
public class GmailService {

    private static final String CLIENT_ID = "870997699573-fhcdgir4d7dkam3qfsnd6u3p88dtun23.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "NiuMpnihI-6JbASKy6jxnPGf";
    private static final String REFRESH_TOKEN = "1/fJX1t9ajUutmGQn6zDEHW4N9EZ09rkoJEd_hqmh2tT4";

    private static final String TOKEN_URL = "https://accounts.google.com/o/oauth2/token";
    private static final String MESSAGES_URL = "https://www.googleapis.com/gmail/v1/users/me/messages/";

    private static final RequestBody TOKEN_REQUEST_BODY =
            RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"),
                    createPostParameter(ImmutableMap.of("client_id", CLIENT_ID,
                            "client_secret", CLIENT_SECRET,
                            "refresh_token", REFRESH_TOKEN,
                            "grant_type", "refresh_token")));

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public AccessToken publishToken() throws IOException {
        final Request request = new Request.Builder()
                .url(TOKEN_URL)
                .post(TOKEN_REQUEST_BODY).build();
        final Response response = new OkHttpClient().newCall(request).execute();
        final String responseStr = response.body().string();
        return MAPPER.readValue(responseStr, AccessToken.class);
    }

    private static String createPostParameter(Map<String, String> paramMap) {
        return paramMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }

    public MailMessageList fetchMessage(final AccessToken token) throws IOException {
        final Request request = new Request.Builder()
                .url(MESSAGES_URL)
                .header("Authorization", "Bearer " + token.getAccessToken())
                .build();
        final Response response = new OkHttpClient().newCall(request).execute();
        final String responseStr = response.body().string();
        return MAPPER.readValue(responseStr, MailMessageList.class);
    }

    public MailContent fetchContent(final AccessToken token,
                                    final MailMessageList.MailMessage message) throws IOException {
        final Request request = new Request.Builder()
                .url(MESSAGES_URL + message.getId())
                .header("Authorization", "Bearer " + token.getAccessToken())
                .build();
        final Response response = new OkHttpClient().newCall(request).execute();
        final String responseStr = response.body().string();
        final MailContent content = MAPPER.readValue(responseStr, MailContent.class);

        final String subject = content.getPayload().getHeaders()
                .stream()
                .filter(h -> "Subject".equals(h.getName()))
                .map(MailContent.Header::getValue)
                .findFirst()
                .get();
        final String bodyShort = content.getSnippet();
        return content;
    }
}
