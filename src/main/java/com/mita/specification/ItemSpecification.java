package com.mita.specification;

import com.mita.entity.Item;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class ItemSpecification {
    public static Specification<Item> withFilters(
            Long categoryId,
            Long userId,
            String title,
            Double rating,
            Double ratingAbove,
            String additionalInfo
    ){
        return (root, query, cb) -> {
                 List<Predicate> predicates = new ArrayList<>();

                 predicates.add(cb.equal(root.get("category").get("id"),categoryId));

                 predicates.add(cb.equal(root.get("user").get("id"),userId));


                 if(title != null){
                     predicates.add(
                             cb.like(cb.lower(root.get("title")),"%" + title.toLowerCase() + "%")
                     );
                 }

                 if(rating != null){
                     predicates.add(cb.equal(root.get("rating"),rating));
                 }

                 if(ratingAbove != null){
                     predicates.add(cb.greaterThanOrEqualTo(root.get("rating"),ratingAbove));
                 }

                 if(additionalInfo != null){
                     predicates.add(cb.like(cb.lower(root.get("additionalInfo")),"%" + additionalInfo.toLowerCase()+ "%"));
                 }


                 return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
