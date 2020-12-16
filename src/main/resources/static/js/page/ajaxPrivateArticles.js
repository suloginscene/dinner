const ajaxPrivateArticles = function () {

    const articles = $('#private-articles');
    const articleTemplate = document.querySelector("#private-article-template").innerHTML;

    const appendArticles = function (articleArr) {
        articleArr.forEach(function (a) {
            const date = a.createdAt.substring(0, "yyyy-MM-dd".length);
            const el = articleTemplate
                .replace("{id}", a.id).replace("{title}", a.title).replace("{date}", date);
            articles.append(el);
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/private-articles',
        success: appendArticles
    });

};


window.addEventListener('load', ajaxPrivateArticles);
