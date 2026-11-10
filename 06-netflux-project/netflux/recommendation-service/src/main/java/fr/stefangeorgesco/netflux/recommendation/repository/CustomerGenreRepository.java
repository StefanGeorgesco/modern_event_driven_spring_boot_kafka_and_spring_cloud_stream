package fr.stefangeorgesco.netflux.recommendation.repository;

import fr.stefangeorgesco.netflux.recommendation.entity.CustomerGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerGenreRepository extends JpaRepository<CustomerGenre, Integer> {
}
