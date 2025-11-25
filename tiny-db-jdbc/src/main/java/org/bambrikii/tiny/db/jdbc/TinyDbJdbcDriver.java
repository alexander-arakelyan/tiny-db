package org.bambrikii.tiny.db.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class TinyDbJdbcDriver implements Driver {
    private static final Pattern URL_PATTERN = Pattern.compile("jdbc:tiny-db://([0-9a-z.-]+):[0-9]+.*");

    @Override
    public Connection connect(String url, Properties info) {
        var m = URL_PATTERN.matcher(url);
        var host = m.group(1);
        var port = Integer.parseInt(m.group(2));
        var cn = new TinyDbConnection(host, port);
        return cn;
    }

    @Override
    public boolean acceptsURL(String url) {
        return URL_PATTERN.matcher(url).matches();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }
}
