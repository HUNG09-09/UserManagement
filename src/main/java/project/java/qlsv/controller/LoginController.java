package project.java.qlsv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.java.qlsv.dto.ResponseDTO;
import project.java.qlsv.service.JwtTokenService;


// jsonwebtoken dung de giai quyet viec chung minh danh tinh, do phai gui di gui lai nhieu lan
@RestController
public class LoginController {

	// thay vi check quyen, xac thuc user, password bang tay thi goi bean nay
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenService jwtTokenService;

	// tao jwt
	@PostMapping("/login")
	public ResponseDTO<String> login(
			@RequestParam("username") String username,
			@RequestParam("password") String password) {

		// authen fail throw exception above
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		// if login success, jwt - gen token (String)
		return ResponseDTO.<String>builder().status(200).data(jwtTokenService.createToken(username)).build();
	}


}
