package com.groupeisi.services.mapping;

import com.groupeisi.services.dto.User;
import com.groupeisi.services.entities.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserEntity fromUser(User User);

    User toUser(UserEntity UserEntity);

    List<User> toUsers(List<UserEntity> UserEntities);
}
