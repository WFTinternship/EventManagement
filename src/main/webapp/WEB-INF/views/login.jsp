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