package org.kodigo.proyectos.tasklist.services;

import org.kodigo.proyectos.tasklist.entities.UserEntity;
import org.kodigo.proyectos.tasklist.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("The user " + username + " does not exist."));

    Collection<? extends GrantedAuthority> authorities =
        Set.of(new SimpleGrantedAuthority("ROLE_USER"));

    return new User(
        userEntity.getUsername(), userEntity.getPassword(), true, true, true, true, authorities);
  }
}
