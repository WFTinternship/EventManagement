/**
 * Created by hermine on 8/27/16.
 */
$(document).ready(function () {

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
            invitationEmail: {
                /* email: true,
                 remote: {
                 url: "/check-email",
                 type: "POST",
                 data: {
                 email: function () {
                 return $("#email").val();
                 }
                 }*/
            }
        },

        messages: {
            eventTitle: "Please enter a title for event.",
            startDate: "Please provide start date for event",
            endDate: "Please provide end date for event",
            invitationEmail: {
                email: "Please enter a valid email address.",
            }
        },

        submitHandler: function (form) {

            //get invitations array
            var invitations = [];
            $('#invitation_list').children('div').each(function (index, value) {
                var email = $(this).text();
                invitations.push(email);
            });

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

    //validate and submit add event form

    $('#add_invitation_btn').click(function (e) {

        var email = $('#invitationEmail').val();
        var str = "<i class='lf_icon icon-user'></i><div class='invitation-item'>" + email + "</div>";

        $('#invitation_list').append(str);
        $("#guests_list").css("display", "block");
    });


    $('#invitation-email').keyup(function () {

        var email = $('#invitation-email').val();
        if (email) {
            $.getJSON("/check-invitation-email?email=" + email, function (response) {
                if (response.status == "FOUND") {
                    var emailsHTML = [];
                    $.each(response.result, function (key, user) {
                        var suggestedEmailHTML = createEmailSuggestion(user)
                        emailsHTML.push(suggestedEmailHTML);
                    });

                    if (emailsHTML != "") {
                        $("#suggested_emails").css("display", "block");
                        $("#suggested_emails").html(emailsHTML.join(""));
                    } else {
                        //$("#event_list").html("There are no events in this category.");
                    }                   // window.location = "/"
                } else if (response.status == "NOT FOUND") {
                    $("#suggested_emails").css("display", "none");
                    $("#suggested_emails").html("");
                }
            })
        } else {
            $("#suggested_emails").css("display", "none");
            $("#suggested_emails").html("");
        }
    })

    function createEmailSuggestion(user) {

        var emailHTML = '<div class="suggested_email">' + user.email + '</div>';

        return emailHTML;
    }
})