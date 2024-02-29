package org.linshy.saas.project.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum VaildDataTypeEnum {

    /**
     * 永久有效期
     */
    PERMANENT(0),
    /**
     * 自定义有效期
     */
    CUSTOM(1);

    private final int type;

}
