window.addEventListener('load', function () {

    const $count = $('#notification-count');

    const logUncheckedCount = function (count) {
        $count.text((count !== 0) ? count : "");
    };


    $.ajax({
        type: 'get',
        url: '/api/notifications/count',
        success: logUncheckedCount
    });

});
