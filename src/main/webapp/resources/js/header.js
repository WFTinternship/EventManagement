$(document).ready(function () {

    //hide invalid login/password label
    $("#lf_email").change(function () {
        $("#login_failed_label").hide();
    });

    $("#lf_password").change(function () {
        $("#login_failed_label").hide();
    });

    //open login modal box
    $("#login_button").click(function () {
        $("#login_modal").modal();
    });

    //validate and submit login form
    $('#login_form').validate({

        rules: {
            email: {
                required: true,
                email: true,
            },
            password: "required"
        },

        messages: {
            email: {
                required: "Please enter your email address.",
                email: "Invalid email format. Please enter valid email address."
            },
            password: {
                required: "Please provide a password.",
                minlength: "Password must be at least 6 characters."
            }
        },

        submitHandler: function (form) {

            var email = $('#lf_email').val();
            var password = $('#lf_password').val();

            $.ajax({
                type: 'POST',
                data: {
                    email: email,
                    password: password
                },
                url: '/login',
                success: function (result) {
                    if (result.status == "SUCCESS") {
                        window.location = "/";
                        location.reload();

                    } else if (result.status == "FAIL") {
                        $("#login_failed_label").show();
                        $("#login_failed_label").html(result.message);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                }
            })
        }
    })
    
    //submit search form
   // $( "#top_search").submit(function( event ) {
   //      event.preventDefault();
   //
   //  $.get("/search?email=" + keyword, function (response) {
   //      if (response.status == "FOUND") {
   //
   //          var emailsHTML = [];
   //
   //          $.each(response.result, function (key, user) {
   //              //get selected invitation emails
   //              var invitation_emails = getSelectedInvitationEmails();
   //
   //              //check if email already selected
   //              var found = $.inArray(user.email, invitation_emails) > -1;
   //
   //              if (!found) {
   //                  var suggestedEmailHTML = createEmailSuggestion(user)
   //                  emailsHTML.push(suggestedEmailHTML);
   //              }
   //          });
   //
   //          if (emailsHTML != "") {
   //              $("#suggested_emails").css("display", "block");
   //              $("#suggested_emails").html(emailsHTML.join(""));
   //          }
   //      } else if (response.status == "NOT FOUND") {
   //          clearEmailSuggestionsList();
   //      }
   //  })
   // });
})