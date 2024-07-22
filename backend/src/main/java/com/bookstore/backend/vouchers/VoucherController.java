package com.bookstore.backend.vouchers;

import com.bookstore.backend.vouchers.dto.VoucherCreateDto;
import com.bookstore.backend.vouchers.dto.VoucherUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/vouchers", produces = MediaType.APPLICATION_JSON_VALUE)
public class VoucherController {

    private final VoucherService voucherService;

    @RequestMapping
    public Page<Voucher> getAll(Pageable pageable){
        return voucherService.getAll(pageable);
    }

    @RequestMapping("/{id}")
    public Voucher findVoucherById(@PathVariable Long id){
        return voucherService.getVoucherById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Voucher addVoucher(@Valid @RequestBody VoucherCreateDto request){
        return voucherService.save(request);
    }

    @PatchMapping("/{id}")
    public Voucher update(@PathVariable Long id, @Validated @RequestBody VoucherUpdateDto dto){
        return voucherService.updateById(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeVoucher(@PathVariable Long id){
        voucherService.deleteById(id);
    }

}
