package com.example.retea_de_socializare.repository.paging;

import com.example.retea_de_socializare.domain.Entity;
import com.example.retea_de_socializare.repository.Repository;
import com.example.retea_de_socializare.utils.dto.FilterDTO;
import com.example.retea_de_socializare.utils.paging.Page;
import com.example.retea_de_socializare.utils.paging.Pageable;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllOnPage(Pageable pageable, FilterDTO filter);
}
