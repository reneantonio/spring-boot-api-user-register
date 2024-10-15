package cl.challenge.api.services;

import cl.challenge.api.models.User;
import cl.challenge.api.models.UserRequest;

public interface UserService {

    User registerUser(UserRequest userRequest);
    
}