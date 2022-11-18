package com.adda.user.service;

import com.adda.advert.AdvertisementEntity;
import com.adda.advert.AdvertisementService;
import com.adda.auth.dto.SignInDTO;
import com.adda.auth.dto.SignupDTO;
import com.adda.auth.jwt.JwtResponse;
import com.adda.auth.jwt.JwtUtils;
import com.adda.auth.token.RefreshToken;
import com.adda.auth.token.service.RefreshTokenService;
import com.adda.exception.NullEntityReferenceException;
import com.adda.user.User;
import com.adda.user.UserRepository;
import com.adda.user.dto.UserDeletedDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.role.ERole;
import com.adda.user.role.Role;
import com.adda.user.role.RoleRepository;
import com.adda.user.wishlist.WishListService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final AdvertisementService advertisementService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final WishListService wishListService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final JavaMailSender mailSender;
    private final String fromAddress;

    @Autowired
    public UserServiceImpl(AdvertisementService advertisementService, UserRepository userRepository,
                           RoleRepository roleRepository, PasswordEncoder passwordEncoder, WishListService wishListService,
                           AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                           RefreshTokenService refreshTokenService,
                           JavaMailSender mailSender, @Value("${spring.mail.username}") String fromAddress) {
        this.advertisementService = advertisementService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.wishListService = wishListService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void createUser(SignupDTO request, String url) {
        User user = createUser(request);

        String content = "Dear " + user.getUsername() + ",<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "ADDA - the best market place";

        String verifyURL = url + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);

        sendEmail(user, content, "Please verify your registration");
    }

    public User createUser(SignupDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Error: Email is already in use!");
        }

        User user = new User(request.getFirstName(), request.getLastName(),
                request.getUsername(), passwordEncoder.encode(request.getPassword()), request.getEmail());

        Set<String> strRoles = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                }
            });
        }
        user.setRoles(roles);

        user.setVerificationCode(RandomString.make(64));
        user.setEnabled(false);

        User save = userRepository.save(user);
        wishListService.createWishList(save);
        return save;
    }

    public void sendEmail(User user, String content, String subject) {
        try {
            String toAddress = user.getEmail();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, "DoubleA");
            helper.setTo(toAddress);
            helper.setSubject(subject);

            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public User getOneUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id: '" + id + "' is not found"));
    }

    public User update(UserUpdateDTO userDTO) {
        User user = null;
        if (userDTO != null) {
            user = getOneUser(userDTO.getId());
        }

        if (user == null) {
            throw new NullEntityReferenceException("User cannot be 'null'");
        } else {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());

            if (!user.getRoles().equals(userDTO.getRoles())) {
                user.setRoles(userDTO.getRoles());
            }

            return userRepository.save(user);
        }

    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public UserDeletedDTO delete(Long id) {
        if (userRepository.existsById(id)) {
            List<AdvertisementEntity> allByUser = advertisementService.getAllByUser(id);
            UserDeletedDTO userDeletedDTO = new UserDeletedDTO(userRepository.getById(id), allByUser.size(), LocalDateTime.now());

            allByUser.forEach(x -> advertisementService.deleteAdvertById(x.getId()));
            userRepository.deleteById(id);

            log.info("Method 'delete(Long id)': User is deleted from the DB");
            return userDeletedDTO;
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email: '" + email + "' is not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public String verify(String verificationCode) {
        log.info("Method 'verify': method is invoked");
        User user = userRepository.findByVerificationCode(verificationCode).orElseThrow();

        if (user.isEnabled()) {
            log.warn("Method 'verify': User is already enabled or null");
            return "Already done";
        } else {
            log.info("Method 'verify': User '" + user.getUsername() + "' is verified");
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            String content = "Hi again! " + user.getUsername() + ",<br>"
                    + "You've completed registration, <br>"
                    + "Feel free to start you business with us, <br>"
                    + "ADDA - the best market place";

            sendEmail(user, content, "Registration completed");
            log.info("Method 'verify': Successful");
            return "Done";
        }
    }

    public User encodeUserFromToken(String token) {
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        Long id = new JSONObject(payload).getLong("id");

        if (userRepository.findById(id).isEmpty()) {
            throw new UsernameNotFoundException("You are not registered");
        }
        return userRepository.findById(id).get();
    }

    public JwtResponse authenticate(@Valid SignInDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (!userRepository.findById(userDetails.getId()).get().isEnabled()) {
            throw new IllegalArgumentException("You need to confirm your email address first");
        } else {
            String jwt = jwtUtils.generateJwtToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
        }
    }

}