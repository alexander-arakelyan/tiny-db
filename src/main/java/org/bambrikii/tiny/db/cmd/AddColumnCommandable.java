package org.bambrikii.tiny.db.cmd;

public interface AddColumnCommandable {
    AddColumnCommandable addColumn(String name, String type, int scale, int precision, boolean nullable, boolean unique);
}
