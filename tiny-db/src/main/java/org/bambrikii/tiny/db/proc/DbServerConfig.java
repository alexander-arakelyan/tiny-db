package org.bambrikii.tiny.db.proc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DbServerConfig {
    private String host;
    private int port;
}
