package br.com.homedical.service.push;

import br.com.homedical.domain.push.Platform;
import br.com.homedical.domain.push.PushMessage;

import java.util.Set;

public interface PushSenderService {

    void sendMessage(Platform platform, Set<String> tokens, PushMessage message, PushSenderCallback callback);

}
