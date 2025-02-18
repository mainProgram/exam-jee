package com.groupeisi.services.services;

import com.groupeisi.services.dao.IUserRepository;
import com.groupeisi.services.mapping.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final IUserRepository iUserRepository;
    private final UserMapper UserMapper;
    private final MessageSource messageSource;

}
