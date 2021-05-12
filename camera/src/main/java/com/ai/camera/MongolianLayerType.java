package com.ai.camera;

/**
 * Created by zxn on 2021/5/12.
 */

/**
 * 注释：蒙版类型
 */
public enum MongolianLayerType {
    /**
     * 护照个人信息
     */
    PASSPORT_PERSON_INFO,
    /**
     * 护照出入境
     */
    PASSPORT_ENTRY_AND_EXIT,
    /**
     * 身份证正面
     */
    IDCARD_POSITIVE,
    /**
     * 身份证反面
     */
    IDCARD_NEGATIVE,
    /**
     * 港澳通行证正面
     */
    HK_MACAO_TAIWAN_PASSES_POSITIVE,
    /**
     * 港澳通行证反面
     */
    HK_MACAO_TAIWAN_PASSES_NEGATIVE,
    /**
     * 银行卡
     */
    BANK_CARD
}
