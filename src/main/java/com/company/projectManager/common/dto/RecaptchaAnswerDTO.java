package com.company.projectManager.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecaptchaAnswerDTO(
        Boolean success,
        @JsonProperty("challenge_ts")
        String challenge_ts,
        String hostname,
        @JsonProperty("error-codes")
        String[] error_codes
) {}
