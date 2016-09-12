/**
 * Created by hermine on 8/27/16.
 */
$(document).ready(function () {

    /***** Date/time picker configs *****/
    $("#start_date").datepicker({
        dateFormat: 'dd/mm/yy'
        //dateFormat: 'yy-mm-dd'
    });
    $("#end_date").datepicker({
        dateFormat: 'dd/mm/yy'
    });
    $('#start_time').timepicker({
        timeFormat: 'HH:mm',
        startTime: new Date(0, 0, 0, 9, 0, 0),
        interval: 15
    });
    $('#end_time').timepicker({
        timeFormat: 'HH:mm',
        startTime: new Date(0, 0, 0, 9, 0, 0),
        interval: 15
    });


    //validate and submit add event form
    $('#event_form').validate({
        rules: {
            eventTitle: "required",
            startDate: "required",
            endDate: "required",
        },

        messages: {
            eventTitle: "Please enter a title for event.",
            startDate: "Please provide start date for event",
            endDate: "Please provide end date for event",
        },

        submitHandler: function (form) {

            //get invitations array
            var invitations = getSelectedInvitationEmails();

            // $('#invitation_list').children('div').each(function (index, value) {
            //     var email = $(this).text();
            //     invitations.push(email);
            // });

            var formData = new FormData($('#event_form')[0]);
            formData.append("invitations", invitations);

            $.ajax({
                url: '/add-event',
                type: 'POST',
                processData: false,
                contentType: false,
                data: formData,
                success: function (result) {
                    if (result.status == "SUCCESS") {
                        //   window.location = "/";
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                }
            })
        }
    })

    //add email to invitations list
    $('#add_invitation_btn').click(function (event) {
        event.preventDefault();

        clearEmailSuggestionsList();

        var email = $('#invitation-email').val();
        if (email == "") {
            generateErrorMessage("Empty email!")
        } else if (isValidEmailAddress(email)) {
            var invitation_emails = getSelectedInvitationEmails();

            //check if email already selected
            var found = $.inArray(email, invitation_emails) > -1;

            if (!found) {
                addEmailToGuestsList(email);
            } else {
                generateErrorMessage("Guest already invited!")
            }
        } else {
            generateErrorMessage("Invalid email form!");
        }
    });

    //create email suggestions list
    $('#invitation-email').keyup(function () {

        hideErrorMessage();

        var email = $('#invitation-email').val();

        if (email) {
            $.getJSON("/check-invitation-email?email=" + email, function (response) {
                if (response.status == "FOUND") {

                    var emailsHTML = [];

                    $.each(response.result, function (key, user) {
                        //get selected invitation emails
                        var invitation_emails = getSelectedInvitationEmails();

                        //check if email already selected
                        var found = $.inArray(user.email, invitation_emails) > -1;

                        if (!found) {
                            var suggestedEmailHTML = createEmailSuggestion(user)
                            emailsHTML.push(suggestedEmailHTML);
                        }
                    });

                    if (emailsHTML != "") {
                        $("#suggested_emails").css("display", "block");
                        $("#suggested_emails").html(emailsHTML.join(""));
                    }
                } else if (response.status == "NOT FOUND") {
                    clearEmailSuggestionsList();
                }
            })
        } else {
            clearEmailSuggestionsList();
        }
    })

    //choose email from suggestions list
    $('#suggested_emails').on("click", '.suggested_email', function () {
        addEmailToGuestsList($(this).text());
        clearEmailSuggestionsList();

        //empty input content
        $('#invitation-email').val('');
    })

    //remove invitation from list
    $('#invitation_list').on("click", '.remove_email', function () {

        $(this).parent().remove();
        if ($('#invitation_list').children().length == 0) {
            $("#guests_list").css("display", "none");
        }
    });

    //helper methods
    function createEmailSuggestion(user) {
        var emailHTML = '<div class="suggested_email">' + user.email + '</div>';
        return emailHTML;
    }

    function isValidEmailAddress(emailAddress) {
        var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
        return pattern.test(emailAddress);
    }

    function addEmailToGuestsList(email) {
        var str = "<div class='invitation_item clearfix' >" +
            "<i class='icon-user'></i>" +
            "<div class='invitation_item_email'>" + email + "</div>" +
            "<span class ='remove_email'>x</span></div>";

        $('#invitation_list').append(str);
        $("#guests_list").css("display", "block");
    }

    function clearEmailSuggestionsList() {
        $("#suggested_emails").css("display", "none");
        $("#suggested_emails").html("");
    }

    function getSelectedInvitationEmails() {

        //get invitations array
        var invitations = [];
        $('#invitation_list').children('div').each(function (index, value) {
            var email = $(this).children('div').text();
            invitations.push(email);
        });
        return invitations;
    }

    function generateErrorMessage(message) {
        if ($("#email_error").length == 0) {
            var errorHTML = "<label id='email_error' class= error'>" + message + "</label>";
            $("#invitation_email").append(errorHTML);
        }
    }

    function hideErrorMessage() {
        if ($("#email_error").length > 0) {
            $("#email_error").remove();
        }
    }

})