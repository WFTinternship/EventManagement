$(document).ready(function () {

    /**** TAB BAR ***/
    jQuery('.hm-tabs').each(function (index) {
        var allparent = jQuery(this);
        var all_width = allparent.width();

        var tabItems = allparent.find('.tabs-navi a'),
            tabContentWrapper = allparent.find('.tabs-body');

        tabItems.on('click', function (event) {
            event.preventDefault();

            var selectedItem = jQuery(this);
            var parentlist = selectedItem.parent();

            if (parentlist.index() === 0) {
                selectedItem.parent().siblings("li").removeClass('prev_selected');
            } else {
                selectedItem.parent().prev().addClass('prev_selected').siblings("li").removeClass('prev_selected');
            }

            if (!selectedItem.hasClass('selected')) {
                var selectedTab = selectedItem.data('content'),
                    selectedContent = tabContentWrapper.find('li[data-content="' + selectedTab + '"]'),
                    slectedContentHeight = selectedContent.innerHeight();

                tabItems.removeClass('selected');
                selectedItem.addClass('selected');
                selectedContent.addClass('selected').siblings('li').removeClass('selected');
                //animate tabContentWrapper height when content changes
                tabContentWrapper.animate({
                    'height': slectedContentHeight
                }, 200);
            }
        });

        //hide the .hm-tabs::after element when tabbed navigation has scrolled to the end (mobile version)
        checkScrolling(jQuery('.hm-tabs nav'));
        jQuery(window).on('resize', function () {
            checkScrolling(jQuery('.hm-tabs nav'));
            tabContentWrapper.css('height', 'auto');
        });
        jQuery('.hm-tabs nav').on('scroll', function () {
            checkScrolling(jQuery(this));
        });

        function checkScrolling(tabs) {
            var totalTabWidth = parseInt(tabs.children('.tabs-navi').width()),
                tabsViewport = parseInt(tabs.width());
            if (tabs.scrollLeft() >= totalTabWidth - tabsViewport) {
                tabs.parent('.hm-tabs').addClass('is-ended');
            } else {
                tabs.parent('.hm-tabs').removeClass('is-ended');
            }
        }

    });

    $(".tabs-navi").ch("a").on(click, function(){
        alert("aaa");
    })

    //**** Edit/Delete Event ***/
    $("#edit-event").click(function (event) {

    });

    $("#edit-delete").click(function (event) {
        $.ajax({
            url: '/delet-event',
            type: 'POST',
            processData: false,
            contentType: false,
            data: formData,
            success: function (result) {
                if (result.status == "SUCCESS") {
                    window.location = "/events";
                } else if (result.status == "FAIL") {
                    window.location = "/error";
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        })
    });
});

function deleteEvent(id){
    $.ajax({
        url: '/events/' + id + "/delete",
        type: 'GET',
        success: function (result) {
            if (result.status == "SUCCESS") {
                $.notify(
                    { message: 'Event successfully deleted' },
                    { type: 'success',
                        delay: 2000
                    }
                );
                //delete from ui
                $("#event_" + id).hide('slow', function(){
                    $("#event_" + id).remove();
                    if($('#organized-events-list').children().length == 0) {
                        $("#organized-events-tab").html('<div class="no-events">No organized events.</div>');
                    }
                });

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

function removeDeletedEvent(){

}
