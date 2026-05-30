package org.example.nidabutik.service;

import org.example.nidabutik.dto.SupplierRequest;
import org.example.nidabutik.entity.Supplier;
import org.example.nidabutik.exception.BusinessRuleException;
import org.example.nidabutik.exception.ResourceNotFoundException;
import org.example.nidabutik.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SupplierService {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplier(Long id) {
        return findSupplier(id);
    }

    @Transactional
    public Supplier createSupplier(SupplierRequest request) {
        if (supplierRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessRuleException("Bu tedarikci e-postasi zaten kullaniliyor.");
        }
        Supplier supplier = new Supplier();
        applyRequest(supplier, request);
        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        Supplier supplier = findSupplier(id);
        supplierRepository.findByEmailIgnoreCase(request.email()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new BusinessRuleException("Bu e-posta baska bir tedarikciye ait.");
            }
        });
        applyRequest(supplier, request);
        return supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        supplierRepository.delete(findSupplier(id));
    }

    private Supplier findSupplier(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tedarikci bulunamadi: " + id));
    }

    private void applyRequest(Supplier supplier, SupplierRequest request) {
        supplier.setName(request.name());
        supplier.setEmail(request.email());
        supplier.setPhone(request.phone());
        supplier.setCity(request.city());
    }
}
