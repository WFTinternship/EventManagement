<script>

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
                        /* if (result.success != null) {
                         window.location = "/"
                        } else if (result.error != null) {
                            $("#login_failed_label").show();
                            $("#login_failed_label").html(result.error);
                         }*/
                        if (result.status == "SUCCESS") {
                            window.location = "/"
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
    })
</script>

<div class="container">

    <!-- Modal -->
    <div class="modal fade " id="login_modal" role="dialog">
        <div class="login_dialog">
            <div class="modal_header">
                <div class="lf_user_row">
                    <span class="lf_header">Login to your Account</span>
                </div>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <label class="error" id="login_failed_label"></label>

            <form id="login_form">

                <div class="lf_user_row">
                    <label for="lf_email">
                        <i class="lf_icon icon-user"></i>
                        <input name="email" id="lf_email" type="text" placeholder="email">
                    </label>
                </div>
                <div class="lf_user_row">
                    <label for="lf_password">
                        <i class="lf_icon icon-lock"></i>
                        <input name="password" id="lf_password" type="password" placeholder="password">
                    </label>
                </div>
                <div class="lf_user_row clearfix">
                    <div class="form_col_half">
                        <label for="rememberme">
                    <span class="remember-box">
                        <input id="rememberme" name="rememberme" type="checkbox">
                        <span>Remember me</span>
                    </span>
                        </label>
                    </div>
                    <div class="form_col_half clearfix">
                        <button name="login" class="btn f_right" id="login">
                            Sign in
                        </button>
                    </div>
                </div>
                <a class="lf_forget_pass" href="#">Forgot Your Password?</a>
            </form>
        </div>
    </div>
</div>