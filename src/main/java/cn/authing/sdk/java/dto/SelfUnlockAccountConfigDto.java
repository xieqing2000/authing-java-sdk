package cn.authing.sdk.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SelfUnlockAccountConfigDto {
    /**
     * 是否允许用户自助解锁账号。
     */
    @JsonProperty("enabled")
    private Boolean enabled;
    /**
     * 自助解锁方式，目前支持原密码 + 验证码和验证码两种方式。
     */
    @JsonProperty("strategy")
    private String  strategy;

    public Boolean getEnabled() {
        return enabled;
    }
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getStrategy() {
        return strategy;
    }
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }


    /**
     * 自助解锁方式，目前支持原密码 + 验证码和验证码两种方式。
     */
    public static enum Strategy {

        @JsonProperty("captcha")
        CAPTCHA("captcha"),

        @JsonProperty("password-captcha")
        PASSWORD_CAPTCHA("password-captcha"),
        ;

        private String value;

        Strategy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


}