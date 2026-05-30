package org.example.nidabutik.repository;

import org.example.nidabutik.dto.CustomerPurchaseSummary;
import org.example.nidabutik.entity.CustomerOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomer_EmailIgnoreCase(String email);
    List<CustomerOrder> findByStatus_CodeIgnoreCase(String code);
    List<CustomerOrder> findTop8ByStatus_CodeIgnoreCaseOrderByTotalAmountDesc(String code);

    @Query("""
            select c.id as id, c.firstName as firstName, c.lastName as lastName, c.email as email,
                   g.code as genderCode, g.label as genderLabel, sum(i.quantity) as purchasedQuantity
            from CustomerOrder o
            join o.customer c
            join c.gender g
            join o.items i
            where lower(g.code) = lower(:genderCode) and lower(o.status.code) = 'paid'
            group by c.id, c.firstName, c.lastName, c.email, g.code, g.label
            order by sum(i.quantity) desc
            """)
    List<CustomerPurchaseSummary> findTopCustomersByPurchasedQuantity(String genderCode, Pageable pageable);
}
