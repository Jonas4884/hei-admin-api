package school.hei.haapi.repository.dao;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import school.hei.haapi.endpoint.rest.model.SortOrder;
import school.hei.haapi.model.Course;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CourseManagerDao {
    private EntityManager entityManager;

    public List<Course> findByCriteria(
            String code,
            String name,
            Integer credits,
            String teacherFirstName,
            String teacherLastName,
            SortOrder creditsOrder,
            SortOrder codeOrder,
            Pageable pageable
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Course> query = builder.createQuery(Course.class);
        Root<Course> root = query.from(Course.class);

        List<Predicate> predicateList = new ArrayList<>();

        if(code != null){
            predicateList.add(builder.or(
                    builder.like(builder.lower(root.get("code")), "%" + code.toLowerCase() + "%")
            ));
        }
        if(name != null){
            predicateList.add(builder.or(
                    builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%")
            ));
        }
        if(credits != null){
            predicateList.add(builder.or(
                    builder.equal(root.get("credits"), credits)
            ));
        }

        if(teacherLastName != null){
            predicateList.add(builder.or(
                    builder.like(builder.lower(root.get("mainTeacher.lastName")), "%" + teacherLastName.toLowerCase() + "%")
            ));
        }

        if(teacherFirstName != null){
            predicateList.add(builder.or(
                    builder.like(builder.lower(root.get("mainTeacher.firstName")), "%" + teacherFirstName.toLowerCase() + "%")
            ));
        }

        query
                .where(builder.and(predicateList.toArray(new Predicate[0])))
                .orderBy(creditsOrder.equals(SortOrder.ASC) ? builder.asc(root.get("credits")) : builder.desc(root.get("credits")))
                .orderBy(codeOrder.equals(SortOrder.ASC) ? builder.asc(root.get("code")) : builder.desc(root.get("code")))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));

        return entityManager.createQuery(query)
                .setFirstResult((pageable.getPageNumber()) * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }


}
