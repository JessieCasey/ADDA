package com.adda.user;

import com.adda.advert.dto.AdvertResponseDTO;
import com.adda.advice.MessageException;
import com.adda.user.dto.UserResponseDTO;
import com.adda.user.dto.UserUpdateDTO;
import com.adda.user.history.HistoryService;
import com.adda.user.repository.UserModel;
import com.adda.user.repository.UserModelAssembler;
import com.adda.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
    private final PagedResourcesAssembler<User> pagedResourcesAssembler;
    private final UserModelAssembler userModelAssembler;

    public UserController(UserService userService, HistoryService historyService,
                          PagedResourcesAssembler<User> pagedResourcesAssembler, UserModelAssembler userModelAssembler) {
        this.userService = userService;
        this.historyService = historyService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.userModelAssembler = userModelAssembler;
    }


    /**
     * @param firstNameFilter Filter for the first Name if required
     * @param lastNameFilter  Filter for the last Name if required
     * @param page            number of the page returned
     * @param size            number of entries in each page
     * @param sortList        list of columns to sort on
     * @param sortOrder       sort order. Can be ASC or DESC
     * @return PagedModel object in Hateoas with customers after filtering and sorting
     */
    @GetMapping
    public PagedModel<UserModel> fetchUsersWithPagination(
            @RequestParam(defaultValue = "") String firstNameFilter,
            @RequestParam(defaultValue = "") String lastNameFilter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        log.info("[GET] Request to method 'fetchCustomersWithPagination'");
        Page<User> customerPage = userService.fetchCustomerDataAsPageWithFilteringAndSorting(firstNameFilter, lastNameFilter, page, size, sortList, sortOrder.toString());
        return pagedResourcesAssembler.toModel(customerPage, userModelAssembler);
    }

    @PutMapping("/{id}")
    @PreAuthorize("#user.id == #id or hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestBody UserUpdateDTO updateDTO,
                                        @AuthenticationPrincipal UserDetails user,
                                        WebRequest request) {
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
            return ResponseEntity.badRequest().body(new MessageException(e.getMessage(), request));
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
                    .body(new MessageException(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#user.id == #id or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails user) {
        log.info("[Delete] Request to method 'deleteUser'");
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (Exception e) {
            log.error("Error in method 'deleteUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }

    @GetMapping("/history/{id}")
    @PreAuthorize("#user.id == #id or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getHistoryOfUser(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails user) {
        log.info("[GET] Request to method 'getHistoryOfUser'");
        try {
            List<AdvertResponseDTO> collect = historyService.getUserHistory(id).stream().map(AdvertResponseDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(collect.size() == 0 ? "User hasn't visited any adverts yet" : collect);
        } catch (Exception e) {
            log.error("Error in method 'getHistoryOfUser': " + e.getMessage());
            return ResponseEntity.badRequest().body("No user found");
        }
    }

}