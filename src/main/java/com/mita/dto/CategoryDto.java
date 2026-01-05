package com.mita.dto;

public class CategoryDto {
    private final Long id;
    private final String name;

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public String getName() {return name;}
}
