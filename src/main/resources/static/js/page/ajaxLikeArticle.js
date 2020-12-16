const ajaxLikeArticle = function () {

    const queryString = $("#like-form").serialize();
    if (queryString === "") return;


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


    const params = queryString.split('&');
    const token = params[0].substring("_csrf=".length);
    const articleId = params[1].substring("articleId=".length);
    const username = params[2].substring("username=".length);

    const like = function (e) {
        e.preventDefault();

        const method = (doesLike) ? 'delete' : 'post';
        $.ajax({
            type: method,
            url: '/api/like/',
            data: {
                articleId: articleId,
                username: username
            },
            beforeSend: function (xhr) {
                xhr.setRequestHeader('X-CSRF-TOKEN', token);
            },
            success: toggleLike
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/likes/',
        data: {
            articleId: articleId,
            username: username
        },
        success: perceive
    });

    $("#like").click(like);

};


window.addEventListener('load', ajaxLikeArticle);
