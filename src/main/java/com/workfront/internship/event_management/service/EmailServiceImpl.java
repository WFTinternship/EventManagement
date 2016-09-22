package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.spring.DevApplicationConfig;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.mail.*;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.*;

import static com.workfront.internship.event_management.service.util.Validator.*;

/**
 * Created by Hermine Turshujyan 7/25/16.
 */
@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	public boolean sendConfirmationEmail(final User user) {

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);
		try {
			mailMsg.setFrom("turshujyan@gmail.com");
			mailMsg.setTo(user.getEmail());
			mailMsg.setSubject("Test mail");
			mailMsg.setText("Hello World!");
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return false;
	}
	@Override
	public void sendInvitations(final Event event) {

		List<Invitation> invitations = event.getInvitations();

		if (isEmptyCollection(invitations)) {
			throw new OperationFailedException("Empty email list");
		}

		for (final Invitation invitation : invitations) {
			//don't send invitation if organizer is not invited
			if(invitation.getUserResponse().getId() != 4 ) {
				MimeMessagePreparator preparator = new MimeMessagePreparator() {

					public void prepare(MimeMessage mimeMessage) throws Exception {

						MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
						message.setTo(invitation.getUser().getEmail());
						message.setFrom("turshujyan@gmail.com"); // TODO: 9/16/16 read from plist
						message.setSubject(String.format("Invitation: %s", event.getTitle()));
						Map model = new HashMap();
						model.put("invitation", invitation);
						model.put("event", event);
						String text = VelocityEngineUtils.mergeTemplateIntoString(
								velocityEngine, "templates/invitation.vm", "UTF-8", model);
						message.setText(text, true);
					}
				};
				mailSender.send(preparator);
			}
		}
	}
}
