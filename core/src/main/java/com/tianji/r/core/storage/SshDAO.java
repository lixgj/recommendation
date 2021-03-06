package com.tianji.r.core.storage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.tianji.r.core.util.SshSource;

@Service
@Scope(value = "prototype")
public class SshDAO {

    private static final Logger log = Logger.getLogger(SshDAO.class);
    private SSHClient client;

    public void init(SshSource sshConnection) throws IOException {
        client = new SSHClient();
        client.loadKnownHosts();// local .ssh/known_hosts
        client.connect(sshConnection.getHost());
        try {
            client.authPassword(sshConnection.getUsername(), sshConnection.getPassword());
        } catch (IOException e) {
            client.disconnect();
            throw e;
        }
    }

    public void exec(String cmd) throws IOException {
        final Session session = client.startSession();
        System.out.println("=============================================================");
        try {
            Command command = session.exec(cmd);
            System.out.println("$:" + cmd);
            System.out.println(IOUtils.readFully(command.getInputStream()).toString());
            command.join(20, TimeUnit.SECONDS);
            System.out.println("=============================================================");

            String msg = command.getExitErrorMessage() == null ? "Success" : command.getExitErrorMessage();
            log.debug("** exit status: " + command.getExitStatus() + " ==> " + msg);
        } finally {
            session.close();
        }
    }

    public void stop() throws IOException {
        client.disconnect();
    }

}
