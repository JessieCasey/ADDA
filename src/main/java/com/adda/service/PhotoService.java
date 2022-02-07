package com.adda.service;

import com.adda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhotoService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.adda.repository.PhotoRepository photoRepository;

}
