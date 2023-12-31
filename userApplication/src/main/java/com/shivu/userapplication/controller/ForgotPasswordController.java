package com.shivu.userapplication.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.shivu.userapplication.exception.UserNotFoundException;
import com.shivu.userapplication.model.ApplicationUser;
import com.shivu.userapplication.repository.UserRepository;
import com.shivu.userapplication.service.UserService;
import com.shivu.userapplication.utils.UrlUtility;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ForgotPasswordController {

	@Autowired
	private UserService userService;

	@Autowired
	UserRepository userRepo;

	@GetMapping("/forgotpassword")
	public String showForgotPasswordForm1() {
		return "forgotpassword";
	}

	@PostMapping("/forgotpassword")
	public String processForgotPassword1(HttpServletRequest request, Model model)
			throws Exception, UserNotFoundException {
		String email = request.getParameter("email");
		String token = UUID.randomUUID().toString();
		try {
			userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = UrlUtility.getSiteURL(request) + "/resetpassword?token=" + token;
			System.out.println(resetPasswordLink);
//            sendEmail(email, resetPasswordLink);
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
		} catch (UserNotFoundException ex) {
			model.addAttribute("error", ex.getMessage());
		}
//        catch ( UnsupportedEncodingException | MessagingException   e) {
//        
//        	model.addAttribute("error", "Error while sending email");
//        }

		return "forgotpassword";
	}

//    
//    public void sendEmail(String recipientEmail, String link)
//            throws MessagingException, UnsupportedEncodingException {
//        MimeMessage message = mailSender.createMimeMessage();              
//        MimeMessageHelper helper = new MimeMessageHelper(message);
//       
//        helper.setFrom(from, "forgot password support");
//        helper.setTo(recipientEmail);
//         
//        String subject = "Here's the link to reset your password";
//         
//        String content = "<p>Hello,</p>"
//                + "<p>You have requested to reset your password.</p>"
//                + "<p>Click the link below to change your password:</p>"
//                + "<p><a href=\"" + link + "\">Change my password</a></p>"
//                + "<br>"
//                + "<p>Ignore this email if you do remember your password, "
//                + "or you have not made the request.</p>";
//         
//        helper.setSubject(subject);
//         
//        helper.setText(content, true);
//         
//        mailSender.send(message);
//    }

	@GetMapping("/resetpassword")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) throws Exception {
		ApplicationUser user = userService.getByResetPasswordToken(token);
		model.addAttribute("token", token);

		if (user == null) {
			model.addAttribute("message", "Invalid Token");
			return "message";
		}

		return "resetpassword";
	}

	@PostMapping("/resetpassword")
	public String processResetPassword(HttpServletRequest request, Model model) throws Exception {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		ApplicationUser user = userService.getByResetPasswordToken(token);
//        model.addAttribute("title", "Reset your password");

		model.addAttribute("message", "message");

		if (user == null) {
			model.addAttribute("message", "Invalid Token");

			return "message";
		} else {
			userService.updatePassword(user, password);
			model.addAttribute("message", "You have successfully changed your password.");
			return "resetsuccess";
		}

	}
}