package org.bambrikii.tiny.db.cmd.selectrows;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bambrikii.tiny.db.cmd.CommandResult;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
public class ScrollableCommandResult extends CommandResult {
    private final String recordsAsStr;
}
