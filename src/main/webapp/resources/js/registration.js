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
            $('input').val(function(_, value) {
                return $.trim(value);
            });
            
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

    //show image after selection
    $("#avatar").on("change", function (elem) {

        if (this.files && this.files[0]) {

            var reader = new FileReader();
            reader.onload = function (e) {

                $('#img_prev').fadeTo(1000 , 1);
                $('#selected_img').attr('src', e.target.result);
            };

            reader.readAsDataURL(this.files[0]);
        } else {
            debugger;
            $('#img_prev').fadeTo(1000,0);
        }
    })

    // remove selected image prev
    $("#delete-img").click(function () {

        $('#img_prev').fadeTo(1000, 0, function () {

            $('#selected_img').attr('src', '#');
        });
        $('#img_prev').removeClass("visible_thumb");
        $("#avatar").val('');

    })
})