package com.restaurant.backend.repositories;

import com.restaurant.backend.domains.entities.ReceiptDetail;
import com.restaurant.backend.domains.entities.ReceiptDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, ReceiptDetailId> {
    List<ReceiptDetail> findAllByRecId(Integer recId);
}
