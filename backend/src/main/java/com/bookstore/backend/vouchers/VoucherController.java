package com.bookstore.backend.vouchers;

import com.bookstore.backend.vouchers.dto.VoucherRequest;
import com.bookstore.backend.vouchers.dto.VoucherUpdateRequest;
import com.bookstore.backend.vouchers.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/api/vouchers",produces = MediaType.APPLICATION_JSON_VALUE)
public class VoucherController {
    private final VoucherService voucherService;

    @RequestMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Page<Voucher> getAll(Pageable pageable){
        return voucherService.getAll(pageable);
    }

    @RequestMapping("/code/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Voucher findVoucherByCode(@PathVariable String code){
        return voucherService.getVoucherByCode(code);
    }
    @RequestMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Voucher findVoucherById(@PathVariable Long id){
        return voucherService.getVoucherById(id);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Voucher update(@PathVariable Long id, @RequestBody VoucherUpdateRequest request){
        return voucherService.update(id,request);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Voucher addVoucher(@RequestBody VoucherRequest request){
        return voucherService.createVoucher(request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeVoucher(@PathVariable Long id){
        voucherService.deleteVoucherById(id);
    }


}
