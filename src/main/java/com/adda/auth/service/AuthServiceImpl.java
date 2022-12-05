package com.adda.auth.service;

import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;
import com.adda.auth.jwt.JwtUtils;
import com.adda.auth.token.RefreshToken;
import com.adda.auth.token.service.RefreshTokenService;
import com.adda.email.EmailService;
import com.adda.user.User;
import com.adda.user.repository.UserRepository;
import com.adda.user.role.Role;
import com.adda.user.service.UserDetailsImpl;
import com.adda.user.service.UserService;
import com.adda.user.wishlist.service.WishListService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The AuthServiceImpl class implements AuthService interface to create methods {@link AuthService}
 */

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final WishListService wishListService;
    private final EmailService emailService;

    /**
     * Constructor for {@link AuthServiceImpl}.
     *
     * @param userRepository        {@link UserRepository}
     * @param authenticationManager {@link AuthenticationManager}
     * @param jwtUtils              {@link JwtUtils}
     * @param refreshTokenService   {@link RefreshTokenService}
     * @param passwordEncoder       {@link PasswordEncoder}
     * @param userService           {@link UserService}
     * @param wishListService       {@link WishListService}
     * @param emailService          {@link EmailService}
     */
    @Autowired
    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, PasswordEncoder passwordEncoder, UserService userService, WishListService wishListService, EmailService emailService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.wishListService = wishListService;
        this.emailService = emailService;
    }

    /**
     * Method that authenticate user into the system and save in DB. {@link com.adda.user.User}
     *
     * @param loginRequest DTO to authenticate data. {@link SignInDTO}
     * @return JwtResponse object in case of success. {@link JwtResponse}
     */
    @Override
    public JwtResponse authenticate(@Valid SignInDTO loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userRepository.findById(userDetails.getId()).orElseThrow().isEnabled()) {
            throw new IllegalArgumentException("You need to confirm your email address first");
        } else {
            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
        }
    }


    /**
     * Method that registering user in the system and save in DB. {@link com.adda.user.User}
     *
     * @param request DTO to register user. {@link SignupDTO}
     * @param url     The request URL.
     */
    @Override
    public void register(SignupDTO request, String url) {
        User user = register(request);

        String content = "Dear " + user.getUsername() + ",<br>" + "Please click the link below to verify your registration:<br>" + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" + "ADDA - the best market place";

        String verifyURL = url + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        emailService.sendEmail(user.getEmail(), content, "Please verify your registration");
    }

    /**
     * Method that registering user in the system and save in DB. {@link com.adda.user.User}
     *
     * @param request DTO to register user. {@link SignupDTO}
     */
    public User register(SignupDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail());

        Set<String> strRoles = request.getRole();
        Set<Role> roles = userService.getRolesList(strRoles);

        user.setRoles(roles);

        user.setVerificationCode(RandomString.make(64));
        user.setEnabled(false);

        User save = userRepository.save(user);
        wishListService.createWishList(save);
        return save;
    }
}
