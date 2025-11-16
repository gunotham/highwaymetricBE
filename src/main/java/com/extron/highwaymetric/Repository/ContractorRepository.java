package com.extron.highwaymetric.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.extron.highwaymetric.Model.Contractor;



public interface ContractorRepository extends JpaRepository<Contractor, String>{
    Contractor findByName(String name);

    boolean existsByName(String name);

}
