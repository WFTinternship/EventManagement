$(document).ready(function () {

    //validate and submit registration form
    $('#registration_form').validate({
        rules: {
            firstName: "required",
            lastName: "required",
            email: {
                required: true,
                email: true,
                remote: {
                    url: "/check-email",
                    type: "POST",
                    data: {
                        email: function () {
                            return $("#email").val();
                        }
                    }
                }
            },
            confirmEmail: {
                required: true,
                equalTo: "#email"
            },
            password: {
                required: true,
                minlength: 6
            },
            confirmPassword: {
                required: true,
                equalTo: "#password"
            }
        },

        messages: {
            firstName: "Please enter your first name.",
            lastName: "Please enter your last name.",
            password: {
                required: "Please provide a password.",
                minlength: "Password must be at least 6 characters."
            },
            confirmPassword: {
                required: "Please confirm password.",
                equalTo: "Passwords do not match."
            },
            email: {
                required: "Please provide an email address.",
                email: "Please enter a valid email address.",
                remote: "Email already in use."
            },
            confirmEmail: {
                required: "Please confirm email address.",
                equalTo: "Emails do not match."
            }
        },

        submitHandler: function (form) {
            var formData = new FormData($('#registration_form')[0]);

            $.ajax({
                url: '/register',
                type: 'POST',
                processData: false,
                contentType: false,
                data: formData,
                success: function (result) {
                    if (result.status == "SUCCESS") {
                        window.location = "/";
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            })
        }
    })
})