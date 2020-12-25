window.addEventListener('load', function () {

    const username = $('#username').text();
    const magazines = $('#magazines-of-user');
    const articles = $('#articles-of-user');

    const magazineTemplate = document.querySelector("#magazine-template-table").innerHTML;
    const articleTemplate = document.querySelector("#article-template").innerHTML;

    const appendMagazines = function (magazineArr) {
        magazineArr.forEach(function (m) {
            const el = magazineTemplate.replace("{id}", m.id).replace("{title}", m.title);
            magazines.append(el);
        });
    };

    const appendArticles = function (articleArr) {
        articleArr.forEach(function (a) {
            const date = a.createdAt.substring(0, "yyyy-MM-dd".length);
            const el = articleTemplate
                .replace("{id}", a.id).replace("{title}", a.title)
                .replace("{date}", date).replace("{read}", a.read).replace("{like}", a.like);
            articles.append(el);
        });
    };


    $.ajax({
        type: 'get',
        url: '/api/magazines/' + username,
        success: appendMagazines
    });

    $.ajax({
        type: 'get',
        url: '/api/articles/' + username,
        success: appendArticles
    });

});
