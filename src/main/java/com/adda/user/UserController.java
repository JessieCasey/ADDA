package com.adda.user;

import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advice.MessageResponse;
import com.adda.user.dto.UserResponseDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.history.HistoryService;
import com.adda.user.repository.UserModel;
import com.adda.user.repository.UserModelAssembler;
import com.adda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final HistoryService historyService;
    private final PagedResourcesAssembler<User> assembler;
    private final UserModelAssembler modelAssembler;

    /**
     * Constructor for {@link UserController}.
     *
     * @param userService    {@link UserService}
     * @param historyService {@link HistoryService}
     * @param assembler      {@link PagedResourcesAssembler}
     * @param modelAssembler {@link UserModelAssembler}
     */
    @Autowired
    public UserController(UserService userService, HistoryService historyService,
                          PagedResourcesAssembler<User> assembler, UserModelAssembler modelAssembler) {
        this.userService = userService;
        this.historyService = historyService;
        this.assembler = assembler;
        this.modelAssembler = modelAssembler;
    }

    /**
     * @param firstName Filter for the first Name if required
     * @param lastName  Filter for the last Name if required
     * @param page      number of the page returned
     * @param size      number of entries in each page
     * @param sortList  list of columns to sort on
     * @param sortOrder sort order. Can be ASC or DESC
     * @return PagedModel object in Hateoas with customers after filtering and sorting
     */
    @GetMapping("/v4/page")
    public PagedModel<UserModel> fetchUsersWithPagination(
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        log.info("[GET] Request to method 'fetchUsersWithPagination'");
        Page<User> customerPage = userService.fetchUserDataAsPageWithFilteringAndSorting
                (firstName, lastName, page, size, sortList, sortOrder.toString());
        return assembler.toModel(customerPage, modelAssembler);
    }

    /**
     * @param firstName Filter for the first Name if required
     * @param lastName  Filter for the last Name if required
     * @param page      number of the page returned
     * @param size      number of entries in each page
     * @param sortList  list of columns to sort on
     * @param sortOrder sort order. Can be ASC or DESC
     * @return Page object with customers after filtering and sorting
     */
    @GetMapping("/v3/page")
    public Page<User> fetchUsersWithPageInterfaceAndSorted(
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return userService.fetchUserDataAsPageWithFilteringAndSorting(firstName, lastName, page, size, sortList, sortOrder.toString());
    }

    /**
     * @param firstName Filter for the first Name if required
     * @param lastName  Filter for the last Name if required
     * @param page      number of the page returned
     * @param size      number of entries in each page
     * @return Page object with customers after filtering
     */
    @GetMapping("/v2/page")
    public Page<User> fetchUsersWithPageInterface(
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return userService.fetchUserDataAsPageWithFiltering(firstName, lastName, page, size);
    }

    /**
     * @param firstName Filter for the first Name if required
     * @param lastName  Filter for the last Name if required
     * @return List of filtered customers
     */
    @GetMapping("/v1/page")
    public List<User> fetchUsersAsFilteredList(@RequestParam(defaultValue = "") String firstName,
                                               @RequestParam(defaultValue = "") String lastName) {
        return userService.fetchFilteredUserDataAsList(firstName, lastName);
    }

    /**
     * @return List of all customers
     */
    @GetMapping("/v0/page")
    public List<User> fetchUsersAsList() {
        return userService.fetchUserDataAsList();
    }

    @PutMapping("/{id}")
    @PreAuthorize("#user.id == #id or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserUpdateDTO updateDTO,
                                        @AuthenticationPrincipal UserDetails user, WebRequest request) {
        log.info("[PUT][UserController] Request to method 'updateUser'");
        try {
            updateDTO.setId(id);
            User updated = userService.update(updateDTO);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(updated.getId())
                    .toUri();

            return ResponseEntity.created(location).body(new UserResponseDTO(updated));
        } catch (Exception e) {
            log.error("Error in method 'updateUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#user.id == #id or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user, WebRequest request) {
        log.info("[Delete] Request to method 'deleteUser'");
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (Exception e) {
            log.error("Error in method 'updateUser': " + e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage(), request));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id, WebRequest request) {
        log.info("[Get] Request to method 'getUser'");
        try {
            return ResponseEntity.ok(new UserResponseDTO(userService.getOneUser(id)));
        } catch (Exception e) {
            log.error("Error in method 'getUser': " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request));
        }
    }

    @GetMapping("/history/{id}")
    @PreAuthorize("#user.id == #id or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getHistoryOfUser(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails user,
                                              WebRequest request) {
        log.info("[GET] Request to method 'getHistoryOfUser'");
        try {
            List<AdvertResponseDTO> collect = historyService.getUserHistory(id).stream()
                    .map(AdvertResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(collect.size() == 0 ? "User hasn't visited any adverts yet" : collect);
        } catch (Exception e) {
            log.error("Error in method 'getUser': " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage(), request));
        }
    }

}