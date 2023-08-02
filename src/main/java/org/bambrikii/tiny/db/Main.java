package org.bambrikii.tiny.db;

import org.bambrikii.tiny.db.cmd.CommandParser;

import java.util.List;

public class Main {
    private static String tryDbLogNameOption(String[] args) {
        return args.length > 1 ? args[1] : "db";
    }

    private static int tryPortOption(String[] args) {
        return args.length > 0 ? Integer.parseInt(args[0]) : 9094;
    }

    public static void main(String[] args) {
        int port = tryPortOption(args);
        var net = new Net(port);
        String db = tryDbLogNameOption(args);
        var register = new Register(db);
        var parser = new CommandParser();

        net.listen(s -> {
            Command cmd = parser.parse(s);
            if (cmd == null) {
                return "failed to parse [" + s + "]";
            }
            List<String> args1 = cmd.getArgs();
            switch (cmd.getCmd()) {
                case "find": {
                    return register.find(args1.get(0));
                }
                case "mod": {
                    String name = args1.get(0);
                    if (args1.size() > 1) {
                        register.mod(name, args1.get(1));
                    } else {
                        register.mod(name);
                    }
                    return "ok";
                }
                case "del": {
                    String name = args1.get(0);
                    register.mod(name);
                    return "ok";
                }
                case "noop":
                    return "noop";
                case "echo":
                    return "echo";
                case "exit":
                    System.exit(-1);
                default:
                    return "TODO";
            }
        });
        ;
    }
}
