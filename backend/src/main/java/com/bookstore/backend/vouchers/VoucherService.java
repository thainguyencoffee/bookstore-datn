package com.bookstore.backend.vouchers;

import com.bookstore.backend.core.exception.CustomNoResultException;
import com.bookstore.backend.core.exception.VoucherDateConflictException;
import com.bookstore.backend.vouchers.dto.VoucherCreateDto;
import com.bookstore.backend.vouchers.dto.VoucherUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoucherService {

    private final VoucherRepository repository;

    public Page<Voucher> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Voucher getVoucherById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomNoResultException(Voucher.class, id));
    }

    public Voucher save(VoucherCreateDto createDto) {
        if (createDto.getEndDate().isBefore(createDto.getStartDate())) {
            throw new VoucherDateConflictException(createDto.getStartDate(), createDto.getEndDate());
        }
        return repository.save(createDto.toEntity());
    }

    public void deleteById(Long id) {
        repository.delete(getVoucherById(id));
    }
    
    public Voucher updateById(Long id, VoucherUpdateDto request) {
        Voucher voucher = getVoucherById(id);
        if (request.getCode() != null)
            voucher.setCode(request.getCode());
        if (request.getType() != null) {
            voucher.setType(request.getType());
        }
        if (request.getValue() != null) {
            if (voucher.getType() == Voucher.Type.PERCENT) {
                if (request.getValue() < 1 || request.getValue() > 100)
                    throw new RuntimeException("Value must be between 1 and 100 for PERCENT type");
            }
            if (request.getValue() < 0)
                throw new RuntimeException("Value cannot be less than 0");
            voucher.setValue(request.getValue());
        }
        if (request.getQuantity() != null)
            voucher.setQuantity(request.getQuantity());
        if (request.getStartDate() != null) {
            if (request.getStartDate().isAfter(voucher.getEndDate()))
                throw new VoucherDateConflictException(request.getStartDate(), voucher.getEndDate());
            voucher.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            if (request.getEndDate().isBefore(voucher.getStartDate()))
                throw new VoucherDateConflictException(voucher.getStartDate(), request.getEndDate());
            voucher.setEndDate(request.getEndDate());
        }
        return repository.save(voucher);
    }

}
