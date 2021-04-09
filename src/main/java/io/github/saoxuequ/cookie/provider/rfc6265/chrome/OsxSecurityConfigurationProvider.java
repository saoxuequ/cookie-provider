package io.github.saoxuequ.cookie.provider.rfc6265.chrome;

import io.github.saoxuequ.cookie.provider.config.ConfigurationProvider;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

public class OsxSecurityConfigurationProvider implements ConfigurationProvider {

    private static final String COMMAND_TEMPLATE = "security find-generic-password -a {0} -w";

    @Override
    public String get(String key) {
        String command = MessageFormat.format(COMMAND_TEMPLATE, key);
        try {
            return execAndGetTrimmedResult(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String execAndGetTrimmedResult(String command) throws IOException {
        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        PumpStreamHandler psh = new PumpStreamHandler(stdout);
        CommandLine cmdLine = CommandLine.parse(command);
        Executor executor = new DefaultExecutor();
        executor.setStreamHandler(psh);
        executor.execute(cmdLine);
        return stdout.toString().trim();
    }

}
