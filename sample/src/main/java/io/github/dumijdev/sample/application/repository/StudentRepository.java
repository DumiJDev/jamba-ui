package io.github.dumijdev.sample.application.repository;

import io.github.dumijdev.sample.application.models.Student;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface StudentRepository extends CrudRepository<Student, Integer> {
}
