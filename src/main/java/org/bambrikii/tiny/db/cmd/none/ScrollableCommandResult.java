package org.bambrikii.tiny.db.cmd.none;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bambrikii.tiny.db.cmd.CommandResult;

@Getter
@Setter
@RequiredArgsConstructor
public class ScrollableCommandResult extends CommandResult {
    private final String recordsAsStr;
}
