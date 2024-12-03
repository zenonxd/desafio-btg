package com.moreira.desafiobtg.dtos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public record PaginationDTO(Integer page,
                            Integer pageSize,
                            Long totalElements,
                            Integer totalPages) {

    public static PaginationDTO fromPage(Page<?> request) {
        return new PaginationDTO(
                request.getNumber(),
                request.getSize(),
                request.getTotalElements(),
                request.getTotalPages()
        );
    }
}
