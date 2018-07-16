package com.github.ptosda.projectvalidationmanager.database.repositories

import com.github.ptosda.projectvalidationmanager.database.entities.License
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface LicenseRepository : PagingAndSortingRepository<License, String>{
    fun findAllByOrderBySpdxIdAsc(pageable: Pageable) : Page<License>
}