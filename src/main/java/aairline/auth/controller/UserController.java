package aairline.auth.controller;


import aairline.auth.dto.LoginRequest;
import aairline.auth.dto.UserDataDTO;
import aairline.auth.dto.UserResponseDTO;
import aairline.auth.entity.User;
import aairline.auth.service.UserService;
import aairline.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "users", description = "사용자 관리 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/signin")
    @Operation(summary = "사용자 로그인", description = "사용자가 아이디와 비밀번호로 로그인합니다.")
    // @ApiOperation 대신 @Operation 사용
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
    })
    public CustomResponse<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = userService.signin(loginRequest.getEmail(), loginRequest.getPassword());

        Cookie jwtCookie = new Cookie("JWT-TOKEN", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/"); // 적용 경로 설정
        response.addCookie(jwtCookie);

        return CustomResponse.success("로그인 성공", token, 200);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "422", description = "이미 사용 중인 사용자 이름")
    })
    public String signup(@RequestBody UserDataDTO user) {
        return userService.signup(modelMapper.map(user, User.class));
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "사용자 검색", description = "특정 사용자의 정보를 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "접근이 거부됨"),
        @ApiResponse(responseCode = "404", description = "사용자가 존재하지 않음")
    })
    public UserResponseDTO search(@PathVariable String email) {
        return modelMapper.map(userService.search(email), UserResponseDTO.class);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "403", description = "접근이 거부됨")
    })
    public UserResponseDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @Operation(summary = "JWT 토큰 갱신", description = "JWT 토큰을 갱신합니다.")
    public String refresh(@AuthenticationPrincipal UserDetails userDetails) {
        return userService.refresh(userDetails);
    }
}
