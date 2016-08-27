/**
 * Created by hermine on 8/27/16.
 */
$(document).ready(function () {
    $("#start_date").datepicker({
        dateFormat: 'dd-mm-yy'
    });
    $("#end_date").datepicker({
        dateFormat: 'dd-mm-yy'
    });
    $('#start_time').timepicker();
    $('#end_time').timepicker();
})