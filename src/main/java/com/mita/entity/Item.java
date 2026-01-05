package com.mita.entity;

import com.mita.dto.ItemDto;
import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "additional_info", nullable = false)
    private String additionalInfo;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Item() {}

    public Item(String title, Double rating, String additionalInfo, Category category) {
        this.category = category;
        this.title = title;
        this.rating = rating;
        this.additionalInfo = additionalInfo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public ItemDto toDto() {
        return new ItemDto(id,
                title,
                rating,
                additionalInfo,
                category.getId()
        );
    }


}
