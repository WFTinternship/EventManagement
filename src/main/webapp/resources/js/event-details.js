$(document).ready(function () {

    $("#animated-thumbnials").lightGallery({
        thumbnail:true,
        animateThumb: false,
        showThumbByDefault: false
    });

    
    $("#change_response").click(function () {
        $("#respond_wrapper").slideToggle( "fast", function () {
        });
    })

    $("#cancel_response").click(function () {
        //close respond to invitation window
        $("#change_response").click();
    })

//validating input type=file
    $.validator.addMethod('fileSize', function(value, element, param) {
        return this.optional(element) || (element.files[0].size <= param)
    });

    $("#upload_images").validate({
        rules: {
            eventImages:{
                required:true,
                extension: "png|jpeg|jpg",
                fileSize: 5242880, //5MB
            },
        },

        messages: {
            eventImages: {
                required:"Please, choose photo(s)",
                extension: "Only .jpg and .png files are allowed",
                fileSize: "Image size should be less then 5MB"
            }
        },

        errorPlacement: function(error, element) {
            if (element.attr("name") == "eventImages" )
                error.insertAfter("#upload_photos_btn");
            else
                error.insertAfter(element);
        },

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
    // //show selected images
    // $("#event_images").on("change", function (elem) {
    //     debugger;
    //     if (this.files && this.files[0] && $("#event_images").valid()) {
    //         $(this.files).each(function () {
    //             var reader = new FileReader();
    //             reader.readAsDataURL(this);
    //
    //             reader.onload = function (e) {
    //             $('#img_prev_div').append("<img id='img_prev' src='" + e.target.result + "'>");
    //             };
    //
    //         });
    //     }
    // })

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

//preview images before upload
function preview(input) {

    if (input.files && input.files[0] && $("#event_images").valid()) {
        $(input.files).each(function () {
            var reader = new FileReader();
            reader.readAsDataURL(this);
            reader.onload = function (e) {
                $("#img_prev_photos").append("<img class='thumb-img' src='" + e.target.result + "'>");
                $("#img_prev_photos").fadeTo(1000 , 1);
            }
        });
    } else {
        $("#img_prev_photos").html('');
        $("#img_prev_photos").fadeTo(1000 , 0);
    }
}