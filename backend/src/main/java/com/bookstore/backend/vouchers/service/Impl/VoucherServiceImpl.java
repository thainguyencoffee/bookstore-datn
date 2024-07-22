package com.bookstore.backend.vouchers.service.Impl;

import com.bookstore.backend.core.exception.CustomNoResultException;
import com.bookstore.backend.vouchers.Voucher;
import com.bookstore.backend.vouchers.VoucherRepository;
import com.bookstore.backend.vouchers.dto.VoucherRequest;
import com.bookstore.backend.vouchers.dto.VoucherUpdateRequest;
import com.bookstore.backend.vouchers.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final VoucherRepository repository;
    Instant now = Instant.now();
    Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);

    @Override
    public Page<Voucher> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Voucher createVoucher(VoucherRequest request) {
        validateVoucherRequest(request);

        // Create and populate Voucher entity
        Voucher voucher = new Voucher();
        voucher.setCode(request.getCode());
        voucher.setType(request.getType());
        voucher.setValue(request.getValue());
        voucher.setQuantity(request.getQuantity());
        voucher.setStartDate(request.getStartdate());
        voucher.setEndDate(request.getEnddate());
        voucher.setCreatedBy("");  // Consider setting this from the current user context
        voucher.setCreatedDate(Instant.now());
        voucher.setLastModifiedBy("");  // Consider setting this from the current user context
        voucher.setLastModifiedDate(Instant.now());
        voucher.setStatus(0);  // Or other default status if necessary

        // Save and return the created Voucher
        return repository.save(voucher);
    }

    @Override
    public void deleteVoucherById(Long id) {
        if(repository.findById(id).isPresent()) {
            repository.deleteById(id);
        }else {
            throw new CustomNoResultException(Voucher.class,String.format("Voucher by %s not found", id));
        }
    }

    @Override
    public Voucher getVoucherById(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomNoResultException(Voucher.class, String.format("Voucher by %s not found", id)));
    }

    @Override
    public Voucher getVoucherByCode(String code) {
        return repository.getVoucherByCode(code)
                .orElseThrow(() -> new CustomNoResultException(Voucher.class, String.format("Voucher by %s not found", code)));
    }


    @Override
    public Voucher update(Long id, VoucherUpdateRequest request) {
        Instant endDate=request.getEndDate();
        Integer status=request.getStatus();
        Voucher voucher=getVoucherById(id);
        if(request.getQuantity()!=null){
            if(request.getQuantity()<0){
                throw new RuntimeException("Quantity cannot be less than 0");
            }
            if (request.getQuantity()>100){
                throw new RuntimeException("Quantity cannot be more than 100");
            }
            voucher.setQuantity(request.getQuantity());
        }
        if(endDate!=null){
            if(endDate.isBefore(endDate)){
                throw new RuntimeException(String.format("The end date cannot be earlier than the start date: %s",endDate));
            }
            request.setEndDate(endDate);
        }
        voucher.setStatus(status);
        return repository.save(voucher);
    }
    private void validateVoucherRequest(VoucherRequest request) {
        Long value = request.getValue();
        Integer quantity = request.getQuantity();
        Instant now = Instant.now();
        Instant oneWeekAgo = now.minus(7, ChronoUnit.DAYS);

        // Validate value based on type
        if (request.getType() == Voucher.Type.PERCENT) {
            if (value < 0) {
                throw new RuntimeException("Value cannot be less than 0: ");
            } else if (value > 100) {
                throw new RuntimeException("Value cannot be greater than 100: " + value);
            }
        } else if (request.getType() == Voucher.Type.MONEY) {
            if (value < 0) {
                throw new RuntimeException("Value cannot be less than 0: " + value);
            } else if (value > 1000000) {
                throw new RuntimeException("Value cannot be greater than 1,000,000: " + value);
            }
        } else {
            throw new RuntimeException( "Invalid voucher type: " + request.getType());
        }

        // Validate quantity
        if (quantity < 0) {
            throw new RuntimeException("Quantity cannot be less than 0: " + quantity);
        }

        // Validate dates
        if (request.getStartdate().isBefore(oneWeekAgo)) {
            throw new RuntimeException("Start date cannot be more than a week before the current date.");
        }
        if (request.getEnddate().isBefore(request.getStartdate())) {
            throw new RuntimeException("End date cannot be earlier than the start date.");
        }
    }
}
