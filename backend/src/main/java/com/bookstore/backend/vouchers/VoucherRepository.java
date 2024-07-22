package com.bookstore.backend.vouchers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("""
                select v from Voucher v where v.code like concat('%',:code,'%')
            """)
    Optional<Voucher> getVoucherByCode(@Param("code") String code);
}
