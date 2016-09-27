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

    /**** Guest list ***/
    if ($('#invitation_list').children().length == 0) {
        $("#guests_list").css("display", "none");
    } else{
        $("#guests_list").css("display", "block");
    }

    /*** All day checkbox ***/
    if ($("#check-all-day").is(':checked')){

        $("#start_time").css("display", "none");
        $("#end_time").css("display", "none");

        $("#start_time-error").css("display", "none");
        $("#end_time-error").css("display", "none");
    }
/******* Validate and submit add event form ******/

    //validator method for checkign start/end date range
    $.validator.addMethod("endDate_greater_startDate", function(value, element) {
        var startDate = new stringToDate($("#start_date").val(), "dd/MM/yyyy", "/");
        var endDate = new stringToDate($("#end_date").val(),  "dd/MM/yyyy", "/");
        if(endDate < startDate) {
            return false
        } else if(endDate > startDate){
            return true;
        } else {
           return  $("#end_time").val() >= $("#start_time").val()
        }
    }, "* End date should be greater than start date");


    //validating file size
    $.validator.addMethod('fileSize', function(value, element, param) {
        return this.optional(element) || (element.files[0].size <= param)
    });

    //validating event form
    $('#event_form').validate({
        rules: {
            eventTitle: "required",
            startDate: {
                required:true,
            },
            endDate: {
                required:true,
                endDate_greater_startDate: true
            },
            location: "required",
            startTime: "required",
            endTime: "required",
            eventImage:{
                extension: "png|jpeg|jpg",
                fileSize: 5242880, //5MB
            },
            eventFile:{
                extension: "pdf|doc|docx|xls|xlsx",
                fileSize: 5242880, //5MB
            },
        },

        messages: {
            eventTitle: "Please enter a title for event.",
            startDate: {
                required: "Please provide start date for event"
            },
            endDate: {
                required: "Please provide end date for event"
            },
            location: "Please provide location for event",
            startTime: "Please, provide start time or choose 'All day'",
            endTime: "Please, provide end time or choose 'All day'",
            eventImage: {
                extension: "Only .jpg and .png files are allowed",
                fileSize: "Image size should be less then 5MB"
            },
            eventFile: {
                extension: "Only .pdf, .doc, .xls files are allowed",
                fileSize: "File size should be less then 5MB"
            }
        },
        
        errorPlacement: function(error, element) {
            if (element.attr("name") == "eventImage" )
                error.insertAfter("#img_prev");
            else
                error.insertAfter(element);
        },

        submitHandler: function (form) {
            //get invitations array
            var invitations = getSelectedInvitationEmails();

            var formData = new FormData($('#event_form')[0]);
            formData.append("invitations", invitations);

            //send the same file name if no new file is uploaded
            var selectedImage = $("#event_image").val();

            var existingImageSrc = $('#selected_img').attr('src').split('/');
            var existingImageName = existingImageSrc[existingImageSrc.length - 1];

            if(!selectedImage){
                if(existingImageName != "#") {
                    formData.append("imageName", existingImageName);
                }
            }
            
            $.ajax({
                url: '/save-event',
                type: 'POST',
                processData: false,
                contentType: false,
                data: formData,
                success: function (result) {
                    if (result.status == "SUCCESS") {
                        window.location = "/my-account";
                    } else if (result.status == "FAIL") {
                        window.location = "/error";
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

    //ALL DAY implementation
    $("#check-all-day").change(function () {
        if (this.checked) {
            $("#start_time").css("display", "none");
            $("#start_time").val("00:00");

            $("#end_time").css("display", "none");
            $("#end_time").val("23:59");

            $("#start_time-error").css("display", "none");
            $("#end_time-error").css("display", "none");

        } else {
            $("#start_time").val("");
            $("#start_time").css("display", "block");

            $("#end_time").val("");
            $("#end_time").css("display", "block");
        }
    })


    //show image after selection
    $("#event_image").on("change", function (elem) {

        if (this.files && this.files[0] && $("#event_image").valid()) {

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
        $("#event_image").val('');

    })

    /********  helper methods **********/
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

    function stringToDate(_date,_format,_delimiter) {
        var formatLowerCase = _format.toLowerCase();
        var formatItems = formatLowerCase.split(_delimiter);
        var dateItems = _date.split(_delimiter);
        var monthIndex = formatItems.indexOf("mm");
        var dayIndex = formatItems.indexOf("dd");
        var yearIndex = formatItems.indexOf("yyyy");
        var month = parseInt(dateItems[monthIndex]);
        month -= 1;
        var formatedDate = new Date(dateItems[yearIndex],month,dateItems[dayIndex]);
        return formatedDate;
    }

    // edit event methods
    $("#event_discard").click(function (event) {
        event.preventDefault();
        window.location = "/my-account";

    })

})

    function readURL(input) {
        debugger;
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#img_prev').removeClass(".visible");
                $('#img_prev').css("display", "block");
                $('#selected_img').attr('src', e.target.result);
            };

            reader.readAsDataURL(input.files[0]);
        }
    }