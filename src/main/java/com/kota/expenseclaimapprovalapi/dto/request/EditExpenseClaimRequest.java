package com.kota.expenseclaimapprovalapi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class EditExpenseClaimRequest {

    @Length(max = 255, message = "Max length of title is 255")
    private String title;

    private String description;

    @DecimalMin(value = "0", message = "amount must be greater than or equal to 0")
    private Integer amount;
}
