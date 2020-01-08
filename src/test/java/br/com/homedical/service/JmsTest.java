package br.com.homedical.service;

import br.com.homedical.HomedicalApp;
import br.com.homedical.service.jms.JmsDestinationQueue;
import br.com.homedical.service.jms.JmsReceiver;
import br.com.homedical.service.jms.types.StringType;
import br.com.homedical.util.ThreadUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HomedicalApp.class)
@Transactional
public class JmsTest {


    @Autowired
    private JmsReceiver receiver;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testJmsQueue() {

        CountDownLatch latch = new CountDownLatch(5);
        IntStream.range(0, 5)
            .parallel()
            .forEach(it -> {
                jmsTemplate.convertAndSend(JmsDestinationQueue.REGISTRATION_EMAIL_QUEUE, StringType.builder().value("hue").build());
            });

        IntStream.range(0, 5)
            .forEach(it -> {
                ThreadUtils.awaitSilently(1000);
                latch.countDown();
            });
        ThreadUtils.awaitSilently(latch);

    }

}
