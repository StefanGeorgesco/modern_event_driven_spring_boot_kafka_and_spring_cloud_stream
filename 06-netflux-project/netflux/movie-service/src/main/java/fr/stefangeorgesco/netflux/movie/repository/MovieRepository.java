package fr.stefangeorgesco.netflux.movie.repository;

import fr.stefangeorgesco.netflux.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
