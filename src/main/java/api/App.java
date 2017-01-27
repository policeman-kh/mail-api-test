package api; /**
 * Created by hirai on 2017/01/27.
 */

import api.model.AccessToken;
import api.model.MailMessageList;
import api.service.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.UncheckedIOException;

@SpringBootApplication
public class App implements CommandLineRunner {

    @Autowired
    GmailService gmailService;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final AccessToken token = gmailService.publishToken();
        final MailMessageList mailMessageList = gmailService.fetchMessage(token);
        mailMessageList.getMailMessages().forEach(m ->{
            try {
                gmailService.fetchContent(token, m);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
    }
}
