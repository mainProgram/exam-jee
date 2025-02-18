package com.groupeisi.services.dao;

import com.groupeisi.services.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
}
