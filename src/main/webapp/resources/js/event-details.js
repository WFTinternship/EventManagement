$(document).ready(function () {

    $("#lightGallery").lightGallery();

    
    $("#change_response").click(function () {
        $("#respond_wrapper").slideToggle( "fast", function () {
        });
    })

    $("#cancel_response").click(function () {
        //close respond to invitation window
        $("#change_response").click();
    })

    $("#upload_images").validate({

        submitHandler: function (form) {
            debugger;
            var formData = new FormData($('#upload_images')[0]);

            $.ajax({
                url: '/upload-photos',
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

})


function showResponseSavedMessage() {
    $.notify(
        {
            message: "Response saved!"
        },
        {
            type: 'success',
            allow_dismiss: false,
            element: '.content_block',
            delay: 2000,
            animate: {
                enter: 'animated fadeInDown',
                exit: 'animated fadeOutUp'
            },
        }
    );
}

function saveResponse(elem, id){
    var response = $('input[name=response]:checked').val();

    $.ajax({
        url: '/events/' + id + "/respond?response=" + response,
        type: 'GET',
        success: function (result) {
            if (result.status == "SUCCESS") {
                //change response icon
                var icon = $("#change_response_wrapper").parent().children(":first");
                icon.removeClass().addClass("icon-response-" + response);

                showResponseSavedMessage();
                //close respond to invitation window
                $("#change_response").click();
            } else if (result.status == "FAIL") {
                $.notify(
                    { message: result.message },
                    { type: 'danger'}
                );
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
        }
    })
}

function emailCurrentPage(){
    var subject = $(".title a").text();

    window.location.href="mailto:?subject="+ subject + "&body=" + encodeURI(window.location.href);
}
