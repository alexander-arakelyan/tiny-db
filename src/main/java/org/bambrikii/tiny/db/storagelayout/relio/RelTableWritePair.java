package org.bambrikii.tiny.db.storagelayout.relio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@RequiredArgsConstructor
public class RelTableWritePair {
    private final Path path;
    private RelTablePageWriteIO pageIo;
}
