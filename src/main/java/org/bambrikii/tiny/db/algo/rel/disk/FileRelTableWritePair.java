package org.bambrikii.tiny.db.algo.rel.disk;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.nio.file.Path;

@Getter
@Setter
@RequiredArgsConstructor
public class FileRelTableWritePair {
    private final Path path;
    private FileRelTablePageWriter pageIo;
}
