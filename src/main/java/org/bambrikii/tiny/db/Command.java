package org.bambrikii.tiny.db;

import java.util.List;

public class Command {
    private final String ver;
    private final String cmd;
    private final List<String> args;

    public Command(String ver, String cmd, List<String> args) {
        this.ver = ver;
        this.cmd = cmd;
        this.args = args;
    }

    public String getVer() {
        return ver;
    }

    public String getCmd() {
        return cmd;
    }

    public List<String> getArgs() {
        return args;
    }
}
