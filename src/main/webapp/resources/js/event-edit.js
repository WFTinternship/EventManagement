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
    $('#add_invitation_btn').click(function (e) {

        var email = $('#invitation-email').val();
        if (email == "") {
            alert("Empty email!");
        } else if (isValidEmailAddress(email)) {
            addEmailToGuestsList(email);
        } else {
            alert("Invalid email form!")
        }
    });

    //create email suggestions list
    $('#invitation-email').keyup(function () {

        var email = $('#invitation-email').val();
        if (email) {
            $.getJSON("/check-invitation-email?email=" + email, function (response) {
                if (response.status == "FOUND") {
                    var emailsHTML = [];
                    $.each(response.result, function (key, user) {
                        //get already selected emails
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
                    } else {
                        //$("#event_list").html("There are no events in this category.");
                    }                   // window.location = "/"
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

    //helper methods
    function createEmailSuggestion(user) {
        var emailHTML = '<div class="suggested_email">' + user.email + '</div>';
        return emailHTML;
    }

    function isValidEmailAddress(emailAddress) {
        var pattern = new RegExp(/^[+a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i);
        // alert( pattern.test(emailAddress) );
        return pattern.test(emailAddress);
    }

    function addEmailToGuestsList(email) {
        var str = "<div class='invitation_item clearfix' >" +
            "<i class='icon-user'></i>" +
            "<div class='invitation-email'>" + email + "</div>" +
            "</div>";

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
            var email = $(this).text();
            invitations.push(email);
        });
        return invitations;
    }
})