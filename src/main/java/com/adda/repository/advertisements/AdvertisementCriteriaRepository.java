package com.adda.repository.advertisements;

import com.adda.domain.AdvertisementEntity;
import com.adda.model.AdvertPage;
import com.adda.model.AdvertSearchCriteria;
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
public class AdvertisementCriteriaRepository {
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public AdvertisementCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<AdvertisementEntity> findAllWithFilters(AdvertPage employeePage,
                                                        AdvertSearchCriteria employeeSearchCriteria) {
        CriteriaQuery<AdvertisementEntity> criteriaQuery = criteriaBuilder.createQuery(AdvertisementEntity.class);
        Root<AdvertisementEntity> employeeRoot = criteriaQuery.from(AdvertisementEntity.class);
        Predicate predicate = getPredicate(employeeSearchCriteria, employeeRoot);
        criteriaQuery.where(predicate);
        setOrder(employeePage, criteriaQuery, employeeRoot);

        TypedQuery<AdvertisementEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(employeePage.getPageNumber() * employeePage.getPageSize());
        typedQuery.setMaxResults(employeePage.getPageSize());

        Pageable pageable = getPageable(employeePage);

        long employeesCount = getEmployeesCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, employeesCount);
    }

    private Predicate getPredicate(AdvertSearchCriteria employeeSearchCriteria,
                                   Root<AdvertisementEntity> employeeRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(employeeSearchCriteria.getPrice())) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("price"),
                            "%" + employeeSearchCriteria.getPrice() + "%")
            );
        }
        if (Objects.nonNull(employeeSearchCriteria.getTitle())) {
            predicates.add(
                    criteriaBuilder.like(employeeRoot.get("title"),
                            "%" + employeeSearchCriteria.getTitle() + "%")
            );
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(AdvertPage employeePage,
                          CriteriaQuery<AdvertisementEntity> criteriaQuery,
                          Root<AdvertisementEntity> employeeRoot) {
        if (employeePage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(employeeRoot.get(employeePage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(employeeRoot.get(employeePage.getSortBy())));
        }
    }

    private Pageable getPageable(AdvertPage employeePage) {
        Sort sort = Sort.by(employeePage.getSortDirection(), employeePage.getSortBy());
        return PageRequest.of(employeePage.getPageNumber(), employeePage.getPageSize(), sort);
    }

    private long getEmployeesCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<AdvertisementEntity> countRoot = countQuery.from(AdvertisementEntity.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
