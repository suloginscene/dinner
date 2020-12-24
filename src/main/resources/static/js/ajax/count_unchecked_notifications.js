window.addEventListener('load', function () {

    const logUncheckedCount = function (count) {
        const $count = $('#notification-count');
        $count.text(count);
    };


    $.ajax({
        type: 'get',
        url: '/api/notifications/count-unchecked',
        success: logUncheckedCount
    });

});
