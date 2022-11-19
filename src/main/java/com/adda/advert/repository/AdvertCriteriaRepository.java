package com.adda.advert.repository;

import com.adda.advert.Advertisement;
import com.adda.advert.filter.AdvertPage;
import com.adda.advert.filter.AdvertSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class AdvertCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public AdvertCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Advertisement> findAllWithFilters(AdvertPage advertPage,
                                                  AdvertSearchCriteria searchCriteria) {
        CriteriaQuery<Advertisement> criteriaQuery = criteriaBuilder.createQuery(Advertisement.class);
        Root<Advertisement> employeeRoot = criteriaQuery.from(Advertisement.class);

        Predicate predicate = getPredicate(searchCriteria, employeeRoot);
        criteriaQuery.where(predicate);
        setOrder(advertPage, criteriaQuery, employeeRoot);

        TypedQuery<Advertisement> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(advertPage.getPageNumber() * advertPage.getPageSize());
        typedQuery.setMaxResults(advertPage.getPageSize());

        Pageable pageable = getPageable(advertPage);

        long employeesCount = getEmployeesCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
    }

    private Predicate getPredicate(AdvertSearchCriteria advertSearchCriteria,
                                   Root<Advertisement> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(advertSearchCriteria.getTitle())) {
            predicates.add(
                    criteriaBuilder.like(root.get("title"),
                            "%" + advertSearchCriteria.getTitle() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(AdvertPage advertPage,
                          CriteriaQuery<Advertisement> criteriaQuery,
                          Root<Advertisement> root) {
        if (advertPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(advertPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(advertPage.getSortBy())));
        }
    }

    private Pageable getPageable(AdvertPage advertPage) {
        Sort sort = Sort.by(advertPage.getSortDirection(), advertPage.getSortBy());
        return PageRequest.of(advertPage.getPageNumber(), advertPage.getPageSize(), sort);
    }

    private long getEmployeesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Advertisement> countRoot = countQuery.from(Advertisement.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
