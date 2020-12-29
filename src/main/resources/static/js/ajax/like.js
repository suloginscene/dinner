window.addEventListener('load', function () {

    let doesLike;
    const perceive = function (like) {
        doesLike = like;
        if (like) toggleHeart();
    };


    const icon = $('#like-icon');
    const toggleHeart = function () {
        icon.toggleClass('fa-heart-o');
        icon.toggleClass('fa-heart');
    };

    const $likeCount = $('#like-count');
    let likeCount = $likeCount.text();
    const toggleCount = function () {
        if (doesLike) likeCount++;
        else likeCount--;
        $likeCount.text(likeCount);
    };


    const toggleLike = function () {
        doesLike = !doesLike;
        toggleHeart();
        toggleCount();
    };


    const articleId = $('#articleId').text();

    const like = function (e) {
        e.preventDefault();
        $.ajax({
            type: (doesLike) ? 'delete' : 'post',
            url: '/api/articles/' + articleId + '/like',
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', $('#csrf').text());
            },
            success: toggleLike
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/articles/' + articleId + '/like',
        success: perceive
    });

    $("#like").click(like);

});
