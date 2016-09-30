package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.*;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
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

	public boolean sendConfirmationEmail(final User user){
		return true; // TODO: 9/29/16 implement
	}

	@Override
	public void sendInvitations(final Event event) {

		List<Invitation> invitations = event.getInvitations();

		if (isEmptyCollection(invitations)) {
			throw new InvalidObjectException("Empty email list");
		}

		for (final Invitation invitation : invitations) {
			sendInvitation(event, invitation);
		}
	}

	@Override
	public void sendInvitation(final Event event, final Invitation invitation) {

		List<Invitation> invitations = event.getInvitations();

		if (isEmptyCollection(invitations)) {
			throw new OperationFailedException("Empty email list");
		}
			//don't send invitation if organizer is not invited
			MimeMessagePreparator preparator = new MimeMessagePreparator() {

				public void prepare(MimeMessage mimeMessage) throws Exception {

					MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
					message.setTo(invitation.getUser().getEmail());
					message.setFrom(event.getOrganizer().getFirstName() + event.getOrganizer().getLastName());
					// TODO: 9/16/16 read from plist
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

	@Override
	public void sendRespondNotificationToOrganizer(final Event event, final Invitation invitation) {

		List<Invitation> invitations = event.getInvitations();

		if (isEmptyCollection(invitations)) {
			throw new OperationFailedException("Empty email list");
		}
		//don't send invitation if organizer is not invited
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				String subject = String.format("%s %s responded to invitation: %s", invitation.getUser().getFirstName(),
						invitation.getUser().getLastName(), event.getTitle());

				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setTo(event.getOrganizer().getEmail());
				message.setFrom("turshujyan@gmail.com"); // TODO: 9/16/16 read from plist
				message.setSubject(subject);

				String responseStatus;
				int responseId = invitation.getUserResponse().getId();
				if(responseId == 1){
					responseStatus = "accepted";
				} else if(responseId == 2){
					responseStatus = "rejected";
				} else {
					responseStatus = "replied 'Maybe' to";
				}
				Map model = new HashMap();

				model.put("responseStatus", responseStatus);
				model.put("invitation", invitation);
				model.put("event", event);
//				model.put("rootPath", )
				String text = VelocityEngineUtils.mergeTemplateIntoString(
						velocityEngine, "templates/event-responded-notificaton.vm", "UTF-8", model);
				message.setText(text, true);
			}
		};
		mailSender.send(preparator);
	}
}
