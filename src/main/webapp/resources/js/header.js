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

    $('#search_form').on('submit', function () {

        var keyword = $.trim($('#search_input').val());
        $('#search_input').val(keyword)

        if(!keyword) {
            return false;
        }
    })
})

function highlightKeywordOnload(keyword) {
    var regExp = new RegExp(keyword, 'gi');


    $(".search-results-page .title").each(function () {        
        highlight(regExp, keyword, $(this))

    })

    $(".search-results-page .desc").each(function () {
        highlight(regExp, keyword, $(this))
    })
    
    $(".search-results-page .meta_part").each(function () {
        highlight(regExp, keyword, $(this))

    })
}

function  highlight(regExp, value, elem) {
    var titleHTML = elem.html();
    var changedHTML = titleHTML.replace(regExp, '<span class="highlight">' + value + '</span>');
    elem.html(changedHTML);
}
