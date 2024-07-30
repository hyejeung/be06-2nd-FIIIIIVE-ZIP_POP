package com.fiiiiive.zippop.member;

import com.fiiiiive.zippop.member.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByEmail(String email);
    Optional<Company> findByCompanyIdx(Long companyIdx);
}
