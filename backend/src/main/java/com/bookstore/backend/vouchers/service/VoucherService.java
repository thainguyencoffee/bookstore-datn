package com.bookstore.backend.vouchers.service;

import com.bookstore.backend.vouchers.Voucher;
import com.bookstore.backend.vouchers.dto.VoucherRequest;
import com.bookstore.backend.vouchers.dto.VoucherUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VoucherService {
    Page<Voucher> getAll(Pageable pageable);
    Voucher createVoucher(VoucherRequest request);
    void deleteVoucherById(Long id);
    Voucher getVoucherById(Long id);
    Voucher getVoucherByCode(String code);
    Voucher update(Long id, VoucherUpdateRequest request);

}
