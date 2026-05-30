package org.example.nidabutik.repository;

import org.example.nidabutik.dto.CustomerPurchaseSummary;
import org.example.nidabutik.entity.CustomerOrder;
import org.example.nidabutik.entity.Gender;
import org.example.nidabutik.entity.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomer_EmailIgnoreCase(String email);
    List<CustomerOrder> findByStatus(OrderStatus status);
    List<CustomerOrder> findTop8ByStatusOrderByTotalAmountDesc(OrderStatus status);

    @Query("""
            select c.id as id, c.firstName as firstName, c.lastName as lastName, c.email as email,
                   c.gender as gender, sum(i.quantity) as purchasedQuantity
            from CustomerOrder o
            join o.customer c
            join o.items i
            where c.gender = :gender and o.status = org.example.nidabutik.entity.OrderStatus.PAID
            group by c.id, c.firstName, c.lastName, c.email, c.gender
            order by sum(i.quantity) desc
            """)
    List<CustomerPurchaseSummary> findTopCustomersByPurchasedQuantity(@Param("gender") Gender gender, Pageable pageable);
}
