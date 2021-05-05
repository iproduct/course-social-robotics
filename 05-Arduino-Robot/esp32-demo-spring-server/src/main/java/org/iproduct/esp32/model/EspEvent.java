package org.iproduct.esp32.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class EspEvent {
    private String id;
    @NonNull
    private long timestamp;
    @NonNull
    private float distance;
}
