package org.zxc_tako25.justnpcs.npc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class NpcSkin {
    private final String value;
    private final String signature;

    public boolean isValid() {
        return value != null && !value.isEmpty();
    }

    public boolean hasSignature() {
        return signature != null && !signature.isEmpty();
    }
}
