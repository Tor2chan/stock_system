package th.team.stock.services;

import th.team.stock.repositories.UsersRepo;
import th.team.stock.models.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ⭐ เช็ค active ถ้า false → login ไม่ได้
    
        System.out.println("username: " + user);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
                
    }
}