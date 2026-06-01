package com.sentinelwatch.ids.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentinelwatch.ids.model.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Standard CRUD operations are inherited automatically
}

/*
This code connects Java code to the database tables even though it is empty. 
By extending JpaRepository, AlertRepository inherits methods for saving, finding, 
and deleting Alert entities without needing to write any SQL queries. 
Spring Data JPA will automatically generate the necessary SQL based on the method names and 
the Alert entity's structure. This allows you to perform database operations on Alert objects 
seamlessly through the repository interface.

-- extends JpaRepository<Alert, Long>
JPA: JAkarta Persistence API, a Java specification for managing relational data in applications.
JpaRepository is a java interjace by spring data jpa that simplifies database access that
sits on top of a powerful instance tree. 
    1. Repository: A marker interface that indicates this is a Spring Data repository.
    2. CrudRepository: Provides basic CRUD (Create, Read, Update, Delete) operations.
    3. PagingAndSortingRepository: Adds methods for pagination and sorting. 
*/