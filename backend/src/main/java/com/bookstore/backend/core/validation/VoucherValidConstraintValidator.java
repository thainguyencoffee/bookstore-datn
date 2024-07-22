package com.bookstore.backend.core.validation;

import com.bookstore.backend.vouchers.Voucher;
import com.bookstore.backend.vouchers.dto.VoucherCreateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VoucherValidConstraintValidator implements ConstraintValidator<VoucherValidConstraint, VoucherCreateDto> {

    @Override
    public boolean isValid(VoucherCreateDto createDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (createDto.getType() == Voucher.Type.PERCENT) {
            return createDto.getValue() <= 100;
        }
        return true;
    }
}
