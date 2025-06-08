package org.bambrikii.tiny.db.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PartitionStruct {
    private final TableStruct tableStruct;
    private final Filter filter;
}
